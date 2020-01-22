public class Deck {
    private Card[] cards;
    private static final int MAX_SIZE = 52;
    private int size;


    public Deck(){
        String[] suits = {"Hearts", "Spades", "Clubs", "Diamonds"};
        String[] ranks = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
        int[] values = {14, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        cards = new Card[MAX_SIZE];

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j++) {
                cards[j + 13*i] = new Card(suits[i], ranks[j], values[j]);
            }
        }
        size = MAX_SIZE;
    }


    public Card deal(){
        size --;
        Card temp = cards[size];
        cards[size] = null;
        return temp;
    }


    public boolean returnToDeck(Card c){
        if(size < MAX_SIZE){
            cards[size] = c;
            size ++;
            return true;
        }else return false;
    }


    public boolean returnToDeck(Card[] returned){

        for(Card c : returned){
            if(c != null){
                if(!returnToDeck(c)) return false;
            }
        }
        return true;
    }


    public void shuffle(){

        //randomizes the order of the deck

        for(int i = 0; i < size; i++){

            int rPosition = (int)(Math.random()*size);

            Card temp = cards[i];
            cards[i] = cards[rPosition];
            cards[rPosition] = temp;
        }
    }

    public static void sortCards(Card[] hand){

        //sorts an array of cards from lowest to highest value (doesn't pay attention to suits)

        for(int i = 0; i < hand.length; i++){
            for(int k = i; k < hand.length; k++){
                if(hand[i].getValue() > hand[k].getValue()){
                    Card temp = hand[i];
                    hand[i] = hand[k];
                    hand[k] = temp;
                }
            }
        }
    }

    public static void fixCards(Card[] hand){


        //puts all null objects at the end of an array of cards

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

    public boolean checkDeck() {

        //for testing purposes

        int spadesCnt=0, diamondsCnt=0, clubsCnt=0, heartsCnt=0;
        int totalValue=0;
        Card[] spades = new Card[20];
        Card[] diamonds = new Card[20];
        Card[] hearts = new Card[20];
        Card[] clubs = new Card[20];
        System.out.println("*******Checking Deck **********/");
        for(int i=0; i<MAX_SIZE; i++)
        {
            if(cards[i]!=null && cards[i].getSuit().equals("Clubs"))
            {

                clubs[clubsCnt]=cards[i];
                clubsCnt++;
            }
            else if(cards[i]!=null && cards[i].getSuit().equals("Diamonds"))
            {
                diamonds[diamondsCnt]=cards[i];
                diamondsCnt++;
            }

            else if(cards[i]!=null && cards[i].getSuit().equals("Hearts"))
            {
                hearts[heartsCnt]=cards[i];
                heartsCnt++;
            }

            else if(cards[i]!=null && cards[i].getSuit().equals("Spades"))
            {
                spades[spadesCnt]=cards[i];
                spadesCnt++;
            }
            if(cards[i]!=null )
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



        System.out.println("Clubs: " + clubsCnt + " Spades: " + spadesCnt + " Diamonds: " + diamondsCnt + " Hearts: " + heartsCnt);
        System.out.println("Total: " + totalValue);

        if(clubsCnt==13 && spadesCnt == 13 && diamondsCnt==13 && heartsCnt==13 && totalValue==416)
            return true;
        return false;

    }

    public String toString(){

        String out = "Size: " + size + "\n";
        for(Card c : cards){
            if(c != null){
                out = out + c + "\n";
            }
        }
        return out;
    }
}
