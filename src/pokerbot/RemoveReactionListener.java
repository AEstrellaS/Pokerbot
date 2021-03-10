package pokerbot;

import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RemoveReactionListener extends ListenerAdapter{
	
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		if(event.getMember().getUser().isBot() == false && event.getMessageId().equals(Main.gameMessageId)) {
			System.out.println(event.getMember().getUser().getId());
			for(int index = 0; index < 8; index++) {
				int idPointer = 31+index;
				String emoteId = "U+"+idPointer+"U+fe0fU+20e3";
				if(event.getReactionEmote().getAsCodepoints().equals(emoteId)) {
					Main.playingUsers[index].setUUID("");
					
					event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage("You left the table!").queue();
                    });
				}
			}
			if(event.getReactionEmote().getAsCodepoints().equals("U+31U+fe0fU+20e3")) {
				System.out.println("removed");
			}
		}
	}
	
}
