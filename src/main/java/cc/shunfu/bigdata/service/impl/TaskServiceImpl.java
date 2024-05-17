package cc.shunfu.bigdata.service.impl;

import cc.shunfu.bigdata.config.DynamicScheduleConfigurer;
import cc.shunfu.bigdata.dto.entity.TripJobLock;
import cc.shunfu.bigdata.dto.mapper.TripMapper;
import cc.shunfu.bigdata.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-30
 */

@Service
public class TaskServiceImpl implements TaskService {


    @Autowired
    TripMapper tripMapper;

    @Autowired
    private DynamicScheduleConfigurer dynamicScheduleConfigurer;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void startTask(String taskId) {

//        获取数据库任务信息
        TripJobLock tripJobLockEntity = tripMapper.selectById(taskId);
        if (tripJobLockEntity == null) {
            throw new RuntimeException("任务不存在");
        }

//        更新任务信息状态
        tripJobLockEntity.setIsLock("1");
        tripMapper.updateById(tripJobLockEntity);

//        提取要执行的方法
        String cronExpression = tripJobLockEntity.getJobCron(); // 获取cron表达式
        String taskClass = tripJobLockEntity.getMethod(); // 获取任务ID
        String[] taskMethods = taskClass.split("\\.");

//        判断定时任务配置是否初始化完成
        while (!dynamicScheduleConfigurer.inited()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

//        添加定时任务
        dynamicScheduleConfigurer.addTriggerTask(taskId,
                new TriggerTask(
                        () -> {
                            runClassMethods(taskMethods);
                        },
                        new CronTrigger(cronExpression)));

    }

    @Override
    public void stopTask(String taskId) {
        if (dynamicScheduleConfigurer.hasTask(taskId)) {
            TripJobLock tripJobLockEntity = tripMapper.selectById(taskId);
            dynamicScheduleConfigurer.cancelTriggerTask(taskId);
            tripJobLockEntity.setIsLock("0");

            tripMapper.updateById(tripJobLockEntity);
        } else {
            throw new RuntimeException("任务未启动");
        }
    }

    @Override
    public void restartTask(String taskId) {
        if (dynamicScheduleConfigurer.hasTask(taskId)) {
            stopTask(taskId);
            startTask(taskId);
        } else {
            TripJobLock tripJobLockEntity = tripMapper.selectById(taskId);
            startTask(taskId);
            tripJobLockEntity.setIsLock("0");
            tripMapper.updateById(tripJobLockEntity);
        }
    }

    @Override
    public void runTask(String taskId) {
        //        获取数据库任务信息
        TripJobLock tripJobLockEntity = tripMapper.selectById(taskId);
        if (tripJobLockEntity == null) {
            throw new RuntimeException("任务不存在");
        }

//        提取要执行的方法
        String taskClass = tripJobLockEntity.getMethod(); // 获取任务ID
        String[] taskMethods = taskClass.split("\\.");

        runClassMethods(taskMethods);
    }

    @Override
    public int addTask(TripJobLock tripJobLock) {
        tripMapper.insert(tripJobLock);
        return tripJobLock.getId();
    }

    private void runClassMethods(String[] taskMethods) {
        try {
            // 通过反射获取类
            Class<?> clazz = Class.forName("cc.shunfu.bigdata.task." + taskMethods[0]);

            // 初始化Bean
            Object bean = applicationContext.getBean(clazz);

            // 获取具体要执行的方法
            Method method = bean.getClass().getMethod(taskMethods[1]);

            // 执行方法
            method.invoke(bean);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception occurred while invoking method: " + e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Method not found: " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access exception: " + e.getMessage(), e);
        }
    }


}
