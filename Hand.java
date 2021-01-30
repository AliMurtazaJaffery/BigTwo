/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards.
 * It has a private instance variable for storing the player who will play this hand.
 * It also has methods for getting the player of this hand, checking its validity,
 * getting the type and top card of this hand, and checking if it beats a specified hand.
 * 
 * @author Ali Murtaza
 */
public abstract class Hand extends CardList {
	private CardGamePlayer player;
	
	/**
	 * The constructor for building a hand with specified player and list of cards
	 * @param player
	 * 		The Player who plays the hand
	 * @param cards
	 * 		The cards to form the hand
	 */
	
	public Hand(CardGamePlayer player, CardList cards) {
		this.player=player;
		for (int i=0;i<cards.size();i++) {
			this.addCard(cards.getCard(i));
		}
	}
	
	/**
	 * The method to get the player of the hand
	 * @return Player who plays the hand
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	/**
	 * The method is to get the top card of the hand
	 * @return Top card of the hand
	 */
	
	public Card getTopCard() {
		this.sort();
		return this.getCard(this.size()-1);
	}
	/**
	 * a method for checking if this hand beats a specified hand.
	 * @param hand
	 * 			 A hand to check with
	 * @return true if beats, false otherwise
	 */
	public boolean beats(Hand hand) {
		
		if( hand.size() == 1 || hand.size() == 2 || hand.size() == 3){
			if(this.isValid() && this.size() == hand.size() && this.getTopCard().compareTo(hand.getTopCard()) == -1){ 
				return false;
			}
			else if (this.isValid() && this.size() == hand.size() && this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
		}
		if (hand.size() == 5 && this.size() == 5) {
			// when we have same types of hands
			if (this.getType() == hand.getType()) {				
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {				
					return true;		
				}
				else {
					return false;
				}
			}
			//when the hands are different.
			//Straight Flush is at the top so it will always win against other hands consisting of five cards
			else if (this.getType() == "StraightFlush") {	
				return true;		
			}			
			else if (this.getType() == "Quad") {				
				if (hand.getType() == "StraightFlush") {
					return false;
				}
				else {
					return true;
				}
			}			
			else if (this.getType() == "FullHouse") {					
				if (hand.getType() == "StraightFlush" || hand.getType() == "Quad") {					
					return false;					
				}					
				else {
					return true;
				}
			}			
			else if (this.getType() == "Flush") {				
				if (hand.getType() == "FullHouse" || hand.getType() == "Quad" || hand.getType() == "StraightFlush") {
					return false;
				}
				else {
					return true;
				}		
			}
			//this is the weakest hand consisting of five cards
			else if (this.getType() == "Straight") {				
				return false;				
			}
		}
		
		return false;
		
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * @return true if valid, false otherwise
	 */
	
	public abstract boolean isValid();
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand
	 */
	
	public abstract String getType() ;
}
