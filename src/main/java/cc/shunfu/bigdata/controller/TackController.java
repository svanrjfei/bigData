package cc.shunfu.bigdata.controller;

import cc.shunfu.bigdata.dto.vo.result.Response;
import cc.shunfu.bigdata.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Response startTask(@PathVariable(required = true) String taskId) {
        taskService.startTask(taskId);
        return Response.success(taskId + "：启动成功", null);
    }

    @Operation(summary = "停止任务")
    @PostMapping("/stopTask/{taskId}")
    public Response stopTask(@PathVariable(required = true) String taskId) {
        taskService.stopTask(taskId);
        return Response.success(taskId + "：已停止", null);
    }
}
