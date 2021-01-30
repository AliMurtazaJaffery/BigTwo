/**
 * This class is used to model a hand of Quad and is a sub-class of Hand. This class would define the abstract methods of Hand : isValid() and getType().
 * @author Ali Murtaza
 *
 */
public class Quad extends Hand {
	/**
	 * a constructor for building a Quad hand with the specified player and list of cards.
	 * @param player A specified player of type CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	/**
	 * The method is to get the top card of the hand
	 * @return
	 * 		Top card of the hand
	 */
	public Card getTopCard() {
		this.sort();
		//assuming the different rank card is at last position
		if (this.getCard(0).getRank()==this.getCard(1).getRank()) {
			return this.getCard(3);
		}
		//assuming the different rank card is at first position
		else {
			return this.getCard(4);
		}
	}
	
	/**
	 * a method for checking if this is a valid Quad hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		this.sort();
		if (this.size()==5) {
			if (this.getCard(0).getRank()==this.getCard(1).getRank() && this.getCard(0).getRank()==this.getCard(2).getRank()) {
				if(this.getCard(0).getRank()==this.getCard(3).getRank()) {
					return true;
				}
			}
			else if (this.getCard(1).getRank()==this.getCard(2).getRank() && this.getCard(1).getRank()==this.getCard(3).getRank()) {
				if(this.getCard(1).getRank()==this.getCard(4).getRank()) {
					return true;
				}
			}
			return false;
		}
		return false;
	}


	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Quad hand
	 */
	
	public String getType() {
		// TODO Auto-generated method stub
		return "Quad";
	}

}
