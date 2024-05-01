package cc.shunfu.bigdata.controller;

import cc.shunfu.bigdata.dto.entity.ProductionEfficiency;
import cc.shunfu.bigdata.dto.vo.param.ReportingForWorkParams;
import cc.shunfu.bigdata.dto.vo.result.Response;
import cc.shunfu.bigdata.service.ProductionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
@Tag(name = "生产效率", description = "生产设备效率")
public class ProductionController {

    @Resource
    private ProductionService productionService;

    /**
     * 查询数据
     *
     * @param reportingForWorkParams 报工信息
     */
    @PostMapping("/production/efficiency")
    @Operation(summary = "查询生产效率数据")
    public Response queryDataFromIotDb(@RequestBody ReportingForWorkParams reportingForWorkParams) throws Exception {
        ProductionEfficiency productionEfficiencyEntity = productionService.productionEfficiency(reportingForWorkParams);
        if (productionEfficiencyEntity == null) {
            return Response.fail("设备不存在");
        }
        return Response.success("请求成功", productionEfficiencyEntity);
    }
}
