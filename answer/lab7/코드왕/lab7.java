package academy.pocu.comp2500.lab7;



public class Author {
    private String firstName;
    private String lastName;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String toString() {
        return this.firstName + ' ' + this.lastName;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Author)) {
            return false;
        }

        return this.toString().equals(obj.toString());
    }

    public int hashCode() {
        return (firstName + lastName).hashCode();
    }
}

public class Book {
    private String title;
    private Author author;
    private int publicationYear;
    private Genre genre;

    public Book(String title, Author author, int publicationYear, Genre genre) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.genre = genre;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author.toString();
    }

    public int getPublicationYear() {
        return this.publicationYear;
    }

    public String getGenre() {
        return this.genre.toString();
    }

    public String toString() {
        return title + " [" + author.toString() + ']';
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Book)) {
            return false;
        }

        ArrayList<Object> allMemberVariables = getAllMemberVariables();
        if (allMemberVariables.equals(((Book) obj).getAllMemberVariables())) {
            return true;
        }

        return false;
    }

    public int hashCode() {
        return (title + author + publicationYear + genre).hashCode();
    }

    public ArrayList<Object> getAllMemberVariables() {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(this.title);
        objects.add(this.author);
        objects.add(Integer.valueOf(this.publicationYear));
        objects.add(this.genre);

        return objects;
    }
}

public class Bookshelf {
    private final int MAX_BOOK_COUNT;
    private ArrayList<Book> books = new ArrayList<>();

    public Bookshelf(int maxBookCount) {
        this.MAX_BOOK_COUNT = maxBookCount;
    }

    public int getMaxBookCount() {
        return this.MAX_BOOK_COUNT;
    }

    public boolean add(Book book) {
        if (books.size() < MAX_BOOK_COUNT) {
            books.add(book);

            return true;
        }

        return false;
    }

    public boolean remove(Book book) {
        return books.remove(book);
    }

    public int getBookCount() {
        return books.size();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public HashSet<Book> getBookSet() {
        return new HashSet<>(books);
    }
}

public class Bundle {
    private String name;
    private HashSet<Book> bookSet = new HashSet<>();
    private final int MAX_BOOK_COUNT = 4;

    public Bundle(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<Book> getBookSet() {
        return this.bookSet;
    }

    public boolean add(Book book) {
        if (bookSet.size() >= MAX_BOOK_COUNT) {
            return false;
        }

        return bookSet.add(book);
    }

    public boolean remove(Book book) {
        return bookSet.remove(book);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Bundle)) {
            return false;
        }

        Bundle other = (Bundle) obj;
        if (this.name.equals(other.name) && this.bookSet.equals(other.bookSet)) {
            return true;
        }

        return false;
    }

    public int hashCode() {
        return (name + bookSet).hashCode();
    }

}

public class ReadingList {
    private String name;
    private ArrayList<Book> books = new ArrayList<>();

    public ReadingList(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(Book book) {
        books.add(book);
    }

    public boolean remove(Book book) {
        return books.remove(book);
    }

    public String toString() {
        String bookNameList = "";
        int bookCount = 1;
        for (Book book : books) {
            bookNameList += String.format("%d. %s [%s]%s", bookCount++, book.getTitle(), book.getAuthor(), System.lineSeparator());
        }

        return bookNameList;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ReadingList)) {
            return false;
        }

        ReadingList other = (ReadingList) obj;

        if (this.name.equals(other.name) && this.books.equals(other.books)) {
            return true;
        }

        return false;
    }

    public int hashCode() {
        return (name + books).hashCode();
    }
}

public enum Genre {
    SCIENCE_FICTION,
    ROMANCE,
    BIOGRAPHY,
    FANTASY,
    MYSTERY,
    SUSPENSE
}