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
 * @date 2020/11/9 9:23 下午
 */

public class Test7 {

    public static void main(String[] args) throws IOException, TimeoutException {

        String[] a = {"warning", "info", "error"};

        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection c = f.newConnection();
        Channel ch = c.createChannel();

        // 创建交换机
        ch.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);

        while (true) {
            System.out.println("输入消息是:");
            String msg = new Scanner(System.in).nextLine();
            if ("exit".equals(msg)) {
                break;
            }
            // 随机选择一个级别
            String level = a[new Random().nextInt(a.length)];
            // 根据执行的routingKey
            // 把消息发送到绑定的队列
            ch.basicPublish("direct_logs", level, null, msg.getBytes());
            System.out.println("消息已发送-" + level + "-" + msg);

        }
    }
}
