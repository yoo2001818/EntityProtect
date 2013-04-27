package kr.kkiro.projects.bukkit.entityprotect.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileInteract {
	public static boolean copy(File source, File target) {
		FileInputStream sourceStream;
		FileOutputStream targetStream;
		
		FileChannel sourceChannel;
		FileChannel targetChannel;
		
		try {
			
			sourceStream = new FileInputStream(source);
			targetStream = new FileOutputStream(target);
			
			sourceChannel = sourceStream.getChannel();
			targetChannel = targetStream.getChannel();
			
			sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
			
			sourceChannel.close();
			targetChannel.close();
			
			sourceStream.close();
			targetStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
