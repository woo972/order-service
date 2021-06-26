package com.wowls.orderservice;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(
            @PathVariable("userId")String userId,
            @RequestBody RequestOrder orderDetail) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(orderDetail, OrderDto.class);
        orderDto.setUserId(userId);

        OrderDto createdOrder = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = modelMapper.map(createdOrder, ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId")String userId) {
        List<ResponseOrder> responseOrderList = new ArrayList<>();
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);
        orderList.forEach(
                orderEntity -> responseOrderList.add(new ModelMapper().map(orderEntity, ResponseOrder.class))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrderList);
    }
}
