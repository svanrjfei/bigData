package cc.shunfu.bigdata.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 生产效率实体类
 * Represents the production efficiency entity in the database.
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-11
 */
@Data
@TableName("production_efficiency") // 假设数据库中的表名是 production_efficiency
public class ProductionEfficiency {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 所在批次
    @TableField("batch")
    private String batch;

    // 所在设备
    @TableField("device")
    private String device;

    // 所在产品
    @TableField("product")
    private String product;

    // 生产开始时间
    @TableField("start_time")
    private Date startTime;

    // 生产结束时间
    @TableField("end_time")
    private Date endTime;

    // 合模数量
    @TableField("mold_count")
    private int moldCount;

    // 开启增压数量
    @TableField("boost_count")
    private int boostCount;

    // 合格数量
    @TableField("qualified_count")
    private int qualifiedCount;

    // 节拍
    @TableField("cycle_time")
    private int cycleTime;

    // 人工数
    @TableField("manual_count")
    private int manualCount;

    // 合格率
    @TableField("pass_rate")
    private double passRate;

    // 冷模数
    @TableField("cooling_count")
    private int coolingCount;

    // 水
    @TableField("water_usage")
    private double waterUsage;

    // 电
    @TableField("electricity_usage")
    private double electricityUsage;

    // 燃气
    @TableField("gas_usage")
    private double gasUsage;

    // 脱模剂
    @TableField("mold_release_agent")
    private String moldReleaseAgent;

    // 压缩空气
    @TableField("compressed_air_usage")
    private double compressedAirUsage;

    // 液压油
    @TableField("hydraulic_oil")
    private String hydraulicOil;

    // 冲头
    @TableField("punch")
    private String punch;

    // 五金配件
    @TableField("hardware_accessories")
    private String hardwareAccessories;
}
