package cc.shunfu.bigdata.task;

import cc.shunfu.bigdata.dto.entity.Bill;
import cc.shunfu.bigdata.dto.entity.ReceiveBillSrc;
import cc.shunfu.bigdata.dto.mapper.BillMapper;
import cc.shunfu.bigdata.dto.mapper.ReceiveBillSrcMapper;
import cc.shunfu.bigdata.dto.vo.result.BillCustomer;
import cc.shunfu.bigdata.dto.vo.result.ReceiveBillSrcCustomer;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static cc.shunfu.bigdata.utils.ChuanUtils.doPost;
import static cc.shunfu.bigdata.utils.DateUtils.formatDate;
import static cc.shunfu.bigdata.utils.DateUtils.formatDateTime;
import static cc.shunfu.bigdata.utils.K3cloudUtils.getK3CloudData;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-06-06
 */

@Log4j2
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync
public class FinanceTask {

    @Autowired
    ReceiveBillSrcMapper receiveBillSrcMapper;

    @Autowired
    BillMapper billMapper;

    private SqlSessionFactory sqlSessionFactory;


    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }


    /**
     * 获取金蝶收款单
     *
     * @author svanrj
     * @date 2024/4/20
     */
    @Async
    public void getCollection() {
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");

        List<ReceiveBillSrc> orderEntities = new ArrayList<>();
        String fieldKeys = "FRECEIVEBILLSRCENTRY_FEntryID,FBillTypeID.FName,FSRCBILLNO,FDATE,FPAYUNIT.Fname,FPAYUNIT.Fnumber,FREALRECAMOUNTFOR_S,FApproveDate";
        LinkedList<String> queryFilters = new LinkedList<>();

        queryFilters.add(String.format("FApproveDate >= '%s'", todayString + " 00:00:00"));
        queryFilters.add(String.format("FApproveDate <= '%s'", todayString + " 23:59:59"));
        queryFilters.add(String.format("FBillTypeID.FName = '%s'", "销售收款单"));


        String filterStr = String.join(" and ", queryFilters);
        // 处理获取到的所有数据


        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {

            getK3CloudData(ReceiveBillSrc.class, filterStr, fieldKeys, orderEntities, "AR_RECEIVEBILL", "FApproveDate Desc");


            ReceiveBillSrcMapper receiveBillSrcMapperNew = sqlSession.getMapper(ReceiveBillSrcMapper.class);
            orderEntities.forEach(receiveBillSrcMapperNew::insertOrUpdateReceiveBill);
            sqlSession.commit();
            sqlSession.clearCache();

            log.info("K3Cloud数据同步完成!" + "共同步" + orderEntities.size() + "条数据");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取金蝶收款计划
     *
     * @author svanrj
     * @date 2024/4/20
     */
    @Async
    public void getCollectionPlan() {
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");

        List<Bill> orderEntities = new ArrayList<>();
        String fieldKeys = "FBillNo,FBillTypeID.FName,FCUSTOMERID.Fname,FCUSTOMERID.Fnumber,FALLAMOUNT_D,FENDDATE_H,,FApproveDate";
        LinkedList<String> queryFilters = new LinkedList<>();

        queryFilters.add(String.format("FApproveDate >= '%s'", todayString + " 00:00:00"));
        queryFilters.add(String.format("FApproveDate <= '%s'", todayString + " 23:59:59"));
        queryFilters.add(String.format("FBillTypeID.FName = '%s'", "标准应收单"));


        String filterStr = String.join(" and ", queryFilters);
        // 处理获取到的所有数据


        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {

            getK3CloudData(Bill.class, filterStr, fieldKeys, orderEntities, "AR_receivable", "FApproveDate Desc");


            BillMapper billMapperNew = sqlSession.getMapper(BillMapper.class);
            orderEntities.forEach(billMapperNew::insertOrUpdateBill);
            sqlSession.commit();
            sqlSession.clearCache();
            log.info("K3Cloud数据同步完成!" + "共同步" + orderEntities.size() + "条数据");

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async
    public void sendCollectionPlan() {
        int page = 0;
        int count = 0;
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");
        while (true) {
            List<BillCustomer> billCustomers = billMapper.getBills(todayString + " 00:00:00", todayString + " 23:59:59", page * 300, 300);

            if (billCustomers.isEmpty()) {
                log.info("氚云数据同步完成!" + "共同步" + count + "条数据");
                break;
            }
            count += billCustomers.size();

            List<String> jsonArray = new ArrayList<>();
            for (BillCustomer billCustomer : billCustomers) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("F0000008", formatDate(billCustomer.getEndDate(), "yyyy-MM-dd"));
                jsonObject.put("F0000017", billCustomer.getBillNo());
                jsonObject.put("F0000003", billCustomer.getObjectId());
                jsonObject.put("F0000016", billCustomer.getCustomerName());
                jsonObject.put("F0000018", billCustomer.getCustomerNumber());
                jsonObject.put("F0000018", billCustomer.getCustomerNumber());
                jsonObject.put("F0000005", billCustomer.getAllAmount());
                jsonObject.put("F0000006", billCustomer.getAllAmount());
                jsonObject.put("F0000019", billCustomer.getDept());
                jsonArray.add(jsonObject.toString());
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ActionName", "CreateBizObjects");// 调用的方法名
            paramMap.put("SchemaCode", "D14857793ccca139e74474d899761cb8d69377c"); // 表单编码

            paramMap.put("BizObjectArray", jsonArray.toArray());
            paramMap.put("IsSubmit", "true");
            String CreateStr = JSONObject.valueToString(paramMap);

            // 请求接口
            doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);

            page++;
        }
    }

    @Async
    public void sendCollection() {
        int page = 0;
        int count = 0;
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");
        while (true) {
            List<ReceiveBillSrcCustomer> receiveBillSrcCustomers = receiveBillSrcMapper.getReceiveBill(todayString + " 00:00:00", todayString + " 23:59:59", page * 300, 300);

            if (receiveBillSrcCustomers.isEmpty()) {
                log.info("氚云数据同步完成!" + "共同步" + count + "条数据");
                break;
            }
            count += receiveBillSrcCustomers.size();

            List<String> jsonArray = new ArrayList<>();
            for (ReceiveBillSrcCustomer receiveBillSrcCustomer : receiveBillSrcCustomers) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("F0000003", formatDate(receiveBillSrcCustomer.getDate(), "yyyy-MM-dd"));
                jsonObject.put("F0000025", receiveBillSrcCustomer.getOrderBillNo());
                jsonObject.put("F0000005", receiveBillSrcCustomer.getObjectId());
                jsonObject.put("F0000022", receiveBillSrcCustomer.getPayUnitName());
                jsonObject.put("F0000023", receiveBillSrcCustomer.getPayUnitNumber());
                jsonObject.put("F0000008", receiveBillSrcCustomer.getRealRecAmountForS());
                jsonObject.put("F0000013", receiveBillSrcCustomer.getDept());
                jsonArray.add(jsonObject.toString());
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ActionName", "CreateBizObjects");// 调用的方法名
            paramMap.put("SchemaCode", "D1485771cd98eabcdaa49f1b958ba3faab5b1cd"); // 表单编码

            paramMap.put("BizObjectArray", jsonArray.toArray());
            paramMap.put("IsSubmit", "true");
            String CreateStr = JSONObject.valueToString(paramMap);

            // 请求接口
            doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);

            page++;
        }
    }

}
