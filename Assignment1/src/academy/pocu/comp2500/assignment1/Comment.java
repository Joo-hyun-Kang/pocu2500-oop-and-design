package academy.pocu.comp2500.assignment1;

import javax.swing.text.LayeredHighlighter;
import java.util.ArrayList;
import java.util.UUID;

public class Comment {
    private UUID commentId;
    private Author author;
    private String content;
    private ArrayList<Comment> subcomment;
    private int vote;
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

    public void updateContent(Author author, String content) {
        assert (author.getAuthorId() == this.author.getAuthorId());
        this.content = content;
    }

    public void addSubComment(Comment comment) {
        this.subcomment.add(comment);
    }

    public boolean removeSubcommnet(Author author, Comment comment) {
        if (author.getAuthorId() == comment.getAuthor().getAuthorId()) {
            this.subcomment.remove(comment);
            return true;
        }
        return false;
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

    public int getvote() {
        return vote;
    }

    public int getNetvote() {
        return vote;
    }

    private void plusUpvote(UUID userId) {
        vote++;
        upVoters.add(userId);
    }

    private void minusUpvote(UUID userId) {
        vote--;
        upVoters.remove(userId);
    }

    private void plusDownvote(UUID userId) {
        vote--;
        downVoters.add(userId);
    }

    private void minusDownvote(UUID userId) {
        vote++;
        downVoters.remove(userId);
    }
}
