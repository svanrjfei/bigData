package cc.shunfu.bigdata.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName("process_output")
@Schema(description = "Process Output Entity")
public class ProcessOutput {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "ID", example = "1")
    private String id;

    @TableField("Sku")
    @Schema(description = "SKU")
    private String sku;

    @TableField("SkuName")
    @Schema(description = "SKU Name")
    private String skuName;

    @TableField("Qty")
    @Schema(description = "Quantity")
    private Integer qty;

    @TableField("Lottable02")
    @Schema(description = "Lottable02")
    private Date lottable02;

    @TableField("AreaCode")
    @Schema(description = "Area Code")
    private String areaCode;

    @TableField("type")
    @Schema(description = "Type", defaultValue = "越库")
    private String type = "越库";

    @TableField("product_name")
    @Schema(description = "Product Name")
    private String productName;

    @TableField("product_id")
    @Schema(description = "Product ID")
    private String productId;

    @TableField("depa")
    @Schema(description = "Department")
    private String depa;

    @TableField("post_processing")
    @Schema(description = "后处理")
    private Float postProcessing;

    @TableField("machine_addition")
    @Schema(description = "机加")
    private Float machineAddition;

    @TableField("friction_welding")
    @Schema(description = "摩擦焊")
    private Float frictionWelding;

    @TableField("cleaning")
    @Schema(description = "清洗")
    private Float cleaning;

    @TableField("side_leakage")
    @Schema(description = "侧漏")
    private Float sideLeakage;

    @TableField("price")
    @Schema(description = "产品单价（最新）")
    private Float price;

    @TableField("surface_treatment")
    @Schema(description = "表面处理")
    private Float surfaceTreatment;

    @TableField("assemble")
    @Schema(description = "8-1.装配料费")
    private Float assemble;

    @TableField("package_material")
    @Schema(description = "9.包材费用")
    private Float packageMaterial;

    @TableField("transport")
    @Schema(description = "10.运输费用（不算三大事业部收入）")
    private Float transport;

    @TableField("other_expenses")
    @Schema(description = "11.其他费用")
    private Float otherExpenses;

    @TableField("material_cost")
    @Schema(description = "0.压铸材料费用（不算三大事业部收入）")
    private Float materialCost;

    @TableField("outside_purchase")
    @Schema(description = "12.外购金额（如外购材料费,毛坯，成品等）")
    private Float outsidePurchase;

    @TableField("assembly_cost")
    @Schema(description = "8-2.装配工费（含全检 ，测漏，包装等人工费用）")
    private Float assemblyCost;

    @TableField("management")
    @Schema(description = "13.管理费用（不算三大事业部收入）")
    private Float management;

    @TableField("profit")
    @Schema(description = "利润")
    private Float profit;

    @TableField("tooling")
    @Schema(description = "16.分摊金额（工装）")
    private Float tooling;

    @TableField("apportion")
    @Schema(description = "15.分摊金额（模具）（不算三大事业部收入）")
    private Float apportion;

    @TableField("ObjectId")
    @Schema(description = "唯一值")
    private String objectId;

    @TableField("mold_closing_cost")
    @Schema(description = "1.压铸合模费（不算三大事业部收入）")
    private Float moldClosingCost;
}
