/**
 * This class is used to model a hand of FullHouse and is a sub-class of Hand. This class would define the abstract methods of Hand : isValid() and getType().
 * @author Ali Murtaza
 *
 */
public class FullHouse extends Hand {
	/**
	 * a constructor for building a FullHouse hand with the specified player and list of cards.
	 * @param player A specified player of type CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * The method is to get the top card of the hand
	 * @return Top card of the hand
	 */
	
	public Card getTopCard() {
		this.sort();		
		if (this.getCard(0).getRank() == this.getCard(2).getRank()) {
			return this.getCard(0);
		}
		else {
			return this.getCard(3);
		}
	}
	
	/**
	 * a method for checking if this is a valid Full House hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		this.sort();
		if(this.size()==5) {
			if((this.getCard(0).getRank()==this.getCard(2).getRank())) {
				if ((this.getCard(0).getRank()==this.getCard(1).getRank())&&(this.getCard(3).getRank()==this.getCard(4).getRank())) {
					return true;
				}
			}
			else if((this.getCard(2).getRank()==this.getCard(4).getRank())) {
				if ((this.getCard(2).getRank()==this.getCard(3).getRank())&&(this.getCard(0).getRank()==this.getCard(1).getRank())) {
					return true;
				}				
			}
			return false;			
		}
		return false;
	}

	@Override

	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Quad hand
	 */
	
	public String getType() {
		// TODO Auto-generated method stub
		return "FullHouse";
	}

}
