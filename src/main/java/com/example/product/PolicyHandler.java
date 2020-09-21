package com.example.product;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.util.Optional;

@Service
public class PolicyHandler {

    @Autowired
    ProductRepository productRepository;

    @StreamListener(Processor.INPUT)
    public void onEventByObject(@Payload OrderCreated orderCreated){
        //  orderPlaced 데이터를 json -> 객체로 파싱
        System.out.println(orderCreated.getEventType());
        //  if문으로 주문생성일때만 작업 진행
        if("OrderCreated".equals(orderCreated.getEventType()) ){
            //  상품ID 값의 재고 변경로직

            Optional<Product> productById = productRepository.findById(orderCreated.getProductId());
            if(productById != null && !productById.isEmpty()){
                Product p = productById.get();
                if(p.getStock()-orderCreated.getQty() >= 0){
                    p.setStock(p.getStock()-orderCreated.getQty());
                    productRepository.save(p);

                    System.out.println("saved Product id="+p.getId()+", qty="+p.getStock());
                }else{
                    OutOfStock outOfStock = new OutOfStock();
                    outOfStock.setOrderId(orderCreated.getId());
                    outOfStock.setProductId(p.getId());

                    //  해당 클래스를 json으로 변환
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = null;

                    try{
                        json = objectMapper.writeValueAsString(outOfStock);
                    }catch(JsonProcessingException e){
                        throw new RuntimeException("JSON format exception");
                    }
                    System.out.println(json);

                    //  메세지 큐에 publish
                    //Processor processor = ProductApplication.applicationContext.getBean(Processor.class);
                    Processor processor = ProductApplication.applicationContext.getBean(KafkaProcessor.class);
                    MessageChannel outputChannel = processor.output();

                    outputChannel.send(MessageBuilder
                            .withPayload(json)
                            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                            .build());

                }
            }
            else{
                System.out.println("해당 product 없음");
            }

        }



    }
}
