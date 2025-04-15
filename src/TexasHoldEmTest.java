import java.util.ArrayList;
import java.util.List;

public class TexasHoldEmTest {
    public static void main(String[] args) {

        for (int i = 1; i <= 10; i++) {
            testGameWinDistribution(1000000, i);
        }
    }

    public static void testGameWinDistribution(int numGames, int numOtherPlayers) {
        System.out.println(numOtherPlayers + " Players");
        int[] results = new int[3];
        for (int i = 0; i < numGames; i++) {
            results[runGame(numOtherPlayers) + 1] ++;
        }

        String[] resultText = new String[]{"Wins:   ", "Ties:   ", "Losses: "};
        for (int i = 0; i < 3; i++) {
            System.out.printf("%s%6d, %6.2f%%\n", resultText[i], results[i], ((float) results[i] / numGames ) * 100);
        }

        System.out.printf("\nPercentage wins: %6.2f%%\n", (float)results[0]/(results[2] + results[0]) * 100);
        System.out.printf("Expected: %6.2f%%\n\n\n", 1.0/(numOtherPlayers + 1) * 100);
    }

    public static void checkHandDistribution() {
        int[] counts = new int[10];

        for (int i = 0; i < 100000000; i++) {
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
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(deck.deal());
        hand.add(deck.deal());
        hand.addAll(table);
        List<Card> playerBest = Rules.getBestHand(hand);

        // Initialize other player's hands
        ArrayList<List<Card>> otherBest = new ArrayList<>();
        for (int i = 0; i < numOtherPlayers; i++) {
            ArrayList<Card> tempHand = new ArrayList<>();
            tempHand.add(deck.deal());
            tempHand.add(deck.deal());
            tempHand.addAll(table);
            otherBest.add(Rules.getBestHand(tempHand));
        }



        // Score hands, return -1 if win, 0 if tie, 1 if loss
        int result = -1;
        for (List<Card> otherHand : otherBest) {


            int tempResult = Rules.breakTie(otherHand, playerBest);
            if (tempResult == -1) {
                // Loss, check for other player tie
                List<Card> bestHand = otherHand;
                boolean tie = false;
                for (List<Card> other : otherBest) {

                    // If these are the same card, skip
                    if (other.get(0).equals(bestHand.get(0))) continue;


                    int compare = Rules.breakTie(other, bestHand);
                    if (compare == 0) tie = true;
                    else if (compare == -1) {
                        tie = false;
                        bestHand = other;
                    }
                }

                return tie ? 0 : 1;
            }
            else if (tempResult == 0) result = 0;
        }

        return result;
    }

}
