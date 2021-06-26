package com.wowls.orderservice;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderDto implements Serializable {
    private String productId;
    private String userId;
    private String orderId;
    private Integer quantity;
    private Integer unitPrice;
    private Integer totalPrice;
}
