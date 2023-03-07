package academy.pocu.comp2500.lab11;

import academy.pocu.comp2500.lab11.pocu.Product;
import academy.pocu.comp2500.lab11.pocu.ProductNotFoundException;
import academy.pocu.comp2500.lab11.pocu.User;
import academy.pocu.comp2500.lab11.pocu.Wallet;
import academy.pocu.comp2500.lab11.pocu.Warehouse;
import academy.pocu.comp2500.lab11.pocu.WarehouseType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.UUID;

public class App {
    private static final String EXIT = "exit";
    private static final WarehouseType[] WAREHOUSE_TYPES = WarehouseType.values();
    private static final String PROMPT_FOR_WAREHOUSE_TYPE = "WAREHOUSE: Choose your warehouse! (Enter \"" + EXIT + "\" to quit the program.)";
    private static final String SELECTABLE_WAREHOUSE_TYPES = getSelectableWarehouseTypesInString();
    private static final String PROMPT_FOR_PRODUCT_LIST = "PRODUCT_LIST: Choose the product you want to buy! (Enter \"" + EXIT + "\" to quit the program.)";

    private SystemStatus systemStatus = SystemStatus.RUNNING;

    private static String getSelectableWarehouseTypesInString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WAREHOUSE_TYPES.length; ++i) {
            sb.append(i + 1).append(". ").append(WAREHOUSE_TYPES[i].name()).append(System.lineSeparator());
        }

        return sb.toString();
    }

    public void run(BufferedReader in, PrintStream out, PrintStream err) {
        WarehouseType warehouseType = getWarehouseType(in, out);

        if (this.systemStatus == SystemStatus.STOPPED) {
            return;
        }

        Warehouse warehouse = new Warehouse(warehouseType);

        Wallet wallet = getWalletOrNull(err);

        if (this.systemStatus == SystemStatus.STOPPED) {
            return;
        }

        purchaseProductFromWarehouse(in, out, warehouse, wallet);
    }

    private WarehouseType getWarehouseType(BufferedReader in, PrintStream out) {
        WarehouseType warehouseType = null;

        while (warehouseType == null && this.systemStatus == SystemStatus.RUNNING) {
            printPromptForWarehouseType(out);
            warehouseType = readWarehouseTypeOrNull(in);
        }

        return warehouseType;
    }

    private void printPromptForWarehouseType(PrintStream out) {
        out.println(PROMPT_FOR_WAREHOUSE_TYPE);
        out.print(SELECTABLE_WAREHOUSE_TYPES);
    }

    private WarehouseType readWarehouseTypeOrNull(BufferedReader in) {
        String userInputOrNull = readUserInput(in);

        if (userInputOrNull != null) {
            if (userInputOrNull.equals(EXIT)) {
                changeSystemStatusToStopped();
            } else if (tryParseListNumber(userInputOrNull, WAREHOUSE_TYPES.length)) {
                return WAREHOUSE_TYPES[Integer.parseInt(userInputOrNull) - 1];
            }
        }

        return null;
    }

    private Wallet getWalletOrNull(PrintStream err) {
        Wallet wallet = null;

        try {
            wallet = new SafeWallet(new User());
        } catch (IllegalAccessException illegalAccessException) {
            err.println("AUTH_ERROR");
            changeSystemStatusToStopped();
        }

        return wallet;
    }

    private void purchaseProductFromWarehouse(BufferedReader in, PrintStream out, Warehouse warehouse, Wallet wallet) {
        while (this.systemStatus == SystemStatus.RUNNING) {
            printWalletBalance(out, wallet);

            List<Product> products = warehouse.getProducts();
            printPromptForProductList(out, products);

            UUID productId = readProductIdOrNull(in, products);

            if (productId == null) {
                continue;
            }

            executePurchase(warehouse, wallet, productId);
        }
    }

    private void printWalletBalance(PrintStream out, Wallet wallet) {
        out.println("BALANCE: " + wallet.getAmount());
    }

    private void printPromptForProductList(PrintStream out, List<Product> products) {
        out.println(PROMPT_FOR_PRODUCT_LIST);
        out.print(getProductListInFormattedString(products));
    }

    private String getProductListInFormattedString(List<Product> products) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < products.size(); ++i) {
            Product product = products.get(i);
            sb.append(i + 1).append(". ");
            sb.append(String.format("%-20s %10d", product.getName(), product.getPrice()));
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    private UUID readProductIdOrNull(BufferedReader in, List<Product> products) {
        String userInputOrNull = readUserInput(in);

        if (userInputOrNull != null) {
            if (userInputOrNull.equals(EXIT)) {
                changeSystemStatusToStopped();
            } else if (tryParseListNumber(userInputOrNull, products.size())) {
                return products.get(Integer.parseInt(userInputOrNull) - 1).getId();
            }
        }

        return null;
    }

    private void executePurchase(Warehouse warehouse, Wallet wallet, UUID productID) {
        Product productToPurchase = null;

        for (Product product : warehouse.getProducts()) {
            if (product.getId().equals(productID)) {
                productToPurchase = product;
                break;
            }
        }

        if (productToPurchase != null && (productToPurchase.getPrice() <= wallet.getAmount())) {
            try {
                if (wallet.withdraw(productToPurchase.getPrice())) {
                    warehouse.removeProduct(productToPurchase.getId());
                }
            } catch (ProductNotFoundException exception) {
                wallet.deposit(productToPurchase.getPrice());
            }
        }
    }

    private String readUserInput(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException ioException) {
            return null;
        }
    }

    private void changeSystemStatusToStopped() {
        this.systemStatus = SystemStatus.STOPPED;
    }

    private boolean tryParseListNumber(String userInputForListNumber, int listLength) {
        int number;

        try {
            number = Integer.parseInt(userInputForListNumber);
        } catch (NumberFormatException exception) {
            return false;
        }

        return number > 0 && number <= listLength;
    }
}
