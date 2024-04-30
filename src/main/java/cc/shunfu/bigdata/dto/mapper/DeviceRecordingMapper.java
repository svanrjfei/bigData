package cc.shunfu.bigdata.dto.mapper;

import cc.shunfu.bigdata.dto.entity.DeviceRecording;
import cc.shunfu.bigdata.dto.vo.param.ReportingForWorkParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-12
 */

@Mapper
public interface DeviceRecordingMapper extends BaseMapper<DeviceRecording> {
    DeviceRecording queryDevice(ReportingForWorkParams reportingForWorkParams);
}
