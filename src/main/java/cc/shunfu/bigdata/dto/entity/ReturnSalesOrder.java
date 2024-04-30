package cc.shunfu.bigdata.dto.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-18
 */
@Data
public class ReturnSalesOrder {
    //    ID
    private Integer id;
    //    退货单号
    private String billNo;
    //    单据状态
    private String documentStatusCaption;
    // 批准日期
    private Date approveDate;
    // 客户ID
    private String customerId;
    // 客户名称
    private String customerName;
    // 物料ID
    private String materialId;
    // 物料名称
    private String materialName;
    // 真实数量
    private double realQuantity;
    // 单价
    private double taxPrice;
    // 总金额（本地货币）
    private double totalAmountLC;

    private String saleOrgName;
}
