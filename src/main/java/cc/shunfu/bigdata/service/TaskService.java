package cc.shunfu.bigdata.service;

import org.springframework.stereotype.Service;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-30
 */
@Service
public interface TaskService {


    public void startTask(String taskId);

    public void stopTask(String taskId);
}
