package cc.shunfu.bigdata.task;

import cc.shunfu.bigdata.dto.entity.Order;
import cc.shunfu.bigdata.dto.entity.SalesOrder;
import cc.shunfu.bigdata.dto.mapper.OrderMapper;
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
public class OrderTask {

    @Autowired
    OrderMapper orderMapper;

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
    public void getOrder() {
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");

        List<Order> orderEntities = new ArrayList<>();
        String fieldKeys = "FSaleOrderEntry_FEntryId,FBillNo, FBillTypeID.FName,FSaleDeptId.FName,FSalerId.FName,FCustId.FName,FCustId.FNumber,FRecConditionId.FName,FMaterialId.FNumber,FMaterialName,FMaterialModel,FQty,FPrice,FTaxPrice,FEntryTaxRate,FEntryTaxAmount,FAmount_LC,FAllAmount_LC,FDeliveryDate,FApproveDate";
        LinkedList<String> queryFilters = new LinkedList<>();

        queryFilters.add(String.format("FApproveDate >= '%s'", todayString + " 00:00:00"));
        queryFilters.add(String.format("FApproveDate <= '%s'", todayString + " 23:59:59"));
        String filterStr = String.join(" and ", queryFilters);
        // 处理获取到的所有数据


        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {

            getK3CloudData(Order.class, filterStr, fieldKeys, orderEntities, "SAL_SaleOrder", "FApproveDate Desc");

            OrderMapper OrderMapperNew = sqlSession.getMapper(OrderMapper.class);
            orderEntities.forEach(OrderMapperNew::insertOrUpdateOrder);
            sqlSession.commit();
            sqlSession.clearCache();

            log.info("K3Cloud数据同步完成!" + "共同步" + orderEntities.size() + "条数据");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async
    public void sendOrder() {
        int page = 0;
        int count = 0;
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");
        while (true) {
            List<Order> salesOrders = salesOrderService.getOrders(todayString + " 00:00:00", todayString + " 23:59:59", page * 300, 300);

            if (salesOrders.isEmpty()) {
                log.info("氚云数据同步完成!" + "共同步" + count + "条数据");
                break;
            }
            count += salesOrders.size();

            List<String> jsonArray = new ArrayList<>();
            for (Order order : salesOrders) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("F0000011", formatDate(order.getApproveDate(), "yyyy-MM-dd"));
                jsonObject.put("F0000071", order.getBillNo());
                jsonObject.put("F0000068", order.getBillTypeIDName());
                jsonObject.put("F0000041", order.getBillTypeIDName());
                jsonObject.put("F0000073", order.getSaleDeptIdName());
                jsonObject.put("F0000074", order.getSalerIdName());
                jsonObject.put("F0000072", order.getCustIdName());
                jsonObject.put("F0000094", order.getCustIdNumber());
                jsonObject.put("F0000075", order.getRecConditionIdName());
                jsonObject.put("F0000076", order.getMaterialIdNumber());
                jsonObject.put("F0000077", order.getMaterialName());
                jsonObject.put("F0000079", order.getMaterialModel());
                jsonObject.put("F0000078", order.getQty());
                jsonObject.put("F0000080", order.getPrice());
                jsonObject.put("F0000081", order.getTaxPrice());
                jsonObject.put("F0000090", order.getEntryTaxRate());
                jsonObject.put("F0000091", order.getEntryTaxAmount());
                jsonObject.put("F0000092", order.getAmountLC());
                jsonObject.put("F0000093", order.getAllAmountLC());
                jsonObject.put("F0000082", formatDate(order.getDeliveryDate(), "yyyy-MM-dd"));
                jsonArray.add(jsonObject.toString());
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ActionName", "CreateBizObjects");// 调用的方法名
            paramMap.put("SchemaCode", "D14857748d1d8a6433e4ab7afbc20ce27252d86"); // 表单编码

            paramMap.put("BizObjectArray", jsonArray.toArray());
            paramMap.put("IsSubmit", "true");
            String CreateStr = JSONObject.valueToString(paramMap);

            // 请求接口
            doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);

            page++;
        }
    }
}

