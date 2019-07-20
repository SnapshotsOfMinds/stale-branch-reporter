package com.github.report.object;

import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Object representation of an Organization.
 */
public class Organization {
  private String name;
  private String image;

  /**
   * Constructor to create an Organization object.
   *
   * @param name The name of the organization.
   * @param image The URL to the organization image.
   */
  public Organization(String name, String image) {
    this.name = name;
    this.image = image;
  }

  /**
   * Retrieve the Organization name.
   *
   * @return The Organization name.
   */
  public String getOrgName() {
    return name;
  }

  /**
   * Retrieve the Organization image URL.
   *
   * @return The Organization image URL.
   */
  public String getOrgImage() {
    return image;
  }

  @Override
  public String toString() {
    return "[{Organization} Name: " + name + "Image URL: " + image;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Organization)) {
      return false;
    }

    final Organization other = (Organization) o;
    return Objects.equals(this.name, other.name) && Objects.equals(this.image, other.image);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.name).append(this.image).toHashCode();
  }
}
