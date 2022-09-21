package academy.pocu.comp2500.assignment1;

import java.util.ArrayList;

public class Blog {
    private int blogId;
    private int count;
    private ArrayList<Post> posts;

    Blog(int blogId) {
        this.blogId = blogId;
    }

    public void createPost(String content, ArrayList<String> tags) {
        Post newPost = new Post(count++, content, tags);
        posts.add(newPost);
    }

    public ArrayList<Post> getPostsAllOrNull() {
        return posts;
    }

    public ArrayList<Post> getPostTagOrNull(String tag) {
        ArrayList<Post> sortedPost = new ArrayList<>();
        for (Post post : posts) {
            if (post.findTag(tag) == true) {
                sortedPost.add(post);
            }
        }

        return sortedPost.size() > 0 ? sortedPost : null;
    }
}
