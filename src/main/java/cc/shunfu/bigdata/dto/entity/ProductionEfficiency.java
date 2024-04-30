package cc.shunfu.bigdata.dto.entity;

import lombok.Data;

import java.util.Date;

/**
 * 生产效率实体类
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-11
 */

@Data
public class ProductionEfficiency {
    // 所在批次
    private String batch;
    // 所在设备
    private String device;
    // 所在产品
    private String product;
    // 生产开始时间
    private Date startTime;
    // 生产结束时间
    private Date endTime;
    // 合模数量
    private int moldCount;
    // 开启增压数量
    private int boostCount;
    // 合格数量
    private int qualifiedCount;
    // 节拍
    private int cycleTime;
    // 人工数
    private int manualCount;
    // 合格率
    private double passRate;
    // 冷模数
    private int coolingCount;
    // 水
    private double waterUsage;
    // 电
    private double electricityUsage;
    // 燃气
    private double gasUsage;
    // 脱模剂
    private String moldReleaseAgent;
    // 压缩空气
    private double compressedAirUsage;
    // 液压油
    private String hydraulicOil;
    // 冲头
    private String punch;
    // 五金配件
    private String hardwareAccessories;
}
