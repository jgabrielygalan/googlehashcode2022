package jesus.gabriel.hashcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import jesus.gabriel.hashcode.model.Contributor;

public class AvailableContributorContainer {

  private final Map<String, TreeMap<Integer, Set<Contributor>>> contributorsBySkill;

  public AvailableContributorContainer(Set<Contributor> availableContributors) {
    this.contributorsBySkill = new HashMap<>();
    availableContributors.forEach(this::addContributor);
  }

  public Optional<Contributor> getWithSkillAndMinimumLevel(String skill, int minimumLevel) { // TODO
    final TreeMap<Integer, Set<Contributor>> innerMap = this.contributorsBySkill.get(skill);
    if (innerMap==null) {
      return Optional.empty();
    }
    final Entry<Integer, Set<Contributor>> entry = innerMap.ceilingEntry(minimumLevel);
    //System.out.println(Optional.ofNullable(entry).map(Entry::getKey) + " " + minimumLevel);
    if(entry==null || entry.getValue().isEmpty()) {
      return Optional.empty();
    }
    //System.out.println(entry.getKey() + " " + minimumLevel);
    final Contributor toReturn = entry.getValue().iterator().next();
    removeFromSkillsMap(toReturn);
    return Optional.of(toReturn);
  }

  public void addContributor(Contributor contributor) {
    contributor.getSkills().forEach((skill, level) -> {
      TreeMap<Integer, Set<Contributor>> innerMap = this.contributorsBySkill.get(skill);
      if (innerMap==null) {
        innerMap = new TreeMap<>();
        this.contributorsBySkill.put(skill, innerMap);
      }
      Set<Contributor> contributors = innerMap.get(level);
      if (contributors==null) {
        contributors = new HashSet<>();
        innerMap.put(level, contributors);
      }
      //System.out.println(contributor.getName());
      contributors.add(contributor);
    });
  }

  private void removeFromSkillsMap(Contributor contributor) {
    contributor.getSkills().forEach((skill, level) -> {
      this.contributorsBySkill.get(skill).get(level).remove(contributor);
      if (this.contributorsBySkill.get(skill).get(level).isEmpty()) {
        this.contributorsBySkill.get(skill).remove(level);
      }
    });
  }
}
