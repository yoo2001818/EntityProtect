package kr.kkiro.projects.bukkit.entityprotect;

public class EPChat {
	public static String packMessage(String message, String... replaces) {
		String out = message;
		 for(int i = 0; i < replaces.length; ++i) {
			 out = out.replace(Integer.toString(i), replaces[i]);
		 }
		return out;
	}
}
