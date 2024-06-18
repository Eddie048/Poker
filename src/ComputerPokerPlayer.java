public class ComputerPokerPlayer extends PokerPlayer{

    private final Strategy strategy;

    public ComputerPokerPlayer(String n, int max, int m){
        super(n, max, m);

        int type = (int)(Math.random()*11);

        if(type < 5) type = 1;
        else if(type < 10) type = 2;
        else type = 3;

        strategy = new Strategy(type);

    }

    public int bet(int currentBet, int myCurrentBet){
        //uses the strategy to find a bet
        return(strategy.bet(currentBet, this.getMoney() + myCurrentBet, this.getHand()));
    }

    public int[] playHand(){

        //uses the strategy to return the discarded cards
        return strategy.playHand(this.getHand());
    }

    public String toString(){
        return super.toString() + "\nStrategy: " + strategy;
    }
}