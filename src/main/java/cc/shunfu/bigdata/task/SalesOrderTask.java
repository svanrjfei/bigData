package cc.shunfu.bigdata.task;

import cc.shunfu.bigdata.dto.entity.SalesOrder;
import cc.shunfu.bigdata.dto.mapper.SalesOrderMapper;
import cc.shunfu.bigdata.service.SalesOrderService;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static cc.shunfu.bigdata.utils.ChuanUtils.doPost;
import static cc.shunfu.bigdata.utils.DateUtils.formatDate;
import static cc.shunfu.bigdata.utils.DateUtils.formatDateTime;
import static cc.shunfu.bigdata.utils.K3cloudUtils.getK3CloudData;

/**
 * 调拨单的获取🐟推送
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-16
 */
@Log4j2
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync
public class SalesOrderTask {

    @Autowired
    SalesOrderMapper salesOrderMapper;

    @Autowired
    SalesOrderService salesOrderService;

    private SqlSessionFactory sqlSessionFactory;


    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }


    /**
     * 获取金蝶直接调拨单
     *
     * @author svanrj
     * @date 2024/4/20
     */
    @Async
    public void getSales() {
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");

        List<SalesOrder> salesOrderEntities = new ArrayList<>();
        String fieldKeys = "FBillEntry_FEntryId,FBILLNO,FDOCUMENTSTATUS.FCaption,FDATE,FMATERIALID.FNumber,FMaterialId.FName,FMODEL,FUnitID.FName,FQty,FConsignPrice,FConsignAmount,FTaxPrice,FAllAmount,FSrcStockId.FName,FSrcStockLocId.FF100001.FName,FDestStockId.FName,F_ora_Base.FName,FTransferDirect.FCaption,F_ora_Text1,FSRCBILLTYPEID,FORDERTYPE.FName,FCreatorId.FName,FApproverId.FName,FAmount,F_ORA_ATTACHMENTCOUNT,FPrice,FOutJoinQty,F_ORA_TEXT3,F_ORA_QTY,FSECOUTJOINQTY,FExchangeRate,FExchangeTypeId.FName,FSETTLECURRID.FName,FBaseCurrId.FName,FApproveDate,F_ora_Base.FNumber,FStockOutOrgId.FName";
        LinkedList<String> queryFilters = new LinkedList<>();

        queryFilters.add(String.format("FDestStockId.FName = '%s'", "客户仓"));
        queryFilters.add(String.format("FDocumentStatus = '%s'", "C"));
        queryFilters.add(String.format("FStockOutOrgId.FName = '%s'", "安徽舜富精密科技股份有限公司"));
        queryFilters.add(String.format("FApproveDate >= '%s'", todayString + " 00:00:00"));
        queryFilters.add(String.format("FApproveDate <= '%s'", todayString + " 23:59:59"));
        String filterStr = String.join(" and ", queryFilters);
        // 处理获取到的所有数据


        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {

            getK3CloudData(SalesOrder.class, filterStr, fieldKeys, salesOrderEntities, "STK_TransferDirect", "FApproveDate Desc");

            SalesOrderMapper salesOrderMapperNew = sqlSession.getMapper(SalesOrderMapper.class);
            salesOrderEntities.forEach(salesOrderMapperNew::insertOrUpdateSalesOrder);
            sqlSession.commit();
            sqlSession.clearCache();

            log.info("K3Cloud数据同步完成!" + "共同步" + salesOrderEntities.size() + "条数据");
            log.info("----------------------------------------------------------------------------------------------------------------");

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async
    public void sendSales() {
        int page = 0;
        int count = 0;
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");
        while (true) {
            List<SalesOrder> salesOrders = salesOrderService.getSalesOrders(todayString + " 00:00:00", todayString + " 23:59:59", page * 300, 300);

            if (salesOrders.isEmpty()) {
                log.info("氚云数据同步完成!" + "共同步" + count + "条数据");
                break;
            }
            count += salesOrders.size();

            List<String> jsonArray = new ArrayList<>();
            for (SalesOrder salesOrder : salesOrders) {
                JSONObject jsonObject = new JSONObject();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(salesOrder.getModifyDate());
                jsonObject.put("F0000039", formatDate(salesOrder.getModifyDate(), "yyyy-MM-dd"));
                jsonObject.put("F0000040", formatDate(salesOrder.getModifyDate(), "yyyy-MM"));
                jsonObject.put("F0000032", calendar.get(Calendar.YEAR));
                jsonObject.put("F0000041", salesOrder.getCustomerId());
                jsonObject.put("F0000038", salesOrder.getAllAmount());
                jsonObject.put("F0000037", salesOrder.getTaxPrice());
                jsonObject.put("F0000036", salesOrder.getQuantity());
                jsonObject.put("F0000019", salesOrder.getDestStockLocName());
                jsonObject.put("F0000034", salesOrder.getMaterialNumber());
                jsonObject.put("F0000035", salesOrder.getMaterialName());
                jsonObject.put("F0000042", salesOrder.getId());
                jsonObject.put("F0000043", "直接调拨单");
                jsonArray.add(jsonObject.toString());
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ActionName", "CreateBizObjects");// 调用的方法名
            paramMap.put("SchemaCode", "D148577f5c4122e303e41ec80a185d139afa45b"); // 表单编码

            paramMap.put("BizObjectArray", jsonArray.toArray());
            paramMap.put("IsSubmit", "true");
            String CreateStr = JSONObject.valueToString(paramMap);

            // 请求接口
            doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);

            page++;
        }
    }

    @Async
    public void testTask() {
        log.info("任务测试");
    }

    @Async
    public void testTask2() {
        log.info("任务测试2");
    }
}

