package com.streamsimple.rt.srcctl;

import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautils.testutils.DirTestWatcher;
import com.streamsimple.rt.executors.ProcessCommandExecutor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
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
      Pair<SourceControlAgent, ReturnError> result = new GitSourceControlAgent.Builder()
        .setDir(getDir())
        .build();
      Assert.assertNull(result.getRight());
      this.sourceControlAgent = result.getLeft();
    }
  }

  @Test
  public void hasUncommittedTestFalse() {
    SourceControlAgent agent = testWatcher.getSourceControlAgent();

    Assert.assertEquals(
      new ImmutablePair<>(false, null),
      agent.hasUncommittedChanges());
  }

  @Test
  public void hasCommittedTestTrue() throws IOException {
    new File(testWatcher.getDir(), "test.txt").createNewFile();
    SourceControlAgent agent = testWatcher.getSourceControlAgent();

    Assert.assertEquals(
      new ImmutablePair<>(true, null),
      agent.hasUncommittedChanges());
  }

  @Test
  public void createBranchTest() throws Exception {
    addCommitToRepo();

    SourceControlAgent agent = testWatcher.getSourceControlAgent();

    ReturnError error = agent.createBranchFrom("master", "test1");
    Assert.assertNull(error);

    checkCurrentBranch("master");
  }

  @Test
  public void checkoutBranchTest() throws Exception {
    addCommitToRepo();

    SourceControlAgent agent = testWatcher.getSourceControlAgent();

    ReturnError error = agent.createBranchFrom("master", "test1");
    Assert.assertNull(error);
    error = agent.checkoutBranch("test1");
    Assert.assertNull(error);

    checkCurrentBranch("test1");
  }

  private void checkCurrentBranch(String name) {
    SourceControlAgent agent = testWatcher.getSourceControlAgent();

    Pair<String, ReturnError> result = agent.getCurrentBranch();
    Assert.assertNull(result.getRight());
    Assert.assertEquals(name, result.getLeft());
  }

  private void addCommitToRepo() throws Exception {
    new File(testWatcher.getDir(), "test.txt").createNewFile();

    Repository repository = new FileRepositoryBuilder()
      .readEnvironment()
      .findGitDir(testWatcher.getDir())
      .build();

    try (Git git = new Git(repository)) {
      git.commit()
        .setCommitter("tester", "tester@release-tool.io")
        .setMessage("Testing")
        .call();
    }
  }
}
