import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Poker {

    private static ComputerPokerPlayer computer;
    private static PokerPlayer player;
    private static Kitty kitty;
    private static ArrayList<Card> discardPile;
    private static Deck deck;

    private static Scanner reader;

    public static void main(String[] args) {

        // Initialize scanner
        reader = new Scanner(System.in);

        boolean keepGoing = true;

        while (keepGoing) {
            // Player choices with ASCII art from https://www.asciiart.eu/
            System.out.println("""
                    Welcome to 5 card draw poker!                    
                              _____
                             |A .  | _____
                             | / \\ ||A ^  | _____
                             |(_._)|| / \\ ||A _  | _____
                             |  |  || \\ / || ( ) ||A_ _ |
                             |____V||  .  ||(_'_)||( v )|
                                    |____V||  |  || \\ / |
                                           |____V||  .  |
                                                  |____V|
                                                  
                    1: Play the game
                    2: Read the rules
                    3: Exit""");

            int choice = reader.nextInt();

            switch (choice) {
                case 1 -> keepGoing = playGame(); // Start game
                case 2 -> System.out.println("Rules"); //TODO: write the rules
                case 3 -> keepGoing = false;
            }
        }

        // Game is exited
        System.out.println("Thanks for playing!");
    }


    /**
     * Starts the game
     * @return true if player would like to play again, false otherwise
     */
    public static boolean playGame() {

        // Initialize variables
        int startingBal = 100;
        deck = new Deck();
        kitty = new Kitty();
        discardPile = new ArrayList<>();

        // Initialize human player
        System.out.println("What is your name?");
        String playerName = reader.nextLine();
        player = new PokerPlayer(playerName, 5, startingBal);

        // Initialize computer player
        computer = new ComputerPokerPlayer("Computer", 5, startingBal);

        // The round number of the game
        int roundNumber = 1;

        //Start doing rounds
        while (true) {
            boolean roundEnd;

            System.out.println("\n\nRound " + roundNumber + "\n\n");

            // Set the ante, take it from the players
            int ante = Rules.getAnte(roundNumber, startingBal);
            System.out.println("The ante is now " + ante + ".");
            kitty.update(player.deduct(ante));
            kitty.update(computer.deduct(ante));

            // Shuffle and deal the deck
            deck.shuffle();
            for (int i = 0; i < 5; i++) {
                player.setCard(deck.deal());
                computer.setCard(deck.deal());
            }

            // Sort hands
            player.sortCards();
            computer.sortCards();

            // Show player hand
            System.out.println("Your hand:\n" + player.showHand());

            // Do round of betting
            roundEnd = bettingRound(roundNumber);

            if (!roundEnd) {

                // Exchange cards
                playerExchangeCards();
                computerExchangeCards();

                System.out.println("Your hand:\n");
                System.out.println(player.showHand());

                // Do round of betting
                roundEnd = bettingRound(roundNumber);
            }

            if (!roundEnd) {
                resolveRound();
            }

            if (player.getMoney() < Rules.getAnte(roundNumber + 1, startingBal)) {
                System.out.println("You lost. :(");
                break;
            } else if (computer.getMoney() < Rules.getAnte(roundNumber + 1, startingBal)) {
                System.out.println("You win!");
                break;
            }

            deck.returnToDeck(List.of(player.discard()));
            deck.returnToDeck(List.of(computer.discard()));
            deck.returnToDeck(discardPile);

            roundNumber ++;
        }


        System.out.println("Good game! \nWould you like to play again?(Y/N)");
        String in = reader.nextLine();
        return (in.equals("Y") || in.equals("y"));
    }


    /**
     * Do round of betting
     * @param roundNumber the current round number
     * @return true if the round is over, false otherwise
     */
    private static boolean bettingRound(int roundNumber) {

        int turnNum = roundNumber%2;
        int userBet = 0;
        int computerBet = 0;
        boolean roundEnd = false;

        while (true) {

            System.out.println();

            if (turnNum % 2 == 1) {
                // Do player betting turn
                userBet = playerBettingTurn(userBet, computerBet);
            } else {
                // Do computer betting turn
                computerBet = computerBettingTurn(userBet, computerBet);
            }
            turnNum++;

            if (userBet == computerBet && turnNum > 2) break;
            else if (userBet == -1 || computerBet == -1) break;
        }

        if (computerBet == -1) {
            kitty.update(userBet);
            System.out.println("You win the round, and " + kitty.getValue() + ".");

            player.increase(kitty.payout());
            System.out.println("You now have " + player.getMoney() + ".");
            System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");

            roundEnd = true;
        } else if (userBet == -1) {
            kitty.update(computerBet);
            System.out.println(computer.getName() + " wins the round, and " + kitty.getValue() + ".");

            computer.increase(kitty.payout());
            System.out.println("You now have " + player.getMoney() + ".");
            System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");

            roundEnd = true;
        } else {
            kitty.update(userBet);
            kitty.update(computerBet);
        }
        return roundEnd;
    }


    /**
     * Logic for computer's betting turn
     * @param userBet current user bet
     * @param computerBet current computer bet
     * @return new computer bet
     */
    public static int computerBettingTurn(int userBet, int computerBet) {
        // Get computer bet and ensure it is at most the total amount the player has
        int bet = computer.bet(userBet, computerBet);
        if(computerBet > userBet + player.getMoney()) computerBet = userBet + player.getMoney();

        if (bet < computerBet || bet == -1 || bet < userBet) {
            System.out.println(computer.getName() + " folded.");
            kitty.update(computerBet);
            computerBet = -1;

        } else if (bet >= computerBet + computer.getMoney()) {
            System.out.println(computer.getName() + " went all in for " + (computerBet + computer.getMoney()) + ".");
            computerBet += computer.getMoney();
            computer.deduct(computer.getMoney());

        } else if (bet == userBet) {
            String word;
            if (userBet == 0) word = "check";
            else word = "call";

            System.out.println(computer.getName() + " " + word + "ed for " + bet + ".");
            computer.deduct(bet - computerBet);
            computerBet = bet;

        } else {
            System.out.println(computer.getName() + " raised to " + bet + ".");
            computer.deduct(bet - computerBet);
            computerBet = bet;
        }

        return computerBet;
    }


    /**
     * Logic for player's betting turn
     * @param userBet current user bet
     * @param computerBet current computer bet
     * @return new computer bet
     */
    public static int playerBettingTurn(int userBet, int computerBet) {
        // Player's turn
        if (computer.getMoney() == 0) {

            System.out.println("The computer has gone all in!");
            System.out.println("It's your turn to bet. You have " + player.getMoney() + " left. What would you like to do?");
            System.out.println("1: Call");
            System.out.println("2: Fold");


            String choice = reader.nextLine();

            while (!(choice.equals("1") || choice.equals("2"))) {
                System.out.println("You must enter 1, 2, 3, or 4.");
                choice = reader.nextLine();
            }

            if(choice.equals("1")){
                System.out.println("You called for " + computerBet);
                player.deduct(computerBet - userBet);
                userBet = computerBet;
            }else{
                System.out.println("You folded!");
                kitty.update(userBet);
                userBet = -1;
            }

        } else if (computerBet > userBet + player.getMoney()) {
            System.out.println("The computer has bet more than you have!");
            System.out.println("It's your turn to bet. You have " + player.getMoney() + " left. What would you like to do?");
            System.out.println("1: Go all in");
            System.out.println("2: Fold");

            String choice = reader.nextLine();

            while (!(choice.equals("1") || choice.equals("2"))) {
                System.out.println("You must enter 1 or 2");
                choice = reader.nextLine();
            }


            if (choice.equals("1")) {
                System.out.println("You went all in for " + (player.getMoney() + userBet) + ".");
                userBet += player.getMoney();
                player.deduct(player.getMoney());
            } else {
                System.out.println("You folded!");
                kitty.update(userBet);
                userBet = -1;
            }

        } else {
            String word;
            if (userBet == 0 && computerBet == 0) word = "Check";
            else word = "Call";
            System.out.println("It's your turn to bet. You have " + player.getMoney() + " left. What would you like to do?");
            System.out.println("1: " + word);
            System.out.println("2: Raise");
            System.out.println("3: Go all in");
            System.out.println("4: Fold");


            String choice = reader.nextLine();

            while (!(choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4"))) {
                System.out.println("You must enter 1, 2, 3, or 4.");
                choice = reader.nextLine();
            }

            switch (choice) {
                case "1" -> {
                    System.out.println("You " + word.toLowerCase() + "ed for " + computerBet);
                    player.deduct(computerBet - userBet);
                    userBet = computerBet;
                }
                case "2" -> {
                    System.out.println("How much would you like to raise to?");
                    int bet = reader.nextInt();
                    while (bet < userBet || bet > (player.getMoney() + userBet) || bet < computerBet) {
                        if (bet < computerBet) System.out.println("You must bet more than the computer!");
                        else if (bet < userBet)
                            System.out.println("You must bet more than you already bet!");
                        else if (bet > (player.getMoney() + userBet))
                            System.out.println("You can't bet more than you have!");

                        bet = reader.nextInt();
                    }
                    System.out.println("You have raised to " + bet + ".");
                    player.deduct(bet - userBet);
                    userBet = bet;
                }
                case "3" -> {
                    System.out.println("You went all in for " + (player.getMoney() + userBet) + ".");
                    userBet += player.getMoney();
                    player.deduct(player.getMoney());
                }
                case "4" -> {
                    System.out.println("You folded!");
                    kitty.update(userBet);
                    userBet = -1;
                }
            }
        }

        return userBet;
    }


    /**
     * Computer exchanges cards in their hand
     */
    private static void computerExchangeCards() {
        // Get computer cards to discard
        int[] discard = computer.playHand();

        // Discard computer cards
        for (int j : discard) {
            discardPile.add(computer.discard(j));
        }

        // Fix computer hand
        computer.fixCards();

        // Refill computer hand
        while(player.hasRoom()) player.setCard(deck.deal());

        // Sort computer hand
        player.sortCards();
    }


    /**
     * Player exchanges cards in their hand
     */
    private static void playerExchangeCards() {
        // Player exchanges cards
        System.out.println("Your hand:\n" + player.showHand());
        System.out.println("How many cards would you like to discard?");

        // Get number of cards to discard
        int total = reader.nextInt();
        while (total < 0 || total > 5) {
            System.out.println("You can only discard between 0 and 5 cards.");
            total = reader.nextInt();
        }

        // Initialize array of cards to discard
        int[] discard = new int[total];
        Arrays.fill(discard, -2);

        // Fill array with cards player wants to discard
        for (int i = 0; i < discard.length; i++) {

            System.out.println("Which card would you like to discard? (-1 if done)");
            discard[i] = reader.nextInt() - 1;

            if(discard[i] == -2)break;

            while (hasRepeat(discard) || discard[i] > 4 || discard[i] < 0) {
                System.out.println("You must enter a number between 1 and 5, no repeats.");
                discard[i] = reader.nextInt() - 1;

                if(discard[i] == -2) break;
            }
            if(discard[i] == -2) break;
        }

        // Discard cards
        for (int j : discard) {
            if (j == -2) break;
            discardPile.add(player.discard(j));
        }

        // Fix player hand
        player.fixCards();

        // Refill player hand
        while(player.hasRoom()) player.setCard(deck.deal());

        // Sort player hand
        player.sortCards();
    }


    /**
     * Reveal player and computer cards, decide winner, payout kitty
     */
    public static void resolveRound() {
        System.out.println("Your hand:\n" + player.showHand());
        System.out.println("You have a " + handToString(player.getHand()) + ".");
        System.out.println("\n" + computer.getName() + " hand:\n" + computer.showHand());
        System.out.println(computer.getName() + " has a " + handToString(computer.getHand()) + ".");

        if(Rules.breakTie(player.getHand(), computer.getHand()) == -1){
            System.out.println("You win!");
            player.increase(kitty.payout());

            System.out.println("You now have " + player.getMoney() + ".");
            System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");


        }else if(Rules.breakTie(player.getHand(), computer.getHand()) == 1){
            System.out.println("You lose!");
            computer.increase(kitty.payout());

            System.out.println("You now have " + player.getMoney() + ".");
            System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");

        }else{
            System.out.println("You tied somehow.");
            int payout = kitty.payout();

            computer.increase(payout/2);
            player.increase(payout/2);

            System.out.println("You now have " + player.getMoney() + ".");
            System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");
        }
    }


    /**
     * Gets the string form of a given hand of 5 cards
     * @param hand the hand to get the string form of
     * @return the string representation
     */
    public static String handToString(Card[] hand) {

        return switch (Rules.scoreCards(hand)) {
            case 10 -> "royal flush";
            case 9 -> "straight flush";
            case 8 -> "four of a kind";
            case 7 -> "full house";
            case 6 -> "flush";
            case 5 -> "straight";
            case 4 -> "three of a kind";
            case 3 -> "two pair";
            case 2 -> "pair";
            default -> "high card";
        };
    }


    /**
     * Searches an array for a repeat, ignoring -2
     * @param arr array to be searched
     * @return true if repeat, false otherwise
     */
    public static boolean hasRepeat(int[] arr) {

        //searches an int array for repeats, breaks at -2

        for (int i = 0; i < arr.length; i++) {
            for (int k = i + 1; k < arr.length; k++) {
                if(arr[k] == -2) break;
                else if(arr[k] == arr[i]) return true;
            }
            if(arr[i] == -2) break;
        }
        return false;
    }
}