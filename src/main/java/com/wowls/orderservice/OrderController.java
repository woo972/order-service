package com.wowls.orderservice;

import com.wowls.orderservice.messagequeue.KafkaProducer;
import com.wowls.orderservice.messagequeue.OrderProducer;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-service")
public class OrderController {

    private OrderService orderService;
    private KafkaProducer kafkaProducer;
    private OrderProducer orderProducer;

    @Autowired
    public OrderController(OrderService orderService, KafkaProducer kafkaProducer, OrderProducer orderProducer) {
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
        this.orderProducer = orderProducer;
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(
            @PathVariable("userId")String userId,
            @RequestBody RequestOrder orderDetail) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(orderDetail, OrderDto.class);
        orderDto.setUserId(userId);

        /* jpa
        OrderDto createdOrder = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = modelMapper.map(createdOrder, ResponseOrder.class);
        */

        // send by kafka

        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDetail.getUnitPrice() * orderDetail.getQuantity());
        ResponseOrder responseOrder = modelMapper.map(orderDto, ResponseOrder.class);

        kafkaProducer.send("catalog-topic", orderDto);
        orderProducer.send("orders", orderDto);

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
