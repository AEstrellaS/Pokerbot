package pokerbot;

import java.util.*;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.requests.Route.Users;

public class Table {
	
	//Declaration of instance variables
	private int pot;
	int seatingUsers = 0;
	private ArrayList<Card> table = new ArrayList<Card>();
	private ArrayList<Player> playingUsers = new ArrayList<Player>();
	private String tableMessageID = "";
    
	public void generatePlayingUsers() {
		for(int index = 0; index < 9; index++) {
			playingUsers.add(new Player(null, 0));
		}
	}
	public ArrayList<Card> deliverFlop(Deck deck) { //Function to deliver the flop (the first 3 cards)
		table.add(deck.deliverCard());
		table.add(deck.deliverCard());
		table.add(deck.deliverCard());
		return table;
	}
	
	public ArrayList<Card> deliverTurn(Deck deck) { //Function to deliver the turn (the next card)
		table.add(deck.deliverCard());
		return table;
	}
	
	public ArrayList<Card> deliverRiver(Deck deck) { //Function to deliver the river (the next card)
		table.add(deck.deliverCard());
		return table;
	}
	
	public ArrayList<Card> fixCards(Deck deck) {//Function to fix the cards in the table's hand (used to debug)
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
	
	public ArrayList<Player> getPlayingUsers() {
		return playingUsers;
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
			if(playingUsers.get(index).getUser().getId() != null) {
				seatingUsers++;
			}
		}
	}
}
