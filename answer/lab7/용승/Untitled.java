// Author.java
package academy.pocu.comp2500.lab7;

public class Author {
    private final String firstName;
    private final String lastName;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (!(obj instanceof Author)) {
            return false;
        }

        Author other = (Author) obj;

        return this.firstName.equals(other.firstName) && this.lastName.equals(other.lastName);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    public String getFullName() {
        return this.toString();
    }
}


// Book.java
package academy.pocu.comp2500.lab7;

public class Book {
    private final String title;
    private final Author author;
    private final int publicationYear;
    private final Genre genre;

    public Book(String title, Author author, int publicationYear, Genre genre) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return this.title + " " + "[" + this.author + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (!(obj instanceof Book) || obj.hashCode() != this.hashCode()) {
            return false;
        }

        Book other = (Book) obj;

        return this.title.equals(other.title)
                && this.author.equals(other.author)
                && this.publicationYear == other.publicationYear
                && this.genre.equals(other.genre);
    }

    @Override
    public int hashCode() {
        int result = 139;
        result = (31 * result) + (this.title.hashCode());
        result = (31 * result) + (this.author.hashCode());
        result = (31 * result) + (this.publicationYear);
        result = (31 * result) + (this.genre.toString().hashCode());
        return result;
    }

    public String getTitle() {
        return this.title;
    }

    public Author getAuthor() {
        return this.author;
    }

    public int getPublicationYear() {
        return this.publicationYear;
    }

    public Genre getGenre() {
        return this.genre;
    }
}


// Bookshelf.java
package academy.pocu.comp2500.lab7;

import java.util.ArrayList;
import java.util.List;

public class Bookshelf {
    private final int capacity;
    private final List<Book> books;

    public Bookshelf(int capacity) {
        this.capacity = capacity;
        this.books = new ArrayList<>();
    }

    public boolean add(Book book) {
        assert (book != null);

        if (this.books.size() == this.capacity) {
            return false;
        }

        return this.books.add(book);
    }

    public boolean remove(Book book) {
        assert (book != null);

        if (!this.books.contains(book)) { // unnecessary if statement
            return false;
        }

        return this.books.remove(book);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(this.books);
    }
}


// Bundle.java
package academy.pocu.comp2500.lab7;

import java.util.ArrayList;
import java.util.List;

public class Bundle {
    private final String name;
    private final int capacity;
    private final List<Book> books;

    public Bundle(String name) {
        this.name = name;
        this.capacity = 4;
        this.books = new ArrayList<>();
    }

    public boolean add(Book book) {
        assert (book != null);

        if (this.books.size() == capacity || this.books.contains(book)) {
            return false;
        }

        return this.books.add(book);
    }

    public boolean remove(Book book) {
        assert (book != null);

        return this.books.remove(book);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (!(obj instanceof Bundle) || obj.hashCode() != this.hashCode()) {
            return false;
        }

        Bundle other = (Bundle) obj;

        if (!this.name.equals(other.name)) {
            return false;
        }

        if (this.books.size() != other.books.size()) {
            return false;
        }

        for (Book book : this.books) {
            if (!other.books.contains(book)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 139;

        result = (31 * result) + (this.name.hashCode());
        result = (31 * result) + (this.books.size());

        for (Book book : this.books) {
            result ^= (book.hashCode() << 16);
        }

        return result;
    }

    public String getName() {
        return this.name;
    }

    public List<Book> getBooks() {
        return new ArrayList<>(this.books);
    }
}

// ReadingList.java
package academy.pocu.comp2500.lab7;

import java.util.ArrayList;
import java.util.List;

public class ReadingList {
    private final String name;
    private final List<Book> books;

    public ReadingList(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    public void add(Book book) {
        assert (book != null);

        this.books.add(book);
    }

    public boolean remove(Book book) {
        assert (book != null);

        return this.books.remove(book);
    }

    @Override
    public String toString() {
        int readingListSize = books.size();

        StringBuilder sb = new StringBuilder();

        int index;

        for (index = 0; index < readingListSize - 1; index++) {
            sb.append(index + 1).append(". ").append(books.get(index)).append(System.lineSeparator());
        }

        sb.append(index + 1).append(". ").append(books.get(index)).append(System.lineSeparator());

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (!(obj instanceof ReadingList) || obj.hashCode() != this.hashCode()) {
            return false;
        }

        ReadingList other = (ReadingList) obj;

        if (!other.name.equals(this.name)) {
            return false;
        }

        if (other.books.size() != this.books.size()) {
            return false;
        }

        int numBooks = this.books.size();

        for (int i = 0; i < numBooks; i++) {
            if (!other.books.get(i).equals(this.books.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 139;

        result = (31 * result) + (this.name.hashCode());

        int numBooks = this.books.size();
        for (int i = 0; i < numBooks; i++) {
            result = (31 * result) + (this.books.get(i).hashCode()) * i;
        }

        return result;
    }

    public String getName() {
        return this.name;
    }

    public List<Book> getBooks() {
        return new ArrayList<>(this.books);
    }
}



// Genre.java (enum)
package academy.pocu.comp2500.lab7;

public enum Genre {
    SCIENCE_FICTION,
    ROMANCE,
    BIOGRAPHY,
    FANTASY,
    MYSTERY,
    SUSPENSE
}

