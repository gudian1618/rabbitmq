import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/5 9:24 下午
 */

public class Test4 {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setUsername("admin");
        f.setPassword("admin");
        Connection c = f.newConnection();
        Channel ch = c.createChannel();
        ch.queueDeclare("helloworld",false,false,false,null);
        System.out.println("等待接收数据");

        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String s1 = new String(delivery.getBody());
                System.out.println("收到消息:"+s1);
                for (int i = 0; i < s1.length(); i++) {
                    if (s1.charAt(i) == '.') {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("消息处理完毕"+s1);
                // 发送回执
                ch.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                System.out.println("----------------\n\n");
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {
            }
        };

        // autoAck:true自动确认,服务器不等待客户端回执,发送消息后,直接删除
        // 默认false手动确认,消费者向服务器发送确认回执
        // 服务器发送消息后,不会等待消费者确认
        ch.basicConsume("helloworld", false, callback, cancelCallback);
    }
}
