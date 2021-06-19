package pokerbot;

import java.util.*;

import net.dv8tion.jda.api.entities.User;

public class Player {

	//Declaration of instance variables
	private User user; //When changing program to discord change this to object type 'User'
	private ArrayList<Card> hand = new ArrayList<Card>(); //Player hand, only 2 cards

	private int chips;
	private int bet;
	private boolean isDealer;
	private boolean isFolded;

	//Hand ratings
	private int handRating; // 0 if nothing or/high card, 1 one pair, 2 two pair, 3 three of a kind, 4 straight, 5 flush, 6 full house, 7 four of a kind, 8 straight flush, 9 royal flush
	private int highestCard; //The highest card in the hand or in the table is set as the int
	private int firstPair = 0; //Pair value is set as the int
	private int secondPair = 0; //Pair value is set as the int
	private int threeOfAKind = 0; //Triplet value is set as the int
	private int straight; //Highest card in the straight is set as the int
	private int[] flush = new int[5]; // Highest card in the flush is set as the int
	private boolean fullHouse = false; // if the user has a Pair and a three of a kind fullHouse boolean is set to true
	private int fourOfAKind = 0; //Quadruplet value is set as the int
	private boolean straightFlush = false; //If user has a straight and a flush boolean is set to true
	private boolean royalFlush = false;

	//Positions in table
	private int positionInTable = -1; //Sitting out, or not in a table, -1
	//Big blind (bb) pos 0 in arrayList
	//Small blind (sb) pos 1 in arrayList
	//Button/Dealer (btn) pos 2 in arrayList
	//Cut-off (CO) pos 3 in arrayList
	//middle (md) pos 4-5 in arrayList
	//under the gun (UTG) pos 6-8 in arrayList

	//Constructor, sets user and initial chips
	public Player(User user, int chips) {
		this.user = user;
		this.chips = chips;
	}

	public ArrayList<Card> deliverHand(Deck deck) { //Function to deliver two cards to the player's hand
		hand.add(deck.deliverCard());
		hand.add(deck.deliverCard());
		return hand;
	}

	public void deliverCard(Deck deck) {
		hand.add(deck.deliverCard());
	}

	public void resetHand() { //Function that removes all cards from the player's hand
		hand.clear();
		handRating = 0;
		highestCard = 0;
		firstPair = 0;
		secondPair = 0;
		threeOfAKind = 0;
		straight = 0;
		for (int index = 0; index < 5; index++) {
			flush[index] = 0;
		}
		fullHouse = false;
		fourOfAKind = 0;
		straightFlush = false;
		royalFlush = false;
	}

	public void handRating(Table table) {
		highestCard(table);
		valueChecks(table);
		flushChecks(table);
		straightChecks(table);
		royalFlushChecks(table);
	}

	private void highestCard(Table table) {
		for(int index1 = 0; index1 < hand.size(); index1++) { //Checks for the players highest card
			for(int index2 = 0; index2 < table.getHand().size(); index2++) { 
				if(hand.get(index1).getValue() > table.getHand().get(index2).getValue() || hand.get(index1).getValue() == 1) { //Checks if the player's cards are higher than the table's hand
					if(hand.get(index1).getValue() == 1) { //If the card in the player's hand is bigger than the table's hand, checks if the card is higher than the current highest Card
						highestCard = hand.get(index1).getValue();
					} else if(hand.get(index1).getValue() > highestCard && highestCard != 1) {
						highestCard = hand.get(index1).getValue();
					}
				} else {
					if(table.getHand().get(index2).getValue() == 1) { //If the card in the player's hand is bigger than the table's hand, checks if the card is higher than the current highest Card
						highestCard = table.getHand().get(index2).getValue();
					} else if(table.getHand().get(index2).getValue() > highestCard && highestCard != 1) {
						highestCard = table.getHand().get(index2).getValue();
					}
				}
			}
		}
	}

	private void valueChecks(Table table) { //Function that determines the pair and highest pair (if there are any)
		ArrayList<Integer> ptHand = new ArrayList<>(); //Player and table hand, max 7 cards
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
					if(ptHand.get(index1) > firstPair || ptHand.get(index1) == 1) { //Determines if the value of the arraylist at location index1 is bigger than the firstPair
						if(firstPair > secondPair || firstPair == 1) { //Determines if firstPair is bigger than secondPair, if it is secondPair assumes firstPair value
							secondPair = firstPair;
						}
						firstPair = ptHand.get(index1); //the value of the arraylist at location index1 gets stored in variable firstPair
					} else if (ptHand.get(index1) > secondPair) {
						secondPair = ptHand.get(index1); //If the value of the arraylist at location index1 is bigger than secondPair, secondPair assumes the value of the arraylist at location index1
					}
				} else { //if the firstPair is empty value of the arrayList at location index1 gets stored
					firstPair = ptHand.get(index1);
				}
			} else if(count == 2) { //Determines if there is a three Of A Kind
				threeOfAKind = ptHand.get(index1);
			} else if (count == 3) { //Determines if there is a four of a kind
				fourOfAKind = ptHand.get(index1);
			}

			ptHand.set(index1, 0);

		}

		if(firstPair != 0 && threeOfAKind != 0) { //If the player has a pair 
			fullHouse = true;
		}
	}

	private void flushChecks(Table table) { //Function that determines if the player has a flush
		ArrayList<Integer> ptHand = new ArrayList<>(); //Player and table hand, max 7 cards
		ptHand.add(hand.get(0).getSuitValue()); 
		ptHand.add(hand.get(1).getSuitValue());

		for(int index = 0; index < table.getHand().size(); index++) { //Adds the cards in the table to player-table hand array
			ptHand.add(table.getHand().get(index).getSuitValue());
		}

		for(int index1 = 0; index1 < ptHand.size(); index1++) {  //Tests if there exists cards with the same suit
			int count = 0;
			for(int index2 = index1+1; index2 < ptHand.size(); index2++) {
				if(ptHand.get(index1) == ptHand.get(index2) && ptHand.get(index1) != 0) { 
					count++; //If there are two cards with the same suit count goes up by one
				}
			}
			if(count >= 4) { //If there are 5 cards with the same suit all other cards with different suits are set to 0
				for(int index2 = 0; index2 < ptHand.size(); index2++) {
					if(ptHand.get(index2) != ptHand.get(index1) ) {
						ptHand.set(index2, 0);
					}
				}

				for(int index2 = 0; index2 < ptHand.size(); index2++) {
					if(ptHand.get(index2) != 0 ) {
						if(index2 < 2) {
							ptHand.set(index2, hand.get(index2).getValue());
						} else {
							ptHand.set(index2, table.getHand().get(index2-2).getValue());
						}
					}
				}
			}
		}

		Collections.sort(ptHand); //Arraylist gets sorted from smallest to biggest
		Collections.reverse(ptHand); //Arraylist gets sorted from biggest to smallest

		//Check for ace
		for(int index1 = 0; index1 < 3; index1++) { //Check if positions 4,5,6 of the arraylist are equal to 1
			if(ptHand.get(index1+4) == 1) { //If positions 4,5,6 are equal to 1, the value 1 is placed infront of the arraylist
				for(int index2 = 3+index1; index2 > 0; index2--) {
					Collections.swap(ptHand, index2-1, index2);
				}
				Collections.swap(ptHand, 0, index1+4);
			}
		}
		ptHand.remove(6);
		ptHand.remove(5);
		for(int index1 = 0; index1 < ptHand.size(); index1++) { //Set the found flush into flush
			flush[index1] = ptHand.get(index1);
		}
	}

	private void straightChecks(Table table) {
		ArrayList<Integer> ptHandValue = new ArrayList<>(); //Player and table hand, max 7 card values
		ptHandValue.add(hand.get(0).getValue()); 
		ptHandValue.add(hand.get(1).getValue());

		ArrayList<Integer> ptHandSuit = new ArrayList<>(); //Player and table hand, max 7 suit values
		ptHandSuit.add(hand.get(0).getSuitValue()); 
		ptHandSuit.add(hand.get(1).getSuitValue());

		for(int index = 0; index < table.getHand().size(); index++) { //Adds the card value in the table to player-table hand value array
			ptHandValue.add(table.getHand().get(index).getValue());
		}

		for(int index = 0; index < table.getHand().size(); index++) { //Adds the suit value in the table to player-table hand value array
			ptHandSuit.add(table.getHand().get(index).getSuitValue());
		}


		for(int index1 = 0; index1 < 7; index1++) { //Orders cards based on the lowest to highest numerical suit value, along with the card value in a separate arrayList
			for(int index2 = 0; index2 <6; index2++) {
				if(ptHandSuit.get(index2) >= ptHandSuit.get(index2+1)) {
					Collections.swap(ptHandValue, index2, index2+1);
					Collections.swap(ptHandSuit, index2, index2+1);
				}
			}
		}

		System.out.println(ptHandValue);
		System.out.println(ptHandSuit);

		for(int index1 = 0; index1 < 4; index1++) {

			int count = 0;

			for(int index2 = 0; index2 < ptHandSuit.size(); index2++) { //Testing if there are 5 or more cards with the same suit value
				if(ptHandSuit.get(index1) == ptHandSuit.get(index2) && ptHandSuit.get(index1) != 0) {
					count++;
				}
			}
			if(count >= 4) { //If there are 5 or more cards with the same suit value, set other cards with different suit value to 0
				for(int index2 = 0; index2 < ptHandSuit.size(); index2++) {
					if(ptHandSuit.get(index1) != ptHandSuit.get(index2)) {
						ptHandSuit.set(index2, 0); //Setting index2 to 0
					}
				}

				for(int index2 = 0; index2 < 7; index2++) { //Sets the suit value to the left and the 0 to the right of the arrayList
					for(int index3 = 0; index3 <6; index3++) {
						if(ptHandSuit.get(index3) <= ptHandSuit.get(index3+1)) {
							Collections.swap(ptHandValue, index3, index3+1);
							Collections.swap(ptHandSuit, index3, index3+1);
						}
					}
				}

				for(int index2 = 0; index2 < count; index2++) { //Orders the cards of the same suit based on card value (lower values towards the left)
					for(int index3 = 0; index3 < count-1; index3++) {
						if(ptHandValue.get(index3) >= ptHandValue.get(index3+1)) {
							Collections.swap(ptHandValue, index3, index3+1);
							Collections.swap(ptHandSuit, index3, index3+1);
						}
					}
				}

				for(int index2 = 0; index2 < count; index2++) { //Tests if the cards with the same suit are in a straight
					if(ptHandValue.get(index2+1) == ptHandValue.get(index2)+1 && ptHandValue.get(index2+2) == ptHandValue.get(index2)+2 && ptHandValue.get(index2+3) == ptHandValue.get(index2)+3 && ptHandValue.get(index2+4) == ptHandValue.get(index2)+4) {
						straight = ptHandValue.get(index2+4); //if cards are in a straight, the biggest value is set to variable straight
						straightFlush = true; //If cards are in a straight, variable straightFlush is set to true.
					} else if(ptHandValue.get(index2) == 1 && ptHandValue.get(index2+1) == 10 && ptHandValue.get(index2+2) == 11 && ptHandValue.get(index2+3) == 12 && ptHandValue.get(index2+4) == 13) {
						royalFlush = true;
					}
				}
			}
		}
	}

	public void royalFlushChecks(Table table) {

	}

	public ArrayList<Card> fixCards() { //Function to fix cards into the player's hand (used to debug)
		Card c1 = new Card(2, 11); // first arg is suit, second arg is card value
		Card c2 = new Card(2, 10);
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public int getPositionInTable() {
		return positionInTable;
	}
	
	public void setPositionInTable(int positionInTable) {
		this.positionInTable = positionInTable;
	}

	public void sendHand() { //Sends the hand to the player via the discord API
		user.openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage("Your Cards: " + hand.get(0).toString() +", "+ hand.get(1).toString()).queue();
        });
	}

	public void placeBet(int bet) {
		if(chips != 0 && bet >= chips) {
			chips = 0;
		} else if (chips !=0) {
			chips -= bet;
		}
	}

	public void cfr() { //Check raise or fold
		user.openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Check, Fold or Raise").queue();
        });
	}

	public void setBet(int bet) {
		this.bet = bet;
	}

	public int getBet() {
		return bet;
	}
}
