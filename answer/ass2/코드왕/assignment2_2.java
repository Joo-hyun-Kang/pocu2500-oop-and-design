public class ApertureProduct extends Product {
    protected OrientationType orientation;
    protected static final int ADDITIONAL_COST_FOR_APERTURE = 5;
    protected ArrayList<Aperture> apertureList = new ArrayList<>();
​
    protected ApertureProduct(String productId, OrientationType orientation, Color color, Size size, Price price) {
        super(productId, color, size, price);
        this.orientation = orientation;
​
    }
​
    public enum OrientationType {
        PORTRAIT, // 세로 방향이 더 긴
        LANDSCAPE // 가로 방향이 더 긴
    }
​
    public OrientationType getOrientation() {
        return orientation;
    }
​
​
    public void addAperture(Aperture aperture) {
        if (!isValidAperture(aperture)) {
            System.out.println("Don't try to add an invalid Aperture");
​
            return;
        }
​
        apertureList.add(aperture);
        price.addPrice(ADDITIONAL_COST_FOR_APERTURE);
    }
​
    public ArrayList<Aperture> getApertureList() {
        return apertureList;
    }
​
    protected boolean isValidAperture(Aperture aperture) {
        int x = aperture.getX();
        int y = aperture.getY();
        int width = getSize().getWidth();
        int height = getSize().getHeight();
​
        return 0 <= x && x <= width && 0 <= y && y <= height;
    }
}
​
public class Aperture {
    protected int x; // [mm]
    protected int y; // [mm]
    protected boolean isValid;
​
    protected Aperture(int x, int y) {
        this.x = x;
        this.y = y;
    }
​
    public int getX() {
        return x;
    }
​
    public int getY() {
        return y;
    }
​
}
​
public class TextAperture extends Aperture {
    private String text;
​
    public TextAperture(String text, int x, int y) {
        super(x, y);
        this.text = text;
    }
​
    public String getText() {
        return text;
    }
​
    public void setText(String text) {
        this.text = text;
    }
​
}
​
public class ImageAperture extends Aperture {
    private String imagePath;
​
    public ImageAperture(String imagePath, int x, int y) {
        super(x, y);
        this.imagePath = imagePath;
    }
​
    public String getImagePath() {
        return imagePath;
    }
​
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
​
public class Banner extends ApertureProduct {
​
    private static final int DEFAULT_PRICE_GLOSS = 5000;
    private static final int DEFAULT_PRICE_SCRIM = 5100;
    private static final int DEFAULT_PRICE_MESH = 5100;
​
    private BannerType bannerType;
    private BannerSizeTypeMeter bannerSize;
​
​
    private static final int FIVE_HUNDRED = 500;
    private static final int ONE_THOUSAND = 1000;
    private static final int TWO_THOUSAND = 2000;
    private static final int THREE_THOUSAND = 3000;
​
    public enum BannerType {
        GLOSS,
        SCRIM,
        MESH
    }
​
    public enum BannerSizeTypeMeter {
        // WIDTH_X_HEIGHT [m] * [m]    Note. only use [mm] in all code but in enum
        ONE_X_POINT_FIVE,
        ONE_X_ONE,
        TWO_X_POINT_FIVE,
        THREE_X_ONE
    }
​
    public Banner(String productId, OrientationType orientation, Color bannerColor, BannerType bannerType, BannerSizeTypeMeter bannerSize) {
        super(productId, orientation, bannerColor, new Size(), new Price());
​
        this.bannerType = bannerType;
        switch (bannerType) {
            case GLOSS:
                price.setPrice(DEFAULT_PRICE_GLOSS);
                break;
            case SCRIM:
                price.setPrice(DEFAULT_PRICE_SCRIM);
                break;
            case MESH:
                price.setPrice(DEFAULT_PRICE_MESH);
                break;
            default:
                System.out.println("Unknown BannerMaterial detected");
                break;
        }
        this.bannerType = bannerType;
​
​
        this.bannerSize = bannerSize;
        switch (bannerSize) {
            case ONE_X_POINT_FIVE:
                size = new Size(ONE_THOUSAND, FIVE_HUNDRED);
                price.addPrice(0);
                break;
            case ONE_X_ONE:
                size = new Size(ONE_THOUSAND, ONE_THOUSAND);
                price.addPrice(200);
                break;
            case TWO_X_POINT_FIVE:
                size = new Size(TWO_THOUSAND, FIVE_HUNDRED);
                price.addPrice(300);
                break;
            case THREE_X_ONE:
                size = new Size(THREE_THOUSAND, ONE_THOUSAND);
                price.addPrice(1000);
                break;
            default:
                System.out.println("Unknown BannerSize detected");
                break;
        }
​
        this.displayName = String.format("%s Banner (%d mm x %d mm)", bannerType, size.getWidth(), size.getHeight());
    }
​
    public BannerType getBannerType() {
        return bannerType;
    }
​
​
    public BannerSizeTypeMeter getBannerSize() {
        return bannerSize;
    }
​
}
​
public class BusinessCard extends ApertureProduct {
    private BusinessCardSizeCentiMeter businessCardSize = BusinessCardSizeCentiMeter.NINE_X_FIVE;
    private BusinessCardColorType businessCardColor;
​
    private static final int DEFAULT_PRICE_LINEN = 110;
    private static final int DEFAULT_PRICE_LAID = 120;
    private static final int DEFAULT_PRICE_SMOOTH = 100;
    protected BusinessCardType businessCardType;
    protected static final int FIFTY = 50;
    protected static final int NINETY = 90;
​
​
    protected BusinessCardSidesType sides;
    protected static final int ADDITIONAL_COST_FOR_DOUBLE_SIDED = 30;
​
    public enum BusinessCardSizeCentiMeter {
        // WIDTH_X_HEIGHT [cm] * [cm]    Note. only use [mm] in all code but in enum
        NINE_X_FIVE
    }
​
    public enum BusinessCardType {
        LINEN,
        LAID,
        SMOOTH
    }
​
    public enum BusinessCardSidesType {
        SINGLE_SIDED,
        DOUBLE_SIDED
    }
​
    public enum BusinessCardColorType {
        GRAY,
        IVORY,
        WHITE
    }
​
​
​
    public enum BusinessCardSizeTypeCentiMeter {
        // WIDTH_X_HEIGHT [cm] * [cm]    Note. only use [mm] in all code but in enum
        NINE_X_FIVE
    }
​
    public BusinessCard(String productId, BusinessCardType businessCardType, BusinessCardSidesType sides, OrientationType orientation, BusinessCardColorType cardColor) {
        super(productId, orientation, null, null, new Price());
​
        size = new Size(NINETY, FIFTY);
​
        this.businessCardType = businessCardType;
        switch (businessCardType) {
            case LINEN:
                price.setPrice(DEFAULT_PRICE_LINEN);
                break;
            case LAID:
                price.setPrice(DEFAULT_PRICE_LAID);
                break;
            case SMOOTH:
                price.setPrice(DEFAULT_PRICE_SMOOTH);
                break;
            default:
                System.out.println("Unknown PaperMaterialType detected");
                break;                
        }
        
        this.sides = sides;
        switch (sides) {
            case SINGLE_SIDED:
                price.addPrice(0);
                break;
            case DOUBLE_SIDED:
                price.addPrice(ADDITIONAL_COST_FOR_DOUBLE_SIDED);
                break;
            default:
​
                break;
        }
​
        this.orientation = orientation;
​
        this.businessCardColor = cardColor;
        switch (cardColor) {
            case GRAY:
                color = new Color(0xE6, 0xE6, 0xE6);
                break;
            case IVORY:
                color = new Color(0xFF, 0xFF, 0xF0);
                break;
            case WHITE:
                color = new Color(0xFF, 0xFF, 0xFF);
                break;
            default:
                System.out.println("Unknown BusinessCardColorType detected");
                break;
        }
​
        this.displayName = businessCardType + " " + "Business Card";
    }
​
    public BusinessCardSizeCentiMeter getBusinessCardSize() {
        return businessCardSize;
    }
​
    public BusinessCardColorType getBusinessCardColor() {
        return businessCardColor;
    }
​
    public BusinessCardType getBusinessCardType() {
        return businessCardType;
    }
​
​
    public BusinessCardSidesType getSides() {
        return sides;
    }
}
​
public class Cart {
    private int totalPrice = 0;
​
    private ArrayList<Product> productList = new ArrayList<>();
​
    public enum ShippingMethodType {
        PICKUP,
        SHIP
    }
​
    public Cart() {
​
    }
​
    public ArrayList<Product> getProductList() {
        return productList;
    }
​
    public void addProduct(Product product) {
        productList.add(product);
    }
​
    public void removeProduct(Product product) {
        productList.remove(product);
    }
​
    public int getTotalPrice() {
        for (Product product : productList) {
            totalPrice += product.getPrice();
        }
​
        return totalPrice;
    }
​
}