package com.github.report.object;

import java.util.ArrayList;
import java.util.List;

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
}
