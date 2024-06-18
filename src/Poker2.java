// This file is the beginning of implementing a multi player game
/*//remove this
import java.util.Arrays;
import java.util.Scanner;

public class Poker2 {


    public static void main(String[] args) {


        Scanner reader = new Scanner(System.in);

        boolean keepGoing = true;

        while(keepGoing) {
            System.out.println("Welcome to 5 card draw poker!\n" +
                    "1: Play the game\n" +
                    "2: Read the rules\n" +
                    "3: Exit");


            int choice = reader.nextInt();

            switch (choice) {
                case 1:
                    keepGoing = playGame();
                    break;
                case 2:
                    System.out.println("Rules");//TODO: write the rules
                    break;
                case 3:
                    keepGoing = false;
                    break;
                default:

            }
        }

        System.out.println("Thanks for playing!");
    }


    public static boolean playGame(){
        Scanner reader = new Scanner(System.in);
        double startingBal = 100.0;

        Deck deck = new Deck();
        Kitty kitty = new Kitty();

        System.out.println("What is your name?");

        PokerPlayer user = new PokerPlayer(reader.nextLine(), 5, startingBal);


        System.out.println("How many other players are there? (Maximum of 4)");

        int playerCnt = reader.nextInt();
        if(playerCnt > 4){
            System.out.println("The maximum is 4 other players! (It has been set to 4)");
            playerCnt = 4;
        }else if(playerCnt < 1){
            System.out.println("You must have at least one other player! (It has been set to one}");
            playerCnt = 1;
        }

        ComputerPokerPlayer[] players = new ComputerPokerPlayer[playerCnt];

        for(int i = 0; i < playerCnt; i++){
            players[i] = new ComputerPokerPlayer("Player " + (i + 1), 5, startingBal);
        }

        //Start doing rounds

        int roundNumber = 1;//the round number of the game



        while(true){

            //get ante, then take it from players and remove players who can't pay

            double ante = Rules.getAnte(roundNumber, startingBal);
            System.out.println("The ante is now " + ante);

            int playersStillIn = playerCnt;//the number of players who haven't folded
            double[] playerBets = new double[playersStillIn + 1];

            if(user.deduct(ante) == -1){
                System.out.println("You can't cover the ante! You lose :(");
                break;
            }else{
                System.out.println("You now have $" + user.getMoney()  + " left.");
                kitty.update(ante);

            }

            for(int i = 0; i < playerCnt; i++){
                if(players[i].deduct(ante) == -1){
                    System.out.println(players[i].getName() + " has been eliminated, they cannot cover the ante.");

                    kitty.update(players[i].getMoney());

                    //put the eliminated player at the end of the array
                    ComputerPokerPlayer temp = players[i];
                    players[i] = players[playerCnt-1];
                    players[playerCnt-1] = temp;

                    playerCnt --;

                    i--;
                    System.out.println("Playercout: " + playerCnt);

                }else{
                    kitty.update(ante);
                }
            }

            if(playerCnt == 0){
                System.out.println("You win!");
                break;
            }

            System.out.println("The kitty now has $" + kitty.getValue());

            //shuffle the deck and deal the cards

            deck.shuffle();

            for(int i = 0; i < 5; i++){
                user.setCard(deck.deal());
                for(int k = 0; k < playerCnt; k++){
                    players[k].setCard(deck.deal());
                }
            }
            user.fixCards();
            System.out.println("\nYour hand:\n" + user.showHand() );

            //do a round of betting
            boolean isUserIn = true;//whether or not the player has folded
            boolean isAllIn = false;//if someone is all in
            double currentBet = 0;
            int turnNum = (roundNumber - 1)%(playersStillIn);
            boolean keepGoing = true;//if the bets are not all equal, so we should keep running
            boolean allHadTurn = false;// if everyone has had a turn to bet


            while(keepGoing){//while all the bets are not equal or everyone hasn't folded
                System.out.println(turnNum);


                if(turnNum == 0 && isUserIn){

                    //if(playerBets[turnNum] == 0)

                    //player turn to bet
                    System.out.println("\nIt's your turn to bet!");
                    System.out.println("You have $" + user.getMoney() + " left.");

                    if(currentBet > 0) System.out.println("You must bet at least " + currentBet + " to stay in.");

                    if(playerBets[turnNum] == 0) System.out.println("How much would you like to bet?");
                    else System.out.println("You already have bet " + playerBets[turnNum] + ". How much would you like to add?");

                    System.out.println("Enter -1 to fold, and all of your money to go all in.");


                    reader.nextLine();
                    double bet = reader.nextDouble();
                    if(bet != -1) bet += playerBets[turnNum];

                    /*String word;
                    if(playerBets[turnNum] == 0) word = "bet";
                    else word = "add";*/ /*//remove this


                    while(bet > user.getMoney() || (bet < currentBet && bet != -1 ) || (bet > currentBet && isAllIn)){

                        if(bet > user.getMoney()) System.out.println("You can't bet more than you have.");
                        if(bet < currentBet && bet != -1) System.out.println("You have to bet at least " + currentBet + ".");
                        if(bet > currentBet && isAllIn) System.out.println("You can't raise if someone is all in!");//for the special rule
                        System.out.println("What would you like to bet?");

                        bet = reader.nextDouble();
                    }

                    if(bet == -1){
                        System.out.println("You folded!");
                        isUserIn = false;
                        kitty.update(playerBets[turnNum]);
                        //user.deduct(playerBets[turnNum]);
                        playerBets[turnNum] = -1;
                        System.out.println("The kitty now has " + kitty.getValue());

                    }else if(bet == user.getMoney()){
                        System.out.println("You went all in for $" + bet + ".");
                        isAllIn = true;

                        user.deduct(bet - playerBets[turnNum]);
                        playerBets[turnNum] = bet;
                        currentBet = bet;

                    }else if(bet == currentBet){
                        System.out.println("You have called for $" + bet + ".");

                        user.deduct(bet - playerBets[turnNum]);
                        playerBets[turnNum] = bet;

                    }else{
                        System.out.println("You have raised to $" + bet + ".");

                        user.deduct(bet - playerBets[turnNum]);
                        playerBets[turnNum] = bet;
                        currentBet = bet;
                    }

                }else if(playerBets[turnNum] != -1){ //TODO: finish the computer betting thing
                    // computer turn to bet
                    double bet = players[turnNum - 1].bet(currentBet);

                    if(bet > players[turnNum - 1].getMoney()) bet = players[turnNum-1].getMoney();
                    if(bet < currentBet && bet != -1) bet = -1;
                    if(bet > currentBet && isAllIn) bet = currentBet;//for the special rule


                    if(bet == -1){
                        System.out.println(players[turnNum-1].getName() + " folded!");

                        //remove the player from the running


                        kitty.update(playerBets[turnNum]);
                        playerBets[turnNum] = -1;
                        //players[turnNum - 1].deduct(playerBets[turnNum]);
                        System.out.println("The kitty now has " + kitty.getValue());

                    }else if(bet == user.getMoney()){
                        System.out.println(players[turnNum-1].getName() + " went all in for $" + bet + ".");
                        isAllIn = true;

                        players[turnNum-1].deduct(bet - playerBets[turnNum]);
                        playerBets[turnNum] = bet;
                        currentBet = bet;



                    }else if(bet == currentBet){
                        System.out.println(players[turnNum-1].getName() + " has called for $" + bet + ".");

                        players[turnNum-1].deduct(bet - playerBets[turnNum]);
                        playerBets[turnNum] = bet;


                    }else{
                        System.out.println(players[turnNum-1].getName() + " has raised to $" + bet + ".");

                        players[turnNum-1].deduct(bet - playerBets[turnNum]);
                        playerBets[turnNum] = bet;
                        currentBet = bet;
                    }
                }

                turnNum ++;
                if(turnNum > playersStillIn){
                    System.out.println("One round passed");
                    if(isUserIn) turnNum = 0;
                    else turnNum = 1;
                    allHadTurn = true;

                }

                System.out.println(Arrays.toString(playerBets));

                if(allHadTurn) keepGoing = areAllEqual(playerBets);


            }

            for(int i = 0; i < playerBets.length; i++){

                if(playerBets[i] > 0){
                    kitty.update(playerBets[i]);
                    playerBets[i] = 0;
                }

            }

            if(playersStillIn == 1)
                //TODO: add all the player bets to the kitty

                //TODO: write the discarding

                //TODO: write the second betting round








                roundNumber ++;
            if(playerCnt == 0){
                System.out.println("You win!");
                break;
            }
        }

        System.out.println("Good game! \nWould you like to play again?(Y/N)");
        reader.nextLine();
        String in = reader.nextLine();
        return(in.equals("Y") || in.equals("y"));
    }


    public static String handToString(Card[] hand){
        switch(Rules.scoreCards(hand)){
            case 10: return "royal flush";
            case 9: return "straight flush";
            case 8: return "four of a kind";
            case 7: return "full house";
            case 6: return "flush";
            case 5: return "straight";
            case 4: return "three of a kind";
            case 3: return "two pair";
            case 2: return "pair";
            default: return "high card";
        }
    }

    public static boolean areAllEqual(double[] bets){
        double x = -2;
        for(int i = 0; i < bets.length; i++){
            if(x > -2 && bets[i] > -1 && bets[i] != x) return true;
            if(bets[i] > -1) x = bets[i];
        }
        return false;
    }
}*/