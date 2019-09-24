import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilterFiles implements FilenameFilter{
	public boolean accept(File f, String path) {
		if (!path.matches(".{1,}_p[0-9]{1,}.{1,}")) {
			System.out.println(path+" is not a pixiv image!");
			return false;
		}
		for (String filter : imageTag.filters.filters) {
			if (path.toLowerCase().endsWith(filter.toLowerCase())) {
				//System.out.println("Accepting "+path+" because of filter "+filter);
				return true;
			}
		}
		return false;
	}
}