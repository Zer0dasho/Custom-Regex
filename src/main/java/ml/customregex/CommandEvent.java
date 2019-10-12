package ml.customregex;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CommandEvent extends AsyncPlayerChatEvent {

	public CommandEvent(boolean async, Player who, String message, Set<Player> players) {
		super(async, who, message, players);
		// TODO Auto-generated constructor stub
	}

}
