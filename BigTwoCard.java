/**
 * This class is used for representing a card in Big Two card game.
 * 
 * @author Ali Murtaza
 */
public class BigTwoCard extends Card {
	
	/**
	 * The constructor to build a card with a specified suit and rank.
	 * @param suit
	 * 			Integer value between 0 and 3 represent the suit of the card
	 * @param rank
	 * 			Integer value between 0 and 12 represent the rank of the card
	 */
	
	public BigTwoCard(int suit, int rank) {
		super(suit,rank);
	}
	
	/**
	 * Compares this card with the specified card for order for Big Two Game
	 * 
	 * @param card
	 *            the card to be compared
	 * @return a negative integer, zero, or a positive integer if this card is
	 *         less than, equal to, or greater than the specified card respectively
	 */
	
	public int compareTo(Card card) {
		if (((this.rank-2)+13)%13>((card.rank-2)+13)%13) {
			return 1;
	    } else if (((this.rank-2)+13)%13<((card.rank-2)+13)%13) {
	    	return -1;	
	    } else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}
