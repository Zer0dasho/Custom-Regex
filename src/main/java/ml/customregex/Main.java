package ml.customregex;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import ml.customregex.config.Config;
import ml.customregex.settings.ConfigS;
import ml.customregex.settings.ConfigS.Replacement;

public class Main extends JavaPlugin implements Listener {

	private Config<ConfigS> config;
	
	@Override
	public void onEnable() {
		config = new Config<ConfigS>(getDataFolder(), "config.yml", ConfigS.class);
		Bukkit.getPluginManager().registerEvents(this, this);
		super.onEnable();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length > 0 && args[0].equalsIgnoreCase("reload")) {
			try {
				config.reload();
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[CustomRegex] &aSuccessfully reloaded configuration!"));
			} catch (IOException ex) {
				Logger logger = Main.getPlugin().getLogger();
				logger.log(Level.SEVERE, "Couldn't read from '{}'.".replace("{}", config.getFile().getName()));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[CustomRegex] &cFailed to reload configuration. Check console for details."));
				ex.printStackTrace();
			}  catch (Exception ex) {
				Logger logger = Main.getPlugin().getLogger();
				logger.log(Level.SEVERE, "YAML syntax incorrect for failed for '{}'.".replace("{}", config.getFile().getName()));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[CustomRegex] &cFailed to reload configuration. Check console for details."));
				ex.printStackTrace();
			} 
		}
		return super.onCommand(sender, command, label, args);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void preprocessEvent(PlayerCommandPreprocessEvent e) {
		//This is so CustomRegex can edit messages within /msg
		//This is disabled by default for every regular expression entry
		CommandEvent e2 = new CommandEvent(true, e.getPlayer(), e.getMessage(), e.getRecipients());
		silentChat(e2);
		e.setPlayer(e2.getPlayer());
		e.setMessage(e2.getMessage());
		e.setCancelled(e2.isCancelled());
	}

	@EventHandler 
	public void silentChat(AsyncPlayerChatEvent e) {
		if(e instanceof ModifiedEvent) return;
		
		ConfigS settings = config.getSettings();
		String silentMessage = e.getMessage();
		String modifiedMessage = e.getMessage();
		boolean isSilent = false, isCommand = e instanceof CommandEvent;
		
		//If settings are null, there won't be any regular expressions to evaluate
		if(settings == null) return;
		
		//Grab every regular expression that evaluates to a command
		for(Map.Entry<String, ConfigS.Command> entry :  settings.getCommands().entrySet()) {
			ConfigS.Command command = entry.getValue();
			
			String regex = command.getRegex();
			String permission = command.getPermission();
			if(permission != null && !e.getPlayer().hasPermission(permission)) continue;
			if(!modifiedMessage.matches(regex)) continue;
			
			//Execute commands
			if(command.isCancel_chat()) e.setCancelled(true);
			for(ConfigS.Command.Command2 command2 : command.getCommands()) {
				CommandSender sender = command2.isPlayer() ? e.getPlayer() : Bukkit.getConsoleSender();
				super.getServer().dispatchCommand(sender, modifiedMessage.replaceAll(regex, command2.getCommand()));
			}
			
			return;
		}
		
		//Grab every regular expression that evaluates to a chat replacement
		for(Map.Entry<String, Replacement> entry : settings.getReplacements().entrySet()) {
			Replacement replacement = entry.getValue();
			
			//If edit_command is disabled, '/' commands won't be affected
			if(!replacement.isEdit_command() && isCommand) continue;
			
			String regex = replacement.getRegex();
			String permission = replacement.getPermission();
			String replacement2 = replacement.getReplacement();
			if(permission != null && !e.getPlayer().hasPermission(permission)) continue;
			if(replacement.isMust_match() && !modifiedMessage.matches(regex)) continue;
			
			//Cancel chat
			String messageBefore = modifiedMessage;
			modifiedMessage = modifiedMessage.replaceAll(regex, replacement2);
			
			if(!messageBefore.equalsIgnoreCase(modifiedMessage)) {
				if(replacement.isCancel_chat()) e.setCancelled(true);
				if(!replacement.isSilent_replace()) silentMessage = silentMessage.replaceAll(regex, replacement2);
				else {
					isSilent = true;
					e.getRecipients().remove(e.getPlayer());
				}
			}
		}
		
		e.setMessage(modifiedMessage);
		
		if(!isSilent) return;
		
		//Format string and check against every listener to avoid plugin collisions
		String toSend = String.format(e.getFormat(), new Object[] { e.getPlayer().getDisplayName(), silentMessage });
		ModifiedEvent e2 = new ModifiedEvent(e.isAsynchronous(), e.getPlayer(), e.getMessage(), e.getRecipients());
		
		for(HandlerList list : HandlerList.getHandlerLists()) {
			for(RegisteredListener listener : list.getRegisteredListeners()) {
				try {
					listener.callEvent(e2);
				} catch (EventException ex) {
					Main.getPlugin().getLogger().log(Level.SEVERE, "Silent chat event was not processed by this listener.");
					ex.printStackTrace();
				}
			}
		
		}
		
		if(e2.isCancelled()) return;
		e.getPlayer().sendMessage(toSend);
	}
	
	public static JavaPlugin getPlugin() {
		return Main.getPlugin(Main.class);
	}

}