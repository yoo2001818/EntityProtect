package kr.kkiro.projects.bukkit.entityprotect.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileInteract {
	public static boolean copy(InputStream source, File target) {
		InputStream sourceStream;
		FileOutputStream targetStream;
		
		try {
			
			sourceStream = source;
			targetStream = new FileOutputStream(target);
			
			byte[] buffer = new byte[1024];
			int len = sourceStream.read(buffer);
			while (len != -1) {
			    targetStream.write(buffer, 0, len);
			    len = sourceStream.read(buffer);
			}
			
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
	public static boolean copy(File source, File target) {
		try {
			InputStream stream = new FileInputStream(source);
			boolean result = copy(stream, target);
			stream.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
