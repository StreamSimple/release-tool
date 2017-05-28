package com.simplifi.it.rt.build;

import com.google.common.collect.Lists;
import com.simplifi.it.rt.parse.ParseResult;
import junit.framework.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by tfarkas on 5/28/17.
 */
public class BuildConfigTest
{
  @Test
  public void simpleDeserializeTest()
  {
    InputStream inputStream = BuildConfigTest.class.getClassLoader().getResourceAsStream("simpleBuildConfig.json");
    ParseResult<BuildConfig> result = BuildConfig.parse(inputStream);

    BuildConfig expectedConfig = new BuildConfig(
      Lists.newArrayList(new RepoConfig("myRepo1", "/repos/my/repo/1", Lists.newArrayList()),
        new RepoConfig("myRepo2", "/repos/my/repo/2", Lists.newArrayList("myRepo1")),
        new RepoConfig("myRepo3", "/repos/my/repo/3", Lists.newArrayList("myRepo2", "myRepo4")),
        new RepoConfig("myRepo4", "/repos/my/repo/4", Lists.newArrayList("myRepo1"))
      ));

    Assert.assertFalse(result.hasError());
    Assert.assertEquals(result.getValue(), expectedConfig);
  }
}
