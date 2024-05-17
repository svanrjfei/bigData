package cc.shunfu.bigdata.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TripJobLock {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "任务id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @TableField(value = "is_lock")
    @Schema(name = "is_lock", description = "是否锁定", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String isLock;

    @TableField(value = "job_cron")
    @Schema(name = "job_cron", description = "定时任务cron表达式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String jobCron;

    @TableField(value = "job_desc")
    @Schema(name = "job_desc", description = "定时任务描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String jobDesc;

    @TableField(value = "method")
    @Schema(name = "method", description = "定时任务方法", requiredMode = Schema.RequiredMode.REQUIRED)
    private String method;
}
