package cc.shunfu.bigdata.model.mapper;

import cc.shunfu.bigdata.model.entity.ReturnSalesOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-18
 */

@Mapper
public interface ReturnSalesOrderMapper {
    int insertOrUpdateReturnSalesOrder(ReturnSalesOrderEntity returnSalesOrder);

    List<ReturnSalesOrderEntity> getReturnSalesOrder(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
}
