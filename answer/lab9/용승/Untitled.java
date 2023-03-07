// Cart.java
package academy.pocu.comp2500.lab9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class Cart {
    private final ArrayList<Book> books = new ArrayList<>();

    public Book getBookOrNull(final int index) {
        if (this.books.size() <= index) {
            return null;
        }

        return this.books.get(index);
    }

    public int getBookCount() {
        return this.books.size();
    }

    public void addBooks(Book[] books) {
        assert (Arrays.stream(books).filter(Objects::nonNull).count() == books.length);

        for (Book book : books) {
            this.books.add(book);
        }
    }

    public void addBook(Book book) {
        assert (book != null);

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

// Book.java
package academy.pocu.comp2500.lab9;

import java.util.UUID;

public final class Book implements Comparable<Book> {
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
    public int compareTo(Book other) {
        return other.getPrice() - this.getPrice();
    }
}

// IPricingModel.java
package academy.pocu.comp2500.lab9;

import java.util.Collection;

public interface IPricingModel {
    int getTotalPrice(Collection<Book> books);
}

// SimplePricing.java
package academy.pocu.comp2500.lab9;

import java.util.Collection;

public class SimplePricing implements IPricingModel {
    @Override
    public int getTotalPrice(Collection<Book> books) {
        assert (books != null);

        return books.stream().mapToInt(Book::getPrice).sum();
    }
}

//BuyOneGetOneFree.java
package academy.pocu.comp2500.lab9;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class BuyOneGetOneFree implements IPricingModel {
    private final HashSet<UUID> skus;

    public BuyOneGetOneFree(final HashSet<UUID> skus) {
        this.skus = skus;
    }

    @Override
    public int getTotalPrice(Collection<Book> books) {
        assert (books != null);

        HashSet<UUID> examinedPromotionBookSkus = new HashSet<>();
        long promotionBookCount;
        int totalPrice = 0;

        for (Book book : books) {
            UUID sku = book.getSku();

            if (!this.skus.contains(sku)) {
                totalPrice += book.getPrice();
                continue;
            }

            if (!examinedPromotionBookSkus.contains(sku)) {
                promotionBookCount = books.stream().filter(b -> b.getSku().equals(sku)).count();
                totalPrice += (promotionBookCount / 2 + promotionBookCount % 2) * (book.getPrice());
                examinedPromotionBookSkus.add(sku);
            }
        }

        return totalPrice;
    }
}

// DecadeMadness.java
package academy.pocu.comp2500.lab9;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DecadeMadness implements IPricingModel {
    private static final double MULTIPLIER_FOR_DISCOUNT = 0.8;

    @Override
    public int getTotalPrice(Collection<Book> books) {
        assert (books != null);

        HashMap<Integer, ArrayList<Book>> mapByDecade = new HashMap<>();
        double totalPrice = 0;

        for (Book book : books) {
            int decade = book.getPublishedYear() / 10;
            mapByDecade.computeIfAbsent(decade, key -> new ArrayList<>());
            mapByDecade.get(decade).add(book);
        }

        for (Integer eachDecade : mapByDecade.keySet()) {
            ArrayList<Book> booksInSameDecade = mapByDecade.get(eachDecade);
            double priceSum = booksInSameDecade.stream().mapToInt(Book::getPrice).sum();

            if (booksInSameDecade.size() == 1) {
                totalPrice += priceSum;
                continue;
            }
            totalPrice += (priceSum * MULTIPLIER_FOR_DISCOUNT);
        }

        return (int) totalPrice;
    }
}

// SkyIsTheLimit.java
package academy.pocu.comp2500.lab9;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SkyIsTheLimit implements IPricingModel {
    private final int minimumTotalPriceForDiscount;
    private final SimplePricing simplePricing;

    public SkyIsTheLimit(int minimumTotalPriceForDiscount) {
        this.minimumTotalPriceForDiscount = minimumTotalPriceForDiscount;
        this.simplePricing = new SimplePricing();
    }

    @Override
    public int getTotalPrice(Collection<Book> books) {
        int originalTotalPrice = this.simplePricing.getTotalPrice(books);

        if (books.size() < 5 || originalTotalPrice < this.minimumTotalPriceForDiscount) {
            return originalTotalPrice;
        }

        List<Book> booksInList = new ArrayList<>(books);
        Collections.sort(booksInList);

        double mostTwoExpensiveBooksPriceSum = booksInList.get(0).getPrice() + booksInList.get(1).getPrice();
        double discountedTotalPrice = originalTotalPrice - (mostTwoExpensiveBooksPriceSum * 0.5);

        return (int) discountedTotalPrice;
    }
}
