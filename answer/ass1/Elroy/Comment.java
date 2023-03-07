package academy.pocu.comp2500.assignment1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Comment {
    private final UUID commentId;
    private final UUID authorId;
    private final List<Comment> comments = new ArrayList<>();
    private final List<UUID> voters = new ArrayList<>();

    private String text;
    private int voteCount;

    public Comment(UUID commentId, UUID authorId, String text) {
        this.commentId = commentId;
        this.authorId = authorId;
        this.text = text;
    }

    public boolean upvote(UUID userId) {
        if (voters.contains(userId)) {
            return false;
        }

        voters.add(userId);
        voteCount++;
        return true;
    }

    public boolean downvote(UUID userId) {
        if (voters.contains(userId)) {
            return false;
        }

        voters.add(userId);
        voteCount--;
        return true;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public boolean updateText(UUID authorId, String text) {
        if (!this.authorId.equals(authorId)) {
            return false;
        }

        this.text = text;
        return true;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public List<Comment> getComments() {
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getVote).reversed())
                .collect(Collectors.toList());
    }

    public String getText() {
        return text;
    }

    public int getVote() {
        return voteCount;
    }
}
