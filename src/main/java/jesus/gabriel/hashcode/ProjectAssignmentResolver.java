package jesus.gabriel.hashcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jesus.gabriel.hashcode.model.AssignedProject;
import jesus.gabriel.hashcode.model.Contributor;
import jesus.gabriel.hashcode.model.Project;
import jesus.gabriel.hashcode.model.TupleProjectsContributors;

public class ProjectAssignmentResolver {

	public Set<AssignedProject> assignContributors(TupleProjectsContributors input) {
		Set<Project> pending = input.getProjects();
		Set<Contributor> available = input.getContributors();
		Map<Integer, Set<Contributor>> willBeFreed = new HashMap<>();
		while(pending.size() > 0) {
			List<Project> projects = sortProjects(pending);
			for (Project project: projects) {
				Set<Contributor> asignees = findContributorsFor(project, available);
				if (asignees.size() > 0) {
					assignContributors(project, asignees, willBeFreed);
				}
			}
		
		}
	}

}
