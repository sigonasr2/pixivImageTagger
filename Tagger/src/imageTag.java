import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JFileChooser;

public class imageTag {
	public static Filters filters;
	public static HashMap<String,Boolean> tag_whitelist = new HashMap<String,Boolean>();
	public static List<String> pixiv_image_list = new ArrayList<String>();
	public static List<File> pixiv_rawimage_list = new ArrayList<File>();
	public static HashMap<String,List<String>> taglist = new HashMap<String,List<String>>();
	public static Map<String,Integer> tagCounter = new TreeMap<String,Integer>();
	
	public static void main(String[] args) {
		
		ExtractExifTool();
		
		try {
			filters =  new Filters();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GetTagWhitelist();
		
		FileChooser fc = new FileChooser();
		
		PixivManager pm = new PixivManager();
		
		System.out.println("Done!");
	}

	private static void GetTagWhitelist() {
		File whitelist = new File("whitelist.txt");
		if (whitelist.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(whitelist);
				BufferedReader br = new BufferedReader(fr);
				String s;
				try {
					s = br.readLine();
					while (s!=null) {
						String newtag = s.trim();
						tag_whitelist.put(newtag,true);
						System.out.println("Read in whitelisted tag: "+newtag);
						s=br.readLine();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static void ExtractExifTool() {
		InputStream tool = imageTag.class.getResourceAsStream("exiftool.exe");
		File f = new File("tool.exe");
		try {
			byte[] buffer = new byte[tool.available()];
			tool.read(buffer);
		    OutputStream outStream = new FileOutputStream(f);
		    outStream.write(buffer);
		    outStream.close();
		    tool.close();
		    f.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}


