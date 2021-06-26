package com.wowls.orderservice;

import lombok.Data;

@Data
public class RequestOrder {
    private String productId;
    private Integer quantity;
    private Integer unitPrice;
}
