package cc.shunfu.bigdata.model.param;

import lombok.Data;

/**
 * 报工信息参数接收类
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-12
 */

@Data
public class ReportingForWorkParams {

    // 所在批次
    private String batch;

    // 所在设备
    private String device;

    //    产品
    private String product;

    //    人数
    private int numberOfPeople;

    //    合格数量
    private int qualifiedQuantity;
}
