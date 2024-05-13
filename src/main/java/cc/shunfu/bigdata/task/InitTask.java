package cc.shunfu.bigdata.task;

import cc.shunfu.bigdata.dto.mapper.TripMapper;
import cc.shunfu.bigdata.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-30
 */

@Component
public class InitTask implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    TaskService taskService;

    @Autowired
    TripMapper tripMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Integer> jobIdList = tripMapper.getJobIdList();
        jobIdList.forEach(id -> taskService.startTask(id.toString()));
    }
}
