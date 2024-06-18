public class Card {

    private final String suit;
    private final String rank;
    private final int value;

    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return rank + " of " + suit;
    }
}
