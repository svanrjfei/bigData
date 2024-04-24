package cc.shunfu.bigdata.controller;

import cc.shunfu.bigdata.model.param.ReportingForWorkParams;
import cc.shunfu.bigdata.service.ProductionService;
import cc.shunfu.bigdata.model.result.ResponseData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("/api")
public class ProductionController {

    @Resource
    private ProductionService productionService;

    /**
     * 查询数据
     *
     * @param reportingForWorkParams 报工信息
     */
    @PostMapping("/production/efficiency")
    public ResponseData queryDataFromIotDb(@RequestBody ReportingForWorkParams reportingForWorkParams) throws Exception {
        return ResponseData.success("请求成功", productionService.productionEfficiency(reportingForWorkParams));
    }
}
