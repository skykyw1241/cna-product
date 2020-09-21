package com.example.product;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.SubscribableChannel;

import javax.print.DocFlavor;

public interface KafkaProcessor extends Processor {
    String INPUT="event-in";
    String OUTPUT="event-out";

    @Input(INPUT)
    SubscribableChannel inboundTopic();

    @Output(OUTPUT)
    SubscribableChannel outboundTopic();
}
