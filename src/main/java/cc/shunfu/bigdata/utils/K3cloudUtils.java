package cc.shunfu.bigdata.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-23
 */

@Log4j2
public class K3cloudUtils {

    static final K3CloudApi client = new K3CloudApi();

    public static String getString(String filterStr, int page, String FieldKeys, String formId, String orderString) {
        JSONObject requestData = new JSONObject();

        requestData.put("FormId", formId);
        requestData.put("FieldKeys", FieldKeys);
        requestData.put("FilterString", filterStr);
        requestData.put("OrderString", orderString);
        requestData.put("TopRowCount", 0);
        requestData.put("StartRow", page * 2000);
        requestData.put("Limit", 2000);
        requestData.put("SubSystemId", "");

        return requestData.toString();
    }

    /**
     * 从K3Cloud获取数据，并转换为指定类型的对象列表
     *
     * @param clazz     目标对象的Class类型
     * @param filterStr 过滤条件字符串
     * @param fieldKeys 字段键字符串
     * @param listMap   存储转换后对象的列表
     * @throws Exception 抛出可能发生的异常
     */
    public static <T> void getK3CloudData(Class<T> clazz, String filterStr, String fieldKeys, List<T> listMap, String formId, String orderString) throws Exception {
        int page = 0;

        // 获取目标对象的所有字段
        Field[] fields = clazz.getDeclaredFields();

        while (true) {
            // 获取K3Cloud数据的JSON字符串
            String jsonData = K3cloudUtils.getString(filterStr, page, fieldKeys, formId, orderString);

            // 调用K3Cloud接口获取数据
            List<List<Object>> resultJson = client.executeBillQuery(jsonData);

            log.info("获取金蝶数据：" + resultJson);

            // 若返回的数据为空，则数据同步完成，跳出循环
            if (resultJson.isEmpty()) {
                log.info("金蝶数据同步完成！");
                break;
            }

            // 遍历结果集，将每条数据转换为目标对象
            resultJson.forEach(list -> {
                Map<String, Object> map = new LinkedHashMap<>();

                // 将数据列表中的每个元素按字段顺序转换为Map
                ForEachUtils.forEach(0, list, (index, item) -> {
                    map.put(fields[index].getName(), list.get(index));
                });

                // 使用Gson将Map转换为目标对象
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                T salesOrder = gson.fromJson(gson.toJson(map), clazz);
                listMap.add(salesOrder); // 将转换后的对象添加到结果列表中
            });

            page++; // 更新页数
        }
    }
}
