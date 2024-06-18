import java.util.Arrays;
import java.util.Scanner;

public class Poker {

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);

        boolean keepGoing = true;

        while (keepGoing) {
            System.out.println("""
                    Welcome to 5 card draw poker!
                    1: Play the game
                    2: Read the rules
                    3: Exit""");

            int choice = reader.nextInt();

            switch (choice) {
                case 1 -> keepGoing = playGame();
                case 2 -> System.out.println("Rules");//TODO: write the rules
                case 3 -> keepGoing = false;
            }
        }

        System.out.println("Thanks for playing!");
    }


    public static boolean playGame() {
        Scanner reader = new Scanner(System.in);
        int startingBal = 100;


        Deck deck = new Deck();
        Kitty kitty = new Kitty();
        Card[] discardPile = new Card[0];

        System.out.println("What is your name?");

        PokerPlayer user = new PokerPlayer(reader.nextLine(), 5, startingBal);

        ComputerPokerPlayer computer = new ComputerPokerPlayer("Computer", 5, startingBal);


        //Start doing rounds

        int roundNumber = 1;//the round number of the game
        boolean keepGoing = true;

        while (keepGoing) {
            boolean roundEnd = false;

            System.out.println("\n\n\nRound " + roundNumber + "\n\n");

            //set the ante, take it from the players

            int ante = Rules.getAnte(roundNumber, startingBal);
            System.out.println("The ante is now " + ante + ".");
            kitty.update(user.deduct(ante));
            kitty.update(computer.deduct(ante));

            //shuffle and deal the deck

            deck.shuffle();

            for (int i = 0; i < 5; i++) {
                user.setCard(deck.deal());
                computer.setCard(deck.deal());
            }

            user.sortCards();
            computer.sortCards();

            System.out.println("Your hand:\n");
            System.out.println(user.showHand());


            //do round of betting


            int turnNum = roundNumber%2;
            int userBet = 0;
            int computerBet = 0;

            while (true) {

                System.out.println();


                if (turnNum % 2 == 1) {//TODO: write the special cases
                    if (computer.getMoney() == 0) {

                        System.out.println("The computer has gone all in!");
                        System.out.println("It's your turn to bet. You have " + user.getMoney() + " left. What would you like to do?");
                        System.out.println("1: Call");
                        System.out.println("2: Fold");


                        String choice = reader.nextLine();

                        while (!(choice.equals("1") || choice.equals("2"))) {
                            System.out.println("You must enter 1, 2, 3, or 4.");
                            choice = reader.nextLine();
                        }

                        if(choice.equals("1")){
                            System.out.println("You called for " + computerBet);
                            user.deduct(computerBet - userBet);
                            userBet = computerBet;
                        }else{
                            System.out.println("You folded!");
                            kitty.update(userBet);
                            userBet = -1;
                        }

                    } else if (computerBet > userBet + user.getMoney()) {
                        System.out.println("The computer has bet more than you have!");
                        System.out.println("It's your turn to bet. You have " + user.getMoney() + "left. What would you like to do?");
                        System.out.println("1: Go all in");
                        System.out.println("2: Fold");

                        String choice = reader.nextLine();

                        while (!(choice.equals("1") || choice.equals("2"))) {
                            System.out.println("You must enter 1 or 2");
                            choice = reader.nextLine();
                        }


                        if (choice.equals("1")) {
                            System.out.println("You went all in for " + (user.getMoney() + userBet) + ".");
                            userBet += user.getMoney();
                            user.deduct(user.getMoney());
                        } else {
                            System.out.println("You folded!");
                            kitty.update(userBet);
                            userBet = -1;
                        }

                    } else {
                        String word;
                        if (userBet == 0 && computerBet == 0) word = "Check";
                        else word = "Call";
                        System.out.println("It's your turn to bet. You have " + user.getMoney() + " left. What would you like to do?");
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
                                user.deduct(computerBet - userBet);
                                userBet = computerBet;
                            }
                            case "2" -> {
                                System.out.println("How much would you like to raise to?");
                                int bet = reader.nextInt();
                                while (bet < userBet || bet > (user.getMoney() + userBet) || bet < computerBet) {
                                    if (bet < computerBet) System.out.println("You must bet more than the computer!");
                                    else if (bet < userBet)
                                        System.out.println("You must bet more than you already bet!");
                                    else if (bet > (user.getMoney() + userBet))
                                        System.out.println("You can't bet more than you have!");

                                    bet = reader.nextInt();
                                }
                                System.out.println("You have raised to " + bet + ".");
                                user.deduct(bet - userBet);
                                userBet = bet;
                            }
                            case "3" -> {
                                System.out.println("You went all in for " + (user.getMoney() + userBet) + ".");
                                userBet += user.getMoney();
                                user.deduct(user.getMoney());
                            }
                            case "4" -> {
                                System.out.println("You folded!");
                                kitty.update(userBet);
                                userBet = -1;
                            }
                        }

                    }


                } else {

                    int bet = computer.bet(userBet, computerBet);

                    if(computerBet > userBet + user.getMoney()) computerBet = userBet + user.getMoney();

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

                }
                turnNum++;

                if (userBet == computerBet && turnNum > 2) break;
                else if (userBet == -1 || computerBet == -1) break;
            }

            if (computerBet == -1) {
                kitty.update(userBet);
                System.out.println("You win the round, and " + kitty.getValue() + ".");

                user.increase(kitty.payout());
                System.out.println("You now have " + user.getMoney() + ".");
                System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");

                roundEnd = true;
            } else if (userBet == -1) {
                kitty.update(computerBet);
                System.out.println(computer.getName() + " wins the round, and " + kitty.getValue() + ".");

                computer.increase(kitty.payout());
                System.out.println("You now have " + user.getMoney() + ".");
                System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");

                roundEnd = true;
            } else {
                kitty.update(userBet);
                kitty.update(computerBet);
            }

            if (!roundEnd) {
                //exchange cards
                System.out.println("Your hand:\n" + user.showHand());
                System.out.println("How many cards would you like to discard?");

                discardPile = new Card[10];
                int dIndex = 0;
                int total = reader.nextInt();

                while (total < 0 || total > 5) {
                    System.out.println("You can only discard between 0 and 5 cards.");

                    total = reader.nextInt();
                }


                int[] discard = new int[total];

                Arrays.fill(discard, -2);

                for (int i = 0; i < discard.length; i++) {

                    System.out.println("Which card would you like to discard? (-1 if done)");
                    discard[i] = reader.nextInt() - 1;

                    if(discard[i] == -2)break;

                    while (hasRepeat(discard) || discard[i] > 4 || discard[i] < 0) {
                        System.out.println("You must enter a number between 1 and 5, no repeats.");
                        discard[i] = reader.nextInt() - 1;

                        if(discard[i] == -2)break;
                    }
                    if(discard[i] == -2)break;
                }

                for (int j : discard) {
                    if (j == -2) break;

                    discardPile[dIndex] = user.discard(j);
                    dIndex++;
                }

                discard = computer.playHand();

                for (int j : discard) {

                    discardPile[dIndex] = computer.discard(j);
                    dIndex++;
                }

                user.fixCards();
                computer.fixCards();

                for(int i = 0; i < 5; i++){
                    if(user.hasRoom()) user.setCard(deck.deal());
                    if(computer.hasRoom()) computer.setCard(deck.deal());
                }

                user.sortCards();
                computer.sortCards();

                System.out.println("Your hand:\n");
                System.out.println(user.showHand());



                //do round of betting

                turnNum = roundNumber%2;
                userBet = 0;
                computerBet = 0;

                while (true) {

                    System.out.println();


                    if (turnNum % 2 == 1) {

                        //player turn to bet

                        if (computer.getMoney() == 0) {

                            //if the computer is all in, the user has fewer choices

                            System.out.println("The computer has gone all in!");
                            System.out.println("It's your turn to bet. You have " + user.getMoney() + "left. What would you like to do?");
                            System.out.println("1: Call");
                            System.out.println("2: Fold");


                            String choice = reader.nextLine();

                            while (!(choice.equals("1") || choice.equals("2"))) {
                                System.out.println("You must enter 1, 2, 3, or 4.");
                                choice = reader.nextLine();
                            }

                            if(choice.equals("1")){
                                System.out.println("You called for " + computerBet);
                                user.deduct(computerBet - userBet);
                                userBet = computerBet;
                            }else{
                                System.out.println("You folded!");
                                kitty.update(userBet);
                                userBet = -1;
                            }

                        } else if (computerBet > userBet + user.getMoney()) {

                            //if the computer bets more that the player has, the user has fewer choices

                            System.out.println("The computer has bet more than you have!");
                            System.out.println("It's your turn to bet. You have " + user.getMoney() + "left. What would you like to do?");
                            System.out.println("1: Go all in");
                            System.out.println("2: Fold");

                            String choice = reader.nextLine();

                            while (!(choice.equals("1") || choice.equals("2"))) {
                                System.out.println("You must enter 1 or 2");
                                choice = reader.nextLine();
                            }


                            if (choice.equals("1")) {
                                System.out.println("You went all in for " + (user.getMoney() + userBet) + ".");
                                userBet += user.getMoney();
                                user.deduct(user.getMoney());
                            } else {
                                System.out.println("You folded!");
                                kitty.update(userBet);
                                userBet = -1;
                            }

                        } else {

                            //default choice

                            String word;
                            if (userBet == 0 && computerBet == 0) word = "Check";
                            else word = "Call";
                            System.out.println("It's your turn to bet. You have " + user.getMoney() + " left. What would you like to do?");
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
                                    user.deduct(computerBet - userBet);
                                    userBet = computerBet;
                                }
                                case "2" -> {
                                    System.out.println("How much would you like to raise to?");
                                    int bet = reader.nextInt();
                                    while (bet < userBet || bet > (user.getMoney() + userBet) || bet < computerBet) {
                                        if (bet < computerBet)
                                            System.out.println("You must bet more than the computer!");
                                        else if (bet < userBet)
                                            System.out.println("You must bet more than you already bet!");
                                        else if (bet > (user.getMoney() + userBet))
                                            System.out.println("You can't bet more than you have!");

                                        bet = reader.nextInt();
                                    }
                                    System.out.println("You have raised to " + bet + ".");
                                    user.deduct(bet - userBet);
                                    userBet = bet;
                                }
                                case "3" -> {
                                    System.out.println("You went all in for " + (user.getMoney() + userBet) + ".");
                                    userBet += user.getMoney();
                                    user.deduct(user.getMoney());
                                }
                                case "4" -> {
                                    System.out.println("You folded!");
                                    kitty.update(userBet);
                                    userBet = -1;
                                }
                            }

                        }


                    } else {

                        //computer turn to bet

                        int bet = computer.bet(userBet, computerBet);

                        if(computerBet > userBet + user.getMoney()) computerBet = userBet + user.getMoney();

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

                    }
                    turnNum++;

                    if (userBet == computerBet && turnNum > 2) break;
                    else if (userBet == -1 || computerBet == -1) break;
                }

                if (computerBet == -1) {
                    kitty.update(userBet);
                    System.out.println("You win the round, and " + kitty.getValue() + ".");

                    user.increase(kitty.payout());
                    System.out.println("You now have " + user.getMoney() + ".");
                    System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");

                    roundEnd = true;
                } else if (userBet == -1) {
                    kitty.update(computerBet);
                    System.out.println(computer.getName() + " wins the round, and " + kitty.getValue() + ".");

                    computer.increase(kitty.payout());
                    System.out.println("You now have " + user.getMoney() + ".");
                    System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");

                    roundEnd = true;
                } else {
                    kitty.update(userBet);
                    kitty.update(computerBet);
                }

            }

            if (!roundEnd) {
                //reveal cards

                System.out.println("Your hand:\n" + user.showHand());
                System.out.println("You have a " + handToString(user.getHand()) + ".");
                System.out.println("\n" + computer.getName() + " hand:\n" + computer.showHand());
                System.out.println(computer.getName() + " has a " + handToString(computer.getHand()) + ".");

                if(Rules.breakTie(user.getHand(), computer.getHand()) == -1){
                    System.out.println("You win!");
                    user.increase(kitty.payout());

                    System.out.println("You now have " + user.getMoney() + ".");
                    System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");


                }else if(Rules.breakTie(user.getHand(), computer.getHand()) == 1){
                    System.out.println("You lose!");
                    computer.increase(kitty.payout());

                    System.out.println("You now have " + user.getMoney() + ".");
                    System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");

                }else{
                    System.out.println("You tied somehow.");
                    int payout = kitty.payout();

                    computer.increase(payout/2);
                    user.increase(payout/2);

                    System.out.println("You now have " + user.getMoney() + ".");
                    System.out.println(computer.getName() + " now has " + computer.getMoney() + ".");
                }
            }

            if (user.getMoney() < Rules.getAnte(roundNumber + 1, startingBal)) {
                System.out.println("You lost. :(");
                break;
            } else if (computer.getMoney() < Rules.getAnte(roundNumber + 1, startingBal)) {
                System.out.println("You win!");
                break;
            }

            deck.returnToDeck(user.discard());
            deck.returnToDeck(computer.discard());
            deck.returnToDeck(discardPile);



            roundNumber ++;
        }


        System.out.println("Good game! \nWould you like to play again?(Y/N)");
        String in = reader.nextLine();
        return (in.equals("Y") || in.equals("y"));
    }


    public static String handToString(Card[] hand) {

        //returns the string form of a given hand type

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

    public static boolean hasRepeat(int[] arr) {

        //searches an int array for repeats, breaks at -2

        for (int i = 0; i < arr.length; i++) {
            for (int k = i + 1; k < arr.length; k++) {
                if(arr[k] == -2) break;
                else if(arr[k] == arr[i]) return true;
            }
            if(arr[i] == -2)break;

        }
        return false;
    }
}