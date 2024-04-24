
package cc.shunfu.bigdata.config;

import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.pool.SessionDataSetWrapper;
import org.apache.iotdb.session.pool.SessionPool;
import org.apache.iotdb.tsfile.file.metadata.enums.TSDataType;
import org.apache.iotdb.tsfile.write.record.Tablet;
import org.apache.iotdb.tsfile.write.schema.MeasurementSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

@Component
@Configuration
public class IotDBSessionConfig {

    private static final Logger log = LoggerFactory.getLogger(IotDBSessionConfig.class);

    @Value("${spring.iotdb.username:root}")
    private String username;

    @Value("${spring.iotdb.password:root}")
    private String password;

    @Value("${spring.iotdb.ip:127.0.0.1}")
    private String ip;

    @Value("${spring.iotdb.port:6667}")
    private int port;

    @Value("${spring.iotdb.maxSize:10}")
    private int maxSize;

    public static SessionPool sessionPool;

    @Bean
    public SessionPool getSessionPool() {
        if (sessionPool == null) {
            sessionPool = new SessionPool(ip, port, username, password, maxSize);
        }

        return sessionPool;
    }

    public void insertRecordType(String deviceId, Long time, List<String> measurementsList, TSDataType type, List<Object> valuesList) throws StatementExecutionException, IoTDBConnectionException, ServerException, IoTDBConnectionException, StatementExecutionException {
        if (measurementsList.size() != valuesList.size()) {
            throw new ServerException("measurementsList 与 valuesList 值不对应");
        }
        List<TSDataType> types = new ArrayList<>();
        measurementsList.forEach(item -> {
            types.add(type);
        });
        sessionPool.insertRecord(deviceId, time, measurementsList, types, valuesList);
    }

    /**
     * description: 带有数据类型的添加操作 - insertRecord没有指定类型
     *
     * @param deviceId:节点路径如：root.a1eaKSRpRty.CA3013A303A25467
     * @param time:时间戳
     * @param measurementsList：物理量                             即：属性
     * @param valuesList：属性值                                   --- 属性必须与属性值一一对应
     * @return
     */
    public void insertRecord(String deviceId, Long time, List<String> measurementsList, List<String> valuesList) throws StatementExecutionException, IoTDBConnectionException, ServerException {
        if (measurementsList.size() == valuesList.size()) {
            sessionPool.insertRecord(deviceId, time, measurementsList, valuesList);
        } else {
            log.error("measurementsList 与 valuesList 值不对应");
        }
    }

    /**
     * description: 批量插入
     */
    public void insertRecords(List<String> deviceIdList, List<Long> timeList, List<List<String>> measurementsList, List<List<String>> valuesList) throws StatementExecutionException, IoTDBConnectionException, ServerException {
        if (measurementsList.size() == valuesList.size()) {
            sessionPool.insertRecords(deviceIdList, timeList, measurementsList, valuesList);
        } else {
            log.error("measurementsList 与 valuesList 值不对应");
        }
    }

    /**
     * description: 插入操作
     *
     * @param deviceId:节点路径如：root.a1eaKSRpRty.CA3013A303A25467
     * @param time:时间戳
     * @param schemaList:                                      属性值 + 数据类型 例子： List<MeasurementSchema> schemaList = new ArrayList<>();  schemaList.add(new MeasurementSchema("breath", TSDataType.INT64));
     * @param maxRowNumber：
     * @return
     */
    public void insertTablet(String deviceId, Long time, List<MeasurementSchema> schemaList, List<Object> valueList, int maxRowNumber) throws StatementExecutionException, IoTDBConnectionException {

        Tablet tablet = new Tablet(deviceId, schemaList, maxRowNumber);
        // 向iotdb里面添加数据
        int rowIndex = tablet.rowSize++;
        tablet.addTimestamp(rowIndex, time);
        for (int i = 0; i < valueList.size(); i++) {
            tablet.addValue(schemaList.get(i).getMeasurementId(), rowIndex, valueList.get(i));
        }
        if (tablet.rowSize == tablet.getMaxRowNumber()) {
            sessionPool.insertTablet(tablet, true);
            tablet.reset();
        }
        if (tablet.rowSize != 0) {
            sessionPool.insertTablet(tablet);
            tablet.reset();
        }
    }

    /**
     * description: 根据SQL查询
     */
    public SessionDataSetWrapper query(String sql) throws StatementExecutionException, IoTDBConnectionException {
        return sessionPool.executeQueryStatement(sql);
    }

    /**
     * description: 删除分组 如 root.a1eaKSRpRty
     *
     * @param groupName：分组名称
     * @return
     */
    public void deleteStorageGroup(String groupName) throws StatementExecutionException, IoTDBConnectionException {
        sessionPool.deleteStorageGroup(groupName);
    }

    /**
     * description: 根据Time series删除  如：root.a1eaKSRpRty.CA3013A303A25467.breath  （个人理解：为具体的物理量）
     */
    public void deleteTimeseries(String timeseries) throws StatementExecutionException, IoTDBConnectionException {
        sessionPool.deleteTimeseries(timeseries);
    }

    /**
     * description: 根据Time series批量删除
     */
    public void deleteTimeserieList(List<String> timeseriesList) throws StatementExecutionException, IoTDBConnectionException {
        sessionPool.deleteTimeseries(timeseriesList);
    }

    /**
     * description: 根据分组批量删除
     */
    public void deleteStorageGroupList(List<String> storageGroupList) throws StatementExecutionException, IoTDBConnectionException {
        sessionPool.deleteStorageGroups(storageGroupList);
    }

    /**
     * description: 根据路径和结束时间删除 结束时间之前的所有数据
     */
    public void deleteDataByPathAndEndTime(String path, Long endTime) throws StatementExecutionException, IoTDBConnectionException {
        sessionPool.deleteData(path, endTime);
    }

    /**
     * description: 根据路径集合和结束时间批量删除 结束时间之前的所有数据
     */
    public void deleteDataByPathListAndEndTime(List<String> pathList, Long endTime) throws StatementExecutionException, IoTDBConnectionException {
        sessionPool.deleteData(pathList, endTime);
    }

    /**
     * description: 根据路径集合和时间段批量删除
     */
    public void deleteDataByPathListAndTime(List<String> pathList, Long startTime, Long endTime) throws StatementExecutionException, IoTDBConnectionException {
        sessionPool.deleteData(pathList, startTime, endTime);
    }
}


