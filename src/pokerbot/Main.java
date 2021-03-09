package pokerbot;

import java.util.*;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


public class Main extends ListenerAdapter {
	
	static String token = "N/a";
    public int games = 0;
    public static String gameMessageId = "";
    private boolean flag = false;
    
	public static void main(String[] args) throws LoginException {
		// TODO Auto-generated method stub
		JDABuilder builder = JDABuilder.createDefault(token);
		builder.enableIntents(GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_MESSAGES);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setActivity(Activity.playing("!host"));
        builder.setToken(token);
        builder.addEventListeners(new Main());
        builder.addEventListeners(new ReactionListener());
        builder.addEventListeners(new RemoveReactionListener());
        builder.build();
        
		fixedProcedure();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		System.out.println(event.getAuthor().getName() + " in " + event.getChannel() + ": " + event.getMessage().getContentRaw());
        String message = event.getMessage().getContentRaw();
        String words[] = message.split("\\s+");

        for (int i = 0; i < words.length; i++) {
            System.out.println(words[i]);
        }
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
