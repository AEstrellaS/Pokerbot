package pokerbot;

import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RemoveReactionListener extends ListenerAdapter{
	public static boolean isBot = false;
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {// Tests if event is made by user or by a bot and tests if event happens on the main message sent by the bot

		if(event.getUser().isBot() == false && isBot == false && Main.table[0].getTableMessageID().equals(event.getMessageId())) {
			for(int index = 0; index < 8; index++) {//Loops around every index position of playingUsers and sees if it matches with the reaction emote
				int idPointer = 31+index;
				String emoteId = "U+"+idPointer+"U+fe0fU+20e3";
				if(event.getReactionEmote().getAsCodepoints().equals(emoteId)) {
					Main.table[0].getSeatingUsers().get(index).setUser(null);
					
					event.getUser().openPrivateChannel().queue(privateChannel -> { //Sends a direct message to the player
                        privateChannel.sendMessage("You left the table!").queue();
                    });
				}
			}
		}
		isBot=false;
	}
	
}
