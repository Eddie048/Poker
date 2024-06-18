public class PokerPlayer extends Player{
    private int money;


    public PokerPlayer(String n, int max, int m){
        super(n, max);
        money = m;
    }

    public boolean canCoverBet(int amount){
        return money >= amount;
    }

    public int deduct(int amount){
        if(canCoverBet(amount)){
            money -= amount;
            return amount;
        }else return -1;
    }

    public void increase(int amount){
        money += amount;
    }

    public int getMoney() {
        return money;
    }

    public String toString(){
        return super.toString() + "\nMoney: " + money;
    }
}