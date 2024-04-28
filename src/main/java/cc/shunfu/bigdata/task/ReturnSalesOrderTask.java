package cc.shunfu.bigdata.task;

import cc.shunfu.bigdata.model.entity.ReturnSalesOrderEntity;
import cc.shunfu.bigdata.model.mapper.ReturnSalesOrderMapper;
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
 * 销售退货单获取，推送
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-16
 */
@Log4j2
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync
public class ReturnSalesOrderTask {

    @Autowired
    SalesOrderService salesOrderService;

    private SqlSessionFactory sqlSessionFactory;
    final LocalDateTime today = LocalDateTime.now();
    final String todayString = formatDateTime(today, "yyyy-MM-dd");

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
    @Scheduled(cron = "0 0 23 * * ? ")
    public void getReturnSales() {
        List<ReturnSalesOrderEntity> salesOrderEntities = new ArrayList<>();

        String fieldKeys = "FId,FBillNo,FDocumentStatus.FCaption,FApproveDate,FRetcustId.FNumber,FRetcustId.FName,FMaterialId.FNumber,FMaterialId.FName,FRealQty,FTaxPrice,FAllAmount_LC,FSaleOrgId.FName";
        LinkedList<String> queryFilters = new LinkedList<>();

        queryFilters.add(String.format("FDocumentStatus = '%s'", "C"));
        queryFilters.add(String.format("FSaleOrgId.FName = '%s'", "安徽舜富精密科技股份有限公司"));
        queryFilters.add(String.format("FApproveDate >= '%s'", todayString + " 00:00:00"));
        queryFilters.add(String.format("FApproveDate <= '%s'", todayString + " 23:59:59"));
        String filterStr = String.join(" and ", queryFilters);
        try {

            getK3CloudData(ReturnSalesOrderEntity.class, filterStr, fieldKeys, salesOrderEntities, "SAL_RETURNSTOCK", "FApproveDate Desc");

            // 处理获取到的所有数据
            SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            ReturnSalesOrderMapper returnSalesOrderMapper = sqlSession.getMapper(ReturnSalesOrderMapper.class);
            salesOrderEntities.forEach(returnSalesOrderMapper::insertOrUpdateReturnSalesOrder);
            sqlSession.commit();
            sqlSession.clearCache();
            log.info("K3Cloud数据同步完成!");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @Async
    @Scheduled(cron = "0 05 23 * * ? ")
    public void sendReturnSales() {
        int page = 0;
        while (true) {
            List<ReturnSalesOrderEntity> ReturnRalesOrders = salesOrderService.getReturnSalesOrders(todayString, todayString, page * 300, 300);
            if (ReturnRalesOrders.isEmpty()) {
                log.info("氚云数据同步完成!");
                break;
            }

            List<String> jsonArray = new ArrayList<>();
            for (ReturnSalesOrderEntity returnSalesOrder : ReturnRalesOrders) {
                JSONObject jsonObject = new JSONObject();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(returnSalesOrder.getApproveDate());
                jsonObject.put("F0000039", formatDate(returnSalesOrder.getApproveDate(), "yyyy-MM-dd"));
                jsonObject.put("F0000040", formatDate(returnSalesOrder.getApproveDate(), "yyyy-MM"));
                jsonObject.put("F0000032", calendar.get(Calendar.YEAR));
                jsonObject.put("F0000041", returnSalesOrder.getCustomerId());
                jsonObject.put("F0000038", Math.abs(returnSalesOrder.getTotalAmountLC()) * -1);
                jsonObject.put("F0000037", returnSalesOrder.getTaxPrice());
                jsonObject.put("F0000036", Math.abs(returnSalesOrder.getRealQuantity()) * -1);
                jsonObject.put("F0000019", returnSalesOrder.getCustomerName());
                jsonObject.put("F0000034", returnSalesOrder.getMaterialId());
                jsonObject.put("F0000035", returnSalesOrder.getMaterialName());
                jsonObject.put("F0000042", returnSalesOrder.getId());
                jsonObject.put("F0000043", "销售退货");
                jsonArray.add(jsonObject.toString());
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ActionName", "CreateBizObjects");// 调用的方法名
            paramMap.put("SchemaCode", "D148577f5c4122e303e41ec80a185d139afa45b"); // 表单编码
            paramMap.put("BizObjectArray", jsonArray.toArray());// BizObject[]对象的json数组
            // 例如：F0000001为表单里面的控件id
            paramMap.put("IsSubmit", "true");// 为true时创建生效数据，false 为草稿数据 默认为false
            String CreateStr = JSONObject.valueToString(paramMap);

            // 请求接口
            doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);

            page++;
        }
    }
}

