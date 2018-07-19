package formsubmission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import formsubmission.model.MyTweet;
import formsubmission.model.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;

@Service
public class QueueReciver {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(10);

    public void collectTweets(User user) {
        final Runnable collector = new Runnable() {
            public void run() {
                try {
                    openChannelAndListen(user);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
                scheduler.execute(collector);
    }

    private void openChannelAndListen(User user) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.queueDeclare(user.getFirstHashtag(), false, false, false, null);
        channel.queueDeclare(user.getSecondHashtag(), false, false, false, null);
        channel.queueDeclare(user.getThirdHashtag(), false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");

                ObjectMapper mapper = new ObjectMapper();
                MyTweet tweet = mapper.readValue(message, MyTweet.class);
                System.err.println("tweet: " + tweet.toString());
                user.addTweet(tweet);
                user.addHashTagTweetCount(tweet.getHashTag(), 1);

            }

        };
        String s = channel.basicConsume(user.getFirstHashtag(), true, consumer);
        channel.basicConsume(user.getSecondHashtag(), true, consumer);
        channel.basicConsume(user.getThirdHashtag(), true, consumer);
    }
}
