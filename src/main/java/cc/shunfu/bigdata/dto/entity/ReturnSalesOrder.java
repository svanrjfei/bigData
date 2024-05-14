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
@TableName("return_sales_order") // 假设数据库中的表名是 return_sales_order
public class ReturnSalesOrder {
    // ID
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 退货单号
    @TableField("bill_no")
    private String billNo;

    // 单据状态
    @TableField("document_status_caption")
    private String documentStatusCaption;

    // 批准日期
    @TableField("approve_date")
    private Date approveDate;

    // 客户ID
    @TableField("customer_id")
    private String customerId;

    // 客户名称
    @TableField("customer_name")
    private String customerName;

    // 物料ID
    @TableField("material_id")
    private String materialId;

    // 物料名称
    @TableField("material_name")
    private String materialName;

    // 真实数量
    @TableField("real_quantity")
    private double realQuantity;

    // 单价
    @TableField("tax_price")
    private double taxPrice;

    // 总金额（本地货币）
    @TableField("total_amount_lc")
    private double totalAmountLC;

    // 销售组织名称
    @TableField("sale_org_name")
    private String saleOrgName;
}