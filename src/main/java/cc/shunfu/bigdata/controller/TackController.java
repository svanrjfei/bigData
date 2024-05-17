package cc.shunfu.bigdata.controller;

import cc.shunfu.bigdata.dto.entity.TripJobLock;
import cc.shunfu.bigdata.dto.vo.result.Response;
import cc.shunfu.bigdata.service.TaskService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-30
 */

@RestController
@RequestMapping("/task")
@Tag(name = "Task", description = "任务")
public class TackController {

    @Autowired
    TaskService taskService;

    @Operation(summary = "启动任务")
    @PostMapping("/startTask/{taskId}")
    public Response startTask(@PathVariable String taskId) {
        taskService.startTask(taskId);
        return Response.success(taskId + "：启动成功", null);
    }

    @Operation(summary = "停止任务")
    @PostMapping("/stopTask/{taskId}")
    public Response stopTask(@PathVariable String taskId) {
        taskService.stopTask(taskId);
        return Response.success(taskId + "：已停止", null);
    }

    @Operation(summary = "重启任务")
    @PostMapping("/restartTask/{taskId}")
    public Response restartTask(@PathVariable String taskId) {
        taskService.restartTask(taskId);
        return Response.success(taskId + "：已重启", null);
    }

    @Operation(summary = "立即运行一个任务")
    @PostMapping("/runTask/{taskId}")
    public Response runTask(@PathVariable String taskId) {
        taskService.runTask(taskId);
        return Response.success(taskId + "：运行成功", null);
    }

    @Operation(summary = "增加一个任务")
    @PostMapping("/addTask")
    public Response addTask(@RequestBody TripJobLock tripJobLock) {
        int taskId = taskService.addTask(tripJobLock);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskId", taskId);

        return Response.success("增加任务成功", jsonObject);
    }
}
