package jesus.gabriel.hashcode.model;

import java.util.List;
import lombok.Data;

@Data
public class AssignedProject {
  Project project;

  List<String> orderedContributorNames;
}