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
	
	static String token = "NzQyODg1NTE1Nzc4NDU3Njgw.XzMoDQ.i4xkIJBuvzuY5RxGxL8enP2equo";
    public int games = 0;
    public static String gameMessageId = "";
    public static Player[] playingUsers = new Player[8];
    
	public static void main(String[] args) throws LoginException {
		// TODO Auto-generated method stub
		for(int index = 0; index < 8; index++) {
			playingUsers[index] = new Player("",0);
		}
		JDABuilder builder = JDABuilder.createDefault(token); //creating new object
		builder.enableIntents(GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES); // Setting gateway intents
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB); //Setting bot status to do not disturb
        builder.setActivity(Activity.playing("!host"));
        builder.setToken(token); //Attaching token of bot to object
        builder.addEventListeners(new Main()); // Adding event listeners
        builder.addEventListeners(new ReactionListener()); 
        builder.addEventListeners(new RemoveReactionListener());
        builder.build(); // building object
        
		fixedProcedure();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		System.out.println(event.getAuthor().getName() + " in " + event.getChannel() + ": " + event.getMessage().getContentRaw());
        String message = event.getMessage().getContentRaw();
        String words[] = message.split("\\s+"); //When a message is recieved, it is split by words

        	//Printing the words of the message in the console
        for (int i = 0; i < words.length; i++) {
            System.out.println(words[i]);
        }
        
        if(words[0].equals("!host")) {
            if(games < 1) {
                event.getChannel().sendMessage("@everyone, A poker table has opened, come take a seat:").queue();
                games++;
            } else {
                event.getChannel().sendMessage("There is a current game playing").queue();
            }
        }

        if(event.getAuthor().isBot() && words[0].equals("@everyone,")) {
            gameMessageId = event.getMessageId();
            System.out.println(gameMessageId);
            event.getMessage().addReaction("U+31U+fe0fU+20e3").queue(); //Command to add a reaction to the message in discord
            event.getMessage().addReaction("U+32U+fe0fU+20e3").queue();
            event.getMessage().addReaction("U+33U+fe0fU+20e3").queue();
            event.getMessage().addReaction("U+34U+fe0fU+20e3").queue();
            event.getMessage().addReaction("U+35U+fe0fU+20e3").queue();
            event.getMessage().addReaction("U+36U+fe0fU+20e3").queue();
            event.getMessage().addReaction("U+37U+fe0fU+20e3").queue();
            event.getMessage().addReaction("U+38U+fe0fU+20e3").queue();
        }
	}
	
	private static void fixedProcedure() {
		Deck deck = new Deck();
		Player player = new Player("alvaro", 500);
		Table table = new Table();
		table.fixCards(deck);
		player.fixCards();
		player.printHand();
		table.printTable();
		player.handRating(table);
	}
	
	private static void procedure() {
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
