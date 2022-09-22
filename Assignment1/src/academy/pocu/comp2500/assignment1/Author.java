package academy.pocu.comp2500.assignment1;

import java.util.UUID;

public class Author {
    private UUID id;

    Author(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
