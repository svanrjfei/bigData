package cc.shunfu.bigdata.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-18
 */
@Data
@TableName("sales_order") // 假设数据库中的表名是 sales_order
public class SalesOrder {
    // 订单ID
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 订单编号
    @TableField("bill_no")
    private String billNo;

    // 文档状态
    @TableField("document_status")
    private String documentStatus;

    // 日期
    @TableField("date")
    private Date date;

    // 物料编号
    @TableField("material_number")
    private String materialNumber;

    // 物料名称
    @TableField("material_name")
    private String materialName;

    // 物料型号
    @TableField("model")
    private String model;

    // 计量单位
    @TableField("unit_name")
    private String unitName;

    // 数量
    @TableField("quantity")
    private double quantity;

    // 委托价格
    @TableField("consign_price")
    private double consignPrice;

    // 委托金额
    @TableField("consign_amount")
    private double consignAmount;

    // 税前价格
    @TableField("tax_price")
    private double taxPrice;

    // 总金额
    @TableField("all_amount")
    private double allAmount;

    // 来源仓库名称
    @TableField("src_stock_name")
    private String srcStockName;

    // 来源库位名称
    @TableField("src_stock_loc_name")
    private String srcStockLocName;

    // 目标仓库名称
    @TableField("dest_stock_name")
    private String destStockName;

    // 目标库位名称
    @TableField("dest_stock_loc_name")
    private String destStockLocName;

    // 转移方向
    @TableField("transfer_direct")
    private String transferDirect;

    // Oracle字段1
    @TableField("ora_text1")
    private String oraText1;

    // 来源单据类型ID
    @TableField("src_bill_type_id")
    private String srcBillTypeId;

    // 订单类型名称
    @TableField("order_type_name")
    private String orderTypeName;

    // 创建者名称
    @TableField("creator_name")
    private String creatorName;

    // 审批者名称
    @TableField("approver_name")
    private String approverName;

    // 金额
    @TableField("amount")
    private double amount;

    // 附件数量
    @TableField("attachment_count")
    private int attachmentCount;

    // 价格
    @TableField("price")
    private double price;

    // 出库数量
    @TableField("out_join_qty")
    private double outJoinQty;

    // Oracle字段3
    @TableField("ora_text3")
    private String oraText3;

    // Oracle数量
    @TableField("ora_qty")
    private double oraQty;

    // 二次出库数量
    @TableField("sec_out_join_qty")
    private double secOutJoinQty;

    // 汇率
    @TableField("exchange_rate")
    private double exchangeRate;

    // 汇率类型名称
    @TableField("exchange_type_name")
    private String exchangeTypeName;

    // 结算货币名称
    @TableField("settle_curr_name")
    private String settleCurrName;

    // 基准货币名称
    @TableField("base_curr_name")
    private String baseCurrName;

    // 修改日期
    @TableField("modify_date")
    private Date modifyDate;

    // 客户编码
    @TableField("customer_id")
    private String customerId;

    @TableField("stock_out_org_name")
    private String stockOutOrgName;
}
