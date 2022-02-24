package jesus.gabriel.hashcode.model;

import java.util.Map;

import lombok.Data;

@Data
public class Project {
	private String name;
	private Integer length;
	private Integer score;
	private Integer bestBefore;
	private Map<String, Integer> roles;
}
