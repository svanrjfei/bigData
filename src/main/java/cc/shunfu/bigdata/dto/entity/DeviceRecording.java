package cc.shunfu.bigdata.dto.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 上下模记录表
 * Represents the device recording entity in the database.
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-12
 */
@Data
@TableName("device_recording") // 假设数据库中的表名是 device_recording
public class DeviceRecording {

    // 设备id
    @TableId("cast_device_id")
    private String castDeviceId;

    // 开始时间
    @TableField("begin_time")
    private Date beginTime;

    // 结束时间
    @TableField("end_time")
    private Date endTime;
}
