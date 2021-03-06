package com.liujiaohan.sell.repository;

import com.liujiaohan.sell.dataobject.OrderMaster;
import com.liujiaohan.sell.enums.OrderStatusEnum;
import com.liujiaohan.sell.enums.PayStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Test
    public void findByBuAndBuyerOpenId() throws Exception {
        PageRequest pageRequest=new PageRequest(0,1);
        Assert.assertNotNull(orderMasterRepository.findByBuyerOpenid("123456",pageRequest));
    }

    @Test
    public void saveTest(){
        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setBuyerAddress("湖北武汉");
        orderMaster.setBuyerName("刘佼涵");
        orderMaster.setBuyerOpenid("123456");
        orderMaster.setBuyerPhone("1234567890");
        orderMaster.setOrderAmount(new BigDecimal(12.5));
        orderMaster.setOrderId("123456");
        orderMaster.setOrderStatus(OrderStatusEnum.NewOrder.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMasterRepository.save(orderMaster);
    }}