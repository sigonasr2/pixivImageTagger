import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Filters {
	List<String> filters = new ArrayList<String>();
	public Filters() throws IOException {
		/*File f = new File("filters.txt");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String s = br.readLine();
		while (s!=null) {
			String newfilter = s.trim();
			filters.add(newfilter);
			System.out.println("Adding filter "+newfilter);
			s = br.readLine();
		}
		br.close(); 
		fr.close();*/
		filters.add("jpg");
		filters.add("jpeg");
		filters.add("tga");
		filters.add("tif");
		filters.add("tiff");
		filters.add("png");
		filters.add("bmp");
		filters.add("gif");
		/*jpg
		jpeg
		tga
		tif
		tiff
		png
		bmp
		gif*/
	}
}
