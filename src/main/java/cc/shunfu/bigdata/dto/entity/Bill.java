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
@TableName("bill")
public class Bill {
    // 单据编号
    @TableId(value = "bill_no", type = IdType.AUTO)
    private String billNo;

    // 单据类型名称
    @TableField("bill_type_name")
    private String billTypeName;

    // 客户名称
    @TableField("customer_name")
    private String customerName;

    // 客户编号
    @TableField("customer_number")
    private String customerNumber;

    // 总金额名称
    @TableField("all_amount")
    private double allAmount;

    // 结束日期
    @TableField("end_date")
    private Date endDate;

    // 审批日期
    @TableField("approve_date")
    private Date approveDate;
}
