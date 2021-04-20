package pokerbot;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class ReactionListener extends ListenerAdapter{
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if(event.getMember().getUser().isBot() == false && Main.table[0].getTableMessageID().equals(event.getMessageId())) { //Test if author of the event is a bot (non-human) and tests if the messageId of the event equals the messageId of the hosting message
			//Tests if the player reacts with 1 of the 8 allowed reactions
			if(event.getReactionEmote().getAsCodepoints().equals("U+31U+fe0fU+20e3") || event.getReactionEmote().getAsCodepoints().equals("U+32U+fe0fU+20e3") || event.getReactionEmote().getAsCodepoints().equals("U+33U+fe0fU+20e3") || event.getReactionEmote().getAsCodepoints().equals("U+34U+fe0fU+20e3") || event.getReactionEmote().getAsCodepoints().equals("U+35U+fe0fU+20e3") || event.getReactionEmote().getAsCodepoints().equals("U+36U+fe0fU+20e3") || event.getReactionEmote().getAsCodepoints().equals("U+37U+fe0fU+20e3") || event.getReactionEmote().getAsCodepoints().equals("U+38U+fe0fU+20e3")) {
				boolean flag = false;
				for(int index = 0; index < 8; index++) {
					if(event.getMember().getUser().equals(Main.table[0].getSeatingUsers().get(index).getUser())) { //Tests if the player is already in the game
						RemoveReactionListener.isBot = true;
						event.getReaction().removeReaction(event.getMember().getUser()).queue(); //If the player is already in the game, the reaction gets removed
						flag = true;
					}
				}

				if(flag == false) {
					for(int index = 0; index < 8; index++) { //Cycles for all valid emotes
						int idPointer = 31+index;
						String emoteId = "U+"+idPointer+"U+fe0fU+20e3";
						if(event.getReactionEmote().getAsCodepoints().equals(emoteId) && Main.table[0].getSeatingUsers().get(index).getUser() == null) {
							Main.table[0].getSeatingUsers().get(index).setUser(event.getMember().getUser());
							
							event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
	                            privateChannel.sendMessage("You are in!").queue();
	                        });
						}
					}
				}
			}
		}
	}

}
