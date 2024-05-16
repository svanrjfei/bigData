package cc.shunfu.bigdata.dto.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@TableName("crm_rx_config")
@Component
public class CrmRxConfig {
    @TableId("ObjectId")
    private String objectId;

    @TableField("mb_date")
    private Date mbDate;

    @TableField("yz_cz")
    private Float yzCz;

    @TableField("yz_rj")
    private Float yzRj;

    @TableField("hd_cz")
    private Float hdCz;

    @TableField("hd_rj")
    private Float hdRj;

    @TableField("cz")
    private Float cz;

    @TableField("rj")
    private Float rj;

    @TableField("type")
    private String type;

    @TableField("jj_cz")
    private Float jjCz;

    @TableField("jj_rj")
    private Float jjRj;

    @TableField("zp_cz")
    private Float zpCz;

    @TableField("zp_rj")
    private Float zpRj;

}
