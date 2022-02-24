package jesus.gabriel.hashcode;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Set;
import jesus.gabriel.hashcode.files.FileReader;
import jesus.gabriel.hashcode.files.HashCodeFileWriter;
import jesus.gabriel.hashcode.photo.Photo;
import jesus.gabriel.hashcode.photo.Slideshow;

public class Main {

	public static void main(String[] args) throws URISyntaxException {
		final String inputResourceName = args[0];
		final String outputFileName = args[1];
//		final Set<Photo> photos = new FileReader().parseInputFile(new File(Main.class.getResource(inputResourceName).toURI()));
//		
//		final Slideshow slideshow = new SlideshowResolver().resolveSlideshow(photos);
		
		new HashCodeFileWriter().writeToOutputFile(outputFileName, slideshow);
	}
	
}
