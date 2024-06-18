/**
 * Represents the kitty, stores the current bets
 */
public class Kitty {
    private int total;


    public void update(int amount) {
        total += amount;
    }


    public int payout() {
        int temp = total;
        total = 0;
        return temp;
    }


    public int getValue() {
        return total;
    }
}