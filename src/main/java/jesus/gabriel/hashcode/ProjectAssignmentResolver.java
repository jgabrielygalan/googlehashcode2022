package jesus.gabriel.hashcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jesus.gabriel.hashcode.model.AssignedProject;
import jesus.gabriel.hashcode.model.Contributor;
import jesus.gabriel.hashcode.model.Project;
import jesus.gabriel.hashcode.model.Role;
import jesus.gabriel.hashcode.model.TupleProjectsContributors;
import lombok.Value;

public class ProjectAssignmentResolver {

	public Set<AssignedProject> assignContributors(TupleProjectsContributors input) {
		Set<Project> pending = input.getProjects();
		Set<Contributor> available = input.getContributors();
		Map<Integer, Set<Contributor>> willBeFreed = new HashMap<>();
		Integer day = 0;
		while(pending.size() > 0) {
			List<Project> projects = sortProjects(pending);
			for (Project project: projects) {
				Set<TupleAssignment> asignees = findContributorsFor(project, available);
				if (asignees.size() > 0) {
					assignContributors(day, project, asignees, willBeFreed);
				}
			}
			
		}
	}

	private List<Project> sortProjects(Set<Project> pending) {
		return new ArrayList<>(pending);
	}
	
	private Set<TupleAssignment> findContributorsFor(Project project, Set<Contributor> available) {
		Set<TupleAssignment> contributors = new HashSet<>();
		for (Map.Entry<String, Role> entry: project.getRoles().entrySet()) {
			Optional<TupleAssignment> assignment = findContributor(available, entry);
			if (assignment.isPresent()) {
				available.remove(assignment.get().getContributor());
				contributors.add(assignment.get());
			}
		}
		return contributors;
	}
	
	private void assignContributors(Integer currentDay, Project project, Set<TupleAssignment> asignees, Map<Integer, Set<Contributor>> willBeFreed) {
		Integer finishDay = currentDay + project.getLength();
		if (willBeFreed.get(finishDay) == null) {
			willBeFreed.put(finishDay, new HashSet<>());
		}
		willBeFreed.get(finishDay).addAll(asignees.stream().map(TupleAssignment::getContributor).collect(Collectors.toList()));
	
	}
	
	private Optional<TupleAssignment> findContributor(Set<Contributor> contributors, Map.Entry<String, Role> entry) {
		for (Contributor contributor: contributors) {
			if (contributor.getSkills().get(entry.getKey()) > entry.getValue().getRequiredLevel()) {
				return Optional.of(new TupleAssignment(contributor, entry.getValue().getIndex()));
			}
		}
		return Optional.empty();
	}

}

@Value
class TupleAssignment {
	private Contributor contributor;
	private Integer index;
}
