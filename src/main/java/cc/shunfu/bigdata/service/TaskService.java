package cc.shunfu.bigdata.service;

import cc.shunfu.bigdata.dto.entity.TripJobLock;
import org.springframework.stereotype.Service;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-30
 */


@Service
public interface TaskService {

    /**
     * 启动任务
     *
     * @param taskId 任务id
     * @author svanrj
     * @date 2024/5/17
     */

    void startTask(String taskId);

    /**
     * 启动任务
     *
     * @param taskId 任务id
     * @author svanrj
     * @date 2024/5/17
     */

    void stopTask(String taskId);

    /**
     * 重启任务
     *
     * @param taskId 任务id
     * @author svanrj
     * @date 2024/5/17
     */
    void restartTask(String taskId);

    /**
     * 立刻运行一个任务
     *
     * @param taskId 任务id
     * @author svanrj
     * @date 2024/5/17
     */

    void runTask(String taskId);

    /**
     * 增加一个任务
     *
     * @param tripJobLock 任务信息
     * @return int 任务id
     * @author svanrj
     * @date 2024/5/17
     */
    int addTask(TripJobLock tripJobLock);
}
