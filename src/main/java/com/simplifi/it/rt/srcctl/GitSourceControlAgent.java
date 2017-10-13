package com.simplifi.it.rt.srcctl;

import com.google.common.base.Preconditions;
import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.javautil.err.ReturnErrorImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

@Slf4j
public class GitSourceControlAgent implements SourceControlAgent
{
  private Repository repository;

  private GitSourceControlAgent(Repository repository) {
    this.repository = Preconditions.checkNotNull(repository);
  }

  @Override
  public ReturnError checkoutBranch(String branch) {
    return null;
  }

  @Override
  public ReturnError createBranchFrom(String srcBranch, String destBranch) {
    try (Git git = new Git(repository)) {
      try {
        git.branchCreate()
          .setStartPoint(srcBranch)
          .setName(destBranch)
          .call();
      } catch (GitAPIException e) {
        return new ReturnErrorImpl(e.getMessage());
      }
    }

    return null;
  }

  @Override
  public Pair<Boolean, ReturnError> hasUncommittedChanges() {
    boolean hasUncommitted = false;

    try (Git git = new Git(repository)) {
      Status status = git.status().call();

      if (!status.getAdded().isEmpty()) {
        log.info("Uncommitted added files {}", status.getAdded());
        hasUncommitted = true;
      }

      if (!status.getChanged().isEmpty()) {
        log.info("Uncommitted changed files {}", status.getChanged());
        hasUncommitted = true;
      }

      if (!status.getConflicting().isEmpty()) {
        log.info("Conflicting files {}", status.getConflicting());
        hasUncommitted = true;
      }

      if (!status.getModified().isEmpty()) {
        log.info("Modified files {}", status.getModified());
        hasUncommitted = true;
      }

      if (!status.getRemoved().isEmpty()) {
        log.info("Removed files {}", status.getRemoved());
        hasUncommitted = true;
      }

      if (!status.getUntracked().isEmpty()) {
        log.info("Untracked files {}", status.getUntracked());
        hasUncommitted = true;
      }

      if (!status.getUntrackedFolders().isEmpty()) {
        log.info("Untracked folders {}", status.getUntrackedFolders());
        hasUncommitted = true;
      }
    } catch (GitAPIException e) {
      return new ImmutablePair<>(null, new ReturnErrorImpl(e.getMessage()));
    }

    return new ImmutablePair<>(hasUncommitted, null);
  }

  public static class Builder {
    private String dirPath;

    public Builder(String dirPath) {
      this.dirPath = Preconditions.checkNotNull(dirPath);
    }

    public Pair<SourceControlAgent, ReturnError> build() {
      File dir = new File(dirPath);

      if (!dir.exists()) {
        return new ImmutablePair<>(null, new ReturnErrorImpl(String.format("dir %s does not exist", dirPath)));
      }

      Repository repository;

      try {
        repository = new FileRepositoryBuilder()
          .readEnvironment()
          .findGitDir(new File(dirPath)).build();
      } catch (IOException e) {
        return new ImmutablePair<>(null,
          new ReturnErrorImpl(String.format("Error accessing repository: %s", e.getMessage())));
      }

      return new ImmutablePair<>(new GitSourceControlAgent(repository), null);
    }
  }
}
