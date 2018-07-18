package com.lp.morequeue;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Worker
{

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.18.8.35");
        factory.setUsername("honddy");
        factory.setPassword("honddy");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        // rabbitmq一次发送一条消息
        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String message = new String(body, "UTF-8");

                try
                {
                    doWork(message);
                }
                finally
                {
                    System.out.println(" [x] Done");

                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        //订阅消费者  关闭自动确认
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    }

    private static void doWork(String task)
    {
        try
        {
            System.out.println(" [x] Received '" + task + "'");

            Thread.sleep(1000);
        }
        catch (InterruptedException _ignored)
        {
            Thread.currentThread().interrupt();
        }
    }
}