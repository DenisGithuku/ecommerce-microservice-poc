package com.ecommerce.notification.service;

import com.ecommerce.notification.payload.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class OrderEventConsumer {

//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void handleOrderEvent(OrderCreatedEvent orderEvent) {
//        System.out.println("Received order event: " + orderEvent);
//
//        // Update database
//        // Send notifications
//        // Send emails
//        // Generate invoice
//        // Send seller notification
//    }

    @Bean
    public Consumer<OrderCreatedEvent> orderCreated() {
        return event -> {
            log.info("Order received: {} ", event);
        };
    }
}
