package com.simplifi.it.rt.config;

import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RepoConfig {
  private String projectType;
  private String name;
  private String srcBranch;
  private String path;
  private List<String> dependencies;
  private Optional<String> command = Optional.empty();
  private Optional<ReleaseConfig> releaseConfig = Optional.empty();

  public RepoConfig() {
  }

  public RepoConfig(String projectType,
                    String name,
                    String srcBranch,
                    String path,
                    List<String> dependencies,
                    Optional<String> command,
                    Optional<ReleaseConfig> releaseConfig) {
    this.projectType = Preconditions.checkNotNull(projectType);
    this.name = Preconditions.checkNotNull(name);
    this.srcBranch = Preconditions.checkNotNull(srcBranch);
    this.path = Preconditions.checkNotNull(path);
    this.dependencies = Preconditions.checkNotNull(dependencies);
    this.command = Preconditions.checkNotNull(command);
    this.releaseConfig = Preconditions.checkNotNull(releaseConfig);
  }

  public String getProjectType() {
    return projectType;
  }

  public void setProjectType(String projectType) {
    this.projectType = projectType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSrcBranch() {
    return srcBranch;
  }

  public void setSrcBranch(String srcBranch) {
    this.srcBranch = srcBranch;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public List<String> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<String> dependencies) {
    this.dependencies = dependencies;
  }

  public Optional<String> getCommand() {
    return command;
  }

  public void setCommand(Optional<String> command) {
    this.command = command;
  }

  public Optional<ReleaseConfig> getReleaseConfig() {
    return releaseConfig;
  }

  public void setReleaseConfig(Optional<ReleaseConfig> releaseConfig) {
    this.releaseConfig = releaseConfig;
  }

  @Override
  public String toString() {
    return "RepoConfig{" +
      "projectType='" + projectType + '\'' +
      ", name='" + name + '\'' +
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

    if (!projectType.equals(that.projectType)) return false;
    if (!name.equals(that.name)) return false;
    if (!path.equals(that.path)) return false;
    if (!dependencies.equals(that.dependencies)) return false;
    if (!command.equals(that.command)) return false;
    return releaseConfig.equals(that.releaseConfig);
  }

  @Override
  public int hashCode() {
    int result = projectType.hashCode();
    result = 31 * result + name.hashCode();
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
    private Optional<String> branchPrefix = Optional.empty();
    private Optional<String> command = Optional.empty();
    private Optional<String> versionHandler = Optional.empty();

    public ReleaseConfig() {
    }

    public ReleaseConfig(Optional<String> branchPrefix,
                         Optional<String> command,
                         Optional<String> versionHandler) {
      this.branchPrefix = Preconditions.checkNotNull(branchPrefix);
      this.command = Preconditions.checkNotNull(command);
      this.versionHandler = Preconditions.checkNotNull(versionHandler);
    }

    public Optional<String> getBranchPrefix() {
      return branchPrefix;
    }

    public void setBranchPrefix(Optional<String> branchPrefix) {
      this.branchPrefix = branchPrefix;
    }

    public Optional<String> getCommand() {
      return command;
    }

    public void setCommand(Optional<String> command) {
      this.command = command;
    }

    public Optional<String> getVersionHandler() {
      return versionHandler;
    }

    public void setVersionHandler(Optional<String> versionHandler) {
      this.versionHandler = versionHandler;
    }

    @Override
    public String toString() {
      return "ReleaseConfig{" +
        "branchPrefix='" + branchPrefix + '\'' +
        ", command='" + command + '\'' +
        ", versionHandler='" + versionHandler + '\'' +
        '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ReleaseConfig that = (ReleaseConfig) o;

      if (!branchPrefix.equals(that.branchPrefix)) return false;
      if (!command.equals(that.command)) return false;
      return versionHandler.equals(that.versionHandler);
    }

    @Override
    public int hashCode() {
      int result = branchPrefix.hashCode();
      result = 31 * result + command.hashCode();
      result = 31 * result + versionHandler.hashCode();
      return result;
    }
  }
}
