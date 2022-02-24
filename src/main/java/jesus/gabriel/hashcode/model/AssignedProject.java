package jesus.gabriel.hashcode.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignedProject {
  Project project;

  List<String> orderedContributorNames;
}