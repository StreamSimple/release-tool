package com.streamsimple.rt.executors;

import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautils.testutils.DirTestWatcher;
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
    Assert.assertTrue(new File(dirTestWatcher.getDir(), "test.txt").exists());
  }
}
