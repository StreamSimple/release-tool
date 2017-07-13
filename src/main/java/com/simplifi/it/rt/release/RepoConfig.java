package com.simplifi.it.rt.release;

import com.google.common.base.Preconditions;

import java.util.Optional;

public class RepoConfig
{
  public static final String DEFAULT_MAIN_BRANCH = "master";

  private String path;
  private String mainBranch = DEFAULT_MAIN_BRANCH;
  private Optional<String> releaseCommand = Optional.empty();

  public RepoConfig()
  {
  }

  public RepoConfig(String path, String mainBranch, Optional<String> releaseCommand)
  {
    this.path = Preconditions.checkNotNull(path);
    this.mainBranch = Preconditions.checkNotNull(mainBranch);
    this.releaseCommand = Preconditions.checkNotNull(releaseCommand);
  }

  public String getPath()
  {
    return path;
  }

  public void setPath(String path)
  {
    this.path = path;
  }

  public String getMainBranch()
  {
    return mainBranch;
  }

  public void setMainBranch(String mainBranch)
  {
    this.mainBranch = mainBranch;
  }

  public Optional<String> getReleaseCommand()
  {
    return releaseCommand;
  }

  public void setReleaseCommand(Optional<String> releaseCommand)
  {
    this.releaseCommand = releaseCommand;
  }

  @Override
  public String toString()
  {
    return "RepoConfig{" +
      "path='" + path + '\'' +
      ", mainBranch='" + mainBranch + '\'' +
      ", releaseCommand=" + releaseCommand +
      '}';
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoConfig that = (RepoConfig) o;

    if (!path.equals(that.path)) return false;
    if (!mainBranch.equals(that.mainBranch)) return false;
    return releaseCommand.equals(that.releaseCommand);
  }

  @Override
  public int hashCode()
  {
    int result = path.hashCode();
    result = 31 * result + mainBranch.hashCode();
    result = 31 * result + releaseCommand.hashCode();
    return result;
  }
}
