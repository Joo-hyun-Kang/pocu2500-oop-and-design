package academy.pocu.comp2500.assignment1;

import java.util.UUID;

public class Emoji {
    private UUID emojiId;
    private Author author;
    private EmojiType emoji;

    public Emoji(UUID id, Author author, EmojiType emoji) {
        this.emojiId = id;
        this.author = author;
        this.emoji = emoji;
    }

    public UUID getEmojiId() {
        return emojiId;
    }

    public Author getAuthor() {
        return author;
    }

    public EmojiType getEmoji() {
        return emoji;
    }
}
