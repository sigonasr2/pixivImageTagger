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

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;

public class imageTag {
	public static Filters filters;
	public static HashMap<String,Boolean> tag_whitelist = new HashMap<String,Boolean>();
	public static List<String> pixiv_image_list = new ArrayList<String>();
	public static List<File> pixiv_rawimage_list = new ArrayList<File>();
	public static HashMap<String,List<String>> taglist = new HashMap<String,List<String>>();
	public static Map<String,Integer> tagCounter = new TreeMap<String,Integer>();
	public static HashMap<String,List<String>> subtaglist = new HashMap<String,List<String>>();
	
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
						String newtag = s.trim().toLowerCase();
						String[] combtag;
						List<String> listofsubs = new ArrayList<String>();
						if (s.contains(": ")); {
							combtag = s.split(": ");
								for (int i=1;i<combtag.length; i++) {
									tag_whitelist.put(combtag[i].trim().toLowerCase(),true);
									System.out.println("Read in whitelisted tag: "+combtag[i]);
									subtaglist.put(combtag[i],listofsubs);
									}
							tag_whitelist.put(combtag[0].trim().toLowerCase(),true);
							System.out.println("Read in whitelisted tag: "+combtag[0]);
							listofsubs.add(combtag[0]);
							}
						/* the first word is read and will commit to the change while other
						 *  tags in colons will be read but default to the first for committing */
						s=br.readLine();
					}
					System.out.println("SUBTAG LIST:");
                    for (String ss : subtaglist.keySet()) {
                      List<String> subtags = subtaglist.get(ss);
                        System.out.println(" "+ss+": ");
                      for (String sss : subtags) {
                        System.out.println("    -"+sss);
                      }
                    }
                    System.out.println("============");
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
		File f = new File("tool.exe");/*
		if (!f.exists()) {
			try {
				byte[] buffer = new byte[tool.available()];
				tool.read(buffer);
			    OutputStream outStream = new FileOutputStream(f);
			    outStream.write(buffer);
			    outStream.close();
			    tool.close();
			    //f.deleteOnExit(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
		 try {
			FileUtils.copyInputStreamToFile(tool,f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Run a test of it.
		System.out.println("Test run of tool to verify it is working...");
		String command = "tool.exe";
		CommandLine cmdLine = CommandLine.parse(command);
		DefaultExecutor executor = new DefaultExecutor();
		try {
			int exitValue = executor.execute(cmdLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


