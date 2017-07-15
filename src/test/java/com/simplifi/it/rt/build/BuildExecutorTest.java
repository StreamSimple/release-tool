package com.simplifi.it.rt.build;

import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.rt.command.MockCommandExecutor;
import com.simplifi.it.rt.parse.ParseException;
import junit.framework.Assert;
import org.junit.Test;

import java.io.InputStream;

public class BuildExecutorTest
{
  @Test
  public void simpleBuildExecutorTest() throws ParseException {
    MockCommandExecutor commandExecutor = new MockCommandExecutor();

    InputStream inputStream = BuildConfigTest.class.getClassLoader().
      getResourceAsStream("simpleBuildConfig.json");

    BuildConfig result = BuildConfig.parse(inputStream);

    BuildExecutor buildExecutor = new BuildExecutor(commandExecutor);
    ReturnError returnError = buildExecutor.execute(result);
    Assert.assertNull(returnError);
  }
}
