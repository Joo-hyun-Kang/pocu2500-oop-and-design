package academy.pocu.comp2500.assignment1;

import javax.lang.model.type.ArrayType;
import java.lang.annotation.Target;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class Post {
    private int postId;
    private OffsetDateTime createAt;
    private String content;
    private ArrayList<String> tags;

    Post(int postId, String content) {
        this(postId, content, null);
    }
    Post(int postId, String content, ArrayList<String> tags) {
        this.postId = postId;
        this.createAt = OffsetDateTime.now();
        this.content = content;
        this.tags = tags;
    }

    public boolean findTag(String tag) {
        for (String element : tags) {
            if (element.equals(tag)) {
                return true;
            }
        }
        return false;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getCreateAt() { return createAt; }

    public void setContent(String content) {
        this.content = content;
    }
}
