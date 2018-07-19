package formsubmission.model;

import org.springframework.social.twitter.api.Tweet;

import java.util.Date;

public class MyTweet{
    private String text;
    private Date createdAt;
    private String fromUser;
    private String hashTag;

    public MyTweet(String text, Date createdAt, String fromUser, String hashTag) {
        this.text = text;
        this.createdAt = createdAt;
        this.fromUser = fromUser;
        this.hashTag = hashTag;
    }

    public MyTweet(){

    }

    public MyTweet(Tweet original, String hashTag){
        this.createdAt = original.getCreatedAt();
        this.fromUser = original.getFromUser();
        this.text = original.getText();
        this.hashTag = hashTag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    @Override
    public String toString() {
        return "MyTweet{" +
                "text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", fromUser='" + fromUser + '\'' +
                ", hashTag='" + hashTag + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyTweet myTweet = (MyTweet) o;

        if (text != null ? !text.equals(myTweet.text) : myTweet.text != null) return false;
        if (createdAt != null ? !createdAt.equals(myTweet.createdAt) : myTweet.createdAt != null) return false;
        if (fromUser != null ? !fromUser.equals(myTweet.fromUser) : myTweet.fromUser != null) return false;
        return hashTag != null ? hashTag.equals(myTweet.hashTag) : myTweet.hashTag == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (fromUser != null ? fromUser.hashCode() : 0);
        result = 31 * result + (hashTag != null ? hashTag.hashCode() : 0);
        return result;
    }
}
