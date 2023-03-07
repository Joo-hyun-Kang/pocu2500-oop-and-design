package academy.pocu.comp2500.assignment1;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Post {
    private final UUID postId;
    private final UUID authorId;
    private final OffsetDateTime createdDateTime;
    private final List<String> tags = new ArrayList<>();
    private final List<Comment> comments = new ArrayList<>();
    private final Map<ReactionType, Integer> reactionsMap = new HashMap<>();
    private final Map<UUID, List<ReactionType>> userReactions = new HashMap<>();
    private OffsetDateTime modifiedDateTime;
    private String title;
    private String body;


    public Post(UUID postId, UUID authorId, String title, String body) {
        this.postId = postId;
        this.authorId = authorId;
        this.createdDateTime = OffsetDateTime.now();
        this.modifiedDateTime = this.createdDateTime;
        this.title = title;
        this.body = body;
    }

    public void addReaction(UUID userId, ReactionType reactionType) {
        if (!userReactions.containsKey(userId)) {
            userReactions.put(userId, new ArrayList<>());
        }

        if (userReactions.get(userId).contains(reactionType)) {
            return;
        }

        userReactions.get(userId).add(reactionType);
        reactionsMap.put(reactionType, reactionsMap.getOrDefault(reactionType, 0) + 1);
    }

    public boolean removeReaction(UUID userId, ReactionType reactionType) {
        if (!userReactions.containsKey(userId)) {
            return false;
        }

        if (!userReactions.get(userId).contains(reactionType)) {
            return false;
        }

        userReactions.get(userId).remove(reactionType);
        reactionsMap.put(reactionType, reactionsMap.get(reactionType) - 1);

        return true;
    }

    public int getReactionCount(ReactionType reactionType) {
        return reactionsMap.get(reactionType);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public List<Comment> getComments() {
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getVote).reversed())
                .collect(Collectors.toList());
    }

    public boolean addTag(String tag) {
        if (tags.contains(tag)) {
            return false;
        }

        this.tags.add(tag);
        return true;
    }

    public boolean updateTitle(UUID authorId, String title) {
        if (!this.authorId.equals(authorId)) {
            return false;
        }

        this.title = title;
        this.modifiedDateTime = OffsetDateTime.now();
        return true;
    }

    public boolean updateBody(UUID authorId, String body) {
        if (!this.authorId.equals(authorId)) {
            return false;
        }

        this.body = body;
        this.modifiedDateTime = OffsetDateTime.now();
        return true;
    }

    public UUID getPostId() {
        return postId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public OffsetDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public OffsetDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public List<String> getTags() {
        return tags;
    }
}
