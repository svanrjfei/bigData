package cc.shunfu.bigdata.dto.vo.result;

import cc.shunfu.bigdata.dto.entity.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-06-05
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderCustomer extends Order {

    private String objectId;
    private String dept;
}
