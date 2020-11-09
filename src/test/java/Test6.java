import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/9 9:01 下午
 */

public class Test6 {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection c = f.newConnection();
        Channel ch = c.createChannel();

        // 定义名字为logs的减缓及,类型为fanout
        ch.exchangeDeclare("logs", "fanout");

        // 创建队列
        // 随机名称
        // 非持久 durable=false
        // 独占 exclusive=true
        // 自动删除 autoDelete=true
        String queueName = ch.queueDeclare().getQueue();
        // 把队列绑定到logs交换机,从logs交换机接收消息
        ch.queueBind(queueName, "logs", "");

        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String s1 = new String(delivery.getBody());
                System.out.println("收到:" + s1);
                System.out.println("-----------------\n\n");

            }
        };

        CancelCallback cancelCall = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };

        ch.basicConsume(queueName, true, callback, cancelCall);

    }
}
