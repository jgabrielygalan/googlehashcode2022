package jesus.gabriel.hashcode.model;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class Project {
	private String name;
	private Integer length;
	private Integer score;
	private Integer bestBefore;
	private List<Role> roles;
	
	public double getSkillLevel() {
	  return roles.stream()
	      .map(Role::getRequiredLevel)
	      .collect(Collectors.summingDouble(lvl -> (double) lvl)) / roles.size();
	}
}
