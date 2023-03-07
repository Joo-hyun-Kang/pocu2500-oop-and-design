package academy.pocu.comp2500.assignment1;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class Comment {
    private final User USER;
    private final String ID;
    private String content;
    private final OffsetDateTime CREATED_DATE_TIME;
    private OffsetDateTime modifiedDateTime;
    private ArrayList<Comment> subcomments = new ArrayList<>();
    private ArrayList<User> upVoter = new ArrayList<>();

    private ArrayList<User> downVoter = new ArrayList<>();
    private int voteNum = 0;

    public Comment(User user, String id, String content) {
        this.USER = user;
        this.ID = id;
        this.content = content;
        CREATED_DATE_TIME = OffsetDateTime.now();
        modifiedDateTime = CREATED_DATE_TIME;
    }

    public User getUSER() {
        return USER;
    }

    public String getID() {
        return ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(User user, String content) {
        if (!this.USER.getUserId().equals(user.getUserId())) {
            return;
        }

        this.content = content;
        this.modifiedDateTime = OffsetDateTime.now();
    }

    public OffsetDateTime getCreatedDateTime() {
        return CREATED_DATE_TIME;
    }

    public OffsetDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public ArrayList<Comment> getSubcomments() {
        return subcomments;
    }

    public void addSubcomment(Comment comment) {
        this.subcomments.add(comment);
    }

    public void upvote(User user) {
        for (User voter : upVoter) {
            if (voter.getUserId().equals(user.getUserId())) {
                return;
            }
        }
        for (User voter : downVoter) {
            if (voter.getUserId().equals(user.getUserId())) {
                return;
            }
        }

        upVoter.add(user);
    }

    public void downvote(User user) {
        for (User voter : upVoter) {
            if (voter.getUserId().equals(user.getUserId())) {
                return;
            }
        }
        for (User voter : downVoter) {
            if (voter.getUserId().equals(user.getUserId())) {
                return;
            }
        }

        downVoter.add(user);
    }

    public int getVoteNum() {
        return this.upVoter.size() - this.downVoter.size();
    }

}