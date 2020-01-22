public class Strategy {
    private final int STRATEGY_TYPE;

    public Strategy(int t){
        STRATEGY_TYPE = t;
        //1 is safe
        //2 is agressive
        //3 is very strange (only goes for royal flushes)

    }

    public int bet(int currentBet, int money, Card[] hand){

        int bet;

        //bets differently depending on the strategy type
        switch (STRATEGY_TYPE){
            case 1:
                switch(Rules.scoreCards(hand)){
                    case 10:
                    case 9:
                    case 8:
                    case 7:
                        bet = money;
                        break;
                    case 6:
                    case 5:
                        bet = (int)Math.max(money/2, currentBet*1.5);
                        break;
                    case 4:
                    case 3:
                    case 2:
                        if(currentBet < money) bet = money / 4;
                        else bet = 0;
                        break;
                    default:
                        if(currentBet > money/8) bet = -1;
                        else bet = currentBet;

                }
                break;
            case 2:
                switch ((Rules.scoreCards(hand))){
                    case 10:
                    case 9:
                    case 8:
                    case 7:
                    case 6:
                    case 5:
                        bet = money;
                        break;
                    case 4:
                    case 3:
                        bet = currentBet*2;
                        break;
                    case 2:
                        if(currentBet < money/6) bet = money/6;
                        else if(currentBet < money/2) bet = currentBet;
                        else bet = -1;
                    default:
                        if(currentBet < money/10) bet = money/6;
                        else if(currentBet < money/6) bet = currentBet;
                        else bet = -1;
                }
                break;
            case 3:
                if(Rules.scoreCards(hand) == 10)bet = money;
                else bet = 0;
                break;
            default: bet = 0; break;
        }

        //make sure the bet is valid
        if(bet < currentBet) return -1;
        else if(bet >= money) return money;
        else return bet;
    }

    public int[] playHand(Card[] hand){
        Deck.sortCards(hand);

        switch(Rules.scoreCards(hand)) {
            case 10:
            case 9:
            case 8:
            case 7:
            case 6:
            case 5:
                return new int[]{};
            case 4:
                if (hand[0].getValue() == hand[2].getValue()) return new int[]{};
                else if (hand[1].getValue() == hand[3].getValue()) return new int[]{0};
                else return new int[]{0};
            case 3:

                if (hand[0].getValue() != hand[1].getValue()) return new int[]{0};
                else if (hand[3].getValue() != hand[4].getValue()) return new int[]{4};
                else return new int[]{2};
            case 2:
                int indexOfPair = 0;
                for (int i = 0; i < hand.length - 1; i++) {
                    if (hand[i].getValue() == hand[i + 1].getValue()) {
                        indexOfPair = i;
                        break;
                    }
                }
                switch (indexOfPair) {
                    case 0:
                        return new int[]{2, 3};
                    case 1:
                        return new int[]{0, 3};
                    case 2:
                        return new int[]{0, 1};
                    default:
                        return new int[]{0, 1, 2};
                }
            default:
                return new int[]{0, 1, 2};
        }
    }
}