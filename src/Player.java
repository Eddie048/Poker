public class Player {
    private Card[] hand;
    private final int MAX_SIZE;
    private int currentSize;
    private String name;

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
        String out = "";

        for(int i = 0; i < hand.length; i++){
            if(hand[i] != null) out += (i + 1) +  ": " + hand[i] + "\n";
        }

        return out;
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