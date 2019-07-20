package com.github.report.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Object representation of a Repository.
 */
public class Repository {
  private String name;
  private List<Branch> branches = new ArrayList<>();

  public Repository(String name) {
    this.name = name;
  }

  public String getRepoName() {
    return name;
  }

  public List<Branch> getAllBranches() {
    return branches;
  }

  public void setBranches(List<Branch> branches) {
    this.branches = branches;
  }

  @Override
  public String toString() {
    return "[{Repository} Name: " + name + ", Branches: " + branches.stream().map(Branch::toString).collect(Collectors.joining("\n"));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Repository)) {
      return false;
    }

    final Repository other = (Repository) o;
    return Objects.equals(this.name, other.name) && Objects.equals(this.branches, other.branches);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.name).append(this.branches).toHashCode();
  }
}
