package academy.pocu.comp2500.assignment1;

import academy.pocu.comp2500.assignment1.registry.Registry;

public class App {
    public App(Registry registry) {
        //example
        //registry.registerPostAdder("Foo", "bar");

                //registerBlogCreator(): 블로그를 생성하는 생성자를 등록한다.
        registry.registerBlogCreator("Blog");

                //registerTagFilterSetter(): 태그 필터를 설정하는 메서드를 등록한다.
        registry.registerTagFilterSetter("Blog", "getPostsByTagOrNull");

                //registerAuthorFilterSetter(): 작성자 필터를 설정하는 메서드를 등록한다.
        registry.registerAuthorFilterSetter("Blog", "getPostsByAuthorOrNull");

                //registerPostOrderSetter(): 블로그 글의 정렬 방법을 설정하는 메서드를 등록한다.
        registry.registerPostOrderSetter("Blog", "getSortingType");

                //registerPostListGetter(): 블로그 글 목록을 가져오는 메서드를 등록한다.
        registry.registerPostListGetter("Blog", "getPosts");

                //registerPostAdder(): 블로그에 글을 추가하는 메서드를 등록한다.
        registry.registerPostAdder("Blog", "addPost");

                //registerPostTitleUpdater(): 발행된 블로그 글의 제목을 바꾸는 메서드를 등록한다.
        registry.registerPostTitleUpdater("Blog", "updateTitle");

                //registerPostBodyUpdater(): 발행된 블로그 글의 본문을 바꾸는 메서드를 등록한다.
        registry.registerPostBodyUpdater("Blog", "updateBody");

                //registerPostTagAdder(): 블로그 글에 태그를 추가하는 메서드를 등록한다.
        registry.registerPostTagAdder("Blog", "updateTag");

                //registerCommentAdder(): 블로그 글에 댓글을 추가하는 메서드를 등록한다.
        registry.registerCommentAdder("Post", "addComment");

                //registerSubcommentAdder(): 댓글에 하위 댓글을 추가하는 메서드를 등록한다.
        registry.registerSubcommentAdder("Comment", "addSubComment");

                //registerCommentUpdater(): 댓글의 내용을 바꾸는 메서드를 등록한다.
        registry.registerCommentUpdater("Comment", "setContent");

                //registerSubcommentUpdater(): 하위 댓글의 내용을 바꾸는 메서드를 등록한다.
        registry.registerSubcommentUpdater("Comment", "setContent");

                //registerReactionAdder(): 블로그 글에 리액션을 추가하는 메서드를 등록한다.
        registry.registerReactionAdder("Post", "addEmoji");

                //registerReactionRemover(): 블로그 글로부터 리액션을 제거하는 메서드를 등록한다.
        registry.registerReactionRemover("Post", "removeEmoji");

                //registerCommentUpvoter(): 댓글을 추천하는 메서드를 등록한다.
        registry.registerCommentUpvoter("Comment", "plusUpvote");

                //registerCommentDownvoter(): 댓글을 비추천하는 메서드를 등록한다.
        registry.registerCommentDownvoter("Comment", "plusDownvote");

                //registerCommentListGetter(): 블로그 글에 달린 댓글들을 가져오는 메서드를 등록한다.
        registry.registerCommentListGetter("Post", "getComments");

                //registerSubcommentListGetter(): 댓글에 달린 하위 댓글들을 가져오는 메서드를 등록한다.
        registry.registerSubcommentListGetter("Comment", "getSubcomment");

                //registerSubcommentUpvoter(): 하위 댓글을 추천하는 메서드를 등록한다.
        registry.registerSubcommentUpvoter("Comment", "plusUpvote");

                //registerSubcommentDownvoter(): 하위 댓글을 비추천하는 메서드를 등록한다.
        registry.registerSubcommentDownvoter("Comment", "plusDownvote");
    }
}
