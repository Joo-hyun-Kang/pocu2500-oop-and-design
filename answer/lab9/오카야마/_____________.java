//----------------------Book.java----------------------

public final class Book {
    private final UUID sku;
    private final String title;
    private final int price;
    private final int publishedYear;

    public Book(final UUID sku, final String title, final int price, final int publishedYear) {
        this.sku = sku;
        this.title = title;
        this.price = price;
        this.publishedYear = publishedYear;
    }

    public int getPrice() {
        return this.price;
    }

    public int getPublishedYear() {
        return this.publishedYear;
    }

    public String getTitle() {
        return this.title;
    }

    public UUID getSku() {
        return this.sku;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.hashCode() != o.hashCode()) {
            return false;
        }

        Book other = (Book) o;
        return this.sku.equals(other.sku);
    }

    @Override
    public int hashCode() {
        return this.sku.hashCode();
    }
}


//----------------------BuyOneGetOneFree.java----------------------

public class BuyOneGetOneFree implements IPricingModel {
    private final HashSet<UUID> skus;
    private final HashMap<Book, Integer> bookCountBySku;

    public BuyOneGetOneFree(final HashSet<UUID> skus) {
        this.skus = skus;
        this.bookCountBySku = new HashMap<>();
    }

    public int getTotalPrice(final Collection<Book> books) {
        if (books.isEmpty()) {
            return 0;
        }

        this.bookCountBySku.clear();

        for (Book book : books) {
            this.bookCountBySku.put(book, this.bookCountBySku.getOrDefault(book, 0) + 1);
        }

        int totalPrice = 0;
        for (HashMap.Entry<Book, Integer> bookToBookCount : this.bookCountBySku.entrySet()) {
            Book book = bookToBookCount.getKey();
            int bookCount = bookToBookCount.getValue();
            int price = book.getPrice();

            if (!this.skus.contains(book.getSku())) {
                totalPrice += (bookCount * price);

                continue;
            }

            assert (this.skus.contains(book.getSku())) : "skus should contains this book's sku";

            if (bookCount % 2 == 1) {
                totalPrice += price;
            }

            totalPrice += (bookCount / 2 * price);
        }

        return totalPrice;
    }
}


//----------------------DecadeMadness.java----------------------

public class DecadeMadness implements IPricingModel {
    private final HashMap<Integer, Integer> bookCountByDecade;

    public DecadeMadness() {
        this.bookCountByDecade = new HashMap<Integer, Integer>();
    }

    public int getTotalPrice(final Collection<Book> books) {
        if (books.isEmpty()) {
            return 0;
        }

        bookCountByDecade.clear();

        for (Book book : books) {
            int decade = book.getPublishedYear() / 10;
            this.bookCountByDecade.put(decade, this.bookCountByDecade.getOrDefault(decade, 0) + 1);
        }

        double totalPrice = 0;

        for (Book book : books) {
            int decade = book.getPublishedYear() / 10;
            double price = book.getPrice();
            if (this.bookCountByDecade.get(decade) >= 2) {
                price *= 0.8;
            }

            totalPrice += price;
        }

        return (int) totalPrice;
    }
}


//----------------------SkyIsTheLimit.java----------------------

public class SkyIsTheLimit implements IPricingModel {
    private final int minPriceForSale;

    public SkyIsTheLimit(final int minPriceForSale) {
        this.minPriceForSale = minPriceForSale;
    }

    public int getTotalPrice(final Collection<Book> books) {
        if (books.isEmpty()) {
            return 0;
        }

        int totalPrice = 0;

        if (books.size() < 5) {
            for (Book book : books) {
                totalPrice += book.getPrice();
            }

            return totalPrice;
        }

        assert (books.size() >= 5) : "book count should be greater than or equal to 5";

        int mostExpensive = -1;
        int secondMost = -1;

        for (Book book : books) {
            int price = book.getPrice();
            if (price > mostExpensive) {
                secondMost = mostExpensive;
                mostExpensive = price;
            } else if (price > secondMost) {
                secondMost = price;
            }

            totalPrice += price;
        }

        if (totalPrice >= this.minPriceForSale) {
            totalPrice -= (mostExpensive / 2.0 + secondMost / 2.0);
        }

        return totalPrice;
    }
}


//----------------------Cart.java----------------------

public final class Cart {
    private ArrayList<Book> books;

    public Cart() {
        this.books = new ArrayList<Book>();
    }

    public Book getBookOrNull(final int index) {
        if (this.books.size() <= index) {
            return null;
        }

        return this.books.get(index);
    }

    public int getBookCount() {
        return this.books.size();
    }

    public void addBooks(final ArrayList<Book> books) {
        for (Book book : books) {
            this.books.add(book);
        }
    }

    public void addBook(final Book book) {
        this.books.add(book);
    }

    public boolean remove(final int index) {
        if (this.books.size() <= index) {
            return false;
        }

        this.books.remove(index);

        return true;
    }

    public int getTotalPrice(IPricingModel pricingModel) {
        return pricingModel.getTotalPrice(this.books);
    }
}