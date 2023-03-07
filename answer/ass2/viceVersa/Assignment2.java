// enum 파일은 생략 (특이사항 없음)

-------------------------- ShoppingCart

package academy.pocu.comp2500.assignment2;

import java.util.LinkedHashSet;

public class ShoppingCart {
    private final LinkedHashSet<Product> itemList;

    public ShoppingCart() {
        this.itemList = new LinkedHashSet<>();
    }

    public LinkedHashSet<Product> getItemList() {
        return itemList;
    }

    public void addItem(Product product) {
        itemList.add(product);
    }

    public void removeItem(Product product) {
        itemList.remove(product);
    }

    public int getTotalPrice() {
        int total = 0;
        for (Product item : itemList) {
            total += item.getPrice();
        }
        return total;
    }

    public void selectShippingMethod(Product product, ShippingMethod shippingMethod) {
        for (Product item : itemList) {
            if (product.equals(item)) {
                product.setShippingMethod(shippingMethod);
            }
        }
    }
}

-------------------------- Product

package academy.pocu.comp2500.assignment2;

public class Product {
    protected String name;
    protected Size size;
    protected Color color;
    protected int price;
    protected ShippingMethod shippingMethod;

    protected Product(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Size getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    protected void addPrice(int price) {
        this.price += price;
    }
}

-------------------------- CustomizableProduct

package academy.pocu.comp2500.assignment2;

import java.util.ArrayList;

public class CustomizableProduct extends Product {
    protected Orientation orientation;
    protected final ArrayList<Aperture> apertures;
    private final int APERTURE_PRICE = 5;

    protected CustomizableProduct(Orientation orientation, ShippingMethod shippingMethod) {
        super(shippingMethod);
        this.orientation = orientation;
        this.apertures = new ArrayList<>();
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public ArrayList<Aperture> getApertures() {
        return apertures;
    }

    public boolean addAperture(Aperture aperture) {
        if (!isValidAperture(aperture) || apertures.contains(aperture)) {
            return false;
        }
        apertures.add(aperture);
        super.addPrice(APERTURE_PRICE);
        return true;
    }

    private boolean isValidAperture(Aperture aperture) {
        int r1w = this.getSize().getWidth();
        int r1h = this.getSize().getHeight();
        int r2x = aperture.getX();
        int r2y = aperture.getY();
        int r2w = aperture.getWidth();
        int r2h = aperture.getHeight();
        return (0 < r2x + r2w && r2x < r1w && 0 < r2y + r2h && r2y < r1h);
    }
}

-------------------------- Stamp

package academy.pocu.comp2500.assignment2;

public class Stamp extends Product {
    private final String text;

    public Stamp(StampSize size, StampColor color, String text, ShippingMethod shippingMethod) {
        super(shippingMethod);

        Size stampSize = null;
        Color stampColor = null;
        int stampPrice = 0;

        if (size == StampSize.W40H30) {
            stampSize = new Size(40, 30);
            stampPrice = 2300;
        } else if (size == StampSize.W50H20) {
            stampSize = new Size(50, 20);
            stampPrice = 2300;
        } else if (size == StampSize.W70H40) {
            stampSize = new Size(70, 40);
            stampPrice = 2600;
        } else {
            assert (false) : "unknown size";
        }

        if (color == StampColor.RED) {
            stampColor = new Color(0xff, 0, 0);
        } else if (color == StampColor.BLUE) {
            stampColor = new Color(0, 0, 0xff);
        } else if (color == StampColor.GREEN) {
            stampColor = new Color(0, 0x80, 0);
        } else {
            assert (false) : "unknown color";
        }

        super.name = String.format("Stamp (%d mm x %d mm)", stampSize.getWidth(), stampSize.getHeight());
        super.size = stampSize;
        super.color = stampColor;
        super.price = stampPrice;
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

-------------------------- Calendar

package academy.pocu.comp2500.assignment2;

public class Calendar extends Product {
    private final CalendarType type;

    public Calendar(CalendarType type, ShippingMethod shippingMethod) {
        super(shippingMethod);

        String calendarType = null;
        Size calendarSize = null;
        int calendarPrice = 0;

        if (type == CalendarType.WALL) {
            calendarType = "Wall";
            calendarSize = new Size(400, 400);
            calendarPrice = 1000;
        } else if (type == CalendarType.DESK) {
            calendarType = "Desk";
            calendarSize = new Size(200, 150);
            calendarPrice = 1000;
        } else if (type == CalendarType.MAGNET) {
            calendarType = "Magnet";
            calendarSize = new Size(100, 200);
            calendarPrice = 1500;
        } else {
            assert (false) : "unknown type";
        }

        super.name = String.format("%s Calendar", calendarType);
        super.size = calendarSize;
        super.color = new Color(0xff, 0xff, 0xff);
        super.price = calendarPrice;
        this.type = type;
    }

    public CalendarType getType() {
        return type;
    }
}

-------------------------- Banner

package academy.pocu.comp2500.assignment2;

public class Banner extends CustomizableProduct {
    private final BannerType type;

    public Banner(BannerType type, BannerSize size, Color color,
                  Orientation orientation, ShippingMethod shippingMethod) {
        super(orientation, shippingMethod);

        String bannerType = null;
        Size bannerSize = null;
        int bannerPrice = 0;

        if (type == BannerType.GLOSS) {
            bannerType = "Gloss";
        } else if (type == BannerType.SCRIM) {
            bannerType = "Scrim";
        } else if (type == BannerType.MESH) {
            bannerType = "Mesh";
        } else {
            assert (false) : "unknown type";
        }

        if (size == BannerSize.W1000H500) {
            bannerSize = new Size(1000, 500);
            bannerPrice = 5100;
        } else if (size == BannerSize.W1000H1000) {
            bannerSize = new Size(1000, 1000);
            bannerPrice = 5300;
        } else if (size == BannerSize.W2000H500) {
            bannerSize = new Size(2000, 500);
            bannerPrice = 5400;
        } else if (size == BannerSize.W3000H1000) {
            bannerSize = new Size(3000, 1000);
            bannerPrice = 6100;
        } else {
            assert (false) : "unknown size";
        }

        if (type == BannerType.GLOSS) {
            bannerPrice -= 100;
        }

        super.name = String.format("%s Banner (%d mm x %d mm)", bannerType,
                bannerSize.getWidth(), bannerSize.getHeight());
        super.size = bannerSize;
        super.color = color;
        super.price = bannerPrice;
        this.type = type;
    }

    public BannerType getType() {
        return type;
    }
}

-------------------------- BusinessCard

package academy.pocu.comp2500.assignment2;

public class BusinessCard extends CustomizableProduct {
    private final BusinessCardType type;
    private final BusinessCardSides sides;

    public BusinessCard(BusinessCardType type, BusinessCardColor color, BusinessCardSides sides,
                        Orientation orientation, ShippingMethod shippingMethod) {
        super(orientation, shippingMethod);

        String businessCardType = null;
        Color businessCardColor = null;
        int businessCardPrice = 0;

        if (type == BusinessCardType.LINEN) {
            businessCardType = "Linen";
            businessCardPrice = 140;
        } else if (type == BusinessCardType.LAID) {
            businessCardType = "Laid";
            businessCardPrice = 150;
        } else if (type == BusinessCardType.SMOOTH) {
            businessCardType = "Smooth";
            businessCardPrice = 130;
        } else {
            assert (false) : "unknown type";
        }

        if (sides == BusinessCardSides.SINGLE) {
            businessCardPrice -= 30;
        }

        if (color == BusinessCardColor.GRAY) {
            businessCardColor = new Color(0xe6, 0xe6, 0xe6);
        } else if (color == BusinessCardColor.IVORY) {
            businessCardColor = new Color(0xff, 0xff, 0xf0);
        } else if (color == BusinessCardColor.WHITE) {
            businessCardColor = new Color(0xff, 0xff, 0xff);
        } else {
            assert (false) : "unknown color";
        }

        super.name = String.format("%s Business Card", businessCardType);
        super.size = new Size(90, 50);
        super.color = businessCardColor;
        super.price = businessCardPrice;
        this.type = type;
        this.sides = sides;
    }

    public BusinessCardType getType() {
        return type;
    }

    public BusinessCardSides getSides() {
        return sides;
    }
}

-------------------------- Color

package academy.pocu.comp2500.assignment2;

public class Color {
    private short red;
    private short green;
    private short blue;

    public Color(int red, int green, int blue) {
        if (0 <= red && red <= 255 && 0 <= green && green <= 255 && 0 <= blue && blue <= 255) {
            this.red = (short) red;
            this.green = (short) green;
            this.blue = (short) blue;
        } else {
            assert (false) : "invalid color";
        }
    }

    public short getRed() {
        return red;
    }

    public short getGreen() {
        return green;
    }

    public short getBlue() {
        return blue;
    }

    public int getRgbColor() {
        // How to use : System.out.format("0x%06x\n", item.getColor().getRgbColor());
        return (int) red * 0x10000 + (int) green * 0x100 + (int) blue;
    }
}

-------------------------- Size

package academy.pocu.comp2500.assignment2;

public class Size {
    private final int width;
    private final int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

-------------------------- Aperture

package academy.pocu.comp2500.assignment2;

public class Aperture {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    protected Aperture(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

-------------------------- TextAperture

package academy.pocu.comp2500.assignment2;

public class TextAperture extends Aperture {
    private final String text;

    public TextAperture(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

-------------------------- ImageAperture

package academy.pocu.comp2500.assignment2;

public class ImageAperture extends Aperture {
    private final String imagePath;

    public ImageAperture(int x, int y, int width, int height, String imagePath) {
        super(x, y, width, height);
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}
