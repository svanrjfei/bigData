package cc.shunfu.bigdata.dto.mapper;

import cc.shunfu.bigdata.dto.entity.ProcessOutput;
import cc.shunfu.bigdata.dto.vo.result.ProcessOutputVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-05-15
 */
@Mapper
public interface ProcessOutputMapper extends BaseMapper<ProcessOutput> {

    List<ProcessOutput> getProcessOutput(@Param("startDate") String startDate,
                                         @Param("endDate") String endDate,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit,
                                         @Param("areaCode") String areaCode);

    List<ProcessOutputVO> getProcessOutputByType(@Param("startDate") String startDate,
                                                 @Param("endDate") String endDate,
                                                 @Param("areaCode") String areaCode,
                                                 @Param("type") String type);
}
