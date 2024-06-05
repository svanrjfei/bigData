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
@TableName("order") // 假设数据库中的表名是 sales_order
public class Order {

    // 订单编号
    @TableId(value = "id", type = IdType.AUTO)
    private String billNo;

    // 订单类型名称
    @TableField("bill_type_id_name")
    private String billTypeIDName;

    // 销售部门名称
    @TableField("sale_dept_id_name")
    private String saleDeptIdName;

    // 销售员名称
    @TableField("saler_id_name")
    private String salerIdName;

    // 客户名称
    @TableField("cust_id_name")
    private String custIdName;

    // 客户编码
    @TableField("cust_id_number")
    private String custIdNumber;

    // 收货条件名称
    @TableField("rec_condition_id_name")
    private String recConditionIdName;

    // 数量
    @TableField("qty")
    private double qty;

    // 总金额（本位币）
    @TableField("all_amount_lc")
    private double allAmountLC;

    // 交货日期
    @TableField("delivery_date")
    private Date deliveryDate;

    // 批准日期
    @TableField("approve_date")
    private Date approveDate;


}
