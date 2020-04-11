import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.imaging.ImageReadException;
import org.json.JSONArray;
import org.json.JSONObject;

public class PixivManager {
	public PixivManager() {
		File folder = new File("downloadedData");
		if (folder.exists()) {
			for (File fff : folder.listFiles()) {
				if (fff.isFile()) {
					fff.delete();
				}
			}
		}
		File skippedItems = new File("skippedItems.txt");
		skippedItems.delete();
		folder.mkdirs();
		File outputTest = new File("TAG_DATA.txt");
		outputTest.delete();
		FileWriter fwOutput,fwOutput2;
		BufferedWriter bwOutput,bwOutput2;
		try {
			fwOutput = new FileWriter(outputTest,true);
			bwOutput = new BufferedWriter(fwOutput);
			
			for (String s : imageTag.pixiv_image_list) {
				String url = "https://www.pixiv.net/en/artworks/"+s;
				try {
					if (!new File("downloadedData/temp"+s+".html").exists()) {
						System.out.println("Starting download of "+url+" ...");
						utils.downloadFileFromUrl(url, "downloadedData/temp"+s+".html");
						if (new File("downloadedData/temp"+s+".html").exists()) {
							String[] data = utils.readFromFile("downloadedData/temp"+s+".html");
							int scriptEndLine = 0;
							while (scriptEndLine<data.length) {
								if (data[scriptEndLine].contains("<meta name=\"preload-data\" id=\"meta-preload-data\" content='")) {
									System.out.println("Found JSON Target line at line "+scriptEndLine+". :: "+data[scriptEndLine] );
									break;
								}
								scriptEndLine++;
							}
							if (scriptEndLine==data.length) {
								System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
								System.out.println("  IMAGE "+s+" FAILED TO PARSE CORRECTLY! Something is messed up about the file!!");
								System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							}
							File finaldata = new File("finaltemp");
							FileWriter fw;
							try {
								fw = new FileWriter(finaldata);
								BufferedWriter bw = new BufferedWriter(fw);
								System.out.println(data[scriptEndLine]);
								int cutpos = data[scriptEndLine].indexOf("<meta name=\"preload-data\" id=\"meta-preload-data\" content='")+58;
								System.out.println(data[scriptEndLine].length()+"///"+data[scriptEndLine].indexOf("}}}'>")+"///"+cutpos);
								if (cutpos<data[scriptEndLine].length()) {
									bw.write(data[scriptEndLine].substring(cutpos,data[scriptEndLine].indexOf("}}}'>")+3));
									System.out.println(data[scriptEndLine].substring(cutpos,data[scriptEndLine].indexOf("}}}'>")+3));
								}
								bw.close();
								fw.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							JSONObject jsonData = utils.readJsonFromFile("finaltemp");
							//System.out.println(Arrays.deepToString(JSONObject.getNames(jsonData.getJSONObject("preload"))));
							//System.out.println(Arrays.deepToString(JSONObject.getNames(jsonData.getJSONObject("preload").getJSONObject("illust"))));
							JSONArray tagsArray = jsonData.getJSONObject("illust").getJSONObject(s).getJSONObject("tags").getJSONArray("tags");
							for (int i=0;i<tagsArray.length();i++) {
								boolean hasEnglishTag=false;
								JSONObject tag = tagsArray.getJSONObject(i);
								String ENTag="";
								//String romaji="";
								/*if (tag.has("romaji") && !tag.isNull("romaji")) {
									romaji = tag.getString("romaji");
								}*/
								if (tag.has("translation")) {
									JSONObject translationObj = tag.getJSONObject("translation");
									if (translationObj.has("en")) {
										hasEnglishTag=true;
										ENTag = translationObj.getString("en");
									}
								} else
								if (tag.has("tag") /*&& romaji.length()==0 */&& !tag.getString("tag").matches(".*[ぁ-んァ-ン一-龯]")) {
									hasEnglishTag=true;
									ENTag = tag.getString("tag");
								}
								
								if (ENTag.replaceAll("\\?", "").trim().length()==0) {
									ENTag="";
									hasEnglishTag=false;
								}
								boolean tagSubmitted=false;
								String insertedTag="";
								if (hasEnglishTag && ENTag.length()>0) {
									insertedTag = ENTag;
									tagSubmitted=true;
								} /*else 
								if (romaji.length()>0){
									insertedTag = romaji;
									tagSubmitted=true;
								}*/
								
								//insertedTag is the tag that will be used for the image.
								insertedTag = ConvertTag(insertedTag.trim().toLowerCase());
								
								if (tagSubmitted) {
									if (imageTag.tag_whitelist.size()==0 || imageTag.tag_whitelist.containsKey(insertedTag.trim().toLowerCase())) {
										if (imageTag.taglist.containsKey(s)) {
											List<String> tags = imageTag.taglist.get(s);
											tags.add(insertedTag);
											imageTag.taglist.put(s, tags);
										} else {
											List<String> tags = new ArrayList<String>();
											tags.add(insertedTag);
											imageTag.taglist.put(s,tags);
										}
										if (imageTag.tagCounter.containsKey(insertedTag)) {
											imageTag.tagCounter.put(insertedTag,imageTag.tagCounter.get(insertedTag)+1);
										} else {
											imageTag.tagCounter.put(insertedTag,1);
										}
									}
								}
							}
							String taglist = s+": <"+imageTag.taglist.get(s)+">";
							System.out.println(taglist);
							bwOutput.append(taglist);
							bwOutput.newLine();
							//jsonData.getJSONObject("preload").getJSONObject("illust").getJSONObject(s).getJSONObject("tags");
						} else {
							System.out.println("Skipping image "+s+" because webpage cannot be found.");
							utils.logToFile(s+"\n", "skippedItems.txt");
						}
					} else {
						System.out.println("Skipping image "+s+" because it has already been processed.");
					}
				} catch (IOException e) {
					if (e instanceof FileNotFoundException) {
						System.out.println("Skipping image "+s+" because webpage cannot be found.");
						utils.logToFile(s, "skippedItems.txt");
					} else {
						e.printStackTrace();
					}
				}
				/*org.apache.commons.io.FileUtils.copyURLToFile(new URL(
						url
						),temp);*/
			}
			
			/*for (String s : imageTag.taglist.keySet()) {
				System.out.println(s+": <"+imageTag.taglist.get(s)+">");
			}*/

			bwOutput.close();
			fwOutput.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		Map sorted = sortByValues(imageTag.tagCounter);
		Set s = sorted.entrySet();
		Iterator i = s.iterator();
		File tagFile = new File("SORTED_TAGS.txt");
		File tagFile2 = new File("RAW_TAGS.txt");
		FileWriter fw,fw2;
		try {
			fw = new FileWriter(tagFile);
			fw2 = new FileWriter(tagFile2);
			BufferedWriter bw = new BufferedWriter(fw);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			while (i.hasNext()) {
				Map.Entry m  = (Map.Entry)i.next();
				String key = (String)m.getKey();
				Integer value = (Integer)m.getValue();
				bw.write(key+" - "+value);
				bw.newLine();
				bw2.write(key);
				bw2.newLine();
			}
			bw.close();
			bw2.close();
			fw.close();
			fw2.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (imageTag.tag_whitelist.size()==0) {
			System.out.println("whitelist.txt not found! No tagging will be done this time. TAG_DATA.txt populated.");
		} else {
			System.out.println("Tagging Images...");
			for (int j=0;j<imageTag.pixiv_image_list.size();j++) {
				try {
					Tagger tags = new Tagger(imageTag.pixiv_rawimage_list.get(j),imageTag.taglist.get(imageTag.pixiv_image_list.get(j)));
				} catch (ImageReadException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//System.out.println("Tagged "+ss+" with "+tagString);
			}
		}
	}
	
	
	private String ConvertTag(String insertedTag) {
		if (imageTag.subtaglist.containsKey(insertedTag.trim().toLowerCase())) {
			System.out.println("  Converting subtag "+insertedTag.trim().toLowerCase()+"->"+imageTag.subtaglist.get(insertedTag.trim().toLowerCase()));
			return imageTag.subtaglist.get(insertedTag.trim().toLowerCase());
		}
		return insertedTag;
	}


	public static <K, V extends Comparable<V>> Map<K, V> 
    sortByValues(final Map<K, V> map) {
    Comparator<K> valueComparator = 
             new Comparator<K>() {
      public int compare(K k1, K k2) {
        int compare = 
              -map.get(k1).compareTo(map.get(k2));
        if (compare == 0) 
          return 1;
        else 
          return compare;
      }
    };
 
    Map<K, V> sortedByValues = 
      new TreeMap<K, V>(valueComparator);
    sortedByValues.putAll(map);
    return sortedByValues;
  }
}
