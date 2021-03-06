package com.liujiaohan.sell.controller;

import com.liujiaohan.sell.VO.ResultVO;
import com.liujiaohan.sell.converter.OrderFormToOrderDTOConverter;
import com.liujiaohan.sell.dataobject.OrderMaster;
import com.liujiaohan.sell.dto.OrderDTO;
import com.liujiaohan.sell.enums.ResultEnum;
import com.liujiaohan.sell.exception.SellException;
import com.liujiaohan.sell.form.OrderForm;
import com.liujiaohan.sell.service.BuyerService;
import com.liujiaohan.sell.service.impI.BuyerServiceImpl;
import com.liujiaohan.sell.service.impI.OrderServiceImpl;
import com.liujiaohan.sell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private BuyerServiceImpl buyerService;
    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            log.error("[创建订单] 参数不正确,orderForm={}",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO= OrderFormToOrderDTOConverter.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getList())){
            log.error("[创建订单]购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createResult=orderService.create(orderDTO);

        Map<String,String> map=new HashMap<>();
        map.put("orderId",createResult.getOrderId());
        return ResultVOUtil.success(map);
    }
    //订单列表
    @PostMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
    @RequestParam(value = "page",defaultValue = "0") Integer page,@RequestParam(value = "size",defaultValue = "10") Integer size){
        if (StringUtils.isEmpty(openid)){
            log.error("[查询订单列表] openid 不能为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        Page<OrderDTO> orderDTOPage=orderService.findList(openid,new PageRequest(page,size));

        return ResultVOUtil.success(orderDTOPage.getContent());
    }


    //订单详情
    @PostMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId){

        OrderDTO orderDTO=buyerService.findOrderOne(orderId,openid);
        return ResultVOUtil.success(orderDTO);
    }
    //取消订单
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("orderId") String orderId,
                           @RequestParam("openid") String openid
                                     ){

        buyerService.findOrderOne(orderId,openid);
        return ResultVOUtil.success();
    }
}
