package jesus.gabriel.hashcode.model;

import java.util.Set;

import lombok.Data;

@Data
public class TupleProjectsContributors {
	private Set<Project> projects;
	private Set<Contributor> contributors;
}
