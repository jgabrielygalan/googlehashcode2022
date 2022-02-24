package jesus.gabriel.hashcode.files;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import jesus.gabriel.hashcode.model.AssignedProject;

public class HashCodeFileWriter {

	public void writeToOutputFile(String filePath, Set<AssignedProject> assignedProjects) {
		try(final FileWriter fw = new FileWriter(filePath)) {
		  final int size = assignedProjects.size();
		  fw.write(size);
		  fw.write("\n");
		  assignedProjects.forEach(assignedProject -> {
		    try {
		    fw.write(assignedProject.getProject().getName());
		    fw.write("\n");
		    final String contributors = assignedProject.getOrderedContributorNames().stream()
		    .collect(Collectors.joining(" "));
		    fw.write(contributors);
		    fw.write("\n");
		    } catch(Exception ee) {
		      throw new RuntimeException(ee);
		    }
		  });
		} catch (IOException e) {
			// ignore
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
