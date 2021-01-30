/**
 * This class is used to model a hand of Single and is a sub-class of Hand. This class would define the abstract methods of Hand : isValid() and getType().
 * @author Ali Murtaza
 *
 */
public class Single extends Hand {
	/**
	 * a constructor for building a Single hand with the specified player and list of cards.
	 * @param player A specified player of type CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid single hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		if (this.size()==1) {
			return true;
		}
		return false;
	}
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Single hand
	 */
	public String getType() {
		return "Single";
	}
	
	
}
