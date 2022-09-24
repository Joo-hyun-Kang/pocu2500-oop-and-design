package academy.pocu.comp2500.assignment1;

import java.util.UUID;

public class Author {
    private UUID authorId;
    private String name;

    public Author(UUID authorId, String name) {
        this.authorId = authorId;
        this.name = name;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getName() {
        return name;
    }
}
