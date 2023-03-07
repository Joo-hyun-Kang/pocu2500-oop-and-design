// Cart
public class Cart {

    private ArrayList<Product> products;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(this.products);
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public boolean removeProduct(Product product) {
        return this.products.removeIf(p -> p.equals(product));
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (Product product : this.products) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }
}

//Product
public class Product {
    private ProductColor productColor;

    private ProductSize productSize;

    private String displayName;

    private ShippingMethod shippingMethod;

    private int price;

    protected Product(ProductSize size, ProductColor color, String displayName, int price) {
        this.productColor = color;
        this.productSize = size;
        this.displayName = displayName;
        this.shippingMethod = ShippingMethod.PICK_UP;
        this.price = price;
    }

    public ProductColor getProductColor() {
        return this.productColor;
    }

    public ProductSize getProductSize() {
        return this.productSize;
    }

    public int getPrice() {
        return this.price;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public ShippingMethod getShippingMethod() {
        return this.shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    protected void addPrice(int price) {
        this.price += price;
    }
}

// ProductColor
public class ProductColor {
    private short red;
    private short green;
    private short blue;

    public ProductColor(short red, short green, short blue) {
        this.red = wrapValue(red);
        this.green = wrapValue(green);
        this.blue = wrapValue(blue);
    }

    public short getRed() {
        return this.red;
    }

    public short getGreen() {
        return this.green;
    }

    public short getBlue() {
        return this.blue;
    }

    private short wrapValue(short color) {
        if (color < 0) {
            return (short) 0;
        } else if (color > 255) {
            return (short) 255;
        }

        return color;
    }
}

// ProductSize
public class ProductSize {
    // unit is millimeter(mm)
    private int width;

    private int height;

    public ProductSize(int width, int height) {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}

// ShippingMethod
public enum ShippingMethod {
    PICK_UP,
    SHIP
}

//Stamp
public class Stamp extends Product {
    private String text;

    public Stamp(StampColor stampColor, StampSize stampSize, String text) {
        super(stampSize.getProductSize(), stampColor.getProductColor(),
                String.format("%s (%d mm x %d mm)", "Stamp", stampSize.getProductSize().getWidth(), stampSize.getProductSize().getHeight()),
                stampSize.getPrice());
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}

// StampSize
public enum StampSize {
    STAMP_OPTION1(new ProductSize(40, 30), 2300),
    STAMP_OPTION2(new ProductSize(50, 20), 2300),
    STAMP_OPTION3(new ProductSize(70, 40), 2600);

    private ProductSize productSize;

    private int price;

    private StampSize(ProductSize productSize, int price) {
        this.productSize = productSize;
        this.price = price;
    }

    public ProductSize getProductSize() {
        return this.productSize;
    }

    public int getPrice() {
        return this.price;
    }
}

// StampColor
public enum StampColor {
    RED(new ProductColor((short) 255, (short) 0, (short) 0)),
    BLUE(new ProductColor((short) 0, (short) 0, (short) 255)),
    GREEN(new ProductColor((short) 0, (short) 128, (short) 0));

    private ProductColor productColor;

    private StampColor(ProductColor productColor) {
        this.productColor = productColor;
    }

    public ProductColor getProductColor() {
        return this.productColor;
    }
}

// Calendar
public class Calendar extends Product {

    private CalendarType calendarType;

    public Calendar(CalendarType calendarType) {
        super(calendarType.getCalendarSize(), calendarType.getCalendarColor(), calendarType.getCalendarName(), calendarType.getPrice());

        this.calendarType = calendarType;
    }

    public CalendarType getCalendarType() {
        return this.calendarType;
    }
}

// CalendarType
public enum CalendarType {
    WALL("Wall Calendar", new ProductSize(400, 400), new ProductColor((short) 255, (short) 255, (short) 255), 1000),
    DESK("Desk Calendar", new ProductSize(200, 150), new ProductColor((short) 255, (short) 255, (short) 255), 1000),
    MAGNET("Magnet Calendar", new ProductSize(100, 200), new ProductColor((short) 255, (short) 255, (short) 255), 1500);

    private String calendarName;

    private ProductSize calendarSize;

    private ProductColor calendarColor;

    private int price;

    private CalendarType(String calendarName, ProductSize calendarSize, ProductColor calendarColor, int price) {
        this.calendarName = calendarName;
        this.calendarSize = calendarSize;
        this.calendarColor = calendarColor;
        this.price = price;
    }

    public String getCalendarName() {
        return this.calendarName;
    }

    public ProductSize getCalendarSize() {
        return this.calendarSize;
    }

    public ProductColor getCalendarColor() {
        return this.calendarColor;
    }

    public int getPrice() {
        return this.price;
    }
}

// ApertureProduct
public class ApertureProduct extends Product {
    private static final int APERTURE_ADDITIONAL_COST = 5;

    private ArrayList<Aperture> apertures;

    private PrintOrientation orientation;

    protected ApertureProduct(ProductSize size, ProductColor color, String displayName, int price, PrintOrientation orientation) {
        super(size, color, displayName, price);

        this.orientation = orientation;
        this.apertures = new ArrayList<>();
    }

    public PrintOrientation getOrientation() {
        return this.orientation;
    }

    public ArrayList<Aperture> getApertures() {
        return this.apertures;
    }

    public boolean addAperture(Aperture aperture) {
        int x = aperture.getX();
        int y = aperture.getY();
        int width = aperture.getWidth();
        int height = aperture.getHeight();
        if (x < 0 || y < 0 || x + width > super.getProductSize().getWidth() || y + height > super.getProductSize().getHeight()) {
            return false;
        }
        this.apertures.add(aperture);
        super.addPrice(APERTURE_ADDITIONAL_COST);
        return true;
    }
}

// PrintOrientation
public enum PrintOrientation {
    PORTRAIT,
    LANDSCAPE
}

// Banner
public class Banner extends ApertureProduct {
    private BannerType bannerType;

    public Banner(ProductColor bannerColor, BannerSize bannerSize, BannerType bannerType, PrintOrientation orientation) {
        super(bannerSize.getProductSize(), bannerColor, String.format("%s (%d mm x %d mm)", bannerType.getDisplayName(),
                        bannerSize.getProductSize().getWidth(), bannerSize.getProductSize().getHeight()),
                bannerSize.getPrice() + bannerType.getPrice(), orientation);

        this.bannerType = bannerType;
    }

    public BannerType getBannerType() {
        return this.bannerType;
    }
}

// BannerSize
public enum BannerSize {
    BANNER_OPTION1(new ProductSize(1000, 500), 5000),
    BANNER_OPTION2(new ProductSize(1000, 1000), 5200),
    BANNER_OPTION3(new ProductSize(2000, 500), 5300),
    BANNER_OPTION4(new ProductSize(3000, 1000), 6000);

    private ProductSize productSize;

    private int price;

    private BannerSize(ProductSize productSize, int price) {
        this.productSize = productSize;
        this.price = price;
    }

    public ProductSize getProductSize() {
        return this.productSize;
    }

    public int getPrice() {
        return this.price;
    }
}

// BannerType
public enum BannerType {
    GLOSS("Gloss Banner", 0),
    SCRIM("Scrim Banner", 100),
    MESH("Mesh Banner", 100);

    private String displayName;

    private int price;

    private BannerType(String displayName, int price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getPrice() {
        return this.price;
    }
}

// BusinessCard
public class BusinessCard extends ApertureProduct {
    private BusinessCardType businessCardType;

    private PaperColor paperColor;

    private BusinessCardSides businessCardSides;

    public BusinessCard(BusinessCardType businessCardType, PaperColor paperColor, PrintOrientation orientation, BusinessCardSides businessCardSides) {
        super(businessCardType.getBusinessCardSize(), paperColor.getProductColor(), businessCardType.displayName, businessCardType.getPrice() + businessCardSides.getPrice(), orientation);

        this.businessCardType = businessCardType;
        this.paperColor = paperColor;
        this.businessCardSides = businessCardSides;
    }

    public BusinessCardType getBusinessCardType() {
        return this.businessCardType;
    }

    public PaperColor getPaperColor() {
        return this.paperColor;
    }

    public BusinessCardSides getBusinessCardSides() {
        return this.businessCardSides;
    }
}

// PaperColor
public enum PaperColor {
    GRAY(new ProductColor((short) 230, (short) 230, (short) 230)),
    IVORY(new ProductColor((short) 255, (short) 255, (short) 240)),
    WHITE(new ProductColor((short) 255, (short) 255, (short) 255));

    private ProductColor productColor;

    private PaperColor(ProductColor productColor) {
        this.productColor = productColor;
    }

    public ProductColor getProductColor() {
        return this.productColor;
    }
}

// BusinessCardSides
public enum BusinessCardSides {
    SINGLE_SIDED(0),
    DOUBLE_SIDED(30);

    private int price;

    private BusinessCardSides(int price) {
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }
}

// BusinessCardType
public enum BusinessCardType {
    LINEN(new ProductSize(90, 50), "Linen Business Card", 110),
    LAID(new ProductSize(90, 50), "Laid Business Card", 120),
    SMOOTH(new ProductSize(90, 50), "Smooth Business Card", 100);

    private ProductSize businessCardSize;

    String displayName;

    int price;

    private BusinessCardType(ProductSize businessCardSize, String displayName, int price) {
        this.businessCardSize = businessCardSize;
        this.displayName = displayName;
        this.price = price;
    }

    public ProductSize getBusinessCardSize() {
        return this.businessCardSize;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getPrice() {
        return this.price;
    }
}

// Aperture
public class Aperture {
    private int x;

    private int y;

    private int width;

    private int height;

    protected Aperture(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}

// TextAperture
public class TextAperture extends Aperture {
    private String text;

    public TextAperture(int x, int y, int width, int height, String text) {
        super(x, y, width, height);

        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}

// ImageAperture
public class ImageAperture extends Aperture {
    private String imagePath;

    public ImageAperture(int x, int y, int width, int height, String imagePath) {
        super(x, y, width, height);

        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return this.imagePath;
    }
}