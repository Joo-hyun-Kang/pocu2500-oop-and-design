package academy.pocu.comp2500.assignment1;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;


public class Post {

    private final User AUTHOR;
    private final String ID;
    private String title;
    private String body;
    private final OffsetDateTime CREATED_DATE_TIME = OffsetDateTime.now();
    private OffsetDateTime modifiedDateTime = CREATED_DATE_TIME;
    private ArrayList<String> tags = new ArrayList<>();

    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Tuple<User, ReactionType>> reactions = new ArrayList<>();

    public enum ReactionType {
        GREAT,
        SAD,
        ANGRY,
        FUN,
        LOVE
    }

    public Post(User user, String id, String title, String body) {
        this.AUTHOR = user;
        this.ID = id;
        this.title = title;
        this.body = body;
    }

    public User getAuthor() {
        return AUTHOR;
    }

    public String getId() {
        return this.ID;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(User user, String title) {
        if (!this.AUTHOR.getUserId().equals(user.getUserId())) {
            return;
        }

        this.title = title;
        this.modifiedDateTime = OffsetDateTime.now();
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(User user, String body) {
        if (!this.AUTHOR.getUserId().equals(user.getUserId())) {
            return;
        }

        this.body = body;
        this.modifiedDateTime = OffsetDateTime.now();
    }

    public OffsetDateTime getCreatedDateTime() {
        return CREATED_DATE_TIME;
    }

    public OffsetDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public boolean isTagMatching(ArrayList<String> tag) {
        if (tags.size() == 0) { // tag와 맞는 녀석이 있을리가 없으므로 걸러내야함
            return false;
        }

        for (String t : tags) { // tag와 맞는 녀석이 있으면 바로 true를 반환해야함
            if (tag.contains(t)) {
                return true;
            }
        }

        return false; // 전부 비교했는데 tag와 tags가 맞는 녀석이 없으므로 걸러내야함
    }

    public void addTag(String tag) {
        for (String t : tags) {
            if (t.equals(tag)) {
                return;
            }
        }
        tags.add(tag);
    }

    public ArrayList<Comment> getComments() {
        Collections.sort(comments, (o1, o2) -> {
            if (o1.getVoteNum() > (o2.getVoteNum())) {
                return -1;
            } else if (o1.getVoteNum() < (o2.getVoteNum())) {
                return 1;
            } else {
                return 0;
            }
        });

        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public int getReactions(ReactionType type) {
        int reactionCount = 0;
        for (Tuple<User, ReactionType> tuple : reactions) {
            if (tuple.getB() == type) {
                reactionCount++;
            }
        }

        return reactionCount;
    }

    public void addReaction(User user, ReactionType reaction) {
        User reactioner = user;
        for (Tuple<User, ReactionType> tuple : reactions) {
            if (tuple.getA().getUserId().equals(reactioner.getUserId()) && tuple.getB().equals(reaction)) {
                return;
            }
        }

        reactions.add(new Tuple<User, ReactionType>(user, reaction));
    }

    public void removeReaction(User user, ReactionType reaction) {
        User reactioner = user;
        for (int i = 0; i < reactions.size(); i++) {
            if (reactions.get(i).getA().getUserId().equals(reactioner.getUserId()) && reactions.get(i).getB().equals(reaction)) {
                reactions.remove(i);
            }

            return;
        }
    }
}