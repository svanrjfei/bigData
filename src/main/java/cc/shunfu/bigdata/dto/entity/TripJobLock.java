package cc.shunfu.bigdata.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-30
 */

@Data
@Schema(name = "TripJobLock", description = "定时任务配置")
@TableName("trip_job_lock")
public class TripJobLock {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "is_lock")
    private String isLock;

    @TableField(value = "job_cron")
    private String jobCron;

    @TableField(value = "job_desc")
    private String jobDesc;

    @TableField(value = "method")
    private String method;
}
