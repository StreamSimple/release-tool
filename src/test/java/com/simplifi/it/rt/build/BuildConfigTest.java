package com.simplifi.it.rt.build;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.rt.dag.DAG;
import com.simplifi.it.rt.parse.ParseException;
import junit.framework.Assert;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.simplifi.it.rt.build.BuildConfig.createRepoConfigMap;

public class BuildConfigTest
{
  @Test
  public void simpleDeserializeTest()
  {
    InputStream inputStream = BuildConfigTest.class.getClassLoader().
      getResourceAsStream("simpleBuildConfig.json");
    BuildConfig result = null;

    try {
      result = BuildConfig.parse(inputStream);
    } catch (ParseException e) {
      Throwables.propagate(e);
    }

    BuildConfig expectedConfig = new BuildConfig(createCorrectRepoConfigList());

    Assert.assertEquals(result, expectedConfig);
  }

  @Test
  public void simpleCreateRepoConfigMapTest() {
    Map<String, RepoConfig> expectedMap = Maps.newHashMap();
    expectedMap.put("myRepo1",
      new RepoConfig("myRepo1", "/repos/my/repo/1", Lists.newArrayList(),
        Optional.empty(), Optional.empty()));
    expectedMap.put("myRepo2",
      new RepoConfig("myRepo2", "/repos/my/repo/2", Lists.newArrayList("myRepo1"),
        Optional.empty(), Optional.empty()));
    expectedMap.put("myRepo3",
      new RepoConfig("myRepo3", "/repos/my/repo/3", Lists.newArrayList("myRepo2", "myRepo4"),
        Optional.empty(), Optional.empty()));
    expectedMap.put("myRepo4",
      new RepoConfig("myRepo4", "/repos/my/repo/4", Lists.newArrayList("myRepo1"),
        Optional.empty(), Optional.empty()));

    Pair<Map<String, RepoConfig>, ReturnError> mapPair = createRepoConfigMap(createCorrectRepoConfigList());
    Map<String, RepoConfig> actualMap = mapPair.getLeft();
    ReturnError actualError = mapPair.getRight();

    Assert.assertNull(actualError);
    Assert.assertEquals(expectedMap, actualMap);
  }

  @Test
  public void simpleCreateRepoConfigMapFailTest() {
    List<RepoConfig> repoConfigs = Lists.newArrayList(
      new RepoConfig("myRepo1", "/repos/my/repo/1", Lists.newArrayList(),
        Optional.empty(), Optional.empty()),
      new RepoConfig("myRepo1", "/repos/my/repo/2", Lists.newArrayList("myRepo1"),
        Optional.empty(), Optional.empty()),
      new RepoConfig("myRepo3", "/repos/my/repo/3", Lists.newArrayList("myRepo2", "myRepo4"),
        Optional.empty(), Optional.empty()),
      new RepoConfig("myRepo4", "/repos/my/repo/4", Lists.newArrayList("myRepo1"),
        Optional.empty(), Optional.empty())
    );

    Pair<Map<String, RepoConfig>, ReturnError> mapPair = createRepoConfigMap(repoConfigs);
    Assert.assertNull(mapPair.getLeft());
    Assert.assertNotNull(mapPair.getRight());
  }

  @Test
  public void simpleCreateRepoConfigDAGTest() {
    Pair<DAG<RepoConfig>, ReturnError> dagPair = BuildConfig.createRepoConfigDAG(createCorrectRepoConfigList());
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
      new RepoConfig("myRepo1", "/repos/my/repo/1", Lists.newArrayList(),
        Optional.empty(), Optional.empty()),
      new RepoConfig("myRepo2", "/repos/my/repo/2", Lists.newArrayList("myRepo1"),
        Optional.empty(), Optional.empty()),
      new RepoConfig("myRepo3", "/repos/my/repo/3", Lists.newArrayList("myRepo2", "myRepo4"),
        Optional.empty(), Optional.empty()),
      new RepoConfig("myRepo4", "/repos/my/repo/4", Lists.newArrayList("myRepo55"),
        Optional.empty(), Optional.empty())
    );

    Pair<DAG<RepoConfig>, ReturnError> dagPair = BuildConfig.createRepoConfigDAG(faultyRepoCofigs);
    DAG<RepoConfig> dag = dagPair.getLeft();
    ReturnError parseError = dagPair.getRight();

    Assert.assertNull(dag);
    Assert.assertNotNull(parseError);
  }

  @Test
  public void simpleCreateRepoConfigDAGFailureCircularDepsTest() {
    List<RepoConfig> faultyRepoCofigs =
      Lists.newArrayList(
        new RepoConfig("myRepo1", "/repos/my/repo/1", Lists.newArrayList("myRepo4"),
          Optional.empty(), Optional.empty()),
        new RepoConfig("myRepo2", "/repos/my/repo/2", Lists.newArrayList("myRepo1"),
          Optional.empty(), Optional.empty()),
        new RepoConfig("myRepo3", "/repos/my/repo/3", Lists.newArrayList("myRepo2", "myRepo4"),
          Optional.empty(), Optional.empty()),
        new RepoConfig("myRepo4", "/repos/my/repo/4", Lists.newArrayList("myRepo1"),
          Optional.empty(), Optional.empty()));

    Pair<DAG<RepoConfig>, ReturnError> dagPair = BuildConfig.createRepoConfigDAG(faultyRepoCofigs);
    DAG<RepoConfig> dag = dagPair.getLeft();
    ReturnError parseError = dagPair.getRight();

    Assert.assertNull(dag);
    Assert.assertNotNull(parseError);
  }

  private List<RepoConfig> createCorrectRepoConfigList() {
    return Lists.newArrayList(
      new RepoConfig("myRepo1", "/repos/my/repo/1", Lists.newArrayList(),
        Optional.empty(), Optional.empty()),
      new RepoConfig("myRepo2", "/repos/my/repo/2", Lists.newArrayList("myRepo1"),
        Optional.empty(), Optional.empty()),
      new RepoConfig("myRepo3", "/repos/my/repo/3", Lists.newArrayList("myRepo2", "myRepo4"),
        Optional.empty(), Optional.empty()),
      new RepoConfig("myRepo4", "/repos/my/repo/4", Lists.newArrayList("myRepo1"),
        Optional.empty(), Optional.empty())
    );
  }
}
