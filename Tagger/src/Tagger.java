import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputField;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

public class Tagger {
	public Tagger(File imagetoTag,List<String> tags) throws ImageReadException, IOException {		
		System.out.println("File: "+imagetoTag.getAbsolutePath()+", "+tags);
		StringBuilder sb = new StringBuilder();
		if (tags!=null) {
			for (String tag : tags) {
					if (imageTag.subtaglist.containsKey(tag)) { 
						List<String> subtag = imageTag.subtaglist.get(tag);
						/* info needed- subtags and
						 *  action- look up the value for the subtag */
					}
				if (sb.length()!=0) {
					sb.append("; ");
				}
				sb.append(tag);
			}
		
			//Process tool = Runtime.getRuntime().exec("tool.exe -exif:XPKeywords=\""+sb.toString()+"\" "+imagetoTag.getAbsolutePath()+" -overwrite_original_in_place -P");
			String command = "tool.exe -exif:XPKeywords=\""+sb.toString()+"\" \""+imagetoTag.getAbsolutePath()+"\" -overwrite_original_in_place -P";
			CommandLine cmdLine = CommandLine.parse(command);
			DefaultExecutor executor = new DefaultExecutor();
			int exitValue = executor.execute(cmdLine);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
