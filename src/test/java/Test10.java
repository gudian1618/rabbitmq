import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/16 9:29 下午
 */

public class Test10 {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection c = f.newConnection();
        Channel ch = c.createChannel();

        ch.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);

        String queueName = ch.queueDeclare().getQueue();

        // #.mybatis.#
        System.out.println("输入bindingKey,用空格隔开:");
        String[] a = new Scanner(System.in).nextLine().split("\\s");

        for (String key : a) {
            ch.queueBind(queueName, "topic_logs", key);
        }

        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String s1 = new String(delivery.getBody());
                String key = delivery.getEnvelope().getRoutingKey();
                System.out.println("收到数据:"+key+" - "+s1);
                System.out.println("---------------------");
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
