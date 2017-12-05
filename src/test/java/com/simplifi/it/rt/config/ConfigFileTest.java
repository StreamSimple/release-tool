package com.simplifi.it.rt.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.streamsimple.javautil.err.ReturnError;
import com.simplifi.it.rt.dag.DAG;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.simplifi.it.rt.config.ConfigFile.createRepoConfigMap;

public class ConfigFileTest
{
  @Test
  public void simpleDeserializeTest() throws Exception {
    try (InputStream inputStream = ConfigFileTest.class
      .getClassLoader()
      .getResourceAsStream("simpleBuildConfig.json")) {
      ConfigFile result = ConfigFile.parse(inputStream);
      ConfigFile expectedConfig = new ConfigFile(createCorrectRepoConfigList());
      Assert.assertEquals(expectedConfig, result);
    }
  }

  @Test
  public void simpleDeserializeTestWithCmd() throws Exception {
    try (InputStream inputStream = ConfigFileTest.class
      .getClassLoader()
      .getResourceAsStream("simpleBuildConfigWithCmd.json")) {
      ConfigFile result = ConfigFile.parse(inputStream);
      ConfigFile expectedConfig = new ConfigFile(createCorrectRepoConfigList());
      expectedConfig.getRepoConfigs().get(0).setCommand(Optional.of("cmd1"));
      expectedConfig.getRepoConfigs().get(1).setCommand(Optional.of("cmd2"));
      expectedConfig.getRepoConfigs().get(2).setCommand(Optional.of("cmd3"));
      Assert.assertEquals(expectedConfig, result);
    }
  }

  @Test
  public void releaseConfigDeserializeTest() throws Exception {
    try (InputStream inputStream = ConfigFileTest.class
      .getClassLoader()
      .getResourceAsStream("simpleReleaseConfig.json")) {
      ConfigFile result = ConfigFile.parse(inputStream);
      List<RepoConfig> repoConfigs = createCorrectRepoConfigList();
      repoConfigs.get(0).setReleaseConfig(Optional.empty());
      repoConfigs.get(1).setReleaseConfig(Optional.of(
        new RepoConfig.ReleaseConfig(Optional.empty(), Optional.empty(), Optional.empty())));
      repoConfigs.get(2).setReleaseConfig(Optional.of(
        new RepoConfig.ReleaseConfig(Optional.of("release-"), Optional.of("runme"), Optional.of("v1"))));
      repoConfigs.get(3).setReleaseConfig(Optional.of(
        new RepoConfig.ReleaseConfig(Optional.of("release-"), Optional.of("runme"), Optional.of("v2"))));
      ConfigFile expectedConfig = new ConfigFile(repoConfigs);
      Assert.assertEquals(expectedConfig, result);
    }
  }

  @Test
  public void simpleCreateRepoConfigMapTest() {
    Map<String, RepoConfig> expectedMap = Maps.newHashMap();
    expectedMap.put("myRepo1",
      new RepoConfig(ProjectManager.MAVEN.toString(),"myRepo1", Optional.of("master"), "/repos/my/repo/1",
        Lists.newArrayList(), Optional.empty(), Optional.empty()));
    expectedMap.put("myRepo2",
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo2", Optional.of("master"), "/repos/my/repo/2",
        Lists.newArrayList("myRepo1"), Optional.empty(), Optional.empty()));
    expectedMap.put("myRepo3",
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo3", Optional.of("master"), "/repos/my/repo/3",
        Lists.newArrayList("myRepo2", "myRepo4"), Optional.empty(), Optional.empty()));
    expectedMap.put("myRepo4",
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo4", Optional.of("master"), "/repos/my/repo/4",
        Lists.newArrayList("myRepo1"), Optional.empty(), Optional.empty()));

    Pair<Map<String, RepoConfig>, ReturnError> mapPair = createRepoConfigMap(createCorrectRepoConfigList());
    Map<String, RepoConfig> actualMap = mapPair.getLeft();
    ReturnError actualError = mapPair.getRight();

    Assert.assertNull(actualError);
    Assert.assertEquals(expectedMap, actualMap);
  }

  @Test
  public void simpleCreateRepoConfigMapFailTest() {
    List<RepoConfig> repoConfigs = Lists.newArrayList(
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo1", Optional.of("master"), "/repos/my/repo/1",
        Lists.newArrayList(), Optional.empty(), Optional.empty()),
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo1", Optional.of("master"), "/repos/my/repo/2",
        Lists.newArrayList("myRepo1"), Optional.empty(), Optional.empty()),
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo3", Optional.of("master"), "/repos/my/repo/3",
        Lists.newArrayList("myRepo2", "myRepo4"), Optional.empty(), Optional.empty()),
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo4", Optional.of("master"), "/repos/my/repo/4",
        Lists.newArrayList("myRepo1"), Optional.empty(), Optional.empty())
    );

    Pair<Map<String, RepoConfig>, ReturnError> mapPair = createRepoConfigMap(repoConfigs);
    Assert.assertNull(mapPair.getLeft());
    Assert.assertNotNull(mapPair.getRight());
  }

  @Test
  public void simpleCreateRepoConfigDAGTest() {
    Pair<DAG<RepoConfig>, ReturnError> dagPair = ConfigFile.createRepoConfigDAG(createCorrectRepoConfigList());
    DAG<RepoConfig> dag = dagPair.getLeft();
    ReturnError parseError = dagPair.getRight();

    Assert.assertNull(parseError);

    List<RepoConfig> repoConfigs = dag.getNodes().stream().collect(Collectors.toList());
    Pair<Map<String, RepoConfig>, ReturnError> actualMapPair = createRepoConfigMap(repoConfigs);
    Map<String, RepoConfig> actualRepoConfigMap = actualMapPair.getLeft();
    ReturnError mapParseError = actualMapPair.getRight();
    Assert.assertNull(mapParseError);

    Pair<Map<String, RepoConfig>, ReturnError> expectedMapPair = createRepoConfigMap(createCorrectRepoConfigList());
    Map<String, RepoConfig> expectedRepoConfigMap = expectedMapPair.getLeft();

    Assert.assertEquals(expectedRepoConfigMap, actualRepoConfigMap);

    RepoConfig repoConfig1 = actualRepoConfigMap.get("myRepo1");
    RepoConfig repoConfig2 = actualRepoConfigMap.get("myRepo2");
    RepoConfig repoConfig3 = actualRepoConfigMap.get("myRepo3");
    RepoConfig repoConfig4 = actualRepoConfigMap.get("myRepo4");

    Assert.assertEquals(Sets.newHashSet(), dag.getChildNodes(repoConfig1));
    Assert.assertEquals(Sets.newHashSet(repoConfig1), dag.getChildNodes(repoConfig2));
    Assert.assertEquals(Sets.newHashSet(repoConfig2, repoConfig4), dag.getChildNodes(repoConfig3));
    Assert.assertEquals(Sets.newHashSet(repoConfig1), dag.getChildNodes(repoConfig4));
  }

  @Test
  public void simpleCreateRepoConfigDAGFailureNoDepTest() {
    List<RepoConfig> faultyRepoCofigs = Lists.newArrayList(
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo1", Optional.of("master"), "/repos/my/repo/1",
        Lists.newArrayList(), Optional.empty(), Optional.empty()),
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo2", Optional.of("master"), "/repos/my/repo/2",
        Lists.newArrayList("myRepo1"), Optional.empty(), Optional.empty()),
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo3", Optional.of("master"), "/repos/my/repo/3",
        Lists.newArrayList("myRepo2", "myRepo4"), Optional.empty(), Optional.empty()),
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo4", Optional.of("master"), "/repos/my/repo/4",
        Lists.newArrayList("myRepo55"), Optional.empty(), Optional.empty())
    );

    Pair<DAG<RepoConfig>, ReturnError> dagPair = ConfigFile.createRepoConfigDAG(faultyRepoCofigs);
    DAG<RepoConfig> dag = dagPair.getLeft();
    ReturnError parseError = dagPair.getRight();

    Assert.assertNull(dag);
    Assert.assertNotNull(parseError);
  }

  @Test
  public void simpleCreateRepoConfigDAGFailureCircularDepsTest() {
    List<RepoConfig> faultyRepoCofigs =
      Lists.newArrayList(
        new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo1", Optional.of("master"), "/repos/my/repo/1",
          Lists.newArrayList("myRepo4"), Optional.empty(), Optional.empty()),
        new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo2", Optional.of("master"), "/repos/my/repo/2",
          Lists.newArrayList("myRepo1"), Optional.empty(), Optional.empty()),
        new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo3", Optional.of("master"), "/repos/my/repo/3",
          Lists.newArrayList("myRepo2", "myRepo4"), Optional.empty(), Optional.empty()),
        new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo4", Optional.of("master"), "/repos/my/repo/4",
          Lists.newArrayList("myRepo1"), Optional.empty(), Optional.empty()));

    Pair<DAG<RepoConfig>, ReturnError> dagPair = ConfigFile.createRepoConfigDAG(faultyRepoCofigs);
    DAG<RepoConfig> dag = dagPair.getLeft();
    ReturnError parseError = dagPair.getRight();

    Assert.assertNull(dag);
    Assert.assertNotNull(parseError);
  }

  private List<RepoConfig> createCorrectRepoConfigList() {
    return Lists.newArrayList(
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo1", Optional.of("master"), "/repos/my/repo/1",
        Lists.newArrayList(), Optional.empty(), Optional.empty()),
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo2", Optional.of("master"), "/repos/my/repo/2",
        Lists.newArrayList("myRepo1"), Optional.empty(), Optional.empty()),
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo3", Optional.of("master"), "/repos/my/repo/3",
        Lists.newArrayList("myRepo2", "myRepo4"), Optional.empty(), Optional.empty()),
      new RepoConfig(ProjectManager.MAVEN.toString(), "myRepo4", Optional.of("master"), "/repos/my/repo/4",
        Lists.newArrayList("myRepo1"), Optional.empty(), Optional.empty())
    );
  }
}
