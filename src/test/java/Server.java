import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/17 8:36 下午
 */

public class Server {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection c = f.newConnection();
        Channel ch = c.createChannel();

        // 定义队列
        ch.queueDeclare("rpc_queue", false, false, false, null);
        // 清除队列
        ch.queuePurge("rpc_queue");

        ch.basicQos(1);

        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                // 接收的是客户端发送的求第几个斐波那契数
                String s1 = new String(delivery.getBody());
                int n = Integer.parseInt(s1);
                long r = f(n);
                String msg = String.valueOf(r);
                AMQP.BasicProperties props =
                    new AMQP.BasicProperties.Builder().correlationId(delivery.getProperties().getCorrelationId()).build();
                ch.basicPublish("", delivery.getProperties().getReplyTo(), props, msg.getBytes());
                ch.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };

        ch.basicConsume("rpc_queue", false, callback, cancelCallback);

    }

    private static int f(int n) {
        if (n == 1 || n == 2) return 1;
        return f(n - 1) + f(n - 2);
    }

}
