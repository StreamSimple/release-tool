package com.simplifi.it.rt.build;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.simplifi.it.rt.dag.DAG;
import com.simplifi.it.rt.parse.ParseError;
import com.simplifi.it.rt.parse.ParseResult;
import junit.framework.Assert;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.simplifi.it.rt.build.BuildConfig.createRepoConfigMap;

public class BuildConfigTest
{
  @Test
  public void simpleDeserializeTest()
  {
    InputStream inputStream = BuildConfigTest.class.getClassLoader().getResourceAsStream("simpleBuildConfig.json");
    ParseResult<BuildConfig> result = BuildConfig.parse(inputStream);

    BuildConfig expectedConfig = new BuildConfig(createCorrectRepoConfigList());

    Assert.assertFalse(result.hasError());
    Assert.assertEquals(result.getValue(), expectedConfig);
  }

  @Test
  public void simpleCreateRepoConfigMapTest() {
    Map<String, RepoConfig> expectedMap = Maps.newHashMap();
    expectedMap.put("myRepo1",
      new RepoConfig("myRepo1", "/repos/my/repo/1", Lists.newArrayList()));
    expectedMap.put("myRepo2",
      new RepoConfig("myRepo2", "/repos/my/repo/2", Lists.newArrayList("myRepo1")));
    expectedMap.put("myRepo3",
      new RepoConfig("myRepo3", "/repos/my/repo/3", Lists.newArrayList("myRepo2", "myRepo4")));
    expectedMap.put("myRepo4",
      new RepoConfig("myRepo4", "/repos/my/repo/4", Lists.newArrayList("myRepo1")));

    Pair<Map<String, RepoConfig>, ParseError> mapPair = createRepoConfigMap(createCorrectRepoConfigList());
    Map<String, RepoConfig> actualMap = mapPair.getLeft();
    ParseError actualError = mapPair.getRight();

    Assert.assertNull(actualError);
    Assert.assertEquals(expectedMap, actualMap);
  }

  @Test
  public void simpleCreateRepoConfigMapFailTest() {
    List<RepoConfig> repoConfigs = Lists.newArrayList(
      new RepoConfig("myRepo1", "/repos/my/repo/1", Lists.newArrayList()),
      new RepoConfig("myRepo1", "/repos/my/repo/2", Lists.newArrayList("myRepo1")),
      new RepoConfig("myRepo3", "/repos/my/repo/3", Lists.newArrayList("myRepo2", "myRepo4")),
      new RepoConfig("myRepo4", "/repos/my/repo/4", Lists.newArrayList("myRepo1"))
    );

    Pair<Map<String, RepoConfig>, ParseError> mapPair = createRepoConfigMap(repoConfigs);
    Assert.assertNull(mapPair.getLeft());
    Assert.assertNotNull(mapPair.getRight());
  }

  @Test
  public void simpleCreateRepoConfigDAGTest() {
    Pair<DAG<RepoConfig>, ParseError> dagPair = BuildConfig.createRepoConfigDAG(createCorrectRepoConfigList());
    DAG<RepoConfig> dag = dagPair.getLeft();
    ParseError parseError = dagPair.getRight();

    Assert.assertNull(parseError);

    List<RepoConfig> repoConfigs = dag.getNodes().stream().collect(Collectors.toList());
    Pair<Map<String, RepoConfig>, ParseError> actualMapPair = createRepoConfigMap(repoConfigs);
    Map<String, RepoConfig> actualRepoConfigMap = actualMapPair.getLeft();
    ParseError mapParseError = actualMapPair.getRight();
    Assert.assertNull(mapParseError);
    
    Pair<Map<String, RepoConfig>, ParseError> expectedMapPair = createRepoConfigMap(createCorrectRepoConfigList());
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

  private List<RepoConfig> createCorrectRepoConfigList() {
    return Lists.newArrayList(new RepoConfig("myRepo1", "/repos/my/repo/1", Lists.newArrayList()),
      new RepoConfig("myRepo2", "/repos/my/repo/2", Lists.newArrayList("myRepo1")),
      new RepoConfig("myRepo3", "/repos/my/repo/3", Lists.newArrayList("myRepo2", "myRepo4")),
      new RepoConfig("myRepo4", "/repos/my/repo/4", Lists.newArrayList("myRepo1"))
    );
  }
}
