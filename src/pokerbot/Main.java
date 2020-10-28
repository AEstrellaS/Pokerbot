package pokerbot;

import java.util.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Deck deck = new Deck();
		Player player = new Player("alvaro", 500);
		Table table = new Table();
		deck.create();
		deck.shuffle();
		player.deliverHand(deck);
		player.printHand();
		table.deliverFlop(deck);
		table.deliverRiver(deck);
		table.deliverTurn(deck);
		table.printTable();
		player.handRating(table);
	}
}
