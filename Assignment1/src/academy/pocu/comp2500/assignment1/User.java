package academy.pocu.comp2500.assignment1;

import java.util.UUID;

public class User {
    private UUID id;

    public User(UUID id, UserType type) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
