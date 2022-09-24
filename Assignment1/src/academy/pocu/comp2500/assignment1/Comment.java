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

    public Comment(UUID commentId, Author author, String content) {
        this.commentId = commentId;
        this.author = author;
        this.content = content;
        this.subcomment = new ArrayList<>();
    }

    public void setContent(Author author, String content) {
        assert (this.author.getId() == author.getId());
        this.content = content;
    }

    public void addSubComment(Comment subcomment) {
        this.subcomment.add(subcomment);
    }

    public void removeSubcommnet(Author author, Comment subcomment) {
        assert (author.getId() == subcomment.getAuthor().getId());
        this.subcomment.remove(subcomment);
    }

    public void plusUpvote() {
        this.upvote++;
    }

    public void plusDownvote() {
        this.downvote++;
    }

    public void minusUpvote() {
        this.upvote--;
    }

    public void minusDownvote() {
        this.downvote--;
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
}
