package net.meano.PortablePortals.Util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;

public enum Msgs {
	Portals_Title("Portals.Title", ""),
	Portals_NeedItem("Portals.Need Item","<item>"),
	Portals_LeftClickTo("Portals.Left Click To", ""),
	Portals_RightClickTo("Portals.Right Click To", ""),
	Portals_Target("Portals.Target","<target>"),
	Portals_NotEnoughRoom("Action.Not Enough Room", ""),
	Portals_PortalOpened("Action.Portal Opened", ""),
	Portals_PortalClosed("Action.Portal Closed", ""),
	Portals_TargetSet("Action.Target Set",""),
	Portals_NoTarget("Portals.No Target", "");
	private String string;
	private String replace;
	
	private Msgs(String s, String r) {
		this.string = s;
		this.replace = r;
	}
	
	public String getTitleWithColor(String Get){
		String message = ChatColor.translateAlternateColorCodes('&',Files.getMessages().getString(this.string));
		Pattern MessagePattern=Pattern.compile(message.replace(replace, "(.*?)\\"));
		Matcher MessageMatcher = MessagePattern.matcher(Get);
		if(MessageMatcher.find()){
			return MessageMatcher.group(1);
		}else{
			return "";
		}
	}
	
	public String getString(String... replacement) {
		try {
			String message = ChatColor.translateAlternateColorCodes('&',
				Files.getMessages().getString(this.string)
					.replaceAll("&x","&"+ String.valueOf(RandomChatColor.getColor().getChar()))
					.replaceAll("&y","&" + String.valueOf(RandomChatColor.getFormat().getChar())))+ " ";
			if (this.replace != "")
				message = message.replaceAll(this.replace, replacement[0]);
			return message;
		} catch (NullPointerException npe) {
			return "找不到消息: " + this.string;
		}
	}
};
