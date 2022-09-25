package academy.pocu.comp2500.assignment1;

import javax.swing.text.LayeredHighlighter;
import java.util.ArrayList;
import java.util.UUID;

public class Comment {
    private UUID commentId;
    private Author author;
    private String content;
    private ArrayList<Comment> subcomment;
    private int upvote;
    private int downvote;
    private ArrayList<UUID> upVoters;
    private ArrayList<UUID> downVoters;

    public Comment(UUID commentId, Author author, String content) {
        this.commentId = commentId;
        this.author = author;
        this.content = content;
        this.subcomment = new ArrayList<>();
        this.upVoters = new ArrayList<>();
        this.downVoters = new ArrayList<>();
    }

    public void setContent(Author author, String content) {
        if (author.getAuthorId() == this.author.getAuthorId()) {
            this.content = content;
        }
    }

    public void addSubComment(Comment subcomment) {
        this.subcomment.add(subcomment);
    }

    public void removeSubcommnet(Author author, Comment subcomment) {
        assert (author.getAuthorId() == subcomment.getAuthor().getAuthorId());
        this.subcomment.remove(subcomment);
    }

    public void upvote(Author user) {
        if (upVoters.contains(user.getAuthorId()) == false) {
            plusUpvote(user.getAuthorId());
            if (downVoters.contains(user.getAuthorId())) {
                minusDownvote(user.getAuthorId());
            }
        } else {
            minusUpvote(user.getAuthorId());
        }
    }

    public void downvote(Author user) {
        if (downVoters.contains(user.getAuthorId()) == false) {
            plusDownvote(user.getAuthorId());
            if (upVoters.contains(user.getAuthorId())) {
                minusUpvote(user.getAuthorId());
            }
        } else {
            minusDownvote(user.getAuthorId());
        }
    }

    public UUID getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public Author getAuthor() {
        return author;
    }

    public ArrayList<Comment> getSubcomment() {
        subcomment.sort((comment1, comment2) -> comment1.getNetvote() - comment2.getNetvote());
        return subcomment;
    }

    public int getUpvote() {
        return upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public int getNetvote() {
        return this.upvote - this.downvote;
    }

    private void plusUpvote(UUID userId) {
        upvote++;
        upVoters.add(userId);
    }

    private void minusUpvote(UUID userId) {
        upvote--;
        upVoters.remove(userId);
    }

    private void plusDownvote(UUID userId) {
        downvote++;
        downVoters.add(userId);
    }

    private void minusDownvote(UUID userId) {
        downvote--;
        downVoters.remove(userId);
    }
}
