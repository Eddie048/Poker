/**
 * Represents one card in a deck with a given suit and rank. The value is the number representing the rank.
 */
public record Card(String suit, String rank, int value) {

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
