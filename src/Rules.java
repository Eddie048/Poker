import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rules {


    /**
     * take in a hand, and return what the score of the hand is, with 10 for royal flush, down to 1 for high card
     *
     * sets a bunch of initial variables, loops through the hand to change them, and thne uses the states
     * of the variables to determine the type of the hand
     */
    public static int scoreCards(List<Card> hand) {

        Deck.sortCards(hand);

        boolean isFlush = true, isStraight = true, hasFour = false, hasTriple = false;
        int doubles = 0;

        String suit = hand.get(0).getSuit();
        int v = hand.get(0).getValue();

        for(int i = 0; i < hand.size(); i++){

            if(!hand.get(i).getSuit().equals(suit)) isFlush = false;

            if (i == 4 && isStraight && v == 6) {
                isStraight = hand.get(i).value() == 6 || hand.get(i).getValue() == 14;
            } else if(hand.get(i).getValue() != v) isStraight = false;
            v ++;

            if(!(i > 0 && hand.get(i-1).getValue() == hand.get(i).getValue())){
                int count = 1;
                for(int k = i+1; k < hand.size(); k++){
                    if(hand.get(i).getValue() == hand.get(k).getValue()) count ++;
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

        if(isFlush && isStraight) {
            if(hand.get(3).getValue() == 13) score = 10;
            else score = 9;
        } else if(hasFour) score = 8;
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
     * Get best hand from all cards available
     * @param fullHand all cards available
     * @return best hand of 5 cards
     */
    public static List<Card> getBestHand(List<Card> fullHand) {
//        System.out.println("Scoring hand");
        if (fullHand.size() != 7) return null;
        Deck.sortCards(fullHand);

        // Count suits
        Map<String, Integer> suitCount = new HashMap<>();
        for (Card c : fullHand) {
            suitCount.put(c.getSuit(), suitCount.containsKey(c.getSuit()) ? suitCount.get(c.getSuit()) + 1 : 1);
        }

//        System.out.println("Checking for straight flush");
        // Check for straight flush
        for (String suit : suitCount.keySet()) {
            if (suitCount.get(suit) >= 5) {

                ArrayList<Card> partialStraight = new ArrayList<>();
                int lastNum = 20; // Impossible number, so it doesn't connect
                for (int i = fullHand.size() - 1; i >= 0; i --) {
                    if (!fullHand.get(i).getSuit().equals(suit)) {
                        // Skip non suited cards
                        continue;
                    } else if (fullHand.get(i).getValue() < lastNum - 1) {
                        // Broke straight, start over
                        partialStraight = new ArrayList<>();
                    }
                    partialStraight.add(fullHand.get(i));
                    lastNum = fullHand.get(i).getValue();

                    // Check for straight
                    if (partialStraight.size() == 5) {
//                        System.out.println("Found straight flush");
                        return partialStraight;
                    }
                }

                // Check for 5 high straight
                if (partialStraight.size() == 4 && partialStraight.get(0).getValue() == 5) {
                    // Check for ace suited
                    for (Card c : fullHand) {
                        if (c.getValue() == 14 && c.getSuit().equals(suit)) {
                            partialStraight.add(c);
//                            System.out.println("Found straight flush");
                            return partialStraight;
                        }
                    }
                }
            }
        }


        // Check for runs
        int[] runs = new int[4];
        int lastDifferent = 0;
        for (int i = 1; i < fullHand.size(); i++) {
            if (fullHand.get(i).getValue() != fullHand.get(i - 1).getValue()) {
                runs[i - lastDifferent - 1] ++;
                lastDifferent = i;
            }
        }
        runs[7 - lastDifferent - 1] ++;

//        System.out.println("Checking for four of a kind");
        // If four of a kind, return it
        if (runs[3] > 0) {
            // Find four of a kind
            int index = findLargestNInRow(fullHand, 4);

            // Return the four of a kind and the largest next card
            ArrayList<Card> result = new ArrayList<>(fullHand.subList(index, index + 4));
            fullHand.removeAll(result);
            result.add(fullHand.get(fullHand.size() - 1));
//            System.out.println("Found four of a kind");
            return result;

        }

//        System.out.println("Checking for full house");
        // If full house, return it
        if (runs[2] >= 2 || (runs[2] == 1 && runs[1] > 0)) {
            int tripleIndex = findLargestNInRow(fullHand, 3);
            ArrayList<Card> result = new ArrayList<>(fullHand.subList(tripleIndex, tripleIndex + 3));
            fullHand.removeAll(result);

            int doubleIndex = findLargestNInRow(fullHand, 2);
            result.addAll(fullHand.subList(doubleIndex, doubleIndex + 2));

//            System.out.println("Found full house");
            return result;
        }

//        System.out.println("Checking for flush");
        // Check for flush
        for (String suit : suitCount.keySet()) {
            if (suitCount.get(suit) >= 5) {
                ArrayList<Card> result = new ArrayList<>();
                for (int i = fullHand.size() - 1; i >= 0; i --) {
                    if (fullHand.get(i).getSuit().equals(suit)) result.add(fullHand.get(i));

                    // Ensure max size flush is 5
                    if (result.size() >= 5) break;
                }
//                System.out.println("Found flush");
                return result;
            }
        }

//        System.out.println("Checking for straight");
        // Check for straight
        ArrayList<Card> partialStraight = new ArrayList<>();
        int lastNum = 20; // Impossible number, so it doesn't connect
        for (int i = fullHand.size() - 1; i >= 0; i --) {
            if (fullHand.get(i).getValue() == lastNum) {
                // Skip multiples
                continue;
            } else if (fullHand.get(i).getValue() < lastNum - 1) {
                // Broke straight, start over
                partialStraight = new ArrayList<>();
            }
            partialStraight.add(fullHand.get(i));
            lastNum = fullHand.get(i).getValue();

            // Check for straight
            if (partialStraight.size() == 5) {
//                System.out.println("Found straight");
                return partialStraight;
            }
        }

        // Check for 5 high straight
        if (partialStraight.size() == 4 && partialStraight.get(0).getValue() == 5) {
            // Check for ace
            for (Card c : fullHand) {
                if (c.getValue() == 14) {
                    partialStraight.add(c);
//                    System.out.println("Found straight");
                    return partialStraight;
                }
            }
        }

//        System.out.println("Checking for three of a kind");
        // Check for three of a kind
        if (runs[2] > 0) {

            // Add the triple
            int tripleIndex = findLargestNInRow(fullHand, 3);
            ArrayList<Card> result = new ArrayList<>(fullHand.subList(tripleIndex, tripleIndex + 3));
            fullHand.removeAll(result);

            // Add the best next two cards
            result.addAll(fullHand.subList(fullHand.size() - 2, fullHand.size()));
//            System.out.println("Found three of a kind");
            return result;
        }
//        System.out.println("Checking for two pair");
        // Check for two pair
        if (runs[1] >= 2) {

            // Add the largest pair
            int pairIndex = findLargestNInRow(fullHand, 2);
            ArrayList<Card> result = new ArrayList<>(fullHand.subList(pairIndex, pairIndex + 2));
            fullHand.removeAll(result);

            // Find the second-largest pair
            pairIndex = findLargestNInRow(fullHand, 2);
            result.addAll(fullHand.subList(pairIndex, pairIndex + 2));
            fullHand.removeAll(result);

            // Add the best next card
            result.add(fullHand.get(fullHand.size() - 1));

//            System.out.println("Found two pair");
            return result;
        }

//        System.out.println("Checking for pair");
        // Check for pair
        if (runs[1] == 1) {

            // Add the largest pair
            int pairIndex = findLargestNInRow(fullHand, 2);
            ArrayList<Card> result = new ArrayList<>(fullHand.subList(pairIndex, pairIndex + 2));
            fullHand.removeAll(result);

            // Add the best next three cards
            result.addAll(fullHand.subList(fullHand.size() - 3, fullHand.size()));
//            System.out.println("Found pair");
            return result;
        }

        // Return top 5 cards
//        System.out.println("Found high card");
        List<Card> result = fullHand.subList(fullHand.size() - 5, fullHand.size());
        return result;
    }

    public static int findLargestNInRow(List<Card> cards, int rowLength) {

        int lastDifferent = cards.size() - 1;
        for (int i = cards.size() - 2; i >= 0; i--) {
            if (cards.get(i).getValue() != cards.get(i + 1).getValue()) {
                if (lastDifferent - i >= rowLength) {
                    return i + 1;
                }
                lastDifferent = i;
            }
        }

        if (lastDifferent + 1 >= rowLength) {
            return 0;
        }

        // No runs of length 'rowLength' found
        return -1;
    }

    /**
     * takes in two hands, and returns which one has a higher score
     * (1 for hand 2, -1 for hand 1, and 0 for a tie.
     *
     * uses the scoreCards method to find the type of the hand, and uses different methods to break ties
     * depending on the type
     */
    public static int breakTie(List<Card> hand1, List<Card> hand2){

        //hand 1 wins : -1, hand 2 wins: 1, tie: 0
        if(scoreCards(hand1) > scoreCards(hand2)) return -1;
        else if(scoreCards(hand1) < scoreCards(hand2)) return 1;

        Deck.sortCards(hand1);
        Deck.sortCards(hand2);

        // Tied initial evaluation, do further analysis
        switch (scoreCards(hand1)){
            case 10:
                // Both players have a royal flush, tie game
                return 0;
            case 3:
                // Two pair, check the larger pair to see the winner
                if(getBigDoubleVal(hand1) > getBigDoubleVal(hand2)) return -1;
                else if(getBigDoubleVal(hand1) < getBigDoubleVal(hand2)) return 1;
                // If they are the same, fall through

            case 2:
                // Check the smaller double in a two pair, or the only double in a one pair
                if(getSmallDoubleVal(hand1) > getSmallDoubleVal(hand2)) return -1;
                else if(getSmallDoubleVal(hand1) < getSmallDoubleVal(hand2)) return 1;
                // If they are the same, checking in a row works
                return compareHandsNaive(hand1, hand2);

            case 9:
            case 5:
                // Check for special case of 6 high straight vs 5 high straight with ace
                if (hand1.get(4).getValue() == 14 && hand1.get(3).getValue() == 5) {
                    // Put ace at the front of the hand, it is low
                    Card temp = hand1.remove(4);
                    hand1.add(0, temp);
                }
                if (hand2.get(4).getValue() == 14 && hand2.get(3).getValue() == 5) {
                    // Put ace at the front of the hand, it is low
                    Card temp = hand2.remove(4);
                    hand2.add(0, temp);
                }
                // Now checking all in a row works
                return compareHandsNaive(hand1, hand2);

            case 6:
            case 1:
                // For high card and flush, it is sufficient compare naively
                return compareHandsNaive(hand1, hand2);

            case 8:
            case 7:
            case 4:
                // Logic for 4 of a kind, full house, and three of a kind
                // Checking only the third card is sufficient, as in each case it will be part of the 3 or 4 matching
                if(hand1.get(2).getValue() > hand2.get(2).getValue()) return -1;
                else if(hand1.get(2).getValue() < hand2.get(2).getValue()) return 1;
                // Tied, check every card against every other, same as above

                return compareHandsNaive(hand1, hand2);

            default:
                // Something is broken
                System.out.println("Oh no what is this hand even");
                return 0;
        }
    }

    public static int compareHandsNaive(List<Card> hand1, List<Card> hand2) {
        for(int i = hand1.size() - 1; i >= 0; i--){
            if(hand1.get(i).getValue() > hand2.get(i).getValue()) return -1;
            else if(hand1.get(i).getValue() < hand2.get(i).getValue()) return 1;
        }

        // Entire hand is tied
        return 0;
    }


    public static int getBigDoubleVal(List<Card> hand){
        // Finds the largest double in a hand
        for(int i = hand.size() - 1; i > 0; i --){
            if(hand.get(i).getValue() == hand.get(i-1).getValue()) return hand.get(i).getValue();
        }
        System.out.println("Uh oh it didnt work :((((  [the big one]");
        return -1;
    }


    public static int getSmallDoubleVal(List<Card> hand){
        // Finds the smallest double in a hand
        for(int i = 0; i < hand.size() -1; i++){
            if(hand.get(i).getValue() == hand.get(i+1).getValue()) return hand.get(i).getValue();
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