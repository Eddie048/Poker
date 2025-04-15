import java.util.Objects;

/**
 * Represents one card in a deck with a given suit and rank. The value is the number representing the rank.
 */
public record Card(String suit, String rank, int value) {

    private static final String[] ranks = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Jack", "Queen", "King", "Ace"};

    public Card(String suit, int value) {
        this(suit, ranks[value - 2], value);
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return value == card.value && Objects.equals(suit, card.suit) && Objects.equals(rank, card.rank);
    }

    public String toString() {
        return rank + " of " + suit;
    }
}
