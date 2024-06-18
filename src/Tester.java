import java.lang.reflect.Array;

public class Tester {
    public static void main2(String[] args) {
        Deck deck = new Deck();
        deck.shuffle();

        Player p1 = new Player("Bilbo", 5);
        Player p2 = new Player("Bobo", 5);


        for(int i = 0; i  < 5; i ++){
            p1.setCard(deck.deal());
            p2.setCard(deck.deal());
        }
        System.out.println(deck);
        System.out.println(p1.showHand());
        System.out.println(p2.showHand());
    }

    public static void mainfake2(String[] args){
        Deck deck = new Deck();
        System.out.println(deck);
        deck.shuffle();
        System.out.println(deck);

        Card[] dealt = {deck.deal(), null, null, deck.deal(), deck.deal()};

        System.out.println("\n" +  dealt[0] + "\n" + dealt[1] + "\n" + dealt[2] + "\n" + dealt[3] + "\n" + dealt[4]);

        deck.fixCards(dealt);

        System.out.println(" \n" + dealt[0] + "\n" + dealt[1] + "\n" + dealt[2] + "\n" + dealt[3] + "\n" + dealt[4]);


    }
    /*
     * Deck deck = new Deck();
        System.out.println(deck);
        deck.shuffle();
        System.out.println(deck);

        Card[] delt = {deck.deal(), deck.deal(), deck.deal(), deck.deal()};
        System.out.println(dealt[0] + "\n" + dealt[1] + "\n" + dealt[2] + "\n" + dealt[3]);
        System.out.println(deck);

        deck.returnToDeck(dealt);
        System.out.println(deck);

     */
    public static void main3(String[] args)
    {

        Card[] player1Hand = new Card[7];
        Card[] player2Hand = new Card[7];

        Deck myDeck = new Deck();

        System.out.println("****New Deck *******");

        System.out.println(myDeck);

        myDeck.shuffle();

        System.out.println("****Shuffled Deck - ensure order changed *******");
        System.out.println(myDeck);

        System.out.println("****Check Deck - ensure all cards here *******");
        System.out.println(myDeck.checkDeck());

        for(int i=0; i<7; i++)
        {
            player1Hand[i] = myDeck.deal();
            player2Hand[i] = myDeck.deal();
        }

        System.out.println("****Check Deck After Dealing 14 Cards *******");
        System.out.println(myDeck.checkDeck());

        System.out.println("*****Deck After Dealing 14 ********");
        System.out.println(myDeck);
        myDeck.shuffle();//shuffle smaller deck

        System.out.println("*******Shuffled Smaller Deck: ");
        System.out.println(myDeck);
        System.out.println();

        System.out.println("**********Player 1 Hand:*********");
        for(int i=0; i<player1Hand.length; i++)
        {
            if(player1Hand[i]!=null) //Ensure we do not try to access null reference
                System.out.print(" " + player1Hand[i]);
        }
        Deck.sortCards(player1Hand);
        System.out.println("\nSorted");
        for(int i=0; i<player1Hand.length; i++)
        {
            if(player1Hand[i]!=null) //Ensure we do not try to access null reference
                System.out.print(" " + player1Hand[i]);
        }

        System.out.println("\n**********Player 2 Hand:*********");
        for(int i=0; i<player2Hand.length; i++)
        {
            if(player2Hand[i]!=null)
                System.out.print(" " + player2Hand[i]);
        }

        //Return player 1's middle card:
        myDeck.returnToDeck(player1Hand[player1Hand.length/2]);
        player1Hand[player1Hand.length/2]=null;

        System.out.println("\n***********player 1 hand after returning card**************:");

        for(int i=0; i<player1Hand.length; i++)
        {
            if(player1Hand[i]!=null) //needed so we don't access null Card
                System.out.print(" " + player1Hand[i]);
        }
        System.out.println("\n**********Deck after having one card returned ***********//*");
        System.out.println("\n" + myDeck);
        System.out.println(myDeck.checkDeck());

        Deck.fixCards(player1Hand);

        System.out.println("Did it work? " + myDeck.returnToDeck(player1Hand));
        System.out.println("Did it work? " + myDeck.returnToDeck(player2Hand));
        System.out.println("\n**********Deck after all cards returned ***********//*");

        System.out.println(myDeck.checkDeck());
        System.out.println(myDeck);

    }
    public static void mainnotreal(String[] args){
        Deck deck = new Deck();
        deck.shuffle();
        Card[] hand1 = new Card[5];
        Card[] hand2 = new Card[5];

        for(int i = 0; i < 5; i++){
            hand1[i] = deck.deal();
            hand2[i] = deck.deal();
        }
        Deck.sortCards(hand1);
        Deck.sortCards(hand2);

        System.out.println(cardString(hand1));
        System.out.println(cardString(hand2));
        System.out.println(Rules.scoreCards(hand1));
        System.out.println(Rules.scoreCards(hand2));
        System.out.println(Rules.breakTie(hand1, hand2));
    }

    public static String cardString(Card[] cards){
        String out = "";
        for(Card c : cards){
            out += c.toString() + ", ";
        }
        return out;
    }

    public static void main(String[] args){

        Strategy s = new Strategy(1);

        Card[] twoPair5 = new Card[]{new Card("Spades", "10", 10), new Card("Hearts", "10", 10), new Card("Hearts", "7", 7), new Card("Diamonds", "7", 7), new Card("Diamonds", "8", 8)};
        Deck.sortCards(twoPair5);

        Card[] twoPair4 = new Card[]{new Card("Spades", "10", 10), new Card("Hearts", "10", 10), new Card("Hearts", "7", 7), new Card("Diamonds", "7", 7), new Card("Diamonds", "8", 8)};
        Deck.sortCards(twoPair4);

        Card[] pair2 = new Card[]{new Card("Spades", "Queen", 12), new Card("Hearts", "Queen", 12), new Card("Spades", "King", 13), new Card("Clubs", "5", 5), new Card("Diamonds", "2", 2)};
        Deck.sortCards(pair2);

        Card[] triple = new Card[]{new Card("Spades", "Ace", 14), new Card("Hearts", "Ace", 14), new Card("Spades", "King", 13), new Card("Clubs", "Ace", 14), new Card("Diamonds", "5", 5)};
        Deck.sortCards(triple);

        Card[] twoPair = new Card[]{new Card("Spades", "Ace", 14), new Card("Hearts", "Ace", 14), new Card("Spades", "King", 13), new Card("Clubs", "King", 13), new Card("Diamonds", "5", 5)};
        Deck.sortCards(twoPair);

        Card[] high3 = new Card[]{new Card("Spades", "8", 8), new Card("Hearts", "4", 4), new Card("Spades", "King", 13), new Card("Clubs", "5", 5), new Card("Diamonds", "10", 10)};
        Deck.sortCards(high3);

        Card[] high2 = new Card[]{new Card("Spades", "8", 8), new Card("Hearts", "4", 4), new Card("Spades", "King", 13), new Card("Clubs", "5", 5), new Card("Diamonds", "10", 10)};
        Deck.sortCards(high2);

        Card[] pair4 = new Card[]{new Card("Spades", "Queen", 12), new Card("Hearts", "Queen", 12), new Card("Spades", "King", 13), new Card("Clubs", "6", 6), new Card("Diamonds", "2", 2)};
        Deck.sortCards(pair4);

        Card[] pair3 = new Card[]{new Card("Spades", "Queen", 12), new Card("Hearts", "Queen", 12), new Card("Spades", "King", 13), new Card("Clubs", "6", 6), new Card("Diamonds", "2", 2)};
        Deck.sortCards(pair3);



        Card[] test = high3;

        for(Card c: test){
            System.out.println(c);
        }
        System.out.println("\n");

        int[] discard = s.playHand(test);

        //Deck.sortCards(test);

        for(int x : discard){
            test[x] = null;
        }



        for(Card c: test){
            System.out.println(c);
        }

    }


}



