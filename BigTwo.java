import java.util.ArrayList;


/**
 * The BigTwo class is used to model a Big Two card game.
 * 
 * @author Ali Murtaza
 *
 */

public class BigTwo implements CardGame{
	
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int currentIdx;
	private BigTwoTable table;
	private int previousPlayer=-1;
	
	/**
	 * a constructor for creating a Big Two card game. Creates 4 players and adds them to the player list. Also creates a table (i.e., a BigTwoTable object) which builds the GUI for the game and handles users actions.
	 */
	BigTwo(){
		playerList = new ArrayList<CardGamePlayer>();
		for (int i = 0; i < 4; i++) {
			this.playerList.add(new CardGamePlayer());
		}
		handsOnTable = new ArrayList<Hand>();
		table=new BigTwoTable(this);


	}
	
	/**
	 * Returns the number of players in this card game.
	 * 
	 * @return the number of players in this card game
	 */
	
	public int getNumOfPlayers() {
		return playerList.size();
	}
	
	/**
	 * a method for retrieving the deck of cards being used.
	 * @return a Deck object with the cards being used
	 */
	public Deck getDeck() {
		return this.deck;
	}
	
	/**
	 * a method for retrieving the list of players.
	 * @return A java ArrayList of type CardGamePlayer with current list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return this.playerList;
	}
	
	/**
	 * a method for retrieving the list of hands played on the table.
	 * @return A java ArrayList of type Hand with all hands that have been played
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return this.handsOnTable;
	}
	
	/**
	 * a method for retrieving the index of the current player.
	 * @return index (integer) of the current active player.
	 */
	public int getCurrentIdx() {
		return this.currentIdx;
	}
	
	/**
	 * a method for starting the game with a (shuffled) deck of 
	 * cards supplied as the argument. It implements the Big Two game logics.
	 * @param deck A deck of cards to distribute and start with
	 */
	public void start(Deck deck) {
		handsOnTable.clear();
		table.reset();
		//to remove cards from players hands
		for(int i = 0; i < 4; i++) {
			this.getPlayerList().get(i).removeAllCards();
		}
		//distributing cards to the players
		for (int i = 0; i < deck.size(); i++) {
			this.getPlayerList().get(i%4).addCard(deck.getCard(i));
			if (deck.getCard(i).getRank() == 2 && deck.getCard(i).getSuit() == 0) {
				currentIdx = i % 4;
				table.setActivePlayer(currentIdx);
			}
		}
		// Sorting the cards for the players
	    for (int i = 0; i < 4; i++ ) {
	    	playerList.get(i).getCardsInHand().sort();
		}	  	
		table.repaint();
		table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"'s turn:");
		
		
		
	}
	
	/**
	 * Makes a move by the player.
	 * 
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	
	public void makeMove(int playerID,int[] cardIdx) {
		checkMove(playerID,cardIdx);
	}
	
	/**
	 * Checks a move made by the player.
	 * 
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	
	public void checkMove(int playerID, int[] cardIdx) {
		Hand playerAttempt;
		//cards played by the player
		CardList cardList = new CardList();
		boolean firstTurn=handsOnTable.isEmpty();
			
		if (cardIdx==null) {
			//need to check if he is not the first player or the player who had the last turn
			if ((firstTurn==false) &&(playerID!=previousPlayer)) {
				currentIdx=(playerID+1)%4;
				table.setActivePlayer(currentIdx);
				table.printMsg("{Pass}");
				table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"'s turn:");
			}
			else {
				table.printMsg("{Pass} <== Not a legal move!!!");
				return;
			}
		}
		else {
			cardList=playerList.get(playerID).play(cardIdx);
			playerAttempt=composeHand(this.getPlayerList().get(playerID),cardList);
			if (playerAttempt==null) {
				table.printMsg(cardList+"<==Not a legal move!!!");
				return;					
			}
			else if (firstTurn && !playerAttempt.contains(new BigTwoCard(0,2))) {
				table.printMsg(playerAttempt+"<== Not a legal move!!!");
		    	return;
		    }
				//if someone is not a first turn player, nor it was his last turn and If the person is not able to beat or if the sizes are not equal, then this if statement will hold true.
			else if ((!firstTurn && playerAttempt!=null && playerID != previousPlayer) ? (!(playerAttempt.beats(this.getHandsOnTable().get(this.getHandsOnTable().size() - 1))) || playerAttempt.size() != this.getHandsOnTable().get(this.getHandsOnTable().size() - 1).size()) : false) {
				table.printMsg(playerAttempt+"<== Not a legal move!!!");
		    	return;
		    }
		    else {
		    	this.getPlayerList().get(playerID).removeCards(cardList);
				playerAttempt.sort();
				this.getHandsOnTable().add(playerAttempt);
				previousPlayer = playerID;
				table.printMsg("{"+playerAttempt.getType()+"} "+playerAttempt);
				System.out.println("");
				currentIdx = (playerID + 1) % 4;
				table.setActivePlayer(currentIdx);
				table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"'s turn:");
			}
		}
		table.resetSelected();
		table.repaint();		

		if (endOfGame()) {
			table.printMsg("Game ends");
			for (int i = 0; i < 4; i++) {
				if (this.getPlayerList().get(i).getNumOfCards() == 0) {
					table.printMsg(this.getPlayerList().get(i).getName() + " wins the game.");
				} else {
					table.printMsg(this.getPlayerList().get(i).getName() + " has " + this.getPlayerList().get(i).getNumOfCards() + " cards in hand.");
				}
			}
			table.disable();
		}
	}
		

	
	/**
	 * Checks for end of game.
	 * 
	 * @return true if the game ends; false otherwise
	 */
	public boolean endOfGame() {
		for (int i = 0; i < 4; i++) {
			if (this.getPlayerList().get(i).getCardsInHand().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * a method for returning a valid hand from the specified list of cards of the player. Returns null is no valid hand can be composed from the specified list of cards.
	 * @param player 
	 * 				A player is of type CardGamePlayer to associate the cards with
	 * @param cards
	 * 				cards is a CardList to check for a valid hand
	 * @return a valid hand if it exists or null
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Hand straightFlush = new StraightFlush(player,cards);
		if (straightFlush.isValid()) {
			return straightFlush;
		}
		Hand quad = new Quad(player,cards);
		if (quad.isValid()) {
			return quad;
		}

		Hand fullHouse = new FullHouse(player,cards);
		if (fullHouse.isValid()) {
			return fullHouse;
		}
		Hand flush = new Flush(player,cards);
		if (flush.isValid()) {
			return flush;
		}
		Hand straight = new Straight(player,cards);
		if (straight.isValid()) {
			return straight;
		}
		Hand triple = new Triple(player,cards);
		if (triple.isValid()) {
			return triple;
		}
		Hand pair = new Pair(player,cards);
		if (pair.isValid()) {
			return pair;
		}
		Hand single = new Single(player,cards);
		if (single.isValid()) {
			return single;
		}
		return null;

	}
	
	/**
	 * a method for starting a Big Two card game. It should create a Big Two card game, create and shuffle a deck of cards, and start the game with the deck of cards.
	 * @param args Arguments for the main function
	 */
	public static void main(String[] args)
	{
		BigTwo game = new BigTwo();
		BigTwoDeck deck = new BigTwoDeck();
		deck.shuffle(); 
		game.start(deck);
	}


	
}
