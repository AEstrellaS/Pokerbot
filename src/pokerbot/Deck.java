package pokerbot;

import java.util.*;

public class Deck {
	
	//Declaration of instance variables
	private ArrayList<Card> deck = new ArrayList<Card>();
	
	//Function that creates a new non-shuffled deck
	public void create() {
		for(int index1 = 1; index1 < 5; index1++) { //Assigns a suit to the card
			for(int index2 = 1; index2 <= 13; index2++) { //Assigns a value to the card
				Card newCard = new Card(index1, index2);
				deck.add(newCard); //Adds the newly created card to the deck
			}
		}
	}
	
	public void shuffle() { //Function that shuffles the cards of the deck
		Random rand = new Random(); //The shuffle of the deck is using the Fisher-Yates Modern Algorithm
		for(int index1 = deck.size()-1; index1 > 0; index1--) {
			int index2 = rand.nextInt(index1);
			Collections.swap(deck, index1, index2);
		}
	}
	
	public Card deliverCard() { //Function that removes and returns a card from the top of the deck
		Card newCard = deck.get(deck.size()-1);
		deck.remove(deck.size()-1);
		return newCard;
	}
	
	public int size() { //Function that returns the size of the deck
		return deck.size();
	}
}
