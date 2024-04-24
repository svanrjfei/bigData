package cc.shunfu.bigdata.task;

import cc.shunfu.bigdata.dto.entity.CrmRxConfig;
import cc.shunfu.bigdata.dto.entity.ProcessOutput;
import cc.shunfu.bigdata.dto.mapper.CrmRxConfigMapper;
import cc.shunfu.bigdata.dto.mapper.ProcessOutputMapper;
import cc.shunfu.bigdata.dto.vo.result.ProcessOutputVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static cc.shunfu.bigdata.utils.ChuanUtils.doPost;
import static cc.shunfu.bigdata.utils.DateUtils.formatDate;
import static cc.shunfu.bigdata.utils.DateUtils.formatDateTime;

/**
 * 入库单的获取🐟推送
 *
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-16
 */
@Log4j2
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync
public class StorageTask {

    @Autowired
    ProcessOutputMapper processOutputMapper;

    @Autowired
    CrmRxConfigMapper crmRxConfigMapper;

    /**
     * 定时任务方法用于定时向CRM系统-工序产值数据源推送数据
     *
     * @author svanrj
     * @date 2024/5/16
     */

    @Async
    public void sendStorage() {

        // 获取当前的日期用于获取当日数据，不提取到公共变量的原因是定时任务执行获取不到最新数据
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");
        String[] areaCodes = {"CPK", "ZPK"};

        //用于分页和计数使用
        int page = 0;
        int count = 0;

        // 利用循环分页获取数据，避免数据量过大导致内存溢出
        while (true) {
            List<ProcessOutput> processOutputs = processOutputMapper.getProcessOutput(todayString + " 00:00:00", todayString + " 23:59:59", page * 300, 300, areaCodes);

//            判断数据是否获取完成
            if (processOutputs.isEmpty()) {
                log.info("氚云数据同步完成!" + "共同步" + count + "条数据");
                break;
            }
//            对获取到的数据进行统计
            count += processOutputs.size();

//            组装要推送到CRM的数据
            List<String> jsonArray = new ArrayList<>();
            for (ProcessOutput processOutput : processOutputs) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("F0000060", "三大事业部");
                jsonObject.put("F0000058", processOutput.getSku());
                jsonObject.put("F0000061", processOutput.getSkuName());
                jsonObject.put("F0000059", processOutput.getQty());
                jsonObject.put("F0000105", processOutput.getType());
                jsonObject.put("F0000108", processOutput.getId());
                jsonObject.put("F0000002", formatDate(processOutput.getLottable02(), "yyyy-MM-dd"));
                jsonArray.add(jsonObject.toString());
            }

//            其他需要根据请求发送的数据
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ActionName", "CreateBizObjects");// 调用的方法名
            paramMap.put("SchemaCode", "D1485777fdfa3e4b8fc49f8bd11239329d23cac"); // 表单编码

            paramMap.put("BizObjectArray", jsonArray.toArray());
            paramMap.put("IsSubmit", "true");
            String CreateStr = JSONObject.valueToString(paramMap);

            // 请求接口
            doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);

            page++; // 翻页
        }
    }


    /**
     * 定时任务方法用于定时向CRM系统-工序产值-半成品数据源推送数据
     *
     * @author svanrj
     * @date 2024/5/16
     */

    @Async
    public void sendSemiStorage() {

        // 获取当前的日期用于获取当日数据，不提取到公共变量的原因是定时任务执行获取不到最新数据
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");
        String[] areaCodes = {"BCPK"};

        //用于分页和计数使用
        int page = 0;
        int count = 0;

        // 利用循环分页获取数据，避免数据量过大导致内存溢出
        while (true) {
            List<ProcessOutput> processOutputs = processOutputMapper.getSemiProcessOutput(todayString + " 00:00:00", todayString + " 23:59:59", page * 300, 300, areaCodes);

//            判断数据是否获取完成
            if (processOutputs.isEmpty()) {
                log.info("氚云数据同步完成!" + "共同步" + count + "条数据");
                break;
            }
//            对获取到的数据进行统计
            count += processOutputs.size();

//            组装要推送到CRM的数据
            List<String> jsonArray = new ArrayList<>();
            for (ProcessOutput processOutput : processOutputs) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("F0000060", "压铸车间");
                jsonObject.put("F0000058", processOutput.getSku());
                jsonObject.put("F0000061", processOutput.getSkuName());
                jsonObject.put("F0000059", processOutput.getQty());
                jsonObject.put("F0000105", Objects.equals(processOutput.getType(), "越库") ? "后道半成品越库" : processOutput.getType());
                jsonObject.put("F0000108", processOutput.getId());
                jsonObject.put("F0000002", formatDate(processOutput.getLottable02(), "yyyy-MM-dd"));
                jsonArray.add(jsonObject.toString());
            }

//            其他需要根据请求发送的数据
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ActionName", "CreateBizObjects");// 调用的方法名
            paramMap.put("SchemaCode", "D1485777fdfa3e4b8fc49f8bd11239329d23cac"); // 表单编码

            paramMap.put("BizObjectArray", jsonArray.toArray());
            paramMap.put("IsSubmit", "true");
            String CreateStr = JSONObject.valueToString(paramMap);

            // 请求接口
            doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);

            page++; // 翻页
        }
    }


    /**
     * 定时任务方法用于定时向CRM系统-汽车人效数据源推送数据
     *
     * @author svanrj
     * @date 2024/5
     */
    @Async
    public void sendAutomobileOutputValue() {

        String[] areaCodes = {"CPK", "ZPK"};

        // 获取当前的日期用于获取当日数据，不提取到公共变量的原因是定时任务执行获取不到最新数据
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");

//        获取今日的入库数据
        List<ProcessOutputVO> processOutputs = processOutputMapper.getProcessOutputByType(todayString + " 00:00:00", todayString + " 23:59:59", areaCodes, "汽车");

        List<String> jsonArray = new ArrayList<>();
//        遍历获取到的数据
        for (ProcessOutputVO processOutput : processOutputs) {

//            定义查询人效产值目标的数据条件
            QueryWrapper<CrmRxConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", "汽车");
            queryWrapper.apply("DATE_FORMAT(mb_date, '%Y-%m') = DATE_FORMAT('" + formatDate(processOutput.getDate(), "yyyy-MM-dd") + "', '%Y-%m')");

            CrmRxConfig crmRxConfig = crmRxConfigMapper.selectOne(queryWrapper);    // 查询人效产值目标

            if (crmRxConfig != null) {
                float jjdcl = crmRxConfig.getJjCz() == 0 ? 0 : processOutput.getTotalMachineAddition() / crmRxConfig.getJjCz();  // 机加达成率
                float gxcz = processOutput.getTotalMachineAddition() + processOutput.getTotalFrictionWelding() + processOutput.getTotalCleaning() + processOutput.getTotalSideLeakage() + processOutput.getTotalAssembleCost(); // 工序总产值
                float gxdcl = crmRxConfig.getCz() == 0 ? 0 : gxcz / crmRxConfig.getCz(); // 工序达成率

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("F0000109", "三大事业部");
                jsonObject.put("F0000016", processOutput.getDepa());
                jsonObject.put("F0000002", formatDate(processOutput.getDate(), "yyyy-MM-dd"));
                jsonObject.put("F0000086", processOutput.getTotalFrictionWelding());
                jsonObject.put("F0000087", processOutput.getTotalCleaning());
                jsonObject.put("F0000088", processOutput.getTotalSideLeakage());
                jsonObject.put("F0000089", processOutput.getTotalAssembleCost());
                jsonObject.put("F0000091", crmRxConfig.getJjCz());
                jsonObject.put("F0000103", crmRxConfig.getJjRj());
                jsonObject.put("F0000085", processOutput.getTotalMachineAddition());
                jsonObject.put("F0000080", crmRxConfig.getCz());
                jsonObject.put("F0000078", crmRxConfig.getRj());
                jsonObject.put("F0000077", gxcz);
                jsonObject.put("F0000110", jjdcl);
                jsonObject.put("F0000081", gxdcl);

                jsonArray.add(jsonObject.toString());
            } else {
                log.info(formatDate(processOutput.getDate(), "yyyy-MM-dd") + " 的人效产值目标暂未设定");
            }
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ActionName", "CreateBizObjects");// 调用的方法名
        paramMap.put("SchemaCode", "D283959slqobmbhahugeuunl8km"); // 表单编码

        paramMap.put("BizObjectArray", jsonArray.toArray());
        paramMap.put("IsSubmit", "true");
        String CreateStr = JSONObject.valueToString(paramMap);

        // 请求接口
        doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);
        log.info("氚云数据同步完成!" + "共同步" + processOutputs.size() + "条数据");
    }

    /**
     * 定时任务方法用于定时向CRM系统-储能人效数据源推送数据
     *
     * @author svanrj
     * @date 2024/5/16
     */
    @Async
    public void sendEnergyOutputValue() {
        String[] areaCodes = {"CPK", "ZPK"};

        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");

        List<ProcessOutputVO> processOutputs = processOutputMapper.getProcessOutputByType(todayString + " 00:00:00", todayString + " 23:59:59", areaCodes, "储能");

        Map<String, Object> paramMap = CyCreate(processOutputs, "D283959swokafnoxwuyxif8jpilk", "储能");
        String CreateStr = JSONObject.valueToString(paramMap);

        // 请求接口
        doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);
        log.info("氚云数据同步完成!" + "共同步" + processOutputs.size() + "条数据");
    }

    /**
     * 定时任务方法用于定时向CRM系统-通用人效数据源推送数据
     *
     * @author svanrj
     * @date 2024/5/16
     */

    @Async
    public void sendUniversalOutputValue() {
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");
        String[] areaCodes = {"CPK", "ZPK"};

        List<ProcessOutputVO> processOutputs = processOutputMapper.getProcessOutputByType(todayString + " 00:00:00", todayString + " 23:59:59", areaCodes, "通用");

        Map<String, Object> paramMap = CyCreate(processOutputs, "D283959szcujforrpeycmctp2u8e", "通用");
        String CreateStr = JSONObject.valueToString(paramMap);

        // 请求接口
        doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);
        log.info("氚云数据同步完成!" + "共同步" + processOutputs.size() + "条数据");
    }

    @Async
    public void sendDieCastingOutputValue() {
        final LocalDateTime today = LocalDateTime.now();
        final String todayString = formatDateTime(today, "yyyy-MM-dd");
        String[] areaCodes = {"BCPK"};

        List<ProcessOutputVO> processOutputs = processOutputMapper.getSemiProcessOutputByType(todayString + " 00:00:00", todayString + " 23:59:59", areaCodes, "后道完工品");

        Map<String, Object> paramMap = CyCreate(processOutputs, "D283959snbcipudg2kqstxip06ou", "压铸");
        String CreateStr = JSONObject.valueToString(paramMap);

        // 请求接口
        doPost("https://www.h3yun.com/OpenApi/Invoke", CreateStr);
        log.info("氚云数据同步完成!" + "共同步" + processOutputs.size() + "条数据");
    }

    /**
     * 内部提取方法用于构建具体要推送的数据
     *
     * @author svanrj
     * @date 2024/5/16
     */
    private Map<String, Object> CyCreate
    (List<ProcessOutputVO> processOutputs, String SchemaCode, String type) {
        List<String> jsonArray = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<>();
        for (ProcessOutputVO processOutput : processOutputs) {
            QueryWrapper<CrmRxConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", type);
            queryWrapper.apply("DATE_FORMAT(mb_date, '%Y-%m') = DATE_FORMAT('" + formatDate(processOutput.getDate(), "yyyy-MM-dd") + "', '%Y-%m')");
            queryWrapper.last("limit 1");
            CrmRxConfig crmRxConfig = crmRxConfigMapper.selectOne(queryWrapper);
            if (crmRxConfig != null) {
                if (type.equals("压铸")) {
                    float yzdcl = crmRxConfig.getYzCz() == 0 ? 0 : processOutput.getTotalMoldClosingCost() / crmRxConfig.getYzCz();
                    float hddcl = crmRxConfig.getHdCz() == 0 ? 0 : processOutput.getTotalPostProcessing() / crmRxConfig.getHdCz();
                    float gxcz = processOutput.getTotalMoldClosingCost() + processOutput.getTotalPostProcessing();

                    float gxdcl = crmRxConfig.getCz() == 0 ? 0 : gxcz / crmRxConfig.getCz();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("F0000109", "压铸车间");
                    jsonObject.put("F0000002", formatDate(processOutput.getDate(), "yyyy-MM-dd"));
                    jsonObject.put("F0000110", crmRxConfig.getYzCz());
                    jsonObject.put("F0000111", crmRxConfig.getYzRj());
                    jsonObject.put("F0000082", processOutput.getTotalMoldClosingCost());
                    jsonObject.put("F0000113", yzdcl);
                    jsonObject.put("F0000114", "");
                    jsonObject.put("F0000115", "");
                    jsonObject.put("F0000116", crmRxConfig.getHdCz());
                    jsonObject.put("F0000096", crmRxConfig.getHdRj());
                    jsonObject.put("F0000083", processOutput.getTotalPostProcessing());
                    jsonObject.put("F0000120", hddcl);
                    jsonObject.put("F0000119", "");
                    jsonObject.put("F0000121", "");
                    jsonObject.put("F0000095", crmRxConfig.getCz());
                    jsonObject.put("F0000096", crmRxConfig.getRj());
                    jsonObject.put("F0000084", gxcz);
                    jsonObject.put("F0000100", gxdcl);


                    jsonArray.add(jsonObject.toString());
                } else {
                    float jjdcl = crmRxConfig.getJjCz() == 0 ? 0 : processOutput.getTotalMachineAddition() / crmRxConfig.getJjCz();
                    float zbdcl = crmRxConfig.getZpCz() == 0 ? 0 : processOutput.getTotalAssembleCost() / crmRxConfig.getZpCz();
                    float gxcz = processOutput.getTotalMachineAddition() + processOutput.getTotalFrictionWelding() + processOutput.getTotalCleaning() + processOutput.getTotalSideLeakage() + processOutput.getTotalAssembleCost();
                    float gxdcl = crmRxConfig.getCz() == 0 ? 0 : gxcz / crmRxConfig.getCz();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("F0000109", "三大事业部");
                    jsonObject.put("F0000016", processOutput.getDepa());
                    jsonObject.put("F0000002", formatDate(processOutput.getDate(), "yyyy-MM-dd"));
                    jsonObject.put("F0000086", processOutput.getTotalFrictionWelding());
                    jsonObject.put("F0000087", processOutput.getTotalCleaning());
                    jsonObject.put("F0000088", processOutput.getTotalSideLeakage());
                    jsonObject.put("F0000089", processOutput.getTotalAssembleCost());
                    jsonObject.put("F0000091", crmRxConfig.getJjCz());
                    jsonObject.put("F0000103", crmRxConfig.getJjRj());
                    jsonObject.put("F0000085", processOutput.getTotalMachineAddition());
                    jsonObject.put("F0000080", crmRxConfig.getCz());
                    jsonObject.put("F0000078", crmRxConfig.getRj());
                    jsonObject.put("F0000077", gxcz);
                    jsonObject.put("F0000110", jjdcl);
                    jsonObject.put("F0000081", gxdcl);
                    jsonObject.put("F0000111", crmRxConfig.getZpCz());
                    jsonObject.put("F0000112", crmRxConfig.getZpRj());
                    jsonObject.put("F0000115", zbdcl);
                    jsonArray.add(jsonObject.toString());
                }
            } else {
                log.info(formatDate(processOutput.getDate(), "yyyy-MM-dd") + " 的人效产值目标暂未设定");
            }
        }
        paramMap.put("BizObjectArray", jsonArray.toArray());
        paramMap.put("IsSubmit", "true");
        paramMap.put("ActionName", "CreateBizObjects");// 调用的方法名
        paramMap.put("SchemaCode", SchemaCode); // 表单编码
        return paramMap;
    }
}

