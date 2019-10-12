package ml.customregex.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.customregex.config.Settings;

public class ConfigS implements Settings {
	
	private Map<String, Command> commands;
	private Map<String, Replacement> replacements;
	
	public Map<String, Replacement> getReplacements() {
		return replacements == null ? new HashMap<String, Replacement>() : replacements;
	}
	public void setReplacements(Map<String, Replacement> replacements) {
		this.replacements = replacements;
	}
	public Map<String, Command> getCommands() {
		return commands == null ? new HashMap<String, Command>() : commands;
	}
	public void setCommands(Map<String, Command> commands) {
		this.commands = commands;
	}
	
	public class Command {
		
		private boolean cancel_chat;
		private List<Command2> commands;
		private String regex, permission;
		
		public class Command2 {
			
			private String command;
			private boolean is_player;
			
			public String getCommand() {
				return command == null ? "" : command;
			}
			public void setCommand(String command) {
				this.command = command;
			}
			public boolean isPlayer() {
				return is_player;
			}
			public void setIs_player(boolean is_player) {
				this.is_player = is_player;
			}
		}
		
		public boolean isCancel_chat() {
			return cancel_chat;
		}
		public void setCancel_chat(boolean cancel_chat) {
			this.cancel_chat = cancel_chat;
		}
		public List<Command2> getCommands() {
			return commands == null ? new ArrayList<Command2>() : commands;
		}
		public void setCommands(List<Command2> commands) {
			this.commands = commands;
		}
		public String getRegex() {
			return regex == null ? "" : regex;
		}
		public void setRegex(String regex) {
			this.regex = regex;
		}
		public String getPermission() {
			return permission;
		}
		public void setPermission(String permission) {
			this.permission = permission;
		}
	}
	
	public class Replacement {
		
		private String regex, replacement, permission;
		private boolean silent_replace, must_match, cancel_chat, edit_command;
		
		
		public boolean isEdit_command() {
			return edit_command;
		}
		public void setEdit_command(boolean edit_command) {
			this.edit_command = edit_command;
		}
		public boolean isCancel_chat() {
			return cancel_chat;
		}
		public void setCancel_chat(boolean cancel_chat) {
			this.cancel_chat = cancel_chat;
		}
		public boolean isSilent_replace() {
			return silent_replace;
		}
		public void setSilent_replace(boolean silent_replace) {
			this.silent_replace = silent_replace;
		}
		public boolean isMust_match() {
			return must_match;
		}
		public void setMust_match(boolean must_match) {
			this.must_match = must_match;
		}
		public String getRegex() {
			return regex == null ? "" : regex;
		}
		public void setRegex(String regex) {
			this.regex = regex;
		}
		public String getReplacement() {
			return replacement == null ? "" : replacement;
		}
		public void setReplacement(String replacement) {
			this.replacement = replacement;
		}
		public String getPermission() {
			return permission;
		}
		public void setPermission(String permission) {
			this.permission = permission;
		}
	}
	
	@Override
	public Class<?> getType() {
		return this.getClass();
	}
}