import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/9 8:47 下午
 */

public class Test5 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection c = f.newConnection();
        Channel ch = c.createChannel();

        // 创建交换机,定义名字为logs的交换机,交换机类型为fanout
        // 如果交换机存在,什么都不做
        // 这一步是必须的，因为禁止发布到不存在的交换
        ch.exchangeDeclare("logs", "fanout");
        while (true) {
            System.out.println("输入信息:");
            String s = new Scanner(System.in).nextLine();
            if ("exit".equals(s)) {
                break;
            }
            ch.basicPublish("logs", "", null, s.getBytes());
        }
        c.close();
    }
}
