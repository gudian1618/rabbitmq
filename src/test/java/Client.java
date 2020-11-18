import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @author gudian1618
 * @version v1.0
 * @date 2020/11/17 9:12 下午
 */

public class Client {
    Connection con;
    Channel ch;

    public Client() throws IOException, TimeoutException {
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("127.0.0.1");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        con = f.newConnection();
        ch = con.createChannel();

    }

    public String call(String msg) throws IOException, InterruptedException {
        ch.queueDeclare("rpc_queue", false, false, false, null);
        String queueName = ch.queueDeclare().getQueue();

        String uuid = UUID.randomUUID().toString();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(uuid).replyTo(queueName).build();

        ch.basicPublish("", "rpc_queue", props, msg.getBytes());

        ArrayBlockingQueue<String> q = new ArrayBlockingQueue<String>(1);

        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                if (uuid.equals(delivery.getProperties().getCorrelationId())) {
                    String s1 = new String(delivery.getBody());
                    q.offer(s1);
                }
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };

        ch.basicConsume(queueName,true, callback, cancelCallback);

        // 会等待,直到收到通知
        return q.take();

    }

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Client c = new Client();

        System.out.println("求斐波那契数:");
        while (true) {
            int n = new Scanner(System.in).nextInt();
            String r = c.call(String.valueOf(n));
            System.out.println(r);
            System.out.println("---------------");
        }
    }

}
