package cc.shunfu.bigdata.dto.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-30
 */

@Data
public class TripJobLock {

    @TableId
    private int id;

    private String isLock;
    private String jobCron;
    private String jobDesc;
    private String method;
}
