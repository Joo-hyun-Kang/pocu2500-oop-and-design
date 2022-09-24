package academy.pocu.comp2500.assignment1;

import java.util.ArrayList;
import java.util.UUID;

public class Blog {
    private final UUID blogId;
    private final String name;
    private final Author owner;
    private ArrayList<Post> posts;
    private SortingType sortingType;

    public Blog(UUID blogId, String name, Author author) {
        this.blogId = blogId;
        this.name = name;
        this.owner = author;
        this.posts = new ArrayList<>();
        this.sortingType = SortingType.CREATED_DES;
    }

    public void addPost(Post post) {
        //assert (author.getId() == post.getAuthor().getId());
        this.posts.add(post);
    }

    public void updateTitle(Author author, Post post, String content) {
        assert (author.getId() == post.getAuthor().getId());
        post.setTitle(content);
    }

    public void updateBody(Author author, Post post, String content) {
        assert (author.getId() == post.getAuthor().getId());
        post.setBody(content);
    }

    public void updateTag(Author author, Post post, String tag) {
        assert (author.getId() == post.getAuthor().getId());
        post.addTags(tag);
    }

    public void removePost(Post post) {
        this.posts.remove(post);
    }

    public void removeTag(Post post, String tag) {
        post.removeTags(tag);
    }

    public void setSortingType(SortingType sortingType) {
        this.sortingType = sortingType;
    }

    public UUID getBlogId() {
        return this.blogId;
    }

    public String getName() {
        return this.name;
    }

    public Author getOwner() {
        return owner;
    }

    public SortingType getSortingType() {
        return sortingType;
    }

    public ArrayList<Post> getPosts() {
        ArrayList<Post> result = null;
        switch (this.sortingType) {
            case CREATED_DES:
                posts.sort((post1, post2) -> post1.getCreateAt().compareTo(post2.getCreateAt()));
                break;
            case CREATED_AEC:
                posts.sort((post1, post2) -> post2.getCreateAt().compareTo(post1.getCreateAt()));
                break;
            case MODIFIED_DES:
                posts.sort((post1, post2) -> post1.getModifiedAt().compareTo(post2.getModifiedAt()));
                break;
            case MODIFIED_AEC:
                posts.sort((post2, post1) -> post2.getModifiedAt().compareTo(post1.getModifiedAt()));
                break;
            default:
                assert (false) : "unknown type";
                break;
        }

        return posts;
    }

    public ArrayList<Post> getPostsByTagOrNull(String tag) {
        ArrayList<Post> sortedPost = new ArrayList<>();
        for (Post post : posts) {
            if (post.isTagExist(tag) == true) {
                sortedPost.add(post);
            }
        }

        return sortedPost.size() > 0 ? sortedPost : null;
    }

    public ArrayList<Post> getPostsByAuthorOrNull(Author author) {
        ArrayList<Post> sortedPost = new ArrayList<>();
        for (Post post : posts) {
            if (post.getAuthor().getId() == author.getId()) {
                sortedPost.add(post);
            }
        }

        return sortedPost.size() > 0 ? sortedPost : null;
    }

}
