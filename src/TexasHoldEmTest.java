import java.util.ArrayList;
import java.util.List;

public class TexasHoldEmTest {
    public static void main(String[] args) {

        
    }

    public static void testGameWinDistribution(int numGames, int numOtherPlayers) {
        System.out.println(numOtherPlayers + " Players");
        int[] results = new int[3];
        for (int i = 0; i < numGames; i++) {
            int result = runGame(numOtherPlayers);
            if (result == -1) results[1] ++;
            else if (result == 0) results[0] ++;
            else results[2] ++;
        }

        String[] resultText = new String[]{"Wins:   ", "Ties:   ", "Losses: "};
        for (int i = 0; i < 3; i++) {
            System.out.printf("%s%6d, %6.2f%%\n", resultText[i], results[i], ((float) results[i] / numGames ) * 100);
        }

        System.out.printf("\nPercentage wins: %7.3f%%\n", (float) results[0] * 100 / (results[2] + results[0]));
        System.out.printf("Expected: %7.3f%%\n\n\n", 100.0/(numOtherPlayers + 1));
    }

    public static void checkHandDistribution() {
        int[] counts = new int[10];

        for (int i = 0; i < 10000000; i++) {
            counts[Test() - 1] ++;
        }

        System.out.println("\n");
        for (int i : counts) {
            System.out.println(i);
        }
    }

    public static int Test() {
        Deck deck = new Deck();

        deck.shuffle();

        ArrayList<Card> hand = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            hand.add(deck.deal());
        }

        List<Card> best = Rules.getBestHand(hand);
        return Rules.scoreCards(best);

    }

    public static int Test2() {
        Deck deck = new Deck();


        List<Card> hand = new ArrayList<>();
        hand.add(new Card("Hearts", "Two", 2));
        hand.add(new Card("Hearts", "Four", 4));
        hand.add(new Card("Hearts", "Ace", 14));
        hand.add(new Card("Hearts", "Five", 5));
        hand.add(new Card("Hearts", "Three", 3));

        deck.removeAll(hand);

        deck.shuffle();

        hand.add(deck.deal());
        hand.add(deck.deal());


        List<Card> best = Rules.getBestHand(hand);
        return Rules.scoreCards(best);
    }

    public static int runGame(int numOtherPlayers) {
        Deck deck = new Deck();
        deck.shuffle();

        // Deal table
        ArrayList<Card> table = new ArrayList<>();
        table.add(deck.deal());
        table.add(deck.deal());
        table.add(deck.deal());
        table.add(deck.deal());
        table.add(deck.deal());

        // Initialize player hand
        ArrayList<Card> hand = new ArrayList<>(table);
        hand.add(deck.deal());
        hand.add(deck.deal());
        List<Card> playerBest = Rules.getBestHand(hand);

        // Initialize other player's hands
        ArrayList<List<Card>> otherBest = new ArrayList<>();
        for (int i = 0; i < numOtherPlayers; i++) {
            ArrayList<Card> tempHand = new ArrayList<>(table);
            tempHand.add(deck.deal());
            tempHand.add(deck.deal());
            otherBest.add(Rules.getBestHand(tempHand));
        }


        // Score hands, index of winner, -1 if tie
        int result = 0;
        for (List<Card> otherHand : otherBest) {


            int tempResult = Rules.breakTie(playerBest, otherHand);
            if (tempResult == 1) {
                // Loss, check for other player tie
                int bestHand = otherBest.indexOf(otherHand);
                boolean tie = false;
                for (int i = 0; i < otherBest.size(); i++) {

                    // If these are the same card, skip
                    if (i == bestHand) continue;


                    int compare = Rules.breakTie(otherBest.get(bestHand), otherBest.get(i));
                    if (compare == 0) tie = true;
                    else if (compare == 1) {
                        tie = false;
                        bestHand = i;
                    }
                }

                return tie ? -1 : bestHand + 1;
            }
            else if (tempResult == 0) result = -1;
        }

        return result;
    }

    public static void testGameWinDistributionFair(int numGames, int numPlayers) {
        System.out.println(numPlayers + " Players");
        int[] results = new int[3];
        for (int i = 0; i < numGames; i++) {
            int result = runGameFair(numPlayers);
            if (result == -1) results[1] ++;
            else if (result == 0) results[0] ++;
            else results[2] ++;
        }

        String[] resultText = new String[]{"Wins:   ", "Ties:   ", "Losses: "};
        for (int i = 0; i < 3; i++) {
            System.out.printf("%s%6d, %6.2f%%\n", resultText[i], results[i], ((float) results[i] / numGames ) * 100);
        }

        System.out.printf("\nPercentage wins: %6.2f%%\n", (float) results[0] * 100 / (results[2] + results[0]));
        System.out.printf("Expected: %6.2f%%\n\n\n", 100.0/numPlayers);
    }

    public static int runGameFair(int numPlayers) {
        Deck deck = new Deck();
        deck.shuffle();

        // Deal table
        ArrayList<Card> table = new ArrayList<>();
        table.add(deck.deal());
        table.add(deck.deal());
        table.add(deck.deal());
        table.add(deck.deal());
        table.add(deck.deal());

        // Initialize hands
        ArrayList<List<Card>> bestHands = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            ArrayList<Card> tempHand = new ArrayList<>(table);
            tempHand.add(deck.deal());
            tempHand.add(deck.deal());
            bestHands.add(Rules.getBestHand(tempHand));
        }


        // Score hands, return -1 if win, 0 if tie, 1 if loss
        int winner = 0;
        boolean tie = false;

        for (int i = 1; i < bestHands.size(); i++) {
            int compare = Rules.breakTie(bestHands.get(winner), bestHands.get(i));
            if (compare == 0) {
                tie = true;
            } else if (compare == 1) {
                tie = false;
                winner = i;
            }
        }
        return tie ? -1 : winner;
    }

}
