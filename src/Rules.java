public class Rules {


    /**
     * take in a hand, and return what the score of the hand is, with 10 for royal flush, down to 1 for high card
     *
     * sets a bunch of initial variables, loops through the hand to change them, and thne uses the states
     * of the variables to determine the type of the hand
     */
    public static int scoreCards(Card[] hand) {

        Deck.sortCards(hand);

        boolean isFlush = true, isStraight = true, hasFour = false, hasTriple = false;
        int doubles = 0;

        String suit = hand[0].getSuit();
        int v = hand[0].getValue();

        for(int i = 0; i < hand.length; i++){

            if(!hand[i].getSuit().equals(suit)) isFlush = false;

            if(hand[i].getValue() != v) isStraight = false;
            v ++;

            if(!(i > 0 && hand[i-1].getValue() == hand[i].getValue())){
                int count = 1;
                for(int k = i+1; k < hand.length; k++){
                    if(hand[i].getValue() == hand[k].getValue()) count ++;
                }

                switch (count) {
                    case 2 -> doubles++;
                    case 3 -> hasTriple = true;
                    case 4 -> hasFour = true;
                    case 5 -> System.out.println("Uh oh");
                }
            }
        }

        int score;

        if(isFlush && isStraight){
            if(hand[4].getValue() == 14){ score = 10;
            }else score = 9;
        }else if(hasFour) score = 8;
        else if(hasTriple && doubles == 1) score = 7;
        else if(isFlush) score = 6;
        else if(isStraight) score = 5;
        else if(hasTriple) score = 4;
        else if(doubles == 2) score = 3;
        else if(doubles == 1) score = 2;
        else score = 1;

        return score;
    }


    /**
     * takes in two hands, and returns which one has a higher score
     * (1 for hand 2, -1 for hand 1, and 0 for a tie.
     *
     * uses the scoreCards method to find the type of the hand, and uses different methods to break ties
     * depending on the type
     */
    public static int breakTie(Card[] hand1, Card[] hand2){
        //hand 1 wins : -1, hand 2 wins: 1, tie: 0
        if(scoreCards(hand1) > scoreCards(hand2)) return -1;
        else if(scoreCards(hand1) < scoreCards(hand2)) return 1;

        Deck.sortCards(hand1);
        Deck.sortCards(hand2);

        switch (scoreCards(hand1)){
            case 10: return 0;

            case 3:
                //checks the larger double in the two pair
                if(getBigDoubleVal(hand1) > getBigDoubleVal(hand1)) return -1;
                else if(getBigDoubleVal(hand1) < getBigDoubleVal(hand2)) return 1;

            case 2:
                //checks the smaller double in the two pair, or the only double in the one pair
                if(getSmallDoubleVal(hand1) > getSmallDoubleVal(hand1)) return -1;
                else if(getSmallDoubleVal(hand1) < getSmallDoubleVal(hand2)) return 1;

            case 9:
            case 6:
            case 5:
            case 1:
                //checks the cards from highest to lowest to return the winner
                for(int i = hand1.length - 1; i >= 0; i--){
                    if(hand1[i].getValue() > hand2[i].getValue()) return -1;
                    else if(hand1[i].getValue() < hand2[i].getValue()) return 1;
                }
                return 0;

            case 8:
            case 7:
            case 4:
                //because the 3rd card in a hand will always be in a triple of 4 of a kind
                if(hand1[2].getValue() > hand2[2].getValue()) return -1;
                else if(hand1[2].getValue() < hand2[2].getValue()) return 1;
                else {
                    System.out.println("There are too many cards ew");
                    return 0;
                }

            default:
                //if the hand is not from 1 - 10
                System.out.println("Oh no what is this hand even");
                return 0;
        }
    }


    public static int getBigDoubleVal(Card[] hand){
        //finds the largest double in a hand
        for(int i = hand.length - 1; i > 0; i --){
            if(hand[i].getValue() == hand[i-1].getValue()) return hand[i].getValue();
        }
        System.out.println("Uh oh it didnt work :((((  [the big one]");
        return -1;
    }


    public static int getSmallDoubleVal(Card[] hand){
        //finds the smallest double in a hand
        for(int i = 0; i < hand.length -1; i++){
            if(hand[i].getValue() == hand[i+1].getValue()) return hand[i].getValue();
        }
        System.out.println("Uh oh it didnt work :((((   [the small one]");
        return -1;
    }


    public static int getAnte(int roundNumber, int startingBal){
        int ante = (roundNumber * startingBal / 100);
        if(ante < 1) ante = 1;
        return ante;
    }
}