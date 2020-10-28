package pokerbot;

import java.util.*;

public class Player {

	//Declaration of instance variables
	private final String user; //When changing program to discord change this to object type 'User'
	private ArrayList<Card> hand = new ArrayList<Card>(); //Player hand, only 2 cards
	private ArrayList<Integer> ptHand = new ArrayList<>(); //Player and table hand, max 7 cards

	private int chips;
	private boolean isDealer;
	private boolean isFolded;

	private int handRating; // 0 if nothing or/high card, 1 one pair, 2 two pair, 3 three of a kind, 4 straight, 5 flush, 6 full house, 7 four of a kind, 8 straight flush, 9 royal flush
	private int highestCard; //The highest card in the hand or in the table is set as the int
	private int firstPair = 0; //Pair value is set as the int
	private int secondPair = 0; //Pair value is set as the int
	private int threeOfAKind = 0; //Triplet value is set as the int
	private int straight; //Highest card in the straight is set as the int
	private int flush; // Highest card in the flush is set as the int
	private boolean fullHouse = false; // if the user has a Pair and a three of a kind fullHouse boolean is set to true
	private int fourOfAKind = 0; //Quadruplet value is set as the int
	private int straightFlush; //Highest card in the striaght flush is set as the int

	//Constructor, sets user and initial chips
	public Player(String user, int chips) {
		this.user = user;
		this.chips = chips;
	}

	public ArrayList<Card> deliverHand(Deck deck) { //Function to deliver two cards to the player's hand
		hand.add(deck.deliverCard());
		hand.add(deck.deliverCard());
		return hand;
	}

	public void resetHand() { //Function that removes all cards from the player's hand
		hand.clear();
		handRating = 0;
		highestCard = 0;
	}

	public void handRating(Table table) {
		highestCard(table);
		valueChecks(table);
	}

	private void highestCard(Table table) {
		for(int index1 = 0; index1 <= hand.size()-1; index1++) { //Checks for the players highest card
			for(int index2 = 0; index2 <= table.getHand().size()-1; index2++) { 
				if(hand.get(index1).getValue() >= table.getHand().get(index2).getValue()) { //Checks if the player's cards are higher than the table's hand
					if(hand.get(index1).getValue() >= highestCard || hand.get(index1).getValue() == 1) { //If the card in the player's hand is bigger than the table's hand, checks if the card is higher than the current highest Card
						highestCard = hand.get(index1).getValue();
					}
				} else {
					if(table.getHand().get(index2).getValue() >= highestCard || hand.get(index1).getValue() == 1) { //If the card in the table's hand is bigger than the player's hand, checks if the card is higher than the current highest Card
						highestCard = table.getHand().get(index2).getValue();
					}
				}
			}
		}
	}

	private void valueChecks(Table table) { //Function that determines the pair and highest pair (if there are any)

		ptHand.add(hand.get(0).getValue()); //Adds both cards to the "player and table" hand
		ptHand.add(hand.get(1).getValue());

		for(int index = 0; index < table.getHand().size(); index++) { //Adds all cards in the table's hand to the "player and table" hand
			ptHand.add(table.getHand().get(index).getValue());
		}

		for(int index1 = 0; index1 < ptHand.size(); index1++) { //For loop to determine: the biggest two pair (if they exist), if theres a three of a kind and if there is a four of a kind

			int count = 0;

			for(int index2 = index1+1; index2 < ptHand.size(); index2++) {
				if(ptHand.get(index1) == ptHand.get(index2) && ptHand.get(index1) != 0) { //If index1 is equal to index2 and it is not equal to zero, count increases by one and the value in index2 is set as 0
					count++;
					ptHand.set(index2, 0);
				}
			}
			
			if(count == 1) { //Determines if theres any pairs
				if(firstPair != 0) { //Determines if firstPair is not empty
					if(ptHand.get(index1) > firstPair) { //Determines if the value of the arraylist at location index1 is bigger than the firstPair
						if(firstPair > secondPair) { //Determines if firstPair is bigger than secondPair, if it is secondPair assumes firstPair value
							secondPair = firstPair;
						}
						firstPair = ptHand.get(index1); //the value of the arraylist at location index1 gets stored in variable firstPair
					} else if (ptHand.get(index1) > secondPair) {
						secondPair = ptHand.get(index1); //If the value of the arraylist at location index1 is bigger than secondPair, secondPair assumes the value of the arraylist at location index1
					}
				} else { //if the firstPair is empty value of the arrayList at location index1 gets stored
					firstPair = ptHand.get(index1);
				}
			} else if(count == 2) { //If
				threeOfAKind = ptHand.get(index1);
			} else if (count == 3) {
				fourOfAKind = ptHand.get(index1);
			}
			
			ptHand.set(index1, 0);
			
		}
		
		if(firstPair != 0 && threeOfAKind != 0) { //If the player has a pair 
			fullHouse = true;
		}
		System.out.println(firstPair);
		System.out.println(secondPair);
		System.out.println(threeOfAKind);
		System.out.println(fourOfAKind);
		System.out.println(fullHouse);
	}


	public ArrayList<Card> fixCards() { //Function to fix cards into the player's hand (used to debug)
		Card c1 = new Card(1, 2);
		Card c2 = new Card(0, 3);
		hand.add(c1);
		hand.add(c2);
		return hand;
	}

	public void printHand() { //Function that prints the player's hand
		System.out.println("Player's Hand:");
		System.out.println(hand);
	}

	public void giveDealer() { //Function that sets isDealer True
		isDealer = true;
	}

	public void removeDealer() { //Function that sets isDealer false
		isDealer = false;
	}
	
	public boolean isDealer() {
		return isDealer;
	}

	public void giveChips(int chips) { //Function that gives chips to the player
		this.chips += chips;
	}

	public void removeChips(int chips) { //Function that removes chips from the player
		this.chips -= chips;
	}
	
	public void transferTo(Player other,int chips) {
		removeChips(chips);
		other.giveChips(chips);
	}

	public boolean isFolded() {
		return isFolded;
	}
	
	public void fold() {
		isFolded = true;
	}
	
	public int getHighestCard() { //Function to return the highest card
		return highestCard;
	}
}
