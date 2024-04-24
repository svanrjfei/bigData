package cc.shunfu.bigdata.dto.vo.result;

import lombok.Data;

import java.util.Date;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-05-15
 */

@Data
public class ProcessOutputVO {
    private Date date;
    private String areaCode;
    private String depa;
    private Float totalMoldClosingCost;
    private Float totalFrictionWelding;
    private Float totalCleaning;
    private Float totalSideLeakage;
    private Float totalAssembleCost;
    private Float totalMachineAddition;
    private Float totalPostProcessing;
}
