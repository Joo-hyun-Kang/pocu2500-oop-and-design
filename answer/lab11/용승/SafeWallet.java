package academy.pocu.comp2500.lab11;

import academy.pocu.comp2500.lab11.pocu.User;
import academy.pocu.comp2500.lab11.pocu.Wallet;

public class SafeWallet extends Wallet {
    private static final String OVERFLOW_EXCEPTION_MESSAGE = "예치 가능한 최대 금액을 초과함.";

    public SafeWallet(User user) throws IllegalAccessException {
        super(user);
    }

    @Override
    public boolean deposit(int amount) {
        int overflowWindowSize = Integer.MAX_VALUE - this.getAmount();

        if (overflowWindowSize >= amount) {
            return super.deposit(amount);
        }

        throw new OverflowException(OVERFLOW_EXCEPTION_MESSAGE);
    }
}
