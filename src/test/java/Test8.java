import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/9 9:51 下午
 */

public class Test8 {

    public static void main(String[] args) throws Exception {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection c = f.newConnection();
        Channel ch = c.createChannel();

        ch.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);

        String queueName = ch.queueDeclare().getQueue();

        System.out.println("输入要接收的日志级别,用空格隔开:");
        String s = new Scanner(System.in).nextLine();

        String[] a = s.split("\\s+");

        for (int i = 0; i < a.length; i++) {
            ch.queueBind(queueName, "direct_logs", a[i]);
        }

        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody());
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println("收到数据:" + routingKey + "-" + msg);
                System.out.println("----------------------\n");
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };

        ch.basicConsume(queueName, true, callback, cancelCallback);

    }
}
