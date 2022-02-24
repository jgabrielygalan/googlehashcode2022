package jesus.gabriel.hashcode.model;

import java.util.Map;

import lombok.Data;

@Data
public class Contributor {
	private String name;
	private Map<String, Integer> skills;
}
