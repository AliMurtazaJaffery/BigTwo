/**
 * This class is used to model a hand of Straight and is a sub-class of Hand. This class would define the abstract methods of Hand : isValid() and getType().
 * @author Ali Murtaza
 *
 */
public class Straight extends Hand {
	/**
	 * a constructor for building a Straight hand with the specified player and list of cards.
	 * @param player A specified player of type CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */

	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);

	}

	@Override
	/**
	 * a method for checking if this is a valid straight hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		// TODO Auto-generated method stub
		this.sort();
		if(this.size()==5) {
			for (int i=0;i<this.size()-1;i++) {
				if (((this.getCard(i+1).getRank()+11)%13-(this.getCard(i).getRank()+11)%13 != 1)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Straight hand
	 */
	
	public String getType() {
		// TODO Auto-generated method stub
		return "Straight";
	}

}
