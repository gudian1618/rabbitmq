import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/3 9:54 下午
 */

public class Test1 {

    public static void main(String[] args) throws Exception {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        // 创建tcp连接,在创建信道
        Connection c = f.newConnection();
        Channel ch = c.createChannel();

        // 在rabbitmq服务器上创建新的队列
        // 如果队列已经存在,什么都不做
        ch.queueDeclare("helloworld",false, false, false, null);

        ch.basicPublish("", "helloworld", null, ("Hello world!"+System.currentTimeMillis()).getBytes());
        System.out.println("消息已发送");
        c.close();
    }
}
