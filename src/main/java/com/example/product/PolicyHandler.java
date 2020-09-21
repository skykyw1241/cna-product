package com.example.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler {

    @Autowired
    ProductRepository productRepository;

    @StreamListener(KafkaProcessor.orderCreated_INPUT)
    public void onEventByOrderCreated(@Payload OrderCreated orderCreated){
        System.out.println("onEventByOrderCreated");
        //  orderPlaced 데이터를 json -> 객체로 파싱
        System.out.println(orderCreated.getEventType());
        //  if문으로 주문생성일때만 작업 진행
        if("OrderCreated".equals(orderCreated.getEventType()) ){
            //  상품ID 값의 재고 변경로직

            Optional<Product> productById = productRepository.findById(orderCreated.getProductId());
            System.out.println("productById = "+ productById );
            if(!productById.isEmpty()){
                Product p = productById.get();
                System.out.println("잔고 = "+ (p.getStock()-orderCreated.getQty()) );
                if(p.getStock() - orderCreated.getQty() >= 0){
                    p.setStock(p.getStock()-orderCreated.getQty());
                    productRepository.save(p);

                    System.out.println("saved Product id="+p.getId()+", qty="+p.getStock());
                }else{
                    ProductExcept productExcept = new ProductExcept();
                    productExcept.setOrderId(orderCreated.getId());
                    productExcept.setProductId(p.getId());

                    try{
                        productExcept.publish();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("*********************** sending Product Except ***********************");
                }
            }
            else{
                System.out.println("해당 product 없음");
            }

        }

    }

    @StreamListener(KafkaProcessor.productChanged_INPUT)
    public void onEventByProductChanged(@Payload ProductChanged productChanged){
        System.out.println("onEventByProductChanged");
        //  orderPlaced 데이터를 json -> 객체로 파싱
        System.out.println(productChanged.getEventType());
        //  if문으로 주문생성일때만 작업 진행
        if("ProductChanged".equals(productChanged.getEventType()) ){
            System.out.println("productId="+productChanged.getProductId());
            System.out.println("productName="+productChanged.getProductName());
            System.out.println("stock="+productChanged.getProductStock());
        }
    }

    @StreamListener(KafkaProcessor.productExcept_INPUT)
    public void onEventByProductExcept(@Payload ProductExcept ProductExcept){
        System.out.println("onEventByProductExcept");
        //  orderPlaced 데이터를 json -> 객체로 파싱
        System.out.println(ProductExcept.getEventType());
        //  if문으로 주문생성일때만 작업 진행
        if("OutOfStock".equals(ProductExcept.getEventType()) ){
            System.out.println("orderId="+ ProductExcept.getOrderId());
            System.out.println("productId="+ ProductExcept.getProductId());
        }

    }

}
