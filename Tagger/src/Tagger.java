import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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
		StringBuilder sb = new StringBuilder();
		if (tags!=null) {
			for (String tag : tags) {
				if (sb.length()!=0) {
					sb.append("; ");
				}
				sb.append(tag);
			}
		
			Process tool = Runtime.getRuntime().exec("tool.exe -exif:XPKeywords=\""+sb.toString()+"\" "+imagetoTag.getAbsolutePath()+" -overwrite_original_in_place -P");
			
			BufferedReader stdInput = new BufferedReader(new 
				     InputStreamReader(tool.getInputStream()));
			String s = null;
			while ((s = stdInput.readLine()) != null) {
			    System.out.println("Tagging "+imagetoTag.getName()+":"+s);
			}
		}
	}
}
