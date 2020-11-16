import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/16 9:21 下午
 */

public class Test9 {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection c = f.newConnection();
        Channel ch = c.createChannel();

        // 创建交换机
        ch.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);

        while (true) {
            System.out.println("输入消息是:");
            String msg = new Scanner(System.in).nextLine();
            if ("exit".contentEquals(msg)) {
                break;
            }

            System.out.println("输入routingKey:");
            String key = new Scanner(System.in).nextLine();

            ch.basicPublish("topic_logs", key, null, msg.getBytes());

        }
        c.close();

    }

}
