public class Product {
    protected String productId;
    protected Color color;
    protected Size size;
    protected Price price;
    protected Cart.ShippingMethodType shippingMethod;
    protected String displayName;
​
    protected Product(String productId, Color color, Size size, Price price) {
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.price = price;
        this.displayName = "Product";
    }
​
    public String getProductId() {
        return productId;
    }
​
    public Color getColor() {
        return color;
    }
​
    public Size getSize() {
        return size;
    }
​
    public int getPrice() {
        return price.getPrice();
    }
​
    public Cart.ShippingMethodType getShippingMethod() {
        return shippingMethod;
    }
​
    public void setShippingMethod(Cart.ShippingMethodType shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
​
    public String getDisplayName() {
        return displayName;
    }
}
​
public class Price {
    private int price; // Monetary unit: KRW[₩]
​
    public Price() {
        this(0);
    }
​
    public Price(int price) {
        if (price < 0) {
            System.out.println("Warning: Price cannot be negative value");
            assert (false);
        }
​
        this.price = price;
    }
​
    public int getPrice() {
        return price;
    }
    
    public void addPrice(int price) {
        this.price += price;        
    }
​
    public void setPrice(int price) {
        if (price < 0) {
            System.out.println("Warning: Price cannot be negative value");
            assert (false);
        }
​
        this.price = price;
    }
    
}
​
public class Color {
    private int r;
    private int g;
    private int b;
​
    public Color(int r, int g, int b) {
        if (r < 0 || r > 255) {
            System.out.println("R value out of range");
            assert (false);
        }
        if (g < 0 || g > 255) {
            System.out.println("G value out of range");
            assert (false);
        }
        if (b < 0 || b > 255) {
            System.out.println("B value out of range");
            assert (false);
        }
​
        this.r = r;
        this.g = g;
        this.b = b;
    }
​
​
    public int getR() {
        return r;
    }
​
    public int getG() {
        return g;
    }
​
    public int getB() {
        return b;
    }
​
}
​
public class Size {
    private int width;
    private int height;
​
    public Size(int width, int height) {
        if (width <= 0) {
            System.out.println("width must be bigger than 0(Zero)");
            assert (false);
        }
        if (height <= 0) {
            System.out.println("height must be bigger than 0(Zero)");
            assert (false);
        }
​
        this.width = width;
        this.height = height;
    }
​
    public Size() {
​
    }
​
    public int getWidth() {
        return width;
    }
​
    public int getHeight() {
        return height;
    }
}
​
public class Stamp extends Product {
    private StampColorType stampColor;
    private StampSizeCentiMeter stampSize;
​
    private static final int PRICE_FOR_FOUR_X_THREE_ = 2300;
    private static final int PRICE_FOR_FIVE_X_TWO_ = 2300;
    private static final int PRICE_FOR_SEVEN_X_FOUR = 2600;
​
​
    protected static final int TWENTY = 20;
    protected static final int THIRTY = 30;
    protected static final int FORTY = 40;
​
    protected static final int FIFTY = 50;
    protected static final int SEVENTY = 70;
    protected String text;
​
    public enum StampColorType {
        RED,
        GREEN,
        BLUE
    }
​
    public enum StampSizeCentiMeter {
        // WIDTH_X_HEIGHT [cm] * [cm]    Note. only use [mm] in all code but in enum
        FOUR_X_THREE,
        FIVE_X_TWO,
        SEVEN_X_FOUR
    }
​
    public Stamp(String productId, StampColorType stampColor, StampSizeCentiMeter stampSize, String text) {
        super(productId, null, null, new Price());
​
        this.productId = productId;
​
        this.stampColor = stampColor;
        switch (stampColor) {
            case RED:
                color = new Color(0xFF, 0, 0);
                break;
            case GREEN:
                color = new Color(0, 0x80, 0);
                break;
            case BLUE:
                color = new Color(0, 0, 0xFF);
                break;
            default:
                System.out.println("Unknown StampColorType detected");
                assert (false);
                break;
        }
​
        this.stampSize = stampSize;
        switch (stampSize) {
            case FOUR_X_THREE:
                this.size = new Size(FORTY, THIRTY);
                price.setPrice(PRICE_FOR_FOUR_X_THREE_);
                break;
            case FIVE_X_TWO:
                this.size = new Size(FIFTY, TWENTY);
                price.setPrice(PRICE_FOR_FIVE_X_TWO_);
                break;
            case SEVEN_X_FOUR:
                this.size = new Size(SEVENTY, FORTY);
                price.setPrice(PRICE_FOR_SEVEN_X_FOUR);
                break;
            default:
                System.out.println("Unknown StampSize detected");
                break;
        }
​
        this.text = text;
​
        this.displayName = String.format("Stamp (%d mm x %d mm)", size.getWidth(), size.getHeight());
    }
​
    public String getText() {
        return text;
    }
​
    public StampColorType getStampColor() {
        return stampColor;
    }
​
    public StampSizeCentiMeter getStampSize() {
        return stampSize;
    }
​
}
​
public class Calendar extends Product {
    protected CalendarType calendarType;
    private CalendarColorType calendarColor = CalendarColorType.WHITE;
    private CalendarSizeCentiMeter calendarSize;
    protected static final int DEFAULT_WALL_CALENDAR_PRICE = 1000;
    protected static final int DEFAULT_DESK_CALENDAR_PRICE = 1000;
    protected static final int DEFAULT_MAGNET_CALENDAR_PRICE = 1500;
​
    private static final int ONE_HUNDRED = 100;
    private static final int ONE_HUNDRED_AND_FIFTY = 150;
    private static final int TWO_HUNDRED = 200;
    private static final int FOUR_HUNDRED = 400;
​
    public enum CalendarSizeCentiMeter {
        FORTY_X_FORTY,
        TWENTY_X_FIFTEEN,
        TEN_X_TWENTY
    }
​
    public enum CalendarColorType {
        WHITE
    }
​
    public enum CalendarType {
        WALL,
        DESK,
        MAGNET
    }
​
​
    public Calendar(String productId, CalendarType calendarType) {
        super(productId, null, null, new Price());
​
        this.calendarType = calendarType;
        switch (calendarType) {
            case WALL:
                this.calendarSize = CalendarSizeCentiMeter.FORTY_X_FORTY;
                price.setPrice(DEFAULT_WALL_CALENDAR_PRICE);
                break;
            case DESK:
                this.calendarSize = CalendarSizeCentiMeter.TWENTY_X_FIFTEEN;
                price.setPrice(DEFAULT_DESK_CALENDAR_PRICE);
                break;
            case MAGNET:
                this.calendarSize = CalendarSizeCentiMeter.TEN_X_TWENTY;
                price.setPrice(DEFAULT_MAGNET_CALENDAR_PRICE);
                break;
            default:
                System.out.println("Unknown CalendarType detected");
                break;
        }
​
        switch (calendarColor) {
            case WHITE:
                color = new Color(0xFF, 0xFF, 0xFF);
                break;
            default:
                System.out.println("Unknown CalendarColor detected");
                break;
        }
​
        switch (calendarSize) {
            case TEN_X_TWENTY:
                this.size = new Size(ONE_HUNDRED, TWO_HUNDRED);
                break;
            case TWENTY_X_FIFTEEN:
                this.size = new Size(TWO_HUNDRED, ONE_HUNDRED_AND_FIFTY);
                break;
            case FORTY_X_FORTY:
                this.size = new Size(FOUR_HUNDRED, FOUR_HUNDRED);
                break;
            default:
                System.out.println("Unknown CalendarSize detected");
                break;
        }
​
        this.displayName = calendarType + " " + "Calendar";
    }
​
    public CalendarType getCalendarType() {
        return calendarType;
    }
​
    public CalendarColorType getCalendarColor() {
        return calendarColor;
    }

    public CalendarSizeCentiMeter getCalendarSize() {
        return calendarSize;
    }

}