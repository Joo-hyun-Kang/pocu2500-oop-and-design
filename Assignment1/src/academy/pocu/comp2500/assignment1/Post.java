package academy.pocu.comp2500.assignment1;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.UUID;

public class Post {
    private UUID postId;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;
    private Author author;
    private String title;
    private String body;
    private ArrayList<String> tags;
    private ArrayList<Comment> comments;
    private ArrayList<Emoji> emojis;

    public Post(UUID postId, String title, String body, Author author) {
        this(postId, title, body, author, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public Post(UUID postId, String title, String body, Author author, ArrayList<String> tags, ArrayList<Comment> comments, ArrayList<Emoji> emojis) {
        this.postId = postId;
        this.title = title == null ? "" : title;
        this.body = body == null ? "" : body;
        this.author = author;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.modifiedAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.tags = tags == null ? new ArrayList<>() : tags;
        this.comments = comments == null ? new ArrayList<>() : comments;
        this.emojis = emojis == null ? new ArrayList<>() : emojis;
    }

    public boolean setTitle(String title) {
        if (title == null) {
            return false;
        }
        this.title = title;
        this.modifiedAt = OffsetDateTime.now(ZoneOffset.UTC);

        return true;
    }

    public boolean isTagExist(String tag) {
        return this.tags.contains(tag);
    }

    public boolean setBody(String body) {
        if (body == null) {
            return false;
        }

        this.body = body;
        this.modifiedAt = OffsetDateTime.now(ZoneOffset.UTC);

        return true;
    }

    public void addTags(String tag) {
        this.tags.add(tag);
    }

    public void removeTags(String tag) {
        this.tags.remove(tag);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(Author author, Comment comment) {
        assert (author.getAuthorId() == comment.getAuthor().getAuthorId());
        this.comments.remove(comment);
    }

    public void addEmoji(Emoji emoji) {
        this.emojis.add(emoji);
    }

    public void removeEmoji(Author author, Emoji emoji) {
        assert (author.getAuthorId() == emoji.getAuthor().getAuthorId());
        this.emojis.remove(emoji);
    }

    public UUID getPostId() {
        return postId;
    }

    public OffsetDateTime getCreateAt() {
        return createdAt;
    }

    public OffsetDateTime getModifiedAt() {
        return modifiedAt;
    }

    public Author getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<Comment> getComments() {
        comments.sort((comment1, comment2) -> comment1.getNetvote() - comment2.getNetvote());
        return comments;
    }

    public ArrayList<Emoji> getEmojis() {
        return emojis;
    }
}
