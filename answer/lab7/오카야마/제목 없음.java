// Author.java
package academy.pocu.comp2500.lab7;

public class Author {
    private final String firstName;
    private final String lastName;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Author) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Author other = (Author) obj;
        return this.firstName.equals(other.firstName) && this.lastName.equals(other.lastName);
    }

    @Override
    public int hashCode() {
        return this.firstName.hashCode() ^ (this.lastName.hashCode() << 16);
    }
}

// Book.java
package academy.pocu.comp2500.lab7;

public class Book {
    private final String title;
    private final Author author;
    private final int publishedYear;
    private final Genre genre;

    public Book(String title, Author author, int publishedYear, Genre genre) {
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.genre = genre;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthorName() {
        return this.author.toString();
    }

    public int getPublishedYear() {
        return this.publishedYear;
    }

    public Genre getGenre() {
        return this.genre;
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", this.title, this.author);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Book) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Book other = (Book) obj;
        return this.title.equals(other.title)
                && this.author.equals(other.author)
                && this.publishedYear == other.publishedYear
                && this.genre.equals(other.genre);
    }

    @Override
    public int hashCode() {
        return this.title.hashCode() ^ (this.author.hashCode() << 16) ^ (this.publishedYear * 17) ^ (this.genre.hashCode() << 16);
    }
}

// Bookshelf.java
package academy.pocu.comp2500.lab7;

import java.util.ArrayList;

public class Bookshelf {
    private final int maxBookCount;
    private final ArrayList<Book> books;

    public Bookshelf(int maxBookCount) {
        this.maxBookCount = maxBookCount;
        this.books = new ArrayList<Book>();
    }

    public boolean add(Book book) {
        if (this.books.size() >= this.maxBookCount) {
            return false;
        }

        this.books.add(book);
        return true;
    }

    public boolean remove(Book book) {
        if (this.books.isEmpty()) {
            return false;
        }

        Book myBook = getBookOrNull(book);
        if (myBook == null) {
            return false;
        }

        this.books.remove(book);
        return true;
    }

    public Book getBookOrNull(Book book) {
        for (Book myBook : this.books) {
            if (myBook.equals(book)) {
                return book;
            }
        }
        return null;
    }
}

// Bundle.java
package academy.pocu.comp2500.lab7;

import java.util.HashSet;

public class Bundle {
    private static final int MAX_BOOK_COUNT = 4;
    private final String name;
    private final HashSet<Book> books;

    public Bundle(String name) {
        this.name = name;
        this.books = new HashSet<Book>();
    }

    public boolean add(Book book) {
        if (this.books.size() >= MAX_BOOK_COUNT) {
            return false;
        }

        return this.books.add(book);
    }

    public boolean remove(Book book) {
        return this.books.remove(book);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Bundle) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Bundle other = (Bundle) obj;
        return this.name.equals(other.name)
                && isAllBookSame(other);
    }

    @Override
    public int hashCode() {
        int result = this.name.hashCode();
        result += (this.books.hashCode() << 16);
        return result;
    }

    private boolean isAllBookSame(Bundle other) {
        if (this.books.size() != other.books.size()) {
            return false;
        }

        for (Book myBook : this.books) {
            if (!other.books.contains(myBook)) {
                return false;
            }
        }
        return true;
    }
}

// ReadingList.java
package academy.pocu.comp2500.lab7;

import java.util.ArrayList;

public class ReadingList {
    private String name;
    private ArrayList<Book> books;

    public ReadingList(String name) {
        this.name = name;
        this.books = new ArrayList<Book>();
    }

    public void add(Book book) {
        this.books.add(book);
    }

    public boolean remove(Book book) {
        for (Book myBook : this.books) {
            if (myBook.equals(book)) {
                this.books.remove(book);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(1024);

        for (int i = 0; i < this.books.size(); ++i) {
            sb.append(String.format("%d. %s%s", i + 1, this.books.get(i), System.lineSeparator()));
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof ReadingList) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        ReadingList other = (ReadingList) obj;
        return this.name.equals(other.name)
                && isReadingListInSameOrder(other);
    }

    @Override
    public int hashCode() {
        int result = this.name.hashCode();
        result += (this.books.hashCode() << 16);
        return result;
    }

    private boolean isReadingListInSameOrder(ReadingList other) {
        if (this.books.size() != other.books.size()) {
            return false;
        }

        for (int i = 0; i < this.books.size(); ++i) {
            if (!this.books.get(i).equals(other.books.get(i))) {
                return false;
            }
        }
        return true;
    }
}