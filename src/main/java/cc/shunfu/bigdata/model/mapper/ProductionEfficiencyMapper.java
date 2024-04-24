package cc.shunfu.bigdata.model.mapper;

import cc.shunfu.bigdata.model.entity.ProductionEfficiencyEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-11
 */

@Mapper
public interface ProductionEfficiencyMapper {


    int insertProductionEfficiency(ProductionEfficiencyEntity productionEfficiencyEntity);
}
