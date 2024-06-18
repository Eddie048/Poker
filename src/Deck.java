import java.util.List;

/**
 * Represents a deck of cards and the logic surrounding the deck
 */
public class Deck {
    private final Card[] cards;
    private static final int MAX_SIZE = 52;
    private int currentSize;


    public Deck(){
        // Possible suits and ranks
        String[] suits = {"Hearts", "Spades", "Clubs", "Diamonds"};
        String[] ranks = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                "Jack", "Queen", "King"};
        int[] values = {14, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        // Create deck
        cards = new Card[MAX_SIZE];

        // Create cards in deck
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 13; j++) {
                cards[j + 13*i] = new Card(suits[i], ranks[j], values[j]);
            }
        }
        currentSize = MAX_SIZE;
    }


    /**
     * Deal one card off the top of the deck
     * @return the dealt card
     */
    public Card deal() {
        currentSize--;
        Card temp = cards[currentSize];
        cards[currentSize] = null;
        return temp;
    }


    /**
     * Return a card to the top of the deck
     * @param c the card to return
     * @return true if success, false otherwise
     */
    public boolean returnToDeck(Card c) {
        if(currentSize < MAX_SIZE) {
            cards[currentSize] = c;
            currentSize++;
            return true;
        } else return false;
    }


    /**
     * Return an array of cards to the deck
     * @param returned the cards to be returned
     */
    public void returnToDeck(List<Card> returned) {

        for(Card c : returned) {
            if(c != null) {
                if(!returnToDeck(c)) return;
            }
        }
    }


    /**
     * Shuffle the deck
     */
    public void shuffle() {

        for(int i = 0; i < currentSize; i++) {

            int rPosition = (int)(Math.random()* currentSize);

            Card temp = cards[i];
            cards[i] = cards[rPosition];
            cards[rPosition] = temp;
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

        int spadesCnt=0, diamondsCnt=0, clubsCnt=0, heartsCnt=0;
        int totalValue=0;
        Card[] spades = new Card[20];
        Card[] diamonds = new Card[20];
        Card[] hearts = new Card[20];
        Card[] clubs = new Card[20];
        System.out.println("*******Checking Deck **********/");
        for(int i=0; i<MAX_SIZE; i++) {
            if(cards[i]!=null && cards[i].getSuit().equals("Clubs")) {

                clubs[clubsCnt]=cards[i];
                clubsCnt++;
            }
            else if(cards[i]!=null && cards[i].getSuit().equals("Diamonds")) {
                diamonds[diamondsCnt]=cards[i];
                diamondsCnt++;
            }

            else if(cards[i]!=null && cards[i].getSuit().equals("Hearts")) {
                hearts[heartsCnt]=cards[i];
                heartsCnt++;
            }

            else if(cards[i]!=null && cards[i].getSuit().equals("Spades")) {
                spades[spadesCnt]=cards[i];
                spadesCnt++;
            }
            if(cards[i]!=null)
                totalValue+=cards[i].getValue();

        }
        for(int i=0; i<clubsCnt; i++)
            if(clubs[i]!=null)
                System.out.println(clubs[i]);

        System.out.println();

        for(int i=0; i<diamondsCnt; i++)
            if(diamonds[i]!=null)
                System.out.println(diamonds[i]);

        System.out.println();

        for(int i=0; i<spadesCnt; i++)
            if(spades[i]!=null)
                System.out.println(spades[i]);

        System.out.println();

        for(int i=0; i<heartsCnt; i++)
            if(hearts[i]!=null)
                System.out.println(hearts[i]);

        System.out.println("Clubs: " + clubsCnt + " Spades: " + spadesCnt +
                " Diamonds: " + diamondsCnt + " Hearts: " + heartsCnt);
        System.out.println("Total: " + totalValue);

        return clubsCnt == 13 && spadesCnt == 13 && diamondsCnt == 13 && heartsCnt == 13 && totalValue == 416;
    }


    public String toString(){

        StringBuilder out = new StringBuilder("Size: " + currentSize + "\n");
        for(Card c : cards){
            if(c != null){
                out.append(c).append("\n");
            }
        }
        return out.toString();
    }
}
