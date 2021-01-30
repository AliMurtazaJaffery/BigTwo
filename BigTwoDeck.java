/**
 * This class is used to represent a deck of cards in Big Two Game.
 * 
 * @author Ali Murtaza
 */
public class BigTwoDeck extends Deck {
	
	/**
	 * Initialize the deck of cards of Big Two Game.
	 */
	
	public void initialize() {
		removeAllCards();
		for (int i=0;i<4;i++) {
			for (int j=0;j<13;j++) {
				BigTwoCard bigTwoCard=new BigTwoCard(i,j);
				addCard(bigTwoCard);
			}
		}
	}
}
