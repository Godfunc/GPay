package com.godfunc.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Slf4j
public class ReturnListener implements RabbitTemplate.ReturnCallback {

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        // TODO 优化 添加业务
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.getBody());
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object o = objectInputStream.readObject();
            log.error("消息被退回 message={}, code={}, text={}, exchange={}, routingKey={}", o, replyCode, replyText, exchange, routingKey);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
