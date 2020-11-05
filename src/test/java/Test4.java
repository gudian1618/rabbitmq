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
                System.out.println("----------------\n\n");
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {
            }
        };

        ch.basicConsume("helloworld", callback, cancelCallback);
    }
}
