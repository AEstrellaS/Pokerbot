package pokerbot;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class Main {
	
	static String token = "N/a";
    public static int games = 0;
    
    public static Table[] table = new Table[1];
    
	public static void main(String[] args) throws LoginException {
		// TODO Auto-generated method stub
		
		table[0] = new Table();
		table[0].generateSeatingUsers();
		
		JDABuilder builder = JDABuilder.createDefault(token); //creating new object
		builder.enableIntents(GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES); // Setting gateway intents
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB); //Setting bot status to do not disturb
        builder.setActivity(Activity.playing("!host"));
        builder.setToken(token); //Attaching token of bot to object
        builder.addEventListeners(new MessageListener()); // Adding event listeners
        builder.addEventListeners(new ReactionListener()); 
        builder.addEventListeners(new RemoveReactionListener());
        builder.build(); // building JDA
        

	}
}
