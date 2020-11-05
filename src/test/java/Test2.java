import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/5 8:56 下午
 */

public class Test2 {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setUsername("admin");
        f.setPassword("admin");

        Connection c = f.newConnection();
        Channel ch = c.createChannel();

        // 创建队列,如果队列已经存在,什么都不做
        ch.queueDeclare("helloworld", false, false, false, null);

        DeliverCallback callback = new DeliverCallback() {
            // s是消费者连接后,为当前消费者生成一个唯一的连接标识
            // delivery是,消息的封装对象
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                // 处理消息
                String s1 = new String(delivery.getBody(), "UTF-8");
                System.out.println("收到消息:"+s1);
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };

        // 1.从指定的队列接收消息
        // 2.接收消息后,处理消息的回调对象
        // 3.取消回调

        // 阻塞方法,程序在这里持续等待接收消息
        ch.basicConsume("helloworld", true, callback, cancelCallback);
    }

}
