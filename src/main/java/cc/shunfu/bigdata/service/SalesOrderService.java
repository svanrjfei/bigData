package cc.shunfu.bigdata.service;

import cc.shunfu.bigdata.model.entity.ReturnSalesOrderEntity;
import cc.shunfu.bigdata.model.entity.SalesOrderEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 金蝶数据-销售出库
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-20
 */
@Service
public interface SalesOrderService {
    /**
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param page      页数
     * @param limit     每页数量
     * @return java.util.List<cc.shunfu.bigdata.model.entity.SalesOrderEntity>
     * @author svanrj
     * @date 2024/4/20
     */
    List<SalesOrderEntity> getSalesOrders(String startDate, String endDate, int page, int limit);

    /**
     * 获取销售退货单
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param offset    跳过多少条数据
     * @param limit     每页数量
     * @return List<ReturnSalesOrderEntity>
     * @author svanrj
     * @date 2024/4/23
     */

    List<ReturnSalesOrderEntity> getReturnSalesOrders(String startDate, String endDate, int offset, int limit);

}
