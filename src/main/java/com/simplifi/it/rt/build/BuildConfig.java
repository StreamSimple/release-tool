package com.simplifi.it.rt.build;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Preconditions;
import com.simplifi.it.rt.parse.ParseError;
import com.simplifi.it.rt.parse.ParseResult;

import java.io.IOException;
import java.io.InputStream;
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

  public static ParseResult<BuildConfig> parse(InputStream dataInputStream)
  {
    ObjectMapper om = new ObjectMapper();
    om.registerModule(new Jdk8Module());

    try {
      BuildConfig buildConfig = om.readValue(dataInputStream, BuildConfig.class);
      return new ParseResult<>(buildConfig);
    } catch (IOException e) {
      return new ParseResult<>(new ParseError(e));
    }
  }
}
