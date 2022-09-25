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

        registry.registerPostTitleUpdater("Post", "updateTitle");

        registry.registerPostBodyUpdater("Post", "updateBody");

        registry.registerPostTagAdder("Blog", "updateTag");

        registry.registerCommentAdder("Post", "addComment");

        registry.registerSubcommentAdder("Comment", "addSubComment");

        registry.registerCommentUpdater("Comment", "updateContent");

        registry.registerSubcommentUpdater("Comment", "updateContent");

        registry.registerReactionAdder("Post", "addEmoji");

        registry.registerReactionRemover("Post", "removeEmoji");

        registry.registerCommentUpvoter("Comment", "upvote");

        registry.registerCommentDownvoter("Comment", "downvote");

        registry.registerCommentListGetter("Post", "getComments");

        registry.registerSubcommentListGetter("Comment", "getSubcomment");

        registry.registerSubcommentUpvoter("Comment", "upvote");

        registry.registerSubcommentDownvoter("Comment", "downvote");
    }
}
