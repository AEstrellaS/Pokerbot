package pokerbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{

	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		String words[] = message.split("\\s+"); //When a message is recieved, it is split by words

		//Printing the words of the message in the console
		for (int i = 0; i < words.length; i++) {
			System.out.println(words[i]);
		}

		if(words[0].equals("!host")) {
			if(Main.games < 1) {
				event.getChannel().sendMessage("@everyone, A poker table has opened, come take a seat:").queue();
				Main.games++;
			} else {
				event.getChannel().sendMessage("There is a current game playing").queue();
			}
		}

		if(words[0].equals("!start") && Main.games > 0) {
			event.getChannel().sendMessage("Game Force Started").queue();
			Main.table[0].nextCycle();
			Main.table[0].startRound();
		}

		if(event.getAuthor().isBot() && words[0].equals("@everyone,")) {
			Main.table[0].setTableMessageID(event.getMessageId());
			event.getMessage().addReaction("U+31U+fe0fU+20e3").queue(); //Command to add a reaction to the message in discord
			event.getMessage().addReaction("U+32U+fe0fU+20e3").queue();
			event.getMessage().addReaction("U+33U+fe0fU+20e3").queue();
			event.getMessage().addReaction("U+34U+fe0fU+20e3").queue();
			event.getMessage().addReaction("U+35U+fe0fU+20e3").queue();
			event.getMessage().addReaction("U+36U+fe0fU+20e3").queue();
			event.getMessage().addReaction("U+37U+fe0fU+20e3").queue();
			event.getMessage().addReaction("U+38U+fe0fU+20e3").queue();
		}

		if(event.getAuthor().isBot() && words[0].equals("Check,")) {
			event.getMessage().addReaction("U+2705").queue();
			event.getMessage().addReaction("U+274C").queue();
			event.getMessage().addReaction("U+261D").queue();
		}
	}
}
