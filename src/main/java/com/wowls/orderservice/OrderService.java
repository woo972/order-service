package com.wowls.orderservice;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQuantity());

        OrderEntity orderEntity = OrderEntity.from(orderDto);
        orderRepository.save(orderEntity);

        return orderDto;
    }

    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("there is no order info with that order id : "+orderId));
        return new ModelMapper().map(orderEntity, OrderDto.class);
    }
}
