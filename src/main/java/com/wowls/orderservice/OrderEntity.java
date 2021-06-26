package com.wowls.orderservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.time.LocalDateTime;

/*
Entity에는 인자 없는 생성자가 필요하다 (lazy loading 등에서 사용하는 듯)
@Builder를 사용하는 경우 AllArgsConstructor도 같이 사용해야한다 (NoArgsConstructor가 안먹는거 같다)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String productId;

    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer totalPrice;


    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    public static OrderEntity from(OrderDto orderDto){
        return OrderEntity.builder()
                .productId(orderDto.getProductId())
                .orderId(orderDto.getOrderId())
                .userId(orderDto.getUserId())
                .quantity(orderDto.getQuantity())
                .unitPrice(orderDto.getUnitPrice())
                .totalPrice(orderDto.getTotalPrice())
                .build();
    }
}
