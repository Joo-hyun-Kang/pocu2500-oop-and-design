// App.java, SortingType.java(enum), ReactionType.java(enum)은 생략

// Blog.java

package academy.pocu.comp2500.assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Blog {
    private String userId;
    private ArrayList<Post> posts = new ArrayList<>();
    private HashSet<String> tagFilterOrEmpty = new HashSet<>();
    private String authorFilterOrNull;
    private SortingType sortingType;

    public Blog(String userId) {
        this.userId = userId;
        this.sortingType = SortingType.CREATED_DESC;
    }

    public void setTagFilter(HashSet<String> tags) {
        this.tagFilterOrEmpty = tags;
    }

    public void setAuthorFilter(String userId) {
        this.authorFilterOrNull = userId;
    }

    public void setPostOrder(SortingType sortingType) {
        this.sortingType = sortingType;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public ArrayList<Post> getPostList() {
        ArrayList<Post> resultPostList;
        resultPostList = doAuthorFilter(this.posts);
        resultPostList = doTagFilter(resultPostList);
        resultPostList = sortPostList(resultPostList);
        return resultPostList;
    }

    private ArrayList<Post> doAuthorFilter(ArrayList<Post> posts) {
        ArrayList<Post> filteredPostList = new ArrayList<>();
        if (this.authorFilterOrNull != null) {
            for (Post post : posts) {
                if (post.isUserIdExists(this.authorFilterOrNull) == true) {
                    filteredPostList.add(post);
                }
            }
        } else {
            filteredPostList = posts;
        }
        return filteredPostList;
    }

    private ArrayList<Post> doTagFilter(ArrayList<Post> posts) {
        ArrayList<Post> filteredPostList = new ArrayList<>();
        if (tagFilterOrEmpty.isEmpty() != true) {
            for (Post post : posts) {
                if (post.isTagAtLeastExists(this.tagFilterOrEmpty) == true) {
                    filteredPostList.add(post);
                }
            }
        } else {
            filteredPostList = posts;
        }
        return filteredPostList;
    }

    private ArrayList<Post> sortPostList(ArrayList<Post> posts) {
        switch (this.sortingType) {
            case CREATED_DESC:
                Collections.sort(posts, (s1, s2) -> s2.getCreatedDateTime().compareTo(s1.getCreatedDateTime()));
                // Collections.sort(posts, Comparator.comparing(Post::getCreatedDateTime).reversed());
                break;
            case CREATED_ASC:
                Collections.sort(posts, (s1, s2) -> s1.getCreatedDateTime().compareTo(s2.getCreatedDateTime()));
                break;
            case UPDATED_DESC:
                Collections.sort(posts, (s1, s2) -> s2.getUpdatedDateTime().compareTo(s1.getUpdatedDateTime()));
                break;
            case UPDATED_ASC:
                Collections.sort(posts, (s1, s2) -> s1.getUpdatedDateTime().compareTo(s2.getUpdatedDateTime()));
                break;
            case LEXICAL_ASC:
                Collections.sort(posts, (s1, s2) -> s1.getTitle().compareTo(s2.getTitle()));
                break;
            default:
                assert (false) : "Unknown case SortingType in 'sortPostList' method";
                break;
        }
        return posts;
    }
}


// Post.java

package academy.pocu.comp2500.assignment1;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Post {
    private String userId;
    private String title;
    private String body;
    private OffsetDateTime createdDateTime;
    private OffsetDateTime updatedDateTime;
    private HashSet<String> tags = new HashSet<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private HashMap<ReactionType, HashSet<String>> reactions = new HashMap<>();

    public Post(String userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.createdDateTime = OffsetDateTime.now();
        this.updatedDateTime = createdDateTime;
        for (ReactionType reactionType : ReactionType.values()) {
            this.reactions.put(reactionType, new HashSet<>());
        }
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }

    public OffsetDateTime getCreatedDateTime() {
        return this.createdDateTime;
    }

    public OffsetDateTime getUpdatedDateTime() {
        return this.updatedDateTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public HashSet<String> getTags() {
        return this.tags;
    }

    public int getReactions(ReactionType reactionType) {
        return this.reactions.get(reactionType).size();
    }

    public boolean isUserIdExists(String userId) {
        return this.userId.equals(userId);
    }

    public boolean isTagAtLeastExists(HashSet<String> tags) {
        for (String tag : tags) {
            if (this.tags.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    public boolean setTitle(String userId, String title) {
        if (this.userId.equals(userId) == false) {
            return false;
        }
        this.updatedDateTime = OffsetDateTime.now();
        this.title = title;
        return true;
    }

    public boolean setBody(String userId, String body) {
        if (this.userId.equals(userId) == false) {
            return false;
        }
        this.updatedDateTime = OffsetDateTime.now();
        this.body = body;
        return true;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public ArrayList<Comment> getCommentListWithSort() {
        Collections.sort(this.comments, (n1, n2) -> Integer.compare(n2.getVotePoints(), n1.getVotePoints()));
        // Collections.sort(comments, Comparator.comparing(Comment::getVotePoints).reversed());
        return this.comments;
    }

    public boolean addReaction(String userId, ReactionType reactionType) {
        return this.reactions.get(reactionType).add(userId);
    }

    public boolean removeReaction(String userId, ReactionType reactionType) {
        return this.reactions.get(reactionType).remove(userId);
    }
}


// Comment.java

package academy.pocu.comp2500.assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Comment {
    private String userId;
    private String text;
    private ArrayList<Comment> subcomments = new ArrayList<>();
    private HashSet<String> upvoters = new HashSet<>();
    private HashSet<String> downvoters = new HashSet<>();

    public Comment(String userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public boolean setCommentFamily(String userId, String text) {
        if (this.userId.equals(userId) == false) {
            return false;
        }
        this.text = text;
        return true;
    }

    public ArrayList<Comment> getSubcommentListWithSort() {
        Collections.sort(this.subcomments, (n1, n2) -> Integer.compare(n2.getVotePoints(), n1.getVotePoints()));
        return this.subcomments;
    }

    public void addSubcomment(Comment comment) {
        this.subcomments.add(comment);
    }

    public boolean addUpvote(String userId) {
        if (this.upvoters.add(userId) == true) {
            this.downvoters.remove(userId);
            return true;
        }
        return false;
    }

    public boolean addDownvote(String userId) {
        if (this.downvoters.add(userId) == true) {
            this.upvoters.remove(userId);
            return true;
        }
        return false;
    }

    int getVotePoints() {
        return this.upvoters.size() - this.downvoters.size();
    }
}
