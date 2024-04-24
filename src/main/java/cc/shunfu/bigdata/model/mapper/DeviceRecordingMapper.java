package cc.shunfu.bigdata.model.mapper;

import cc.shunfu.bigdata.model.entity.DeviceRecordingEntity;
import cc.shunfu.bigdata.model.param.ReportingForWorkParams;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-12
 */

@Mapper
public interface DeviceRecordingMapper {
    DeviceRecordingEntity queryDevice(ReportingForWorkParams reportingForWorkParams);
}
