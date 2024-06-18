public class Player {
    private Card[] hand;
    private final int MAX_SIZE;
    private int currentSize;
    private final String name;

    public Player(String n, int max){
        MAX_SIZE = max;
        name = n;
        currentSize = 0;
        hand = new Card[MAX_SIZE];
    }

    public String getName(){
        return name;
    }

    public void setCard(Card c){
        if(currentSize <= MAX_SIZE){
            hand[currentSize] = c;
            currentSize ++;
        }
    }

    public Card discard(int i){
        currentSize --;

        Card temp = hand[i];
        hand[i] = null;
        return temp;
    }

    public Card[] discard(){
        currentSize = 0;

        Card[] temp = hand;
        hand = new Card[MAX_SIZE];
        return temp;
    }

    public Card[] getHand(){
        return hand;
    }

    public String showHand(){
        StringBuilder out = new StringBuilder();

        for(int i = 0; i < hand.length; i++){
            if(hand[i] != null) out.append(i + 1).append(": ").append(hand[i]).append("\n");
        }

        return out.toString();
    }

    public boolean hasRoom(){
        return currentSize != 5;
    }

    public void fixCards(){
        Deck.fixCards(hand);
    }

    public void sortCards(){
        Deck.sortCards(hand);
    }

    public String toString(){
        return name + "\n" + showHand();
    }
}