import java.io.File;

import javax.swing.JFileChooser;

public class FileChooser {
	
	public FileChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int val = chooser.showOpenDialog(null);
		
		FilterFiles filter = new FilterFiles();
		
		if (val==JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFile().listFiles(filter);
			for (File file : files) {
		        if (file.isFile()) {
		            String filename = file.getName();
		            String newName = ReplaceExtraBits(filename);
		        	System.out.println("Adding to Pixiv Album: " + newName);
		            imageTag.pixiv_image_list.add(newName);
		            imageTag.pixiv_rawimage_list.add(file);
		        }
		    }
		}
	}
	
	String ReplaceExtraBits(String filename) {
		return filename
				.replaceAll("\\..{1,}", "") //regex to replace file extensions
				.replaceAll("\\(.{1,}\\)", "") //regex to remove weird trimead parenthesis
				.replaceAll("_p.{1,}","") //Remove the p stuff from pixiv images.
				.trim(); //trim whitespace
	}
}
