package jesus.gabriel.hashcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

  // cosas a tener en cuenta
  // puntos que da un proyecto (uno puede ser mas grande que el resto)
  // 
  // podriamos meter personas en un map con dias disponibles
	public List<AssignedProject> assignContributors(TupleProjectsContributors input) {
	  final int allowedIterations = 100;
	  int its = 0;
	  final List<AssignedProject> assignedProjects = new ArrayList<>();
		Set<Project> pending = input.getProjects();
		final AvailableContributorContainer contributorContainer = new AvailableContributorContainer(input.getContributors());
		Map<Integer, Set<BusyContributor>> willBeFreed = new HashMap<>();
		int day = 0;
		while(pending.size() > 0) {
		  its++;
		  if (its > allowedIterations)
		    break;
		  System.out.println(pending.size());
		  final Set<BusyContributor> contributorsToFree = willBeFreed.remove(day);
		  if (contributorsToFree != null) {
		    contributorsToFree.forEach(busyContributor -> {
		      if (busyContributor.isGetSkillIncrease()) {
		        final String skillName = busyContributor.getSkillUsed();
		        final Map<String, Integer> skillsMap = busyContributor.getContributor().getSkills();
		        skillsMap.put(skillName, skillsMap.get(skillName) + 1);
		      }
		    });
		    contributorsToFree.stream().map(BusyContributor::getContributor).forEach(contributorContainer::addContributor);
		  }
		  
			List<Project> projects = sortProjects(pending);
			for (Project project: projects) {
				Set<TupleAssignment> asignees = findContributorsFor(project, contributorContainer);
				if (asignees.size() > 0) {
					assignContributors(day, project, asignees, willBeFreed);
					pending.remove(project);
					final List<String> orderedContributors = asignees.stream()
					    .sorted(Comparator.comparing(TupleAssignment::getIndex))
					    .map(tuple -> tuple.getBusyContributor().getContributor().getName())
					    .toList();
					final AssignedProject assignedProject = new AssignedProject(project, orderedContributors);
					assignedProjects.add(assignedProject);
				}
			}
			if (willBeFreed.isEmpty()) {
			  //projects.stream().forEach(p -> System.out.println("pending " + p.getSkillLevel() + " " + p.getRoles()));
			  break;
			}
			day = willBeFreed.keySet().stream().min(Integer::compareTo).get();
			
		}
		//System.out.println(contributorContainer.getWithSkillAndMinimumLevel("Parallelization-NET", 0));
		//assignedProjects.forEach(p -> System.out.println("returning " + p.getProject().getSkillLevel() + " " + p.getProject().getRoles()));
		return assignedProjects;
	}

	private List<Project> sortProjects(Set<Project> pending) {
	  final int maxScore = pending.stream()
	      .map(Project::getScore)
	      .max(Comparator.naturalOrder())
	      .get();
	  return pending.stream()
	      .sorted(Comparator.comparing(p -> this.getProjectSortScore(p, maxScore)))
	      .toList();
	}

	private double getProjectSortScore(Project project, int maxScore) {
	  return (project.getScore() * 100) / maxScore;
	  //return  project.getSkillLevel();
	}
	
	private Set<TupleAssignment> findContributorsFor(Project project, AvailableContributorContainer contributorContainer) {
		Set<TupleAssignment> contributors = new HashSet<>();
		final int requiredNumberOfRoles = project.getRoles().size();
		for (Role role: project.getRoles()) {
			Optional<TupleAssignment> assignment = findContributor(contributorContainer, role);
			if (assignment.isPresent()) {
				contributors.add(assignment.get());
			}
		}
		if (contributors.size() == requiredNumberOfRoles) {
		  return contributors;
		} else {
		  contributors.stream()
		      .map(TupleAssignment::getBusyContributor)
		      .map(BusyContributor::getContributor)
		      .forEach(contributorContainer::addContributor);
		  return Collections.emptySet();
		}
		
	}
	
	private void assignContributors(Integer currentDay, Project project, Set<TupleAssignment> asignees, Map<Integer, Set<BusyContributor>> willBeFreed) {
		Integer finishDay = currentDay + project.getLength();
		if (willBeFreed.get(finishDay) == null) {
			willBeFreed.put(finishDay, new HashSet<>());
		}
		final List<BusyContributor> contributorsToAssign = asignees.stream()
		    .map(TupleAssignment::getBusyContributor)
		    .collect(Collectors.toList());
		willBeFreed.get(finishDay).addAll(contributorsToAssign);
	
	}
	
	private Optional<TupleAssignment> findContributor(AvailableContributorContainer contributorContainer, Role role) {
	  final int requiredLevel = role.getRequiredLevel();
	  return contributorContainer.getWithSkillAndMinimumLevel(role.getName(), requiredLevel)
	      .map(contributor -> {
	        final Integer contributorSkillLevelInteger = contributor.getSkills().get(role.getName());
	        final int contributorSkillLevel = contributorSkillLevelInteger!= null ? contributorSkillLevelInteger : 0;
	        final boolean getsIncrease = requiredLevel >= contributorSkillLevel;
	        final BusyContributor busyContributor = new BusyContributor(contributor, getsIncrease, role.getName());
            return new TupleAssignment(busyContributor, role.getIndex());
	      });
	}

}

@Value
class TupleAssignment {
	private BusyContributor busyContributor;
	private Integer index;
}

@Value
class BusyContributor {
  Contributor contributor;
  boolean getSkillIncrease;
  String skillUsed;
}
