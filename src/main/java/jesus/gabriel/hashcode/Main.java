package jesus.gabriel.hashcode;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Set;

import jesus.gabriel.hashcode.files.FileReader;
import jesus.gabriel.hashcode.files.HashCodeFileWriter;
import jesus.gabriel.hashcode.model.AssignedProject;
import jesus.gabriel.hashcode.model.TupleProjectsContributors;

public class Main {

	public static void main(String[] args) throws URISyntaxException {
		final String inputResourceName = args[0];
		final String outputFileName = args[1];
		final TupleProjectsContributors input = new FileReader().parseInputFile(new File(Main.class.getResource(inputResourceName).toURI()));
		
		final Set<AssignedProject> assignments = new ProjectAssignmentResolver().assignContributors(input);
		
		new HashCodeFileWriter().writeToOutputFile(outputFileName, assignments);
	}
	
}
