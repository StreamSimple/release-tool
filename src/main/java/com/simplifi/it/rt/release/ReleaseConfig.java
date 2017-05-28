package com.simplifi.it.rt.release;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ReleaseConfig
{
  private List<RepoConfig> repoConfigs;

  public ReleaseConfig()
  {
  }

  public ReleaseConfig(List<RepoConfig> repoConfigs)
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
    return "ReleaseConfig{" +
      "repoConfigs=" + repoConfigs +
      '}';
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ReleaseConfig that = (ReleaseConfig) o;

    return repoConfigs.equals(that.repoConfigs);
  }

  @Override
  public int hashCode()
  {
    return repoConfigs.hashCode();
  }

  public static ReleaseConfig parse(InputStream dataInputStream) throws IOException {
    ObjectMapper om = new ObjectMapper();
    return om.readValue(dataInputStream, ReleaseConfig.class);
  }
}
