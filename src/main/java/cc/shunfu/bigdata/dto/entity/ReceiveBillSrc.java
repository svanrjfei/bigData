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
@TableName("receive_bill_src")
public class ReceiveBillSrc {
    // 条目ID
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 单据类型编号
    @TableField("bill_type_id_number")
    private String billTypeIdNumber;

    // 订单单据编号
    @TableField("order_bill_no")
    private String orderBillNo;

    // 日期
    @TableField("date")
    private Date date;

    // 付款单位名称
    @TableField("pay_unit_name")
    private String payUnitName;

    // 付款单位编号
    @TableField("pay_unit_number")
    private String payUnitNumber;

    // 实际收款金额
    @TableField("real_rec_amount_for_s")
    private double realRecAmountForS;

    // 审批日期
    @TableField("approve_date")
    private Date approveDate;
}
