package academy.pocu.comp2500.assignment1;

import java.util.UUID;

public class Author {
    private UUID id;
    private String name;

    public Author(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
