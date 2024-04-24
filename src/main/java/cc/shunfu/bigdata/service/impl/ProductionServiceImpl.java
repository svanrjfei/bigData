package cc.shunfu.bigdata.service.impl;

import cc.shunfu.bigdata.config.IotDBSessionConfig;
import cc.shunfu.bigdata.model.entity.DeviceRecordingEntity;
import cc.shunfu.bigdata.model.entity.ProductionEfficiencyEntity;
import cc.shunfu.bigdata.model.entity.SalesOrderEntity;
import cc.shunfu.bigdata.model.mapper.DeviceRecordingMapper;
import cc.shunfu.bigdata.model.mapper.ProductionEfficiencyMapper;
import cc.shunfu.bigdata.model.mapper.SalesOrderMapper;
import cc.shunfu.bigdata.model.param.IotDbParam;
import cc.shunfu.bigdata.model.param.ReportingForWorkParams;
import cc.shunfu.bigdata.service.ProductionService;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.iotdb.session.pool.SessionDataSetWrapper;
import org.apache.iotdb.tsfile.read.common.Field;
import org.apache.iotdb.tsfile.read.common.RowRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ProductionServiceImpl implements ProductionService {

    @Autowired
    private IotDBSessionConfig iotDBSessionConfig;

    @Autowired
    private ProductionEfficiencyMapper productionEfficiencyMapper;

    @Autowired
    private DeviceRecordingMapper deviceRecordingMapper;


    /**
     * @param reportingForWorkParams 报工数据
     * @return ProductionEfficiency
     * @author svanrj
     * @date 2024/4/12
     */
    @Override
    public ProductionEfficiencyEntity productionEfficiency(ReportingForWorkParams reportingForWorkParams) throws Exception {
//        生产效率初始化
        ProductionEfficiencyEntity equipmentResult = new ProductionEfficiencyEntity();
//        查询上下模记录
        DeviceRecordingEntity deviceRecordingEntity = deviceRecordingMapper.queryDevice(reportingForWorkParams);
//        指定日期格式
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        equipmentResult.setBatch(reportingForWorkParams.getBatch());
        equipmentResult.setProduct(reportingForWorkParams.getProduct());
        equipmentResult.setQualifiedCount(reportingForWorkParams.getQualifiedQuantity());
        equipmentResult.setManualCount(reportingForWorkParams.getNumberOfPeople());
        equipmentResult.setStartTime(deviceRecordingEntity.getBeginTime());
        equipmentResult.setEndTime(deviceRecordingEntity.getEndTime());

        IotDbParam iotDbParam = new IotDbParam();
        iotDbParam.setSn("10004_1");
//        iotDbParam.setStartTime(inputDateFormat.format(deviceRecordingEntity.getBeginTime()));
//        iotDbParam.setEndTime(inputDateFormat.format(deviceRecordingEntity.getEndTime()));

        iotDbParam.setStartTime("2024-04-12 10:00:00");
        iotDbParam.setEndTime("2024-04-12 11:00:00");

//        查询设备运行数据
        queryDataFromIotDb(iotDbParam, equipmentResult);
//        插入生产效率到数据库
        productionEfficiencyMapper.insertProductionEfficiency(equipmentResult);

        return equipmentResult;
    }

    private void queryDataFromIotDb(IotDbParam iotDbParam, ProductionEfficiencyEntity equipmentResult) throws Exception {
        String originalFormat = "yyyy-MM-dd HH:mm:ss";

        if (null != iotDbParam.getSn()) {
            String sql = "select LAST_VALUE(Number) - FIRST_VALUE(Number) as moldCount, AVG(Cycle) as cycleTime from root.b379c5286f8346e5b03150b6b01cdaa4." + iotDbParam.getSn() + " where time >= " + iotDbParam.getStartTime() + " and time < " + iotDbParam.getEndTime();
            SessionDataSetWrapper sessionDataSet = iotDBSessionConfig.query(sql);

            if (sessionDataSet.hasNext()) {
                RowRecord rowRecord = sessionDataSet.next();
                List<Field> fields = rowRecord.getFields();
                List<String> columnNames = sessionDataSet.getColumnNames();
                List<String> titleList = new ArrayList<>();

                // 排除Time字段 -- 方便后面后面拼装数据
                for (String columnName : columnNames) {
                    String[] temp = columnName.split("\\.");
                    titleList.add(temp[temp.length - 1]);
                }

                // 封装处理数据
                packagingData(equipmentResult, fields, titleList);
            }
        } else {
            log.info("设备号不能为空");
        }
        equipmentResult.setStartTime(new SimpleDateFormat(originalFormat).parse(iotDbParam.getStartTime()));
        equipmentResult.setEndTime(new SimpleDateFormat(originalFormat).parse(iotDbParam.getEndTime()));

    }


    /**
     * 处理数据库数据
     *
     * @param equipmentResult 生产效率数据
     * @param fields          字段
     * @param titleList       表头列表
     * @author svanrj
     * @date 2024/4/11
     */
    private void packagingData(ProductionEfficiencyEntity equipmentResult, List<Field> fields, List<String> titleList) {
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            // 这里的需要按照类型获取
            if (field.getDataType() == null) {
                map.put(titleList.get(i), null);
                continue;
            }
            map.put(titleList.get(i), field.getObjectValue(field.getDataType()).toString());
        }

        equipmentResult.setMoldCount((int) Double.parseDouble(map.get("moldCount")));
        equipmentResult.setCycleTime((int) Double.parseDouble(map.get("cycleTime")));
    }
}

