package com.simplifi.it.rt.build;

import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.List;

public class RepoConfig
{
  private String name;
  private String path;
  private List<String> dependencies;

  public RepoConfig()
  {
  }

  public RepoConfig(String name, String path, List<String> dependencies)
  {
    this.name = Preconditions.checkNotNull(name);
    this.path = Preconditions.checkNotNull(path);
    this.dependencies = Preconditions.checkNotNull(dependencies);
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getPath()
  {
    return path;
  }

  public void setPath(String path)
  {
    this.path = path;
  }

  public List<String> getDependencies()
  {
    return dependencies;
  }

  public void setDependencies(List<String> dependencies)
  {
    this.dependencies = dependencies;
  }

  @Override
  public String toString()
  {
    return "RepoConfig{" +
      "name='" + name + '\'' +
      ", path='" + path + '\'' +
      ", dependencies=" + dependencies +
      '}';
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoConfig that = (RepoConfig) o;

    if (!name.equals(that.name)) return false;
    if (!path.equals(that.path)) return false;
    return dependencies.equals(that.dependencies);
  }

  @Override
  public int hashCode()
  {
    int result = name.hashCode();
    result = 31 * result + path.hashCode();
    result = 31 * result + dependencies.hashCode();
    return result;
  }

  public static class NameComparator implements Comparator<RepoConfig> {
    public static final NameComparator INSTANCE = new NameComparator();

    private NameComparator() {
      // Singleton
    }

    @Override
    public int compare(RepoConfig configA, RepoConfig configB) {
      return configA.getName().compareTo(configB.getName());
    }
  }
}
