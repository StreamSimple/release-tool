package com.simplifi.it.rt.executors;

import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.javautils.testutils.DirTestWatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

public class ProcessCommandExecutorTest {
  @Rule
  public DirTestWatcher dirTestWatcher = new DirTestWatcher();

  @Test
  public void simpleProcessCommand() {
    final String command = String.format("bash -c %s/src/test/resources/scripts/makefile.sh",
      new File(".").getAbsolutePath());
    ProcessCommandExecutor commandExecutor = new ProcessCommandExecutor();

    ReturnError returnError = commandExecutor.execute(dirTestWatcher.getDirPath(), command);
    Assert.assertNull(returnError);

    Assert.assertTrue(new File(String.format("%s/%s", dirTestWatcher.getDirPath(), "test.txt")).exists());
  }
}
