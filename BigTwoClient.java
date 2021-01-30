import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * The BigTwoClient class is used to model a Big Two client.
 * 
 * @author Ali Murtaza
 *
 */

public class BigTwoClient implements CardGame, NetworkGame {
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos; 
	private int currentIdx;
	private BigTwoTable table;
	private int previousPlayer=-1;
	
	/**
	 * a constructor for creating a Big Two client. Creates 4 players and adds them to the player list. Also creates a table (i.e., a BigTwoTable object) which builds the GUI for the game and handles users actions.Then it sets up a connection to the server.
	 */
	
	public BigTwoClient() {
		playerList = new ArrayList<CardGamePlayer>();
		for (int i = 0; i < 4; i++) {
			playerList.add(new CardGamePlayer());
		}
		numOfPlayers = playerList.size();
		handsOnTable = new ArrayList<Hand>();
		table=new BigTwoTable(this);
		
		playerName= JOptionPane.showInputDialog("Please enter your name:");

		if (playerName!=null) {
			this.setServerIP("127.0.0.1");
			this.setServerPort(2396);
			//this.playerName=name;
			makeConnection();
			table.disable();
		}
		else {
			table.printMsg("Please enter a valid name in the input field!");
			table.disable();
		}
	}

	/**
	 * returns playerID of the local player
	 * @return the int value of the playerID of the local player
	 */
	public int getPlayerID() {
		return this.playerID;
	}

	/**
	 * sets playerID of the local player
	 * @param playerID the value of playerID of local player
	 */
	public void setPlayerID(int playerID) {
		this.playerID=playerID;

	}

	/**
	 * returns name of the local player
	 * @return the String value of the name of the local player
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * sets name of the local player
	 * @param playerName the name of the local player
	 */
	public void setPlayerName(String playerName) {
		this.playerName=playerName;

	}
	
	/**
	 * returns the ip address of the server
	 * @return ip address of the server
	 */
	public String getServerIP() {
		return this.serverIP;
	}

	/**
	 * sets the IP address of the server
	 * @param serverIP the IP address of the server
	 */
	public void setServerIP(String serverIP) {
		this.serverIP=serverIP;

	}
	
	/**
	 * for getting the server port
	 * @return the value of TCP port 
	 */
	public int getServerPort() {
		return this.serverPort;
	}

	/**
	 * for setting the TCP port
	 * @param serverPort the value of the TCP port
	 */
	public void setServerPort(int serverPort) {
		this.serverPort=serverPort;

	}
	/**
	 * for checking connection to the server
	 * @return it returns true if the client is connected to the server
	 */
	public boolean checkConnection() {
		if (sock.isClosed()) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * for connecting to the server
	 */
	public void makeConnection() {
		if (sock==null || sock.isClosed()) {
			try {
				sock = new Socket(this.getServerIP(),this.getServerPort());
				oos=new ObjectOutputStream(sock.getOutputStream());
				
				Runnable job = new ServerHandler();
				Thread thread = new Thread(job);
				thread.start();
				} 
			catch (Exception ex) { 
				ex.printStackTrace(); 
			}
		}

	}

	/**
	 * a method for parsing the messages received from the game server. 
	 * @param message the message recieved from the server
	 */
	public void parseMessage(GameMessage message) {
		if (message.getType()==CardGameMessage.MOVE) {
			checkMove(message.getPlayerID(),(int[]) message.getData());
		}
		
		else if (message.getType()==CardGameMessage.PLAYER_LIST) {
			setPlayerID(message.getPlayerID());
			getPlayerList().get(playerID).setName(playerName);
			//getPlayerList().get(playerID).setName(name);
			for (int i=0;i<getNumOfPlayers();i++) {
				
				if (i==playerID) {
					continue;
				}
				if (((String[])message.getData())[i]!=null) {
					getPlayerList().get(i).setName(((String[]) message.getData())[i]);
				}
			}
			table.repaint();
			sendMessage(new CardGameMessage(CardGameMessage.JOIN,-1,playerName));
		}
		
		else if (message.getType()==CardGameMessage.JOIN) {
			getPlayerList().get(message.getPlayerID()).setName((String)(message.getData()));
			this.table.repaint();
			if (message.getPlayerID()==this.playerID) {
				sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));
			}	
		}
		
		else if(message.getType()==CardGameMessage.FULL) {
			table.printMsg("You cannot join the game as the server is full.");
			try {
				this.sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		else if (message.getType()==CardGameMessage.QUIT) {
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " left the game.\n");
			getPlayerList().get(message.getPlayerID()).setName("");
			
			if (endOfGame()==false) {
				table.disable();
				sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));
				
			}				
			table.repaint();		
		}
		else if (message.getType()==CardGameMessage.READY) {
			table.printMsg(getPlayerList().get(message.getPlayerID()).getName()+" is ready.");
		}
		
		else if (message.getType()==CardGameMessage.START) {
			deck=(Deck) message.getData();
			start(deck);		
		}
		else if (message.getType()==CardGameMessage.MSG) {
			table.printChatMsg((String) message.getData());
		}

	}

	/**
	 * for sending messages to the server
	 * @param message the message that needs to be sent
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		}
		catch (Exception ex) { 
			ex.printStackTrace();
			
		}	

	}
	
	/**
	 * The ServerHandler class is used to communicate with the server and implements the runnable method.
	 * 
	 * @author Ali Murtaza
	 *
	 */
	public class ServerHandler implements Runnable{
		private ObjectInputStream ois;
		
		/**
		 * This method parses the messages recieved from the server
		 */
		public void run() {
			try {
				ois=new ObjectInputStream(sock.getInputStream());
				CardGameMessage message;
				while (!sock.isClosed()) {
					message =(CardGameMessage) ois.readObject();
					
					if(message!=null) {
						parseMessage(message);
					}				
				}
				
				ois.close();
		
			}
			catch (Exception ex) { 
				ex.printStackTrace();
				
			}
			
			
		}
		
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
				table.setActivePlayer(playerID);
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
		sendMessage(new CardGameMessage(CardGameMessage.MOVE,-1,cardIdx));

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
			String displayMessage="";
			table.printMsg("Game ends \n");
			
			for (int i = 0; i < 4; i++) {
				if (this.getPlayerList().get(i).getNumOfCards() == 0) {
					displayMessage+=this.getPlayerList().get(i).getName() + " wins the game.\n";
				} else {
					displayMessage+=this.getPlayerList().get(i).getName() + " has " + this.getPlayerList().get(i).getNumOfCards() + " cards in hand.\n";
				}
			}
			table.disable();
			JOptionPane.showMessageDialog(null, displayMessage);
			sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));
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
		BigTwoClient game = new BigTwoClient();

	}

}
