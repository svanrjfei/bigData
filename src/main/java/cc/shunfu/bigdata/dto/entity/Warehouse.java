package cc.shunfu.bigdata.dto.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-05-06
 */
@Data
@Schema(name = "Warehouse", description = "入库记录")
public class Warehouse {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    private int id;
    @Schema(description = "仓库编码")
    private String AreaCode;
    private String sku;
    private String skuName;
    private int qty;
    private Date lottable02;
}
