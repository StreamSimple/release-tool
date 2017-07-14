package com.simplifi.it.rt.release;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Preconditions;
import com.simplifi.it.rt.parse.ParseException;
import com.simplifi.it.rt.parse.ParseResult;

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

  public static ParseResult<ReleaseConfig> parse(InputStream dataInputStream) throws ParseException {
    ObjectMapper om = new ObjectMapper();
    om.registerModule(new Jdk8Module());

    try {
      ReleaseConfig releaseConfig = om.readValue(dataInputStream, ReleaseConfig.class);
      return new ParseResult<>(releaseConfig);
    } catch (IOException e) {
      throw new ParseException(e);
    }
  }
}
