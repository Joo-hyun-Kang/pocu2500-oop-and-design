package academy.pocu.comp2500.assignment1;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Blog {
    private Author author;
    private ArrayList<Post> posts;
    private SortingType sortingType;
    private boolean isAuthorFiltered;
    private boolean isTagFiltered;
    private ArrayList<Post> sortedPosts;

    public Blog(Author author) {
        this.author = author;
        this.posts = new ArrayList<>();
        this.sortingType = SortingType.CREATED_DES;
        this.isTagFiltered = false;
        this.isAuthorFiltered = false;
        this.sortedPosts = new ArrayList<>();
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void updateTitle(Author author, Post post, String content) {
        assert (author.getAuthorId() == post.getAuthor().getAuthorId());
        post.setTitle(content);
    }

    public void updateBody(Author author, Post post, String content) {
        assert (author.getAuthorId() == post.getAuthor().getAuthorId());
        post.setBody(content);
    }

    public void updateTag(Author author, Post post, String tag) {
        assert (author.getAuthorId() == post.getAuthor().getAuthorId());
        post.addTags(tag);
    }

    public void setSortingType(SortingType sortingType) {
        this.sortingType = sortingType;
    }

    public Author getAuthor() {
        return author;
    }

    public SortingType getSortingType() {
        return sortingType;
    }

    public ArrayList<Post> getPosts() {
        ArrayList<Post> res = null;
        if (isTagFiltered || isAuthorFiltered) {
            res = sortedPosts;
        } else {
            res = posts;
        }

        switch (this.sortingType) {
            case CREATED_DES:
                res.sort((post1, post2) -> post1.getCreateAt().compareTo(post2.getCreateAt()));
                break;
            case CREATED_AEC:
                res.sort((post1, post2) -> post2.getCreateAt().compareTo(post1.getCreateAt()));
                break;
            case MODIFIED_DES:
                res.sort((post1, post2) -> post1.getModifiedAt().compareTo(post2.getModifiedAt()));
                break;
            case MODIFIED_AEC:
                res.sort((post2, post1) -> post2.getModifiedAt().compareTo(post1.getModifiedAt()));
                break;
            case TITLE_AEC:
                res.sort((post1, post2) -> post1.getTitle().compareTo(post2.getTitle()));
                break;
            default:
                assert (false) : "unknown type";
                break;
        }

        return new ArrayList<>(res);
    }

    public void setTagFilter(String tag) {
        if (isTagFiltered || isAuthorFiltered) {
            ArrayList<Post> newSortedPosts = new ArrayList<>();
            for (Post post : sortedPosts) {
                if (post.isTagExist(tag) == true) {
                    newSortedPosts.add(post);
                }
            }

            sortedPosts = newSortedPosts;
        } else {
            for (Post post : posts) {
                if (post.isTagExist(tag) == true) {
                    sortedPosts.add(post);
                }
            }
        }

        this.isTagFiltered = true;
    }

    public void setAuthorFilter(Author author) {
        if (isTagFiltered || isAuthorFiltered) {
            ArrayList<Post> newSortedPosts = new ArrayList<>();
            for (Post post : sortedPosts) {
                if (post.getAuthor().getAuthorId() == author.getAuthorId()) {
                    newSortedPosts.add(post);
                }
            }

            sortedPosts = newSortedPosts;
        } else {
            for (Post post : posts) {
                if (post.getAuthor().getAuthorId() == author.getAuthorId()) {
                    sortedPosts.add(post);
                }
            }
        }

        this.isAuthorFiltered = true;
    }
}
