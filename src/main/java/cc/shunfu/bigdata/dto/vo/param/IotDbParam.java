package cc.shunfu.bigdata.dto.vo.param;

import lombok.Data;

@Data
public class IotDbParam {
    /***
     * 设备号
     */
    private String sn;
    /***
     * 查询开始时间
     */
    private String startTime;
    /***
     * 查询结束时间
     */
    private String endTime;

}