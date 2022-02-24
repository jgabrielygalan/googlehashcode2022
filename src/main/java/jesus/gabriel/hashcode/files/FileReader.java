package jesus.gabriel.hashcode.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import jesus.gabriel.hashcode.model.Contributor;
import jesus.gabriel.hashcode.model.Project;
import jesus.gabriel.hashcode.model.Role;
import jesus.gabriel.hashcode.model.TupleProjectsContributors;

public class FileReader {

	public TupleProjectsContributors parseInputFile(File f) {
		TupleProjectsContributors input = new TupleProjectsContributors();
		final Set<Contributor> contributors = new HashSet<>();
		final Set<Project> projects = new HashSet<>();
		try {
		  final Scanner sc = new Scanner(f);
		  final int numberOfContributors = sc.nextInt();
		  final int numberOfProjects = sc.nextInt();
          for(int id=0; id<numberOfContributors; id++) {
            final String contributorName = sc.next();
            final int numberOfSkills = sc.nextInt();
            final Map<String, Integer> skills = new HashMap<>();
            for (int skillId = 0; skillId<numberOfSkills; skillId++) {
              final String skillName = sc.next();
              final int skillLevel = sc.nextInt();
              skills.put(skillName, skillLevel);
            }
            final Contributor contributor = new Contributor();
            contributor.setName(contributorName);
            contributor.setSkills(skills);
            contributors.add(contributor);
          }
          for(int id=0; id<numberOfProjects; id++) {
            final String projectName = sc.next();
            final int numberOfDaysToComplete = sc.nextInt();
            final int projectScore = sc.nextInt();
            final int bestBefore = sc.nextInt();
            final int numberOfRoles = sc.nextInt();
            final Project project = new Project();
            project.setName(projectName);
            project.setBestBefore(bestBefore);
            project.setLength(numberOfDaysToComplete);
            project.setScore(projectScore);
            final List<Role> roles = new ArrayList<>();
            project.setRoles(roles);
            for(int roleId=0; roleId<numberOfRoles; roleId++) {
              final String skillName = sc.next();
              final int skillLevel = sc.nextInt();
              
              final Role role = new Role();
              role.setIndex(roleId);
              role.setName(skillName);
              role.setRequiredLevel(skillLevel);
              roles.add(role);
            }
            projects.add(project);
          }
          sc.close();
          input.setProjects(projects);
          input.setContributors(contributors);
          return input;
      } catch (FileNotFoundException e) {
          // ignore
          throw new RuntimeException(e);
      }
	}
	
}
