// Product
package academy.pocu.comp2500.assignment2;
import java.util.UUID;
public class Product {
    private final UUID id;
    private final String displayName;
    private int price;
    private final Color color;
    private final int height;
    private final int width;
    private ShippingMethod shippingMethod;
    protected Product(UUID id, String displayName, int price, Color color, int height, int width, ShippingMethod shippingMethod) {
        this.id = id;
        this.displayName = displayName;
        this.price = price;
        this.color = color;
        this.height = height;
        this.width = width;
        this.shippingMethod = shippingMethod;
    }
    protected void addPrice(int price) {
        this.price += price;
    }
    public UUID getId() {
        return this.id;
    }
    public String getDisplayName() {
        return this.displayName;
    }
    public int getPrice() {
        return this.price;
    }
    public Color getColor() {
        return this.color;
    }
    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }
    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }
    public void changeShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
}
// Stamp
package academy.pocu.comp2500.assignment2;
import java.util.UUID;
public class Stamp extends Product {
    private static final String DISPLAY_NAME = "Stamp";
    private final StampColor stampColor;
    private final String text;
    public Stamp(UUID productId, StampColor stampColor, StampSize stampSize, String text, ShippingMethod shippingMethod) {
        super(productId,
                DISPLAY_NAME, calculatePrice(stampSize),
                stampColor.getColor(),
                stampSize.getHeightInMillimeters(), stampSize.getWidthInMillimeters(),
                shippingMethod);
        this.stampColor = stampColor;
        this.text = text;
    }
    public StampColor getStampColor() {
        return stampColor;
    }
    public String getText() {
        return this.text;
    }
    private static int calculatePrice(StampSize stampSize) {
        if (stampSize == StampSize.SEVEN_X_FOUR_CENTIMETERS) {
            return 2600;
        } else {
            return 2300;
        }
    }
}
// StampColor (Enum)
package academy.pocu.comp2500.assignment2;
public enum StampColor {
    RED(new Color((short) 0xFF, (short) 0x00, (short) 0x00)),
    GREEN(new Color((short) 0x00, (short) 0xFF, (short) 0x00)),
    BLUE(new Color((short) 0x00, (short) 0x00, (short) 0xFF));
    private final Color color;
    StampColor(Color inkColor) {
        this.color = inkColor;
    }
    public Color getColor() {
        return this.color;
    }
}
// StampSize (Enum)
package academy.pocu.comp2500.assignment2;
public enum StampSize {
    FOUR_X_THREE_CENTIMETERS(40, 30),
    FIVE_X_TWO_CENTIMETERS(50, 20),
    SEVEN_X_FOUR_CENTIMETERS(70, 40);
    private final int heightInMillimeters;
    private final int widthInMillimeters;
    StampSize(int heightInMillimeters, int widthInMillimeters) {
        this.heightInMillimeters = heightInMillimeters;
        this.widthInMillimeters = widthInMillimeters;
    }
    public int getHeightInMillimeters() {
        return this.heightInMillimeters;
    }
    public int getWidthInMillimeters() {
        return this.widthInMillimeters;
    }
}
// Calendar 
package academy.pocu.comp2500.assignment2;
import java.util.UUID;
public class Calendar extends Product {
    private static final Color DEFAULT_CALENDAR_COLOR = new Color((short) 0xFF, (short) 0xFF, (short) 0xFF);
    private final CalendarType calendarType;
    public Calendar(UUID productId, CalendarType calendarType, ShippingMethod shippingMethod) {
        super(productId,
                calendarType.getName(),
                calendarType.getPrice(),
                DEFAULT_CALENDAR_COLOR,
                calendarType.getHeightInMillimeters(), calendarType.getWidthInMillimeters(),
                shippingMethod);
        this.calendarType = calendarType;
    }
    public CalendarType getCalendarType() {
        return this.calendarType;
    }
}
// Calendar Type (Enum)
package academy.pocu.comp2500.assignment2;
public enum CalendarType {
    WALL("Wall Calendar", 400, 400, 1000),
    DESK("Desk Calendar", 200, 150, 1000),
    MAGNET("Magnet Calendar", 100, 200, 1500);
    private final String name;
    private final int height;
    private final int width;
    private final int price;
    CalendarType(String name, int height, int width, int price) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.price = price;
    }
    public String getName() {
        return this.name;
    }
    public int getHeightInMillimeters() {
        return height;
    }
    public int getWidthInMillimeters() {
        return width;
    }
    public int getPrice() {
        return price;
    }
}
// Banner
package academy.pocu.comp2500.assignment2;
import java.util.UUID;
public class Banner extends CustomizableProduct {
    private final BannerType bannerType;
    public Banner(UUID productId, Color color, BannerSize bannerSize, Orientation bannerOrientation, BannerType bannerType, ShippingMethod shippingMethod) {
        super(productId,
                bannerType.getName(),
                calculateBasePrice(bannerType, bannerSize),
                color,
                bannerSize.getHeightInMillimeters(), bannerSize.getWidthInMillimeters(),
                bannerOrientation, shippingMethod);
        this.bannerType = bannerType;
    }
    public BannerType getBannerType() {
        return this.bannerType;
    }
    public Orientation getOrientation() {
        return super.orientation;
    }
    private static int calculateBasePrice(BannerType bannerType, BannerSize bannerSize) {
        int price = 5000;
        if (bannerSize == BannerSize.ONE_X_ONE_METERS) {
            price += 200;
        } else if (bannerSize == BannerSize.TWO_X_HALF_METERS) {
            price += 300;
        } else if (bannerSize == BannerSize.THREE_X_ONE_METERS) {
            price += 1000;
        }
        if (bannerType != BannerType.GLOSS) {
            price += 100;
        }
        return price;
    }
}
// BannerSize (Enum)
package academy.pocu.comp2500.assignment2;
public enum BannerSize {
    ONE_X_HALF_METERS(1000, 500),
    ONE_X_ONE_METERS(1000, 1000),
    TWO_X_HALF_METERS(2000, 500),
    THREE_X_ONE_METERS(3000, 1000);
    private final int heightInMillimeters;
    private final int widthInMillimeters;
    BannerSize(int heightInMillimeters, int widthInMillimeters) {
        this.heightInMillimeters = heightInMillimeters;
        this.widthInMillimeters = widthInMillimeters;
    }
    public int getHeightInMillimeters() {
        return heightInMillimeters;
    }
    public int getWidthInMillimeters() {
        return widthInMillimeters;
    }
}
// BannerType (Enum)
package academy.pocu.comp2500.assignment2;
public enum BannerType {
    GLOSS("Gloss Banner"),
    SCRIM("Scrim Banner"),
    MESH("Mesh Banner");
    private final String name;
    BannerType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
// BusinessCard
package academy.pocu.comp2500.assignment2;
import java.util.UUID;
public class BusinessCard extends CustomizableProduct {
    private final static int DEFAULT_BUSINESS_CARD_HEIGHT = 90;
    private final static int DEFAULT_BUSINESS_CARD_WIDTH = 50;
    private final BusinessCardSides businessCardSides;
    private final PaperType paperType;
    public BusinessCard(UUID productId, BusinessCardColor businessCardColor, Orientation businessCardOrientation, PaperType paperType, BusinessCardSides businessCardSides, ShippingMethod shippingMethod) {
        super(productId,
                paperType.getName(),
                calculatePrice(paperType, businessCardSides),
                businessCardColor.getColor(),
                DEFAULT_BUSINESS_CARD_HEIGHT, DEFAULT_BUSINESS_CARD_WIDTH,
                businessCardOrientation, shippingMethod);
        this.businessCardSides = businessCardSides;
        this.paperType = paperType;
    }
    public BusinessCardSides getBusinessCardSides() {
        return this.businessCardSides;
    }
    public PaperType getPaperType() {
        return paperType;
    }
    public Orientation getOrientation() {
        return super.orientation;
    }
    private static int calculatePrice(PaperType paperType, BusinessCardSides businessCardSides) {
        int price = 100;
        if (paperType == PaperType.LINEN) {
            price += 10;
        } else if (paperType == PaperType.LAID) {
            price += 20;
        }
        if (businessCardSides == BusinessCardSides.DOUBLE_SIDED) {
            price += 30;
        }
        return price;
    }
}
// BusinessCardColor (Enum)
package academy.pocu.comp2500.assignment2;
public enum BusinessCardColor {
    GRAY(new Color((short) 0xE6, (short) 0xE6, (short) 0xE6)),
    IVORY(new Color((short) 0xFF, (short) 0xFF, (short) 0xF0)),
    WHITE(new Color((short) 0xFF, (short) 0xFF, (short) 0xFF));
    private final Color color;
    BusinessCardColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return this.color;
    }
}
// BusinessCardSides (Enum)
package academy.pocu.comp2500.assignment2;
public enum BusinessCardSides {
    SINGLE_SIDED,
    DOUBLE_SIDED
}
// Orientation (Enum)
package academy.pocu.comp2500.assignment2;
public enum Orientation {
    PORTRAIT,
    LANDSCAPE
}
// PaperType (Enum)
package academy.pocu.comp2500.assignment2;
public enum PaperType {
    LINEN("Linen Business Card"),
    LAID("Laid Business Card"),
    SMOOTH("Smooth Business Card");
    private final String name;
    PaperType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
// ShippingMethod (Enum)
package academy.pocu.comp2500.assignment2;
public enum ShippingMethod {
    PICKUP("Pickup"),
    SHIP("Ship");
    private final String value;
    ShippingMethod(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
// Color
package academy.pocu.comp2500.assignment2;
public class Color {
    private final short r;
    private final short g;
    private final short b;
    public Color(short r, short g, short b) {
        assert (r >= 0x00 && r <= 0xFF);
        assert (g >= 0x00 && g <= 0xFF);
        assert (b >= 0x00 && b <= 0xFF);
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public short getR() {
        return this.r;
    }
    public short getG() {
        return this.g;
    }
    public short getB() {
        return this.b;
    }
}
// Aperture
package academy.pocu.comp2500.assignment2;
public class Aperture {
    private int x;
    private int y;
    private final int height;
    private final int width;
    protected Aperture(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }
    protected void setX(int x) {
        this.x = x;
    }
    protected void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
}
// ImageAperture
package academy.pocu.comp2500.assignment2;
public class ImageAperture extends Aperture {
    private final String imagePath;
    public ImageAperture(int x, int y, int height, int width, String imagePath) {
        super(x, y, height, width);
        this.imagePath = imagePath;
    }
    public String getImagePath() {
        return imagePath;
    }
}
// TextAperture
package academy.pocu.comp2500.assignment2;
public class TextAperture extends Aperture {
    private final String text;
    public TextAperture(int x, int y, int height, int width, String text) {
        super(x, y, height, width);
        this.text = text;
    }
    public String getText() {
        return text;
    }
}
// CustomizableProduct
package academy.pocu.comp2500.assignment2;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class CustomizableProduct extends Product {
    protected static final int EXTRA_COST_PER_APERTURE = 5;
    protected final Orientation orientation;
    private final List<Aperture> apertures;
    protected CustomizableProduct(UUID id, String name, int price, Color color, int height, int width, Orientation orientation, ShippingMethod shippingMethod) {
        super(id, name, price, color, height, width, shippingMethod);
        this.orientation = orientation;
        this.apertures = new ArrayList<>();
    }
    public boolean addAperture(Aperture aperture) {
        if (isValidAperture(aperture)) {
            this.apertures.add(aperture);
            addAperturePrice();
            return true;
        }
        return false;
    }
    public List<Aperture> getApertures() {
        return apertures;
    }
    private boolean isValidAperture(Aperture aperture) {
        return aperture.getX() + aperture.getWidth() >= 0
                && aperture.getX() <= this.getWidth()
                && aperture.getY() + aperture.getHeight() >= 0
                && aperture.getY() <= this.getHeight();
    }
    private void addAperturePrice() {
        super.addPrice(EXTRA_COST_PER_APERTURE);
    }
}
// Cart
package academy.pocu.comp2500.assignment2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
public class Cart {
    private final UUID id;
    private int totalPrice;
    private final HashSet<Product> products;
    public Cart(UUID id) {
        this.id = id;
        this.totalPrice = 0;
        this.products = new HashSet<>();
    }
    public void addProduct(Product product) {
        this.products.add(product);
        this.totalPrice += product.getPrice();
    }
    public void removeProduct(Product product) {
        this.products.remove(product);
        this.totalPrice -= product.getPrice();
    }
    public UUID getId() {
        return id;
    }
    public List<Product> getProducts() {
        return new ArrayList<>(this.products);
    }
    public int getTotalPrice() {
        return this.totalPrice;
    }
}
