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
  private Optional<ReleaseConfig> releaseConfig = Optional.empty();

  public RepoConfig()
  {
  }

  public RepoConfig(String name, String path, List<String> dependencies, Optional<String> command,
                    Optional<ReleaseConfig> releaseConfig)
  {
    this.name = Preconditions.checkNotNull(name);
    this.path = Preconditions.checkNotNull(path);
    this.dependencies = Preconditions.checkNotNull(dependencies);
    this.command = Preconditions.checkNotNull(command);
    this.releaseConfig = Preconditions.checkNotNull(releaseConfig);
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

  public Optional<ReleaseConfig> getReleaseConfig()
  {
    return releaseConfig;
  }

  public void setReleaseConfig(Optional<ReleaseConfig> releaseConfig)
  {
    this.releaseConfig = releaseConfig;
  }

  @Override
  public String toString() {
    return "RepoConfig{" +
      "name='" + name + '\'' +
      ", path='" + path + '\'' +
      ", dependencies=" + dependencies +
      ", command=" + command +
      ", releaseConfig=" + releaseConfig +
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
    if (!command.equals(that.command)) return false;
    return releaseConfig.equals(that.releaseConfig);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + path.hashCode();
    result = 31 * result + dependencies.hashCode();
    result = 31 * result + command.hashCode();
    result = 31 * result + releaseConfig.hashCode();
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

  public static class ReleaseConfig {
    private String mainBranch;
    private String releaseCommand;

    public ReleaseConfig() {
    }

    public ReleaseConfig(String mainBranch, String releaseCommand) {
      this.mainBranch = Preconditions.checkNotNull(mainBranch);
      this.releaseCommand = Preconditions.checkNotNull(releaseCommand);
    }

    public String getMainBranch() {
      return mainBranch;
    }

    public void setMainBranch(String mainBranch) {
      this.mainBranch = mainBranch;
    }

    public String getReleaseCommand() {
      return releaseCommand;
    }

    public void setReleaseCommand(String releaseCommand) {
      this.releaseCommand = releaseCommand;
    }

    @Override
    public String toString() {
      return "ReleaseConfig{" +
        "mainBranch='" + mainBranch + '\'' +
        ", releaseCommand='" + releaseCommand + '\'' +
        '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ReleaseConfig that = (ReleaseConfig) o;

      if (!mainBranch.equals(that.mainBranch)) return false;
      return releaseCommand.equals(that.releaseCommand);
    }

    @Override
    public int hashCode() {
      int result = mainBranch.hashCode();
      result = 31 * result + releaseCommand.hashCode();
      return result;
    }
  }
}
