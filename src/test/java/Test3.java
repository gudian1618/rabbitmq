import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/5 9:17 下午
 */

public class Test3 {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setUsername("admin");
        f.setPassword("admin");

        Connection c = f.newConnection();
        Channel ch = c.createChannel();
        //参数:queue,durable,exclusive,autoDelete,arguments
        ch.queueDeclare("helloworld", false,false,false,null);

        while (true) {
            System.out.println("输入消息:");
            String msg = new Scanner(System.in).nextLine();
            if ("exit".equals(msg)) {
                break;
            }
            ch.basicPublish("", "helloworld", null, msg.getBytes());
            System.out.println("消息已发送");
        }
        c.close();
    }
}
