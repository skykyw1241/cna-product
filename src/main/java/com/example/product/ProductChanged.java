package com.example.product;

import org.springframework.messaging.MessageChannel;

public class ProductChanged extends AbstractEvent {
    Long productId;
    String productName;
    int productStock;

    @Override
    public String getEventType() {
        return eventType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }


    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public ProductChanged(){
        eventType = ProductChanged.class.getSimpleName();
    }
}
