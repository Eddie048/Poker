import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deck of cards and the logic surrounding the deck
 */
public class Deck {
    private final ArrayList<Card> cards;


    public Deck(){
        // Possible suits and ranks
        String[] suits = {"Hearts", "Spades", "Clubs", "Diamonds"};
        String[] ranks = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                "Jack", "Queen", "King"};
        int[] values = {14, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        // Create deck
        cards = new ArrayList<>();

        // Create cards in deck
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 13; j++) {
                cards.add(new Card(suits[i], ranks[j], values[j]));
            }
        }
    }


    /**
     * Deal one card off the top of the deck
     * @return the dealt card
     */
    public Card deal() {
        if (cards.size() == 0) return null;
        return cards.remove(cards.size() - 1);
    }


    /**
     * Return an array of cards to the deck
     * @param returned the cards to be returned
     */
    public void returnToDeck(List<Card> returned) {
        cards.addAll(returned);
    }


    /**
     * Shuffle the deck
     */
    public void shuffle() {

        for(int i = 0; i < cards.size(); i++) {

            // Choose location to swap
            int rPosition = (int) (Math.random() * cards.size());

            // Swap cards
            Card temp = cards.get(i);
            cards.set(i, cards.get(rPosition));
            cards.set(rPosition, temp);
        }
    }


    /**
     * Sort a given array of cards from lowest to highest value, suit is ignored
     * @param hand the cards to be sorted
     */
    public static void sortCards(Card[] hand) {

        for(int i = 0; i < hand.length; i++) {
            for(int k = i; k < hand.length; k++) {
                if(hand[i].getValue() > hand[k].getValue()) {
                    Card temp = hand[i];
                    hand[i] = hand[k];
                    hand[k] = temp;
                }
            }
        }
    }


    /**
     * Puts all null objects in a card array at the end
     * @param hand the card array to be fixed
     */
    public static void fixCards(Card[] hand) {

        for(int i = 0; i < hand.length; i++){
            if(hand[i] == null){
                for(int k = i; k < hand.length; k ++){
                    if(hand[k] != null){
                        Card temp = hand[i];
                        hand[i] = hand[k];
                        hand[k] = temp;
                    }
                }
            }
        }
    }


    /**
     * For testing purposes, tests the deck
     * @return true if success, false otherwise
     */
    public boolean checkDeck() {

        // Ensure deck is correct size
        if (cards.size() != 52) return false;

        Deck temp = new Deck();
        Card current = temp.deal();

        // Check all cards in deck against a new deck
        while (current != null) {
            if (!cards.contains(current)) return false;

            current = temp.deal();
        }

        return true;
    }


    public String toString(){

        StringBuilder out = new StringBuilder("Size: " + cards.size() + "\n");
        for(Card c : cards){
            if(c != null){
                out.append(c).append("\n");
            }
        }
        return out.toString();
    }
}
