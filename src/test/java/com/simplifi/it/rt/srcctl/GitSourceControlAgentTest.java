package com.simplifi.it.rt.srcctl;

import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.javautils.testutils.DirTestWatcher;
import com.simplifi.it.rt.executors.ProcessCommandExecutor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;

import java.io.File;
import java.io.IOException;

public class GitSourceControlAgentTest {
  @Rule
  public final GitSourceControlAgentTestWatcher testWatcher = new GitSourceControlAgentTestWatcher();

  public static class GitSourceControlAgentTestWatcher extends DirTestWatcher {
    private SourceControlAgent sourceControlAgent;

    @Override
    protected void starting(Description description) {
      super.starting(description);
      initGitRepo();
      initSourceControlAgent();
    }

    public SourceControlAgent getSourceControlAgent() {
      return sourceControlAgent;
    }

    private void initGitRepo() {
      final String command = "bash -c 'git init'";
      ProcessCommandExecutor commandExecutor = new ProcessCommandExecutor();
      ReturnError returnError = commandExecutor.execute(getDirPath(), command);
      Assert.assertNull(returnError);
    }

    private void initSourceControlAgent() {
      Pair<SourceControlAgent, ReturnError> result = new GitSourceControlAgent.Builder(getDirPath()).build();
      Assert.assertNull(result.getRight());
      this.sourceControlAgent = result.getLeft();
    }
  }

  @Test
  public void hasUncommittedTestFalse() {
    SourceControlAgent sourceControlAgent = testWatcher.getSourceControlAgent();

    Assert.assertEquals(
      new ImmutablePair<>(false, null),
      sourceControlAgent.hasUncommittedChanges());
  }

  @Test
  public void hasCommittedTestTrue() throws IOException {
    new File(testWatcher.getDir(), "test.txt").createNewFile();
    SourceControlAgent sourceControlAgent = testWatcher.getSourceControlAgent();

    Assert.assertEquals(
      new ImmutablePair<>(true, null),
      sourceControlAgent.hasUncommittedChanges());
  }
}
