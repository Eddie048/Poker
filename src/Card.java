public class Card {


    private final String suit;
    private final String rank;
    private final int value;


    public Card(String s, String r, int v){
        suit = s;
        rank = r;
        value = v;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue(){
        return value;
    }


    public String toString(){

        return rank + " of " + suit;
    }

}
