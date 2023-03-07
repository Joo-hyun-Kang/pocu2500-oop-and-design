package academy.pocu.comp2500.assignment1;

import academy.pocu.comp2500.assignment1.registry.Registry;

public class App {
    String user = "User";


    String blog = "Blog";
    String addPost = "addPost";
    String getPosts = "getPosts";
    String setTagFilter = "setTagFilter";
    String setAuthorFilter = "setAuthorFilter";
    String setType = "setType";

    String post = "Post";
    String setBodyPost = "setBody";
    String setTitle = "setTitle";
    String addTag = "addTag";
    String addComment = "addComment";
    String addReaction = "addReaction";
    String removeReaction = "removeReaction";
    String getComments = "getComments";

    String comment = "Comment";
    String addSubcomment = "addSubcomment";
    String setContentComment = "setContent";
    String upvoteComment = "upvote";
    String downvoteComment = "downvote";
    String getSubcomments = "getSubcomments";

    public App(Registry registry) {
        registry.registerBlogCreator(blog); //블로그를 생성하는 생성자를 등록한다.
        registry.registerTagFilterSetter(blog, setTagFilter); // 태그 필터를 설정하는 메서드를 등록한다.
        registry.registerAuthorFilterSetter(blog, setAuthorFilter); // 작성자 필터를 설정하는 메서드를 등록한다.
        registry.registerPostOrderSetter(blog, setType); // 블로그 글의 정렬 방법을 설정하는 메서드를 등록한다.
        registry.registerPostListGetter(blog, getPosts); // 블로그 글 목록을 가져오는 메서드를 등록한다.
        registry.registerPostAdder(blog, addPost); // 블로그에 글을 추가하는 메서드를 등록한다.
        registry.registerPostTitleUpdater(post, setTitle); // 발행된 블로그 글의 제목을 바꾸는 메서드를 등록한다.
        registry.registerPostBodyUpdater(post, setBodyPost); //발행된 블로그 글의 본문을 바꾸는 메서드를 등록한다.
        registry.registerPostTagAdder(post, addTag); // 블로그 글에 태그를 추가하는 메서드를 등록한다.
        registry.registerCommentAdder(post, addComment); // 블로그 글에 댓글을 추가하는 메서드를 등록한다.
        registry.registerSubcommentAdder(comment, addSubcomment); // 댓글에 하위 댓글을 추가하는 메서드를 등록한다.
        registry.registerCommentUpdater(comment, setContentComment); //댓글의 내용을 바꾸는 메서드를 등록한다.
        registry.registerSubcommentUpdater(comment, setContentComment); // 하위 댓글의 내용을 바꾸는 메서드를 등록한다.
        registry.registerReactionAdder(post, addReaction); // 블로그 글에 리액션을 추가하는 메서드를 등록한다.
        registry.registerReactionRemover(post, removeReaction); // 블로그 글로부터 리액션을 제거하는 메서드를 등록한다.
        registry.registerCommentUpvoter(comment, upvoteComment); // 댓글을 추천하는 메서드를 등록한다.
        registry.registerCommentDownvoter(comment, downvoteComment); // 댓글을 비추천하는 메서드를 등록한다.
        registry.registerCommentListGetter(post, getComments); // 블로그 글에 달린 댓글들을 가져오는 메서드를 등록한다.
        registry.registerSubcommentListGetter(comment, getSubcomments); //댓글에 달린 하위 댓글들을 가져오는 메서드를 등록한다.
        registry.registerSubcommentUpvoter(comment, upvoteComment); // 하위 댓글을 추천하는 메서드를 등록한다.
        registry.registerSubcommentDownvoter(comment, downvoteComment); // 하위 댓글을 비추천하는 메서드를 등록한다.
    }
}