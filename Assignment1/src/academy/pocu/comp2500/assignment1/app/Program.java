package academy.pocu.comp2500.assignment1.app;

import academy.pocu.comp2500.assignment1.*;
import academy.pocu.comp2500.assignment1.registry.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Program {

    public static void main(String[] args) {
        Registry registry = new Registry();
        App app = new App(registry);
        registry.validate();

        Author jokang = new Author(UUID.randomUUID(), "jokang");

        Post post2 = new Post(UUID.randomUUID(), "1", "body", jokang);
        Post post3 = new Post(UUID.randomUUID(), "2", "body", jokang);
        Comment comment = new Comment(UUID.randomUUID(), jokang, "why?");

        post3.addComment(comment);

        Blog blog = new Blog(jokang);
        blog.addPost(post2);
        //blog.addPost(post3);
        ArrayList<Post> posts = new ArrayList<>();
        posts = blog.getPosts();

        posts.forEach(post -> System.out.println(post.getTitle()));
        System.out.println("hello world");

        comment.downvote(jokang);
        System.out.println(comment.getvote());

        comment.upvote(jokang);
        System.out.println(comment.getvote());

        comment.upvote(jokang);
        System.out.println(comment.getvote());

        comment.downvote(jokang);
        System.out.println(comment.getvote());

        comment.downvote(jokang);
        System.out.println(comment.getvote());
    }
}
