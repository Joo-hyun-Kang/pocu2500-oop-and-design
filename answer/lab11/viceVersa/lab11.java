//----------------------App.java----------------------

package academy.pocu.comp2500.lab11;

import academy.pocu.comp2500.lab11.pocu.Product;
import academy.pocu.comp2500.lab11.pocu.ProductNotFoundException;
import academy.pocu.comp2500.lab11.pocu.User;
import academy.pocu.comp2500.lab11.pocu.Warehouse;
import academy.pocu.comp2500.lab11.pocu.WarehouseType;
import java.io.BufferedReader;
import java.io.PrintStream;

public class App {
    public void run(final BufferedReader in, final PrintStream out, final PrintStream err) {
        User user = new User();
        Warehouse warehouse;
        SafeWallet wallet;

        while (true) {
            int selectedNum;
            WarehouseType[] types = WarehouseType.values();

            out.println("WAREHOUSE: Choose your warehouse!");
            for (int i = 0; i < types.length; i++) {
                out.printf("%d. %s%n", i + 1, types[i].toString());
            }

            try {
                String input = in.readLine();
                if (input.equals("exit")) {
                    return;
                }
                selectedNum = Integer.parseInt(input);
            } catch (Exception e) {
                continue;
            }

            try {
                warehouse = new Warehouse(types[selectedNum - 1]);
                break;
            } catch (IndexOutOfBoundsException e) {
                // PermanentlyClosedException should be crashed
            }
        }

        try {
            wallet = new SafeWallet(user);
        } catch (IllegalAccessException e) {
            err.println("AUTH_ERROR");
            return;
        }

        while (true) {
            int selectedNum;
            Product product;

            out.printf("BALANCE: %d%n", wallet.getAmount());
            out.println("PRODUCT_LIST: Choose the product you want to buy!");
            for (int i = 0; i < warehouse.getProducts().size(); i++) {
                product = warehouse.getProducts().get(i);
                out.printf("%d. %-20s %d%n", i + 1, product.getName(), product.getPrice());
            }

            try {
                String input = in.readLine();
                if (input.equals("exit")) {
                    return;
                }
                selectedNum = Integer.parseInt(input);
                product = warehouse.getProducts().get(selectedNum - 1);
            } catch (Exception e) {
                continue;
            }

            if (!wallet.withdraw(product.getPrice())) {
                continue;
            }

            try {
                warehouse.removeProduct(product.getId());
            } catch (ProductNotFoundException e) {
                wallet.deposit(product.getPrice());
            }
        }
    }
}


//----------------------SafeWallet.java----------------------

package academy.pocu.comp2500.lab11;

import academy.pocu.comp2500.lab11.pocu.User;
import academy.pocu.comp2500.lab11.pocu.Wallet;

public class SafeWallet extends Wallet {
    public SafeWallet(final User user) throws IllegalAccessException {
        super(user);
    }

    @Override
    public boolean deposit(final int amount) {
        if (amount > 0 && this.getAmount() + amount <= 0) {
            throw new OverflowException("wallet exceeded the limit, are you bin salman?");
        }
        return super.deposit(amount);
    }
}


//----------------------OverflowException.java----------------------

package academy.pocu.comp2500.lab11;

public class OverflowException extends RuntimeException {
    private static final long serialVersionUID = 123454321L; // is any number available?

    public OverflowException(final String message) {
        super(message);
    }
}
