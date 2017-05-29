package com.simplifi.it.rt.release;

import com.google.common.collect.Lists;
import com.simplifi.it.rt.parse.ParseResult;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Created by tfarkas on 5/27/17.
 */
public class ReleaseConfigTest
{
  @Test
  public void simpleDeserializeTest() throws IOException {
    InputStream inputStream = ReleaseConfigTest.class.getClassLoader().getResourceAsStream("simpleReleaseConfig.json");
    ParseResult<ReleaseConfig> result = ReleaseConfig.parse(inputStream);

    ReleaseConfig expectedConfig = new ReleaseConfig(
      Lists.newArrayList(new RepoConfig("/repos/myRepo1", "test1", Optional.empty()),
      new RepoConfig("/repos/myRepo2", RepoConfig.DEFAULT_MAIN_BRANCH, Optional.empty()),
      new RepoConfig("/repos/myRepo3", RepoConfig.DEFAULT_MAIN_BRANCH, Optional.empty()),
      new RepoConfig("/repos/myRepo4", "test2", Optional.of("echo 'Hello World'"))
      ));

    System.out.println(result.getValue().getRepoConfigs().get(3).getReleaseCommand().
      equals(expectedConfig.getRepoConfigs().get(3).getReleaseCommand()));

    Assert.assertFalse(result.hasError());
    Assert.assertEquals(expectedConfig, result.getValue());
  }
}
