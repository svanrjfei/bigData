package cc.shunfu.bigdata.dto.mapper;

import cc.shunfu.bigdata.dto.entity.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-05-06
 */

@Mapper
public interface WarehouseMapper {

    @Select("select Sku,SkuName,Qty,Lottable02,AreaCode from inventory left join area on inventory.WhseId=area.Id WHERE Lottable02 BETWEEN STR_TO_DATE(#{startDate}, '%Y-%m-%d %H:%i:%s') AND STR_TO_DATE(#{endDate}, '%Y-%m-%d %H:%i:%s') " +
            "  and AreaCode=#{areaCode}     ORDER BY Lottable02 DESC " +
            "        LIMIT #{offset}, #{limit}")
    List<Warehouse> getWarehouses(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("offset") int offset,
            @Param("limit") int limit, @Param("areaCode") String areaCode);

    @Select("select MaterialCode as Sku,MaterialName as SkuName,TpQty as Qty,CreatedTime as Lottable02,AreaCode from overwarehouse WHERE CreatedTime BETWEEN STR_TO_DATE(#{startDate}, '%Y-%m-%d %H:%i:%s') AND STR_TO_DATE(#{endDate}, '%Y-%m-%d %H:%i:%s') " +
            "  and  AreaCode=#{areaCode}     ORDER BY CreatedTime DESC " +
            "        LIMIT #{offset}, #{limit}")
    List<Warehouse> getOverWareHouse(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("areaCode") String areaCode);
}
