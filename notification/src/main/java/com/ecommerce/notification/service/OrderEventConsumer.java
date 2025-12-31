package com.ecommerce.notification.service;

import com.ecommerce.notification.payload.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderEvent) {
        System.out.println("Received order event: " + orderEvent);

        // Update database
        // Send notifications
        // Send emails
        // Generate invoice
        // Send seller notification
    }
}
