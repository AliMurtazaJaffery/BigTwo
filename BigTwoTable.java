import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.text.DefaultCaret;


import java.util.*;



/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI for the Big Two card game and handle all user actions.
 * @author Ali Murtaza
 *
 */

public class BigTwoTable implements CardGameTable {
	
	private BigTwoClient game;
	
	private boolean[] selected;
	
	private int activePlayer;
	
	private JFrame frame;
	
	private JPanel bigTwoPanel;
	
	private JButton playButton;
	
	private JButton passButton;
	
	private JTextArea msgArea;
	
	private Image[][] cardImages;
	
	private Image cardBackImage;
	
	private Image[] avatars;
	
	private JTextArea chatArea;
	
	private JTextField chatField;
	

	
	
	/**
	 * a constructor for creating a BigTwoTable. The parameter game is a reference to a card game associates with this table.
	 * @param game A Card Game of BigTwo type to play through this GUI
	 */
	
	public BigTwoTable(CardGame game) {
		this.game=(BigTwoClient) game;
		loadImages();
		this.selected=new boolean[13];
		setupGUI();
		
	}
	
	/**
	 * a method to load all the images of the GUI
	 */
	
	public void loadImages() {
		
		avatars=new Image[4];
		
		Image batman=new ImageIcon("images/avatars/batman.png").getImage();
		avatars[0]=batman.getScaledInstance(65, 75, Image.SCALE_SMOOTH);
		
		Image pikachu=new ImageIcon("images/avatars/pikachu.png").getImage();
		avatars[1]=pikachu.getScaledInstance(65, 75, Image.SCALE_SMOOTH);
		
		Image goku=new ImageIcon("images/avatars/goku.png").getImage();
		avatars[2]=goku.getScaledInstance(65, 75, Image.SCALE_SMOOTH);
		
		Image popeye=new ImageIcon("images/avatars/popeye.png").getImage();
		avatars[3]=popeye.getScaledInstance(68, 75, Image.SCALE_SMOOTH);
		
		cardBackImage=new ImageIcon("images/cards/b.gif").getImage();
		
		
		char[] suit= {'d','c','h','s'};
		char[] rank= {'a','2','3','4','5','6','7','8','9','t','j','q','k'};
		
		cardImages=new Image[4][13];
		
		for (int i=0; i<4 ; i++) {
			for (int j=0;j<13;j++) {
				cardImages[i][j]=new ImageIcon("images/cards/"+rank[j]+suit[i]+".gif").getImage();
			}
		}
	}
	/**
	 * A method to setup the GUI
	 */
	public void setupGUI() {
		frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize (1000,900);
		
		JMenuBar menuBar=new JMenuBar();
		
		JMenu gameMenu=new JMenu("Game");
		
		JMenuItem quitMenuItem=new JMenuItem("Quit");
		quitMenuItem.addActionListener(new QuitMenuItemListener());	
		
		JMenuItem connectMenuItem=new JMenuItem("Connect");
		connectMenuItem.addActionListener(new ConnectMenuItemListener());
		
		gameMenu.add(connectMenuItem);
		gameMenu.add(quitMenuItem);
		menuBar.add(gameMenu);
		
		frame.setJMenuBar(menuBar);
		
		bigTwoPanel=new BigTwoPanel();
		bigTwoPanel.setPreferredSize(new Dimension(700,900));
		frame.add(bigTwoPanel,BorderLayout.CENTER);
		
		JPanel communicationPanel=new JPanel();
		communicationPanel.setLayout(new BoxLayout(communicationPanel, BoxLayout.PAGE_AXIS));
		communicationPanel.setPreferredSize(new Dimension(320,900));
		
		msgArea=new JTextArea(15,25);
		msgArea.setFont(new Font("SansSerif", Font.BOLD, 15));
		msgArea.setEnabled(false);
		
		
		
		//for automatic scrolling
	    DefaultCaret caret = (DefaultCaret) msgArea.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    
	    JScrollPane scrollPanel = new JScrollPane();	    
	    scrollPanel.setViewportView(msgArea);
	    
	    communicationPanel.add(scrollPanel);
	    
	    
		chatArea=new JTextArea(15,25);
		chatArea.setFont(new Font("SansSerif", Font.BOLD, 15));
		chatArea.setEnabled(false);
		
	    DefaultCaret chatCaret = (DefaultCaret) chatArea.getCaret();
	    chatCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    
	    JScrollPane chatScrollPanel = new JScrollPane();
	    chatScrollPanel.setViewportView(chatArea);
	    
	    communicationPanel.add(chatScrollPanel);
		
	    JPanel chatSendPanel=new JPanel(); 
	    chatSendPanel.setLayout(new FlowLayout());
	    chatSendPanel.add(new JLabel("Message:"));
		// sets up a text field for getting use inputs
		chatField = new JTextField();
		chatField.addActionListener(new EnterListener());
	    chatField.setPreferredSize( new Dimension( 200, 25 ) );
		chatSendPanel.add(chatField);
		
		communicationPanel.add(chatSendPanel);

		
		frame.add(communicationPanel,BorderLayout.EAST);
		
		
		JPanel buttonPanel=new JPanel();
		
		playButton=new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
		
		passButton=new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		
		buttonPanel.add(playButton);
		
		buttonPanel.add(Box.createHorizontalStrut(50));
		
		buttonPanel.add(passButton);
		
		frame.add(buttonPanel,BorderLayout.SOUTH);
		
		frame.setVisible(true);
				
		
	}
	
	
	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 */
	
	public void setActivePlayer(int activePlayer) {
		// TODO Auto-generated method stub
		this.activePlayer=activePlayer;
	}

	/**
	 * method for getting an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 */
	public int[] getSelected() {
		// TODO Auto-generated method stub
		ArrayList<Integer> cardsSelected= new ArrayList<Integer>();
		for (int i=0;i<selected.length;i++) {
			if (selected[i]==true) {
				cardsSelected.add(i);
			}
		}
		if (cardsSelected.size()>0){
			int [] indicesSelected=new int[cardsSelected.size()];
			for (int i=0;i<cardsSelected.size();i++) {
				indicesSelected[i]=cardsSelected.get(i);
			}
			return indicesSelected;
		}
		else {
			return null;
		}
	}

	/**
	 * method for resetting the list of selected cards
	 */
	public void resetSelected() {
		// TODO Auto-generated method stub
		this.selected=new boolean[13];
		
	}

	/**
	 * method to repaint the complete frame
	 */
	public void repaint() {
		frame.repaint();
	}
	
	
	/**
	 * method for printing the specified string to the message area of the GUI.
	 * 
	 * @param msg
	 *            the string to be printed to the message area of the card game
	 *            table
	 */
	public void printMsg(String msg) {
		msgArea.append(msg+"\n");
		
	}

	/**
	 * method for clearing message area of GUI
	 */
	public void clearMsgArea() {
		msgArea.setText("");
		
	}
	
	public void printChatMsg(String msg) {
		chatArea.append(msg+"\n");
	}

	/**
	 * method for resetting the GUI
	 */
	public void reset() {
		// TODO Auto-generated method stub
		resetSelected();
		clearMsgArea();
		enable();
		
	}

	/**
	 * method for enabling user interactions with the GUI
	 */
	public void enable() {
		
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}

	/**
	 * method for disabling user interactions with the GUI
	 */
	public void disable() {
		// TODO Auto-generated method stub
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
		
	}
	
	/**
	 * an inner class that extends the JPanel class and implements the
	 * MouseListener interface.It overrides the paintComponent() method inherited from the
	 * JPanel class to draw the card game table and implements the mouseClicked() method from
	 * the MouseListener interface to handle mouse click events. 
	 * 
	 * @author Ali Murtaza
	 *
	 */
	
	class BigTwoPanel extends JPanel implements MouseListener{
		
		/**
		 * BigTwoPanel default constructor which adds the Mouse Listener and sets background of the card table.
		 */
		
		public BigTwoPanel() {
			this.addMouseListener(this);
			this.setBackground(new Color(34,139,34));
		}
		
		/**
		 * 		
		 * A method to draw the avatars, text and cards on card table
		 * @param g Provided by system to allow drawing
		 */
		 
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.white);
			
			for (int i=0;i<4;i++) {
				if (i==game.getPlayerID()) {
					//if it's the player's turn
					if (i==game.getCurrentIdx()) {
						g.setColor(Color.blue);						
					}
					else {
						g.setColor(Color.yellow);
					}
					g.drawString("  (You)",15,21+i*130);
					g.setColor(Color.white);
				}
				else if(i==game.getCurrentIdx()) {
					g.setColor(Color.blue);
					g.drawString(game.getPlayerList().get(i).getName(),15,21+i*130);
					g.setColor(Color.white);
				}
				else {
					g.drawString(game.getPlayerList().get(i).getName(),15,21+i*130);
				}
				g.drawImage(avatars[i],5,27+i*130,this);
				g.setColor(Color.black);
				g.drawLine(0,130+130*i,1300, 130+130*i);
				g.setColor(Color.white);
				for (int j=0;j<game.getPlayerList().get(i).getNumOfCards();j++) {
					if (i==game.getPlayerID()) {
						int suit=game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit();
						int rank=game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank();
						if(selected[j]==true) {
							g.drawImage(cardImages[suit][rank],120+j*cardImages[0][0].getWidth(this)/2,5+i*130,this);
						}
						else {
							g.drawImage(cardImages[suit][rank],120+j*cardImages[0][0].getWidth(this)/2,15+i*130,this);
						}
					}
					else {
						g.drawImage(cardBackImage,120+j*cardImages[0][0].getWidth(this)/2,15+i*130,this);
					}
				}
				if (game.getHandsOnTable().isEmpty() == false) {
					
					Hand latestHand=game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
					g.setColor(Color.black);
					g.drawString("Played by "+ latestHand.getPlayer().getName(), 5, 10+4*130 + 5 );
					g.setColor(Color.white);
					for(int k=0;k<latestHand.size();k++) {
						int suit=latestHand.getCard(k).getSuit();
						int rank=latestHand.getCard(k).getRank();
						g.drawImage(cardImages[suit][rank],5+k*20,25+4*130,this);
					}
					
				}
				
			}
			this.repaint();
		}
		
		/**
		 * 	
		 * A method to define what happens when mouse is clicked on the card table. It only allows clicks on cards of active player. Once cards are selected, the JPanel is repainted to reflect changes.
		 * @param event Mouse event created when Mouse Clicked
		 */
		public void mouseClicked(MouseEvent event) {
			if(activePlayer == game.getPlayerID()) {
				int width = cardImages[0][0].getWidth(this);
				int height = cardImages[0][0].getHeight(this);
				int num = game.getPlayerList().get(activePlayer).getNumOfCards();
				int minX = 120;
				int maxX = 90+(width/2)*num+width;
				int minY = 10+activePlayer*130-10;
				int maxY = 10+activePlayer*130+height;
				
				if (event.getX() >= minX && event.getX() <= maxX && event.getY() >= minY && event.getY() <= maxY) {	
					int card = (int)Math.ceil((event.getX()-120)/(width/2));
					
					if ((card/num)>0) {
						card=num-1;
					}
	
					if (selected[card]==true) {
						if (event.getY() > (maxY - 10) && event.getX() < (120+(width/2)*card + width/2) && selected[card-1] == false) {
							if (card != 0) {
								card = card - 1;
							}
							selected[card] = true;
						} else if (event.getY() < (maxY - 10)){
							selected[card] = false;
						}
					} else if (event.getY() > (minY + 10)){
						selected[card] = true;
					} else if (selected[card - 1] && event.getX() < (120+(width/2)*card + width/2)) {
						selected[card-1] = false;
					}
					this.repaint();
				}
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {	
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {	
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method 
	 * from the ActionListener interface to handle menu-item-click events for the “Quit” menu item. When the
	 * “Quit” menu item is selected, it terminates application.
	 * @author Ali Murtaza
	 */
	
	class QuitMenuItemListener implements ActionListener{

		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method 
	 * from the ActionListener interface to handle menu-item-click events for the “Connect” menu item. When the
	 * “Connect” menu item is selected, it connects the player to the server if he is not already connected.
	 * @author Ali Murtaza
	 */
	
	
	class ConnectMenuItemListener implements ActionListener{

		public void actionPerformed(ActionEvent event) {
			if (game.checkConnection()==false) {
				reset();
				game.makeConnection();
			}
			
		}
		
	}
	/**
	 * an inner class that implements the ActionListener interface. It implements the actionPerformed() method from the 
	 * ActionListener interface to handle button-click events for the “Play” button. When the “Play” button is clicked, 
	 * it calls the makeMove() method of CardGame object to make a move.
	 * @author Ali Murtaza
	 */
	class PlayButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent event) {
			if (game.getCurrentIdx()==game.getPlayerID()) {
				if (getSelected()!=null) {
					game.makeMove(activePlayer,getSelected());
				}
				else {
					printMsg("No cards selected!");
				}
			}
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the 
	 * ActionListener interface to handle button-click events for the “Pass” button. When the “Pass” button is clicked, 
	 * it calls the makeMove() method of CardGame object to make a move.
	 * 
	 * @author Ali Murtaza
	 */
	class PassButtonListener implements ActionListener{
		

		@Override
		public void actionPerformed(ActionEvent event) {
			if (game.getCurrentIdx()==game.getPlayerID()) {
				game.makeMove(activePlayer, null);
			}
		}
	}
	
	/**
	 * The EnterListener class is an innerClass that implements ActionListener Interface.It performs the action performed method from the 
	 * Actionlistener interface to handle the situation when someone presses the enter button. When the Enter button is clicked,it's going to 
	 * send whatever the user inputed to the server and then reset the chat field.
	 * 
	 * @author Ali Murtaza
	 *
	 */
	class EnterListener implements ActionListener {

		
		public void actionPerformed(ActionEvent event) {
			try {
				// sends the text in the text field to the server
				CardGameMessage message= new CardGameMessage(CardGameMessage.MSG,-1,chatField.getText());
				game.sendMessage(message);
				
				chatField.setText("");
				chatField.requestFocus();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// resets the text field

		}
	} // SendButtonListener
	
	

}
