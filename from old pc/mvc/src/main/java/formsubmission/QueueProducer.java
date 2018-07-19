package formsubmission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import formsubmission.model.MyTweet;
import formsubmission.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Service("QueueProducer")
public class QueueProducer {
        @Autowired
    Twitter twitter;

  public void openChanelAndPost(User user) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(user.getFirstHashtag(), false, false, false, null);
        channel.queueDeclare(user.getSecondHashtag(), false, false, false, null);
        channel.queueDeclare(user.getThirdHashtag(), false, false, false, null);

        if(user.getFirstHashtag().length() > 0){
            List<Tweet> first = getTweets(user.getFirstHashtag());
            publishTweets(channel, first,  user.getFirstHashtag());
        }

      if(user.getSecondHashtag().length() > 0) {
          List<Tweet> second = getTweets(user.getSecondHashtag());
          publishTweets(channel, second, user.getSecondHashtag());
      }
      if(user.getThirdHashtag().length() > 0) {
          List<Tweet> third = getTweets(user.getThirdHashtag());
          publishTweets(channel, third, user.getThirdHashtag());
      }



      channel.close();
        connection.close();
    }

    private void publishTweets(Channel channel, List<Tweet> first, String queueName) {
        first.forEach(tweet -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String jsonInString = mapper.writeValueAsString(new MyTweet(tweet, queueName));
//                System.err.println(jsonInString);

                channel.basicPublish("", queueName, null, jsonInString.getBytes());
                 } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<Tweet> getTweets(String hashTag){
        return twitter.searchOperations().search(hashTag).getTweets();
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }


}
