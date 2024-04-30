package cc.shunfu.bigdata.dto.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-18
 */
@Data
public class SalesOrder {
    // 订单ID
    private int id;
    // 订单编号
    private String billNo;
    // 文档状态
    private String documentStatus;
    // 日期
    private Date date;
    // 物料编号
    private String materialNumber;
    // 物料名称
    private String materialName;
    // 物料型号
    private String model;
    // 计量单位
    private String unitName;
    // 数量
    private double quantity;
    // 委托价格
    private double consignPrice;
    // 委托金额
    private double consignAmount;
    // 税前价格
    private double taxPrice;
    // 总金额
    private double allAmount;
    // 来源仓库名称
    private String srcStockName;
    // 来源库位名称
    private String srcStockLocName;
    // 目标仓库名称
    private String destStockName;
    // 目标库位名称
    private String destStockLocName;
    // 转移方向
    private String transferDirect;
    // Oracle字段1
    private String oraText1;
    // 来源单据类型ID
    private String srcBillTypeId;
    // 订单类型名称
    private String orderTypeName;
    // 创建者名称
    private String creatorName;
    // 审批者名称
    private String approverName;
    // 金额
    private double amount;
    // 附件数量
    private int attachmentCount;
    // 价格
    private double price;
    // 出库数量
    private double outJoinQty;
    // Oracle字段3
    private String oraText3;
    // Oracle数量
    private double oraQty;
    // 二次出库数量
    private double secOutJoinQty;
    // 汇率
    private double exchangeRate;
    // 汇率类型名称
    private String exchangeTypeName;
    // 结算货币名称
    private String settleCurrName;
    // 基准货币名称
    private String baseCurrName;
    // 修改日期
    private Date modifyDate;
    // 客户编码
    private String customerId;

    private String stockOutOrgName;
}
