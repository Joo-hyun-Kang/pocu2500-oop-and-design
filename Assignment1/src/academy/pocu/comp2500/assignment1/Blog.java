package academy.pocu.comp2500.assignment1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class Blog {
    private UUID id;
    private String name;
    private Author owner;
    private ArrayList<Post> posts;

    Blog(UUID id, String name, Author author) {
        this.id = id;
        this.name = name;
        this.owner = author;
        this.posts = new ArrayList<>();
    }

    public void addPost(Author author, Post post) {
        assert(author.getId() == post.getAuthor().getId());
        this.posts.add(post);
    }

    public void updateTitle(Author author, Post post, String content) {
        assert(author.getId() == post.getAuthor().getId());
        post.setTitle(content);
    }

    public void removePost(Post post) {
        this.posts.remove(post);
    }

    public UUID getId() {
        return id;
    }

    public ArrayList<Post> getPostAll() {
        return posts;
    }

    public Post getPost(Post post) {
        int idx = posts.indexOf(post);
        assert(idx != -1);
        return posts.get(idx);
    }
}
