--------------------------------- Product ---------------------------------
public class Product {
    protected final UUID productId;
    protected final Color color;
    protected final Size size;
    protected int price;
    protected ShippingMethod shippingMethod;

    protected Product(UUID productId, Color color, Size size, int price) {
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.price = price;
        this.shippingMethod = ShippingMethod.SHIP;
    }

    public UUID getProductId() {
        return this.productId;
    }

    public Color getColor() {
        return this.color;
    }

    public Size getSize() {
        return this.size;
    }

    public int getPrice() {
        return this.price;
    }

    public ShippingMethod getShippingMethod() {
        return this.shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }
}

--------------------------------- Stamp ---------------------------------

public class Stamp extends Product {
    public enum StampColor {
        RED,
        GREEN,
        BLUE
    }

    public enum StampSize {
        OPTION_1,
        OPTION_2,
        OPTION_3
    }

    private final TextAperture textAperture;
    private final StampColor stampColor;

    public Stamp(UUID productId, StampColor stampColor, StampSize stampSize, String text) {
        super(productId, getStampColorOrNull(stampColor), getStampSizeOrNull(stampSize), calculateStampPrice(stampSize));

        this.stampColor = stampColor;
        this.textAperture = new TextAperture(0, 0, new Size(this.size.getWidth(), this.size.getHeight()), text);
    }

    public StampColor getStampColor() {
        return this.stampColor;
    }

    public String getText() {
        return this.textAperture.getText();
    }

    public String getDisplayName() {
        return String.format("Stamp (%d mm x %d mm)", super.size.getWidth(), super.size.getHeight());
    }

    private static Size getStampSizeOrNull(StampSize stampSize) {
        switch (stampSize) {
            case OPTION_1:
                return new Size(40, 30);
            case OPTION_2:
                return new Size(50, 20);
            case OPTION_3:
                return new Size(70, 40);
            default:
                assert (false) : "unrecognized stamp size" + stampSize;
                return null;
        }
    }

    private static Color getStampColorOrNull(StampColor stampColor) {
        switch (stampColor) {
            case RED:
                return new Color((short) 0xFF, (short) 0, (short) 0);
            case GREEN:
                return new Color((short) 0, (short) 0x80, (short) 0);
            case BLUE:
                return new Color((short) 0, (short) 0, (short) 0xFF);
            default:
                assert (false) : "unrecognized stamp color" + stampColor;
                return null;
        }
    }

    private static int calculateStampPrice(StampSize stampSize) {
        switch (stampSize) {
            case OPTION_1:
                // intentional fallthrough
            case OPTION_2:
                return 2300;
            case OPTION_3:
                return 2600;
            default:
                assert (false) : "unrecognized stamp size" + stampSize;
                return -1;
        }
    }
}

--------------------------------- Calendar ---------------------------------

public class Calendar extends Product {
    public enum CalendarColor {
        WHITE
    }

    public enum CalendarType {
        WALL,
        MAGNET,
        DESK
    }

    public enum CalendarSize {
        OPTION_1,
        OPTION_2,
        OPTION_3
    }

    private final CalendarType calendarType;
    private final CalendarColor calendarColor;

    public Calendar(UUID productId, CalendarType calendarType) {
        super(productId, getCalendarColorObject(), getCalendarSizeOrNull(calendarType), calculateCalendarPrice(calendarType));

        this.calendarColor = CalendarColor.WHITE;
        this.calendarType = calendarType;
    }

    public String getDisplayNameOrNull() {
        switch (this.calendarType) {
            case WALL:
                return String.format("Wall Calendar");
            case MAGNET:
                return String.format("Magnet Calendar");
            case DESK:
                return String.format("Desk Calendar");
            default:
                assert (false) : "unrecognized calendar type" + this.calendarType;
                return null;
        }
    }

    public CalendarColor getCalendarColor() {
        return this.calendarColor;
    }

    public CalendarType getCalendarType() {
        return this.calendarType;
    }

    private static Color getCalendarColorObject() {
        return new Color((short) 0xFF, (short) 0xFF, (short) 0xFF);
    }

    private static Size getCalendarSizeOrNull(CalendarType calendarType) {
        switch (calendarType) {
            case WALL:
                return new Size(400, 400);
            case DESK:
                return new Size(200, 150);
            case MAGNET:
                return new Size(100, 200);
            default:
                assert (false) : "unrecognized calendar type" + calendarType;
                return null;
        }
    }

    private static int calculateCalendarPrice(CalendarType calendarType) {
        switch (calendarType) {
            case WALL:
                // intentional fallthrough
            case DESK:
                return 1000;
            case MAGNET:
                return 1500;
            default:
                assert (false) : "unrecognized calendar type" + calendarType;
                return -1;
        }
    }
}

--------------------------------- Banner ---------------------------------

public class Banner extends ApertureProduct {
    public enum BannerType {
        GLOSS,
        SCRIM,
        MESH
    }

    public enum BannerSize {
        OPTION_1,
        OPTION_2,
        OPTION_3,
        OPTION_4
    }

    private final BannerType bannerType;

    public Banner(UUID productId, Orientation orientation, BannerType bannerType, Color color, BannerSize bannerSize) {
        super(productId, orientation, checkColor(color), getBannerSizeOrNull(bannerSize), calculateBannerPrice(bannerType, bannerSize));

        this.bannerType = bannerType;
    }

    public String getDisplayNameOrNull() {
        switch (this.bannerType) {
            case GLOSS:
                return String.format("Gloss Banner (%d mm x %d mm)", super.size.getWidth(), super.size.getHeight());
            case SCRIM:
                return String.format("Scrim Banner (%d mm x %d mm)", super.size.getWidth(), super.size.getHeight());
            case MESH:
                return String.format("Mesh Banner (%d mm x %d mm)", super.size.getWidth(), super.size.getHeight());
            default:
                assert (false) : "unrecognized banner type" + this.bannerType;
                return null;
        }
    }

    public BannerType getBannerType() {
        return this.bannerType;
    }

    private static Color checkColor(Color color) {
        short red = color.getRed();
        short green = color.getGreen();
        short blue = color.getBlue();

        if ((red < 0x00 || red > 0xFF) || (green < 0x00 || green > 0xFF) || (blue < 0x00 || blue > 0xFF)) {
            throw new IllegalArgumentException();
        }
        return color;
    }

    private static Size getBannerSizeOrNull(BannerSize bannerSize) {
        switch (bannerSize) {
            case OPTION_1:
                return new Size(1000, 500);
            case OPTION_2:
                return new Size(1000, 1000);
            case OPTION_3:
                return new Size(2000, 500);
            case OPTION_4:
                return new Size(3000, 1000);
            default:
                assert (false) : "unrecognized banner size" + bannerSize;
                return null;
        }
    }

    private static int calculateBannerPrice(BannerType bannerType, BannerSize bannerSize) {
        switch (bannerType) {
            case GLOSS:
                if (bannerSize == BannerSize.OPTION_1) {
                    return 5000;
                } else if (bannerSize == BannerSize.OPTION_2) {
                    return 5200;
                } else if (bannerSize == BannerSize.OPTION_3) {
                    return 5300;
                } else {
                    return 6000;
                }
            case SCRIM:
                // intentional fallthrough
            case MESH:
                if (bannerSize == BannerSize.OPTION_1) {
                    return 5100;
                } else if (bannerSize == BannerSize.OPTION_2) {
                    return 5300;
                } else if (bannerSize == BannerSize.OPTION_3) {
                    return 5400;
                } else {
                    return 6100;
                }
            default:
                assert (false) : "invalid banner type" + bannerType;
                return -1; // return invalid price
        }
    }
}

--------------------------------- BusinessCard ---------------------------------

public class BusinessCard extends ApertureProduct {
    public enum BusinessCardType {
        LINEN,
        LAID,
        SMOOTH
    }

    public enum CardSides {
        SINGLE_SIDE,
        DOUBLE_SIDE
    }

    public enum BusinessCardColor {
        GRAY,
        IVORY,
        WHITE
    }

    private final BusinessCardType businessCardType;
    private final CardSides cardSides;
    private final BusinessCardColor businessCardColor;

    public BusinessCard(UUID productId, Orientation orientation, BusinessCardType businessCardType, CardSides cardSides, BusinessCardColor businessCardColor) {
        super(productId, orientation, getBusinessCardColorOrNull(businessCardColor), getCardSizeObject(), calculateCardPrice(businessCardType, cardSides));

        this.businessCardColor = businessCardColor;
        this.businessCardType = businessCardType;
        this.cardSides = cardSides;
    }

    public String getDisplayNameOrNull() {
        switch (this.businessCardType) {
            case LINEN:
                return String.format("Linen Business Card");
            case LAID:
                return String.format("Laid Business Card");
            case SMOOTH:
                return String.format("Smooth Business Card");
            default:
                assert (false) : "unrecognized banner type" + this.businessCardType;
                return null;
        }
    }

    public BusinessCardColor getBusinessCardColor() {
        return this.businessCardColor;
    }

    public BusinessCardType getBusinessCardType() {
        return this.businessCardType;
    }

    public CardSides getCardSides() {
        return this.cardSides;
    }

    private static Color getBusinessCardColorOrNull(BusinessCardColor businessCardColor) {
        switch (businessCardColor) {
            case GRAY:
                return new Color((short) 0xE6, (short) 0xE6, (short) 0xE6);
            case IVORY:
                return new Color((short) 0xFF, (short) 0xFF, (short) 0xF0);
            case WHITE:
                return new Color((short) 0xFF, (short) 0xFF, (short) 0xFF);
            default:
                assert (false) : "unrecognized card color" + businessCardColor;
                return null;
        }
    }

    private static Size getCardSizeObject() {
        return new Size(90, 50);
    }

    private static int calculateCardPrice(BusinessCardType businessCardType, CardSides cardSides) {
        switch (businessCardType) {
            case LINEN:
                if (cardSides == CardSides.SINGLE_SIDE) {
                    return 110;
                } else {
                    return 140;
                }
            case LAID:
                if (cardSides == CardSides.SINGLE_SIDE) {
                    return 120;
                } else {
                    return 150;
                }
            case SMOOTH:
                if (cardSides == CardSides.SINGLE_SIDE) {
                    return 100;
                } else {
                    return 130;
                }
            default:
                assert (false) : "unrecognized card type" + businessCardType;
                return -1;
        }
    }
}

--------------------------------- Cart ---------------------------------

public class Cart {
    private ArrayList<Product> products;

    public Cart() {
        this.products = new ArrayList<Product>();
    }

    public boolean addProduct(Product product) {
        this.products.add(product);
        return true;
    }

    public boolean removeProduct(Product product) {
        for (Product myProduct : this.products) {
            if (myProduct == product) {
                this.products.remove(product);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (Product product : this.products) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }
}

--------------------------------- Aperture ---------------------------------

public class Aperture {
    protected final int x;
    protected final int y;
    protected final Size size;

    protected Aperture(int x, int y, Size size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Size getSize() {
        return this.size;
    }
}

--------------------------------- TextAperture ---------------------------------

public class TextAperture extends Aperture {
    private final String text;

    public TextAperture(int x, int y, Size size, String text) {
        super(x, y, size);
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}

--------------------------------- ImageAperture ---------------------------------

public class ImageAperture extends Aperture {
    private final String imagePath;

    public ImageAperture(int x, int y, Size size, String imagePath) {
        super(x, y, size);
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return this.imagePath;
    }
}