package academy.pocu.comp2500.assignment1;


public class User {
    private final String ID;
    private final String NAME;
    private final String NICKNAME;


    public User(String name, String nickname, String id) {
        this.NAME = name;
        this.NICKNAME = nickname;
        this.ID = id;
    }

    public String getUserId() {
        return ID;
    }

    public String getNickname() {
        return NICKNAME;
    }

}