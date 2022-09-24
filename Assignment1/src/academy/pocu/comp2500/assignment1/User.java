package academy.pocu.comp2500.assignment1;

import java.util.UUID;

public class User {
    private UUID id;
    private String name;

    public User(UUID id, String name) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
