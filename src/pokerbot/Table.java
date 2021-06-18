package pokerbot;

import java.util.*;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.requests.Route.Users;

public class Table {
	
	//Declaration of instance variables
	private int pot;
	int usersInHand = 0;
	private ArrayList<Card> table = new ArrayList<Card>();
	private ArrayList<Player> playingUsers = new ArrayList<Player>(); //Users playing the hand
	private ArrayList<Player> seatingUsers = new ArrayList<Player>(); //Users in table
	private String tableMessageID = "";
	
    private int dealerPos = -1; //Stores the current dealer position
    private int sbPos = 0; //Stores the small blind position
    private int bbPos = 1; //Stores the big blind position

	public int minimumBet = 20;

    Deck deck = new Deck();
    
	public void generateSeatingUsers() {
		for(int index = 0; index < 9; index++) {
			seatingUsers.add(new Player(null, 0));
		}
	}

	private ArrayList<Card> deliverFlop() { //Function to deliver the flop (the first 3 cards)
		table.add(deck.deliverCard());
		table.add(deck.deliverCard());
		table.add(deck.deliverCard());
		return table;
	}
	
	private ArrayList<Card> deliverTurn() { //Function to deliver the turn (the next card)
		table.add(deck.deliverCard());
		return table;
	}
	
	private ArrayList<Card> deliverRiver() { //Function to deliver the river (the next card)
		table.add(deck.deliverCard());
		return table;
	}
	
	public ArrayList<Card> fixCards() {//Function to fix the cards in the table's hand (used to debug)
		Card c1 = new Card(1, 5);
		Card c2 = new Card(1, 1);
		Card c3 = new Card(1, 12);
		Card c4 = new Card(1, 13);
		Card c5 = new Card(1, 11);
		table.add(c1);
		table.add(c2);
		table.add(c3);
		table.add(c4);
		table.add(c5);
		return table;
	}
	public void resetTable() { //Function that remove all cards from the table
		for(int index1 = table.size()-1; index1 >= 0; index1--) {
			table.remove(index1);
		}
	}
	
	public void printTable() { //Function that prints the player's hand
		System.out.println("Table's Hand:");
		System.out.println(table);
	}
	
	public ArrayList<Player> getSeatingUsers() {
		return seatingUsers;
	}
	
	public ArrayList<Card> getHand() { //Function that returns the table's hand
		return table;
	}
	
	public String getTableMessageID() { //returns table message ID
		return tableMessageID;
	}
	
	public void setTableMessageID(String tableMessageID) { //sets table message ID
		this.tableMessageID = tableMessageID;
	}
	
	public void nextCycle() {

		for(int index = 0; index < 9; index++) {
			if(seatingUsers.get(index).getUser() != null) {//Add all users who are seating to PlayingUsers
				usersInHand++;
				playingUsers.add(seatingUsers.get(index));
			}
		}
		
		bbPos++;
		
		if(bbPos > playingUsers.size()-1) {
			bbPos = 0;
		}
		
		sbPos++;
		
		if(sbPos > playingUsers.size()-1) {
			sbPos = 0;
		}
		
		dealerPos++;
		
		if(dealerPos > playingUsers.size()-1) {
			dealerPos = 0;
		}
		
	}
	
	public void startRound() {
		deck.create();
		deck.shuffle();

		playingUsers.get(dealerPos).getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("You are the dealer!").queue();
        });
		
		playingUsers.get(sbPos).getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("You are the small blind! You have paid a small blind of "+minimumBet/2+" chips.").queue();
        });
		playingUsers.get(sbPos).placeBet(minimumBet/2);
		addToPot(minimumBet/2);
		playingUsers.get(bbPos).getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("You are the big blind! You have paid a Big Blind of " +minimumBet+" chips.").queue();
        });
		playingUsers.get(bbPos).placeBet(minimumBet);
		addToPot(minimumBet/2);

		for(int index=0; index < 2; index++) {
			for(int index2=0; index2 < usersInHand; index2++) {
				playingUsers.get(index2).deliverCard(deck);
			}
		}

		for(int index=0; index < usersInHand; index++) {
			playingUsers.get(index).sendHand();
		}
		
		bettingPhase();
	}

	public void addToPot(int bet) {
		pot += bet;
	}

	public void bettingPhase() {
		for(int index2=0; index2 < usersInHand; index2++) {
			playingUsers.get(index2).crf();
		}
	}


}
