package academy.pocu.comp2500.assignment1;

import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.UUID;

public class Post {
    private static final ZoneOffset TIME_ZONE = ZoneOffset.UTC;
    private UUID id;
    private OffsetDateTime createAt;
    private OffsetDateTime modifiedAt;
    private Author author;
    private String title;
    private String body;
    private ArrayList<String> tags;


    Post(UUID id, String title, Author author) {
        this(id, title, author, new ArrayList<>());
    }

    Post(UUID id, String title, Author author, ArrayList<String> tags) {
        this(id, title, "", author, tags);
    }

    Post(UUID id, String title, String body, Author author, ArrayList<String> tags) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.createAt = OffsetDateTime.now(TIME_ZONE);
        this.modifiedAt = OffsetDateTime.now(TIME_ZONE);
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
        this.modifiedAt = OffsetDateTime.now(TIME_ZONE);
    }

    public boolean isTagExist(String tag) {
        return this.tags.contains(tag);
    }

    public void setBody(String body) {
        this.body = body;
        this.modifiedAt = OffsetDateTime.now(TIME_ZONE);
    }

    public void addTags(String tag) {
        this.tags.add(tag);
    }

    public void removeTags(String tag) {
        this.tags.remove(tag);
    }

    public UUID getId() {
        return id;
    }

    public OffsetDateTime getCreateAt() {
        return createAt;
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
}
