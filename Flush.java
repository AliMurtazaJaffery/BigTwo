/**
 * This class is used to model a hand of Flush and is a sub-class of Hand. This class would define the abstract methods of Hand : isValid() and getType().
 * @author Ali Murtaza
 *
 */
public class Flush extends Hand {
	/**
	 * a constructor for building a Flush hand with the specified player and list of cards.
	 * @param player A specified player of type CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * a method for checking if this is a valid Flush hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() == 5) {
			for (int i = 0; i < 4; i++) {
				if (this.getCard(i).getSuit() != this.getCard(i+1).getSuit()) {
					return false;
				}
			}
			return true;
		} 
		return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Flush hand
	 */
	public String getType() {
		return "Flush";
	}

}
