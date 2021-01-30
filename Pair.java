/**
 * This class is used to model a hand of Pair and is a sub-class of Hand. This class would define the abstract methods of Hand : isValid() and getType().
 * @author Ali Murtaza
 *
 */
public class Pair extends Hand {
	/**
	 * a constructor for building a Pair hand with the specified player and list of cards.
	 * @param player A specified player of type CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);

	}
	/**
	 * a method for checking if this is a valid Pair hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		if (this.size()==2) { 
			if (this.getCard(0).rank==this.getCard(1).rank)  {
				return true;
			}
			return false;
		}
		return false;
	}
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Pair hand
	 */
	
	public String getType() {
		return "Pair";
	}
	
}
