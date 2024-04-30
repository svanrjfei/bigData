package cc.shunfu.bigdata.service.impl;

import cc.shunfu.bigdata.dto.entity.ReturnSalesOrder;
import cc.shunfu.bigdata.dto.entity.SalesOrder;
import cc.shunfu.bigdata.dto.mapper.ReturnSalesOrderMapper;
import cc.shunfu.bigdata.dto.mapper.SalesOrderMapper;
import cc.shunfu.bigdata.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-20
 */

@Service
public class SalesOrderServiceImpl implements SalesOrderService {


    ReturnSalesOrderMapper returnSalesOrderMapper;
    SalesOrderMapper salesOrderMapper;

    @Autowired
    public void setReturnSalesOrderMapper(ReturnSalesOrderMapper returnSalesOrderMapper) {
        this.returnSalesOrderMapper = returnSalesOrderMapper;
    }

    @Autowired
    public void setSalesOrderMapper(SalesOrderMapper salesOrderMapper) {
        this.salesOrderMapper = salesOrderMapper;
    }

    @Override
    public List<SalesOrder> getSalesOrders(String startDate, String endDate, int offset, int limit) {
        return salesOrderMapper.getSalesOrders(startDate, endDate, offset, limit);
    }

    @Override
    public List<ReturnSalesOrder> getReturnSalesOrders(String startDate, String endDate, int offset, int limit) {
        return returnSalesOrderMapper.getReturnSalesOrder(startDate, endDate, offset, limit);
    }

}
