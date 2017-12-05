package com.simplifi.it.rt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;
import com.simplifi.it.rt.dag.DAG;
import com.simplifi.it.rt.dag.Edge;
import com.simplifi.it.rt.parse.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigFile
{
  private List<RepoConfig> repoConfigs;

  public ConfigFile()
  {
  }

  public ConfigFile(List<RepoConfig> repoConfigs)
  {
    this.repoConfigs = Preconditions.checkNotNull(repoConfigs);
  }

  public List<RepoConfig> getRepoConfigs()
  {
    return repoConfigs;
  }

  /**
   * This method should not be used directly. Use parse.
   * @param repoConfigs
   */
  public void setRepoConfigs(List<RepoConfig> repoConfigs)
  {
    this.repoConfigs = repoConfigs;
  }

  public DAG<RepoConfig> toDAG() {
    Pair<DAG<RepoConfig>, ReturnError> dagPair = createRepoConfigDAG(repoConfigs);
    DAG<RepoConfig> dag = dagPair.getLeft();
    ReturnError parseError = dagPair.getRight();

    if (parseError != null) {
      throw new IllegalStateException(parseError.getMessage());
    }

    return dag;
  }

  @Override
  public String toString()
  {
    return "ConfigFile{" +
      "repoConfigs=" + repoConfigs +
      '}';
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ConfigFile that = (ConfigFile) o;

    return repoConfigs.equals(that.repoConfigs);
  }

  @Override
  public int hashCode()
  {
    return repoConfigs.hashCode();
  }


  public static Pair<Map<String, RepoConfig>, ReturnError> createRepoConfigMap(List<RepoConfig> repoConfigs) {
    Map<String, RepoConfig> repoConfigMap = Maps.newHashMap();
    List<RepoConfig> duplicateRepoConfigs = Lists.newArrayList();

    repoConfigs.forEach(repoConfig -> {
      String repoName = repoConfig.getName();

      if (repoConfigMap.containsKey(repoConfig.getName())) {
        duplicateRepoConfigs.add(repoConfig);
      }

      repoConfigMap.put(repoName, repoConfig);
    });

    ReturnError parseError = null;

    if (!duplicateRepoConfigs.isEmpty()) {
      List<String> repoNames = duplicateRepoConfigs.
        stream().
        map(repoConfig -> repoConfig.getName()).
        collect(Collectors.toList());

      String errMessage = "The following repo names were duplicated: " +
        StringUtils.join(repoNames, ", ");

      parseError = new ReturnErrorImpl(errMessage);
      return new ImmutablePair<>(null, parseError);
    } else {
      return new ImmutablePair<>(repoConfigMap, null);
    }
  }

  public static Pair<DAG<RepoConfig>, ReturnError> createRepoConfigDAG(List<RepoConfig> repoConfigs) {
    Pair<Map<String, RepoConfig>, ReturnError> repoConfigMapPair = createRepoConfigMap(repoConfigs);
    Map<String, RepoConfig> repoConfigMap = repoConfigMapPair.getLeft();
    ReturnError parseError = repoConfigMapPair.getRight();

    if (parseError != null) {
      return new ImmutablePair<>(null, parseError);
    }

    DAG<RepoConfig> dag = new DAG<>();
    List<ReturnError> dagErrors = Lists.newArrayList();

    repoConfigs.forEach(repoConfig -> {
      repoConfig.getDependencies().forEach(depRepoConfigName -> {
        RepoConfig depRepoConfig = repoConfigMap.get(depRepoConfigName);

        if (depRepoConfig == null) {
          String errorMessage = "The repo \"" + depRepoConfigName + "\" does not exist.";
          dagErrors.add(new ReturnErrorImpl(errorMessage));
        } else {
          ReturnError dagError = dag.addEdge(new Edge<>(repoConfig, depRepoConfig));

          if (dagError != null) {
            dagErrors.add(dagError);
          }
        }
      });
    });

    if (!dagErrors.isEmpty()) {
      List<String> dagErrorMessages = dagErrors.
        stream().
        map(error -> error.getMessage()).
        collect(Collectors.toList());

      String errMessage = "DAG creation errors:\n" + StringUtils.join(dagErrorMessages, "\n");
      return new ImmutablePair<>(null, new ReturnErrorImpl(errMessage));
    }

    return new ImmutablePair<>(dag, null);
  }

  public static ConfigFile parse(InputStream dataInputStream) throws ParseException
  {
    ObjectMapper om = new ObjectMapper();
    om.registerModule(new Jdk8Module());

    try {
      ConfigFile configFile = om.readValue(dataInputStream, ConfigFile.class);

      if (configFile.getRepoConfigs().isEmpty()) {
        throw new ParseException("repoConfigs must be non-empty.");
      }

      ReturnError projectTypeError = validateProjectTypes(configFile.getRepoConfigs());

      if (projectTypeError != null) {
        throw new ParseException(projectTypeError.getMessage());
      }

      Pair<DAG<RepoConfig>, ReturnError> dagPair = createRepoConfigDAG(configFile.getRepoConfigs());
      ReturnError parseError = dagPair.getRight();

      if (parseError != null) {
        throw new ParseException(parseError.getMessage());
      } else {
        return configFile;
      }
    } catch (IOException e) {
      throw new ParseException(e);
    }
  }

  public static ReturnError validateProjectTypes(List<RepoConfig> repoConfigs)
  {
    Iterator<RepoConfig> repoIterator = repoConfigs.iterator();
    String projectType = repoIterator.next().getProjectType();

    while(repoIterator.hasNext()) {
      RepoConfig repoConfig = repoIterator.next();

      if (!projectType.equals(repoConfig.getProjectType())) {
        return new ReturnErrorImpl("project types don't match");
      }
    }

    return null;
  }
}
