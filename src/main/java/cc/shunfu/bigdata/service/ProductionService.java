package cc.shunfu.bigdata.service;

import cc.shunfu.bigdata.dto.entity.ProductionEfficiency;
import cc.shunfu.bigdata.dto.vo.param.ReportingForWorkParams;
import org.springframework.stereotype.Service;

@Service
public interface ProductionService {

    /**
     * 生成生产效率工
     *
     * @param reportingForWorkParams 查询参数
     * @return 生产效率数据
     * @throws Exception 查询过程中发生异常时抛出
     */

    ProductionEfficiency productionEfficiency(ReportingForWorkParams reportingForWorkParams) throws Exception;

}
