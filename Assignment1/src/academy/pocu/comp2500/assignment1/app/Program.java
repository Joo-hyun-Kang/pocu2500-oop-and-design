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

        Post post1 = new Post(UUID.randomUUID(), "Test1", "body", jokang);
        Post post2 = new Post(UUID.randomUUID(), "Test2", "body", jokang);
        Post post3 = new Post(UUID.randomUUID(), "Test3", "body", jokang);

        Blog myBlog = new Blog(UUID.randomUUID(), "myblog", jokang, null);
        myBlog.addPost(post1);
        myBlog.addPost(post2);
        myBlog.addPost(post3);

        System.out.print("hello\n");

        ArrayList<Post> posts = myBlog.getPosts();
        for (Post post: posts) {
            System.out.printf("%s %s\n", post.getTitle().toString(), post.getCreateAt().toString());
        }

        myBlog.setSortingType(SortingType.CREATED_AEC);
        posts = myBlog.getPosts();
        for (Post post: posts) {
            System.out.printf("%s %s\n", post.getTitle().toString(), post.getCreateAt().toString());
        }

        myBlog.setSortingType(SortingType.MODIFIED_DES);
        posts = myBlog.getPosts();
        for (Post post: posts) {
            System.out.printf("%s %s\n", post.getTitle().toString(), post.getCreateAt().toString());
        }

        myBlog.setSortingType(SortingType.MODIFIED_AEC);
        posts = myBlog.getPosts();
        for (Post post: posts) {
            System.out.printf("%s %s\n", post.getTitle().toString(), post.getCreateAt().toString());
        }
    }
}
