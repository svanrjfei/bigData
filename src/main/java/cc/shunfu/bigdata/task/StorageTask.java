package cc.shunfu.bigdata.task;

import cc.shunfu.bigdata.dto.entity.Warehouse;
import cc.shunfu.bigdata.dto.mapper.WarehouseMapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import static cc.shunfu.bigdata.utils.ChuanUtils.doPost;
import static cc.shunfu.bigdata.utils.DateUtils.formatDate;
import static cc.shunfu.bigdata.utils.DateUtils.formatDateTime;

/**
 * 入库单的获取🐟推送
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-16
 */
@Log4j2
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync
public class StorageTask {

    @Autowired
    WarehouseMapper warehouseMapper;

    final LocalDateTime today = LocalDateTime.now();
    final String todayString = formatDateTime(today, "yyyy-MM-dd");


    @Async
    public void sendStorage() {
        int page = 0;
        int count = 0;
        while (true) {
            List<Warehouse> salesOrders = warehouseMapper.getWarehouses("2024-05-01" + " 00:00:00", todayString + " 23:59:59", page * 150, 150, "CPK");
            List<Warehouse> warehouses = warehouseMapper.getOverWareHouse("2024-05-01" + " 00:00:00", todayString + " 23:59:59", page * 150, 150, "CPK");

            List<Warehouse> sets = Stream.of(salesOrders, warehouses).flatMap(Collection::stream).toList();

            if (salesOrders.isEmpty()) {
                log.info("氚云数据同步完成!" + "共同步" + count + "条数据");
                break;
            }
            count += sets.size();

            List<String> jsonArray = new ArrayList<String>();
            for (Warehouse warehouse : sets) {
                if (!Objects.equals(warehouse.getAreaCode(), "CPK"))
                    continue;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("F0000060", "三大事业部");
                jsonObject.put("F0000058", warehouse.getSku());
                jsonObject.put("F0000061", warehouse.getSkuName());
                jsonObject.put("F0000059", warehouse.getQty());
                jsonObject.put("F0000002", formatDate(warehouse.getLottable02(), "yyyy-MM-dd"));
                jsonArray.add(jsonObject.toString());
            }

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ActionName", "CreateBizObjects");// 调用的方法名
            paramMap.put("SchemaCode", "D1485777fdfa3e4b8fc49f8bd11239329d23cac"); // 表单编码

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
        log.info(todayString);
    }

    @Async
    public void testTask2() {
        log.info("任务测试2");
    }
}

