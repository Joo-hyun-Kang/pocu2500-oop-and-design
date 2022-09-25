package academy.pocu.comp2500.assignment1.app;

import academy.pocu.comp2500.assignment1.*;
import academy.pocu.comp2500.assignment1.registry.Registry;

import java.util.ArrayList;
import java.util.UUID;

public class Program {

    public static void main(String[] args) {
        Registry registry = new Registry();
        App app = new App(registry);
        registry.validate();

        Author jokang = new Author(UUID.randomUUID(), "jokang");

        Post post3 = new Post(UUID.randomUUID(), "cest3", "body", jokang);
        Post post2 = new Post(UUID.randomUUID(), "best2", "body", jokang);
        Post post1 = new Post(UUID.randomUUID(), "aest1", "body", jokang);

        ArrayList<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);

        System.out.println(post1.getCreateAt());
        System.out.println(post1.getModifiedAt());

        Blog blog = new Blog(null);
        ArrayList<Post> post = blog.getPosts();
        System.out.print(post);

    }
}
