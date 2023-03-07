package academy.pocu.comp2500.assignment1;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;


public class Blog {
    private final User USER;
    private final String ID;
    private String title;
    private final OffsetDateTime CREATED_DATE_TIME;
    private ArrayList<Post> posts = new ArrayList<>();
    private String authorFilter = "";
    private boolean isAuthorFilterOn = false;
    private ArrayList<String> tagList = new ArrayList<>();
    private boolean isTagFilterOn = false;
    private SortingType type = SortingType.CREATED_DATE_TIME_DESCENDING;

    public enum FilterMode {
        AUTHOR_FILTER,
        TAG_FILTER,
        AUTHOR_AND_TAG_FILTER,
        SHOW_ALL
    }

    public Blog(User user, String title, String id) {
        this.USER = user;
        this.title = title;
        this.CREATED_DATE_TIME = OffsetDateTime.now();
        this.ID = id;
    }

    public String getAuthorFilter() {
        return authorFilter;
    }

    public void setAuthorFilter(User user) {
        if (user == null) {
            this.isAuthorFilterOn = false;

            return;
        }

        this.authorFilter = user.getUserId();
        this.isAuthorFilterOn = true;
    }


    public ArrayList<String> getTagFilter() {
        return tagList;
    }

    public void setTagFilter(ArrayList<String> tags) {
        if (tags.size() == 0) {
            tagList.clear();
            this.isTagFilterOn = false;

            return;
        }

        for (String t : tags) {
            this.tagList.add(t);
        }
        this.isTagFilterOn = true;
    }

    public SortingType getType() {
        return type;
    }

    public void setType(SortingType sortingType) {
        this.type = sortingType;
    }

    public User getUser() {
        return this.USER;
    }

    public String getId() {
        return this.ID;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public ArrayList<Post> getPosts() {
        SortingType type = getType();

        switch (type) {
            case CREATED_DATE_TIME_ASCENDING:
                ArrayList<Post> sortedPosts = new ArrayList<>();
                filterPosts(sortedPosts);

                return sortedPosts;

            case CREATED_DATE_TIME_DESCENDING:
                sortedPosts = new ArrayList<Post>();
                filterPosts(sortedPosts);

                Collections.sort(sortedPosts, (o1, o2) -> {
                    if (o1.getCreatedDateTime().isAfter(o2.getCreatedDateTime())) {
                        return -1;
                    } else if (o1.getCreatedDateTime().isBefore(o2.getCreatedDateTime())) {
                        return 1;
                    } else {
                        return 0;
                    }
                });

                return sortedPosts;

            case MODIFIED_DATE_TIME_ASCENDING:
                sortedPosts = new ArrayList<Post>();
                filterPosts(sortedPosts);

                Collections.sort(sortedPosts, (o1, o2) -> {
                    if (o1.getModifiedDateTime().isAfter(o2.getModifiedDateTime())) {
                        return 1;
                    } else if (o1.getModifiedDateTime().isBefore(o2.getModifiedDateTime())) {
                        return -1;
                    } else {
                        return 0;
                    }
                });

                return sortedPosts;

            case MODIFIED_DATE_TIME_DESCENDING:
                sortedPosts = new ArrayList<>();
                filterPosts(sortedPosts);

                Collections.sort(sortedPosts, (o1, o2) -> {
                    if (o1.getModifiedDateTime().isAfter(o2.getModifiedDateTime())) {
                        return -1;
                    } else if (o1.getModifiedDateTime().isBefore(o2.getModifiedDateTime())) {
                        return 1;
                    } else {
                        return 0;
                    }
                });

                return sortedPosts;

            case TITLE_ASCENDING:
                sortedPosts = new ArrayList<>();
                filterPosts(sortedPosts);

                Collections.sort(sortedPosts, (o1, o2) -> {
                    if (o1.getTitle().compareTo(o2.getTitle()) > 0) {
                        return 1;
                    } else if (o1.getTitle().compareTo(o2.getTitle()) < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                });

                return sortedPosts;

            default:
                sortedPosts = new ArrayList<>();
                System.out.println("Warning: Undefined SortMode!");

                return sortedPosts;

        }

    }

    private void filterPosts(ArrayList<Post> sortedPosts) {
        for (Post post : posts) {
            switch (getFilterMode()) {
                case AUTHOR_FILTER:
                    if (authorFilter.equals(post.getAuthor().getUserId())) {
                        sortedPosts.add(post);
                    }
                    break;
                case TAG_FILTER:
                    if (post.isTagMatching(tagList)) {
                        sortedPosts.add(post);
                    }
                    break;
                case AUTHOR_AND_TAG_FILTER:
                    if (authorFilter.equals(post.getAuthor().getUserId()) && post.isTagMatching(tagList)) {
                        sortedPosts.add(post);
                    }
                    break;
                case SHOW_ALL:
                    sortedPosts.add(post);
                    break;
                default:
                    System.out.println("Warning: undefined filter mode!");
                    break;
            }
            
        }
    }

    private FilterMode getFilterMode() {
        if (isAuthorFilterOn) {
            if (isTagFilterOn) {
                return FilterMode.AUTHOR_AND_TAG_FILTER;
            } else {
                return FilterMode.AUTHOR_FILTER;
            }
        } else {
            if (isTagFilterOn) {
                return FilterMode.TAG_FILTER;
            } else {
                return FilterMode.SHOW_ALL;
            }
        }

    }

}