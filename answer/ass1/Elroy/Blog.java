package academy.pocu.comp2500.assignment1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Blog {
    private final List<Post> posts = new ArrayList<>();
    private final UUID blogId;

    private UUID authorFilter;
    private List<String> tagFilters = new ArrayList<>();
    private SortingType sortingType = SortingType.DESC_CREATED_TIME;

    public Blog(UUID blogId) {
        this.blogId = blogId;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void setAuthorFilter(UUID authorId) {
        this.authorFilter = authorId;
    }

    public void setTagFilters(List<String> tags) {
        this.tagFilters = tags;
    }

    public void setSortingType(SortingType sortingType) {
        this.sortingType = sortingType;
    }

    public List<Post> getPosts() {
        List<Post> result = new ArrayList<>();

        if (this.tagFilters.isEmpty()) {
            result = this.posts;
        } else {
            for (Post post : this.posts) {
                for (String tag : this.tagFilters) {
                    if (post.getTags().contains(tag)) {
                        result.add(post);
                        break;
                    }
                }
            }
        }


        if (authorFilter != null) {
            result = result.stream()
                    .filter(post -> post.getAuthorId().equals(authorFilter))
                    .collect(Collectors.toList());
        }

        Comparator<Post> createdTimeComparator = (o1, o2) -> {
            if (o1.getCreatedDateTime().isBefore(o2.getCreatedDateTime())) {
                return -1;
            }

            if (o1.getCreatedDateTime().isAfter(o2.getCreatedDateTime())) {
                return 1;
            }
            return 0;
        };

        Comparator<Post> modifiedTimeComparator = (o1, o2) -> {
            if (o1.getModifiedDateTime().isBefore(o2.getModifiedDateTime())) {
                return -1;
            }

            if (o1.getModifiedDateTime().isAfter(o2.getModifiedDateTime())) {
                return 1;
            }
            return 0;
        };

        switch (sortingType) {
            case ASC_CREATED_TIME:
                result.sort(createdTimeComparator);
                break;

            case DESC_CREATED_TIME:
                result.sort(createdTimeComparator.reversed());
                break;

            case ASC_MODIFIED_TIME:
                result.sort(modifiedTimeComparator);
                break;

            case DESC_MODIFIED_TIME:
                result.sort(modifiedTimeComparator.reversed());
                break;

            case ASC_TITLE:
                result = result.stream()
                        .sorted(Comparator.comparing(Post::getTitle))
                        .collect(Collectors.toList());
                break;
        }

        return result;
    }

    public UUID getBlogId() {
        return blogId;
    }
}
