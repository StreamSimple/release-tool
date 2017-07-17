package com.simplifi.it.rt.build;

import com.google.common.collect.Lists;
import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.rt.config.ConfigFile;
import com.simplifi.it.rt.executors.MockCommandExecutor;
import com.simplifi.it.rt.executors.ConfigExecutor;
import com.simplifi.it.rt.parse.ParseException;
import junit.framework.Assert;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class ConfigExecutorTest
{
  @Test
  public void simpleBuildExecutorTest() throws ParseException {
    MockCommandExecutor commandExecutor = new MockCommandExecutor();

    InputStream inputStream = com.simplifi.it.rt.executors.ConfigExecutorTest.class.getClassLoader().
      getResourceAsStream("simpleBuildConfigWithCmd.json");

    ConfigFile result = ConfigFile.parse(inputStream);

    ConfigExecutor configExecutor = new ConfigExecutor(commandExecutor);
    ReturnError returnError = configExecutor.execute(result);
    Assert.assertNull(returnError);

    List<Pair<String, String>> expectedPairs = Lists.newArrayList();
    expectedPairs.add(new ImmutablePair<>("/repos/my/repo/1", "cmd1"));
    expectedPairs.add(new ImmutablePair<>("/repos/my/repo/2", "cmd2"));
    expectedPairs.add(new ImmutablePair<>("/repos/my/repo/3", "cmd3"));

    List<Pair<String, String>> actualPairs = commandExecutor.getArgs();

    Assert.assertEquals(expectedPairs, actualPairs);
  }
}
