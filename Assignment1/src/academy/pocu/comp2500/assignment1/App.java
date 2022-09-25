package academy.pocu.comp2500.assignment1;

import academy.pocu.comp2500.assignment1.registry.Registry;

public class App {
    public App(Registry registry) {
        registry.registerBlogCreator("Blog");

        registry.registerTagFilterSetter("Blog", "setTagFilter");

        registry.registerAuthorFilterSetter("Blog", "setAuthorFilter");

        registry.registerPostOrderSetter("Blog", "getSortingType");

        registry.registerPostListGetter("Blog", "getPosts");

        registry.registerPostAdder("Blog", "addPost");

        registry.registerPostTitleUpdater("Blog", "updateTitle");

        registry.registerPostBodyUpdater("Blog", "updateBody");

        registry.registerPostTagAdder("Blog", "updateTag");

        registry.registerCommentAdder("Post", "addComment");

        registry.registerSubcommentAdder("Comment", "addSubComment");

        registry.registerCommentUpdater("Post", "updateCommet");

        registry.registerSubcommentUpdater("Comment", "setContent");

        registry.registerReactionAdder("Post", "addEmoji");

        registry.registerReactionRemover("Post", "removeEmoji");

        registry.registerCommentUpvoter("Comment", "plusUpvote");

        registry.registerCommentDownvoter("Comment", "plusDownvote");

        registry.registerCommentListGetter("Post", "getComments");

        registry.registerSubcommentListGetter("Comment", "getSubcomment");

        registry.registerSubcommentUpvoter("Comment", "plusUpvote");

        registry.registerSubcommentDownvoter("Comment", "plusDownvote");
    }
}
