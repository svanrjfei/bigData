package cc.shunfu.bigdata.dto.mapper;

import cc.shunfu.bigdata.dto.entity.ReturnSalesOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-18
 */

@Mapper
public interface ReturnSalesOrderMapper extends BaseMapper<ReturnSalesOrder> {
    int insertOrUpdateReturnSalesOrder(ReturnSalesOrder returnSalesOrder);

    List<ReturnSalesOrder> getReturnSalesOrder(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
}
