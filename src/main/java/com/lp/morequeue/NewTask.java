package com.lp.morequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.18.8.35");
        factory.setUsername("honddy");
        factory.setPassword("honddy");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // durable是否持久化
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        for (int i=0;i<1000;i++)
        {
            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    ("hello world : "+i).getBytes("UTF-8"));
        }
        channel.close();
        connection.close();

    }

}