package com.simplifi.it.rt.build;

import com.google.common.collect.Lists;
import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.rt.command.MockCommandExecutor;
import com.simplifi.it.rt.parse.ParseException;
import junit.framework.Assert;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class BuildExecutorTest
{
  @Test
  public void simpleBuildExecutorTest() throws ParseException {
    MockCommandExecutor commandExecutor = new MockCommandExecutor();

    InputStream inputStream = BuildConfigTest.class.getClassLoader().
      getResourceAsStream("simpleBuildConfigWithCmd.json");

    BuildConfig result = BuildConfig.parse(inputStream);

    BuildExecutor buildExecutor = new BuildExecutor(commandExecutor);
    ReturnError returnError = buildExecutor.execute(result);
    Assert.assertNull(returnError);

    List<Pair<String, String>> expectedPairs = Lists.newArrayList();
    expectedPairs.add(new ImmutablePair<>("/repos/my/repo/1", "cmd1"));
    expectedPairs.add(new ImmutablePair<>("/repos/my/repo/2", "cmd2"));
    expectedPairs.add(new ImmutablePair<>("/repos/my/repo/3", "cmd3"));

    List<Pair<String, String>> actualPairs = commandExecutor.getArgs();

    Assert.assertEquals(expectedPairs, actualPairs);
  }
}
