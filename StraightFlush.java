/**
 * This class is used to model a hand of Straight Flush and is a sub-class of Hand. This class would define the abstract methods of Hand : isValid() and getType().
 * @author Ali Murtaza
 *
 */
public class StraightFlush extends Hand {
	/**
	 * a constructor for building a Straight Flush hand with the specified player and list of cards.
	 * @param player A specified player of type CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid straight flush hand.
	 * @return true if valid, false otherwise
	 */
	@Override
	public boolean isValid() {
		this.sort();
		if (this.size()==5) {
			for(int i=0;i<4;i++) {
				if ((((this.getCard(i+1).getRank()+11)%13-(this.getCard(i).getRank()+11)%13)!=1) || (this.getCard(i).getSuit() != this.getCard(i+1).getSuit()) ) {
					return false;
				}
			}
			return true;
		}
		return false;
		
	}
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Straight flush hand
	 */
	@Override
	public String getType() {
		
		return "StraightFlush";
	}

}
