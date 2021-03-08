package pokerbot;

import java.util.*;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {
	
	static String token = "NzQyODg1NTE1Nzc4NDU3Njgw.XzMoDQ.kBo08V_4EO3JRCqDn2JDfbPHmYs";
    public int games = 0;
    public static String gameMessageId = "";
    private boolean flag = false;
    
	public static void main(String[] args) throws LoginException {
		// TODO Auto-generated method stub
		JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setActivity(Activity.playing("!host"));
        builder.setToken(token);
        builder.addEventListeners(new Main());
        builder.addEventListeners(new ReactionListener());
        builder.addEventListeners(new RemoveReactionListener());
        builder.build();
        
		fixedProcedure();
	}
	
	public static void fixedProcedure() {
		Deck deck = new Deck();
		Player player = new Player("alvaro", 500);
		Table table = new Table();
		table.fixCards(deck);
		player.fixCards();
		player.printHand();
		table.printTable();
		player.handRating(table);
	}
	public static void procedure() {
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
