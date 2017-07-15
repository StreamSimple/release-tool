package com.simplifi.it.rt.build;

import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RepoConfig
{
  private String name;
  private String path;
  private List<String> dependencies;
  private Optional<String> command = Optional.empty();

  public RepoConfig()
  {
  }

  public RepoConfig(String name, String path, List<String> dependencies, Optional<String> command)
  {
    this.name = Preconditions.checkNotNull(name);
    this.path = Preconditions.checkNotNull(path);
    this.dependencies = Preconditions.checkNotNull(dependencies);
    this.command = Preconditions.checkNotNull(command);
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

  public Optional<String> getCommand()
  {
    return command;
  }

  public void setCommand(Optional<String> command)
  {
    this.command = command;
  }

  @Override
  public String toString() {
    return "RepoConfig{" +
      "name='" + name + '\'' +
      ", path='" + path + '\'' +
      ", dependencies=" + dependencies +
      ", command=" + command +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoConfig that = (RepoConfig) o;

    if (!name.equals(that.name)) return false;
    if (!path.equals(that.path)) return false;
    if (!dependencies.equals(that.dependencies)) return false;
    return command.equals(that.command);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + path.hashCode();
    result = 31 * result + dependencies.hashCode();
    result = 31 * result + command.hashCode();
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
