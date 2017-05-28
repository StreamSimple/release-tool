package com.simplifi.it.rt.build;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Created by tfarkas on 5/27/17.
 */
public class BuildConfig
{
  private List<RepoConfig> repoConfigs;

  public BuildConfig()
  {
  }

  public BuildConfig(List<RepoConfig> repoConfigs)
  {
    this.repoConfigs = Preconditions.checkNotNull(repoConfigs);
  }

  public List<RepoConfig> getRepoConfigs()
  {
    return repoConfigs;
  }

  public void setRepoConfigs(List<RepoConfig> repoConfigs)
  {
    this.repoConfigs = repoConfigs;
  }

  @Override
  public String toString()
  {
    return "BuildConfig{" +
      "repoConfigs=" + repoConfigs +
      '}';
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BuildConfig that = (BuildConfig) o;

    return repoConfigs.equals(that.repoConfigs);
  }

  @Override
  public int hashCode()
  {
    return repoConfigs.hashCode();
  }
}
