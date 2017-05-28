package com.simplifi.it.rt.release;

import com.google.common.base.Preconditions;

/**
 * Created by tfarkas on 5/27/17.
 */
public class RepoConfig
{
  public static final String DEFAULT_MAIN_BRANCH = "master";

  private String path;
  private String mainBranch = DEFAULT_MAIN_BRANCH;

  public RepoConfig()
  {
  }

  public RepoConfig(String path, String mainBranch)
  {
    this.path = Preconditions.checkNotNull(path);
    this.mainBranch = Preconditions.checkNotNull(mainBranch);
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

  @Override
  public String toString()
  {
    return "RepoConfig{" +
      "path='" + path + '\'' +
      ", mainBranch='" + mainBranch + '\'' +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoConfig that = (RepoConfig) o;

    if (!path.equals(that.path)) return false;
    return mainBranch.equals(that.mainBranch);
  }

  @Override
  public int hashCode() {
    int result = path.hashCode();
    result = 31 * result + mainBranch.hashCode();
    return result;
  }
}
