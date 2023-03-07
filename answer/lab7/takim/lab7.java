// Author.java
public class Author {
    private String firstName;

    private String lastName;

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

    public void setFullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Author)) {
            return false;
        }

        Author other = (Author) object;

        if (this.firstName.equals(other.firstName) && this.lastName.equals(other.lastName)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (this.firstName + this.lastName).hashCode();
    }
}

// Book.java
public class Book {
    private String bookName;

    private Author author;

    private int yearOfPublication;

    private Genre genre;

    public Book(String bookName, Author author, int yearOfPublication, Genre genre) {
        this.bookName = bookName;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.genre = genre;
    }

    public String getBookName() {
        return this.bookName;
    }

    public Author getAuthor() {
        return this.author;
    }

    public int getYearOfPublication() {
        return this.yearOfPublication;
    }

    public Genre getGenre() {
        return this.genre;
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", this.bookName, this.author.toString());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Book)) {
            return false;
        }

        Book other = (Book) object;
        if (this.bookName.equals(other.bookName) && this.author.equals(other.author) && this.yearOfPublication == other.yearOfPublication && this.genre.equals(other.genre)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.bookName.hashCode() * this.author.hashCode() * this.yearOfPublication * this.genre.hashCode();
    }
}

// Genre.java
public enum Genre {
    SCIENCE_FICTION,
    ROMANCE,
    BIOGRAPHY,
    FANTASY,
    MYSTERY,
    SUSPENSE
}

// Books.java
import java.util.ArrayList;

public class Books {
    protected int maxBookCount;

    protected ArrayList<Book> books;

    protected Books(int maxBookCount) {
        this.maxBookCount = maxBookCount;
        this.books = new ArrayList<>();
    }

    public int getMaxBookCount() {
        return this.maxBookCount;
    }

    public ArrayList<Book> getBooks() {
        return this.books;
    }

    public boolean remove(Book book) {
        for (int i = 0; i < this.books.size(); ++i) {
            if (this.books.get(i).equals(book)) {
                this.books.remove(book);
                return true;
            }
        }
        return false;
    }
}

// Bookshelf.java
public class Bookshelf extends Books {
    private static int hashGenerator;

    private int hash;

    public Bookshelf(int maxBookCount) {
        super(maxBookCount);
        this.hash = hashGenerator;
        ++hashGenerator;
    }

    public boolean add(Book book) {
        if (super.books.size() < super.maxBookCount) {
            super.books.add(book);
            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Bookshelf)) {
            return false;
        }

        Bookshelf other = (Bookshelf) object;

        if (this != other) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }
}

// Bundle.java
public class Bundle extends Books {
    private static final int MAX_BOOK_COUNT = 4;

    private String bundleName;

    public Bundle(String bundleName) {
        super(MAX_BOOK_COUNT);
        this.bundleName = bundleName;
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public boolean add(Book book) {
        if (super.books.size() < super.maxBookCount && !super.books.contains(book)) {
            super.books.add(book);
            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Bundle)) {
            return false;
        }

        Bundle other = (Bundle) object;

        if (!this.bundleName.equals(other.bundleName)) {
            return false;
        }

        for (int i = 0; i < super.books.size(); ++i) {
            if (!super.books.contains(other.books.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = this.bundleName.hashCode();

        for (Book book : super.books) {
            hash *= book.hashCode();
        }
        return hash;
    }
}

// ReadingList.java
public class ReadingList extends Books {
    private String readingListName;

    public ReadingList(String readingListName) {
        super(Integer.MAX_VALUE);
        this.readingListName = readingListName;
    }

    public String getReadingListName() {
        return this.readingListName;
    }

    public void add(Book book) {
        super.books.add(book);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < super.books.size(); ++i) {
            stringBuilder.append(String.format("%d. %s%s", (i + 1), super.books.get(i).toString(), System.lineSeparator()));
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof ReadingList)) {
            return false;
        }

        ReadingList other = (ReadingList) object;

        if (!this.readingListName.equals(other.readingListName)) {
            return false;
        }

        for (int i = 0; i < super.books.size(); ++i) {
            if (!super.books.get(i).equals(other.books.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = this.readingListName.hashCode();

        for (int i = 0; i < super.books.size(); ++i) {
            hash *= (super.books.get(i).hashCode() + i);
        }

        return hash;
    }
}
