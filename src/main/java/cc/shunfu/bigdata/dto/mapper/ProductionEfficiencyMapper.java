package cc.shunfu.bigdata.dto.mapper;

import cc.shunfu.bigdata.dto.entity.ProductionEfficiency;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-11
 */

@Mapper
public interface ProductionEfficiencyMapper extends BaseMapper<ProductionEfficiency> {


    int insertProductionEfficiency(ProductionEfficiency productionEfficiencyEntity);
}
