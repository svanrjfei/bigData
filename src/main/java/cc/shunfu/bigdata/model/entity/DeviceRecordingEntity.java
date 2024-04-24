package cc.shunfu.bigdata.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 上下模记录表
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-12
 */

@Data
public class DeviceRecordingEntity {
    //    设备id
    private String castDeviceId;
    //    开始时间
    private Date beginTime;
    //    结束时间
    private Date endTime;
}
