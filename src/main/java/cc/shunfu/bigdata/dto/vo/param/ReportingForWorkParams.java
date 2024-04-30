package cc.shunfu.bigdata.dto.vo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 报工信息参数接收类
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-12
 */

@Data
@Schema(name = "报工信息",description = "报工信息参数接收类")
public class ReportingForWorkParams {

    @Schema(description = "所在批次")
    // 所在批次
    private String batch;

    @Schema(description = "所在设备")
    // 所在设备
    private String device;

    @Schema(description = "产品")
    //    产品
    private String product;

    @Schema(description = "人数")
    //    人数
    private int numberOfPeople;

    @Schema(description = "合格数量")
    //    合格数量
    private int qualifiedQuantity;
}
