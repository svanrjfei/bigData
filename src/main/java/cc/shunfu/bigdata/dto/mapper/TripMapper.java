package cc.shunfu.bigdata.dto.mapper;

import cc.shunfu.bigdata.dto.entity.TripJobLock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-30
 */

@Mapper
public interface TripMapper extends BaseMapper<TripJobLock> {
    @Select("SELECT is_lock FROM trip_job_lock WHERE id = #{id}")
    public String getIsLock(String id);

    @Select("SELECT * FROM trip_job_lock WHERE id = #{id}")
    public TripJobLock getJob(String id);

    @Select("SELECT id FROM trip_job_lock where is_lock=1")
    public List<Integer> getJobIdList();

}
