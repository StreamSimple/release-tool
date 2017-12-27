/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsimple.rt.srcctl;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.streamsimple.commons.lang3.tuple.ImmutablePair;
import com.streamsimple.commons.lang3.tuple.Pair;
import com.streamsimple.guava.common.base.Preconditions;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GitSourceControlAgent implements SourceControlAgent
{
  private Repository repository;

  private GitSourceControlAgent(Repository repository)
  {
    this.repository = Preconditions.checkNotNull(repository);
  }

  @Override
  public ReturnError checkoutBranch(String branch)
  {
    try (Git git = new Git(repository)) {
      try {
        git.checkout()
          .setName(branch)
          .call();
      } catch (GitAPIException e) {
        return new ReturnErrorImpl(e.getMessage());
      }
    }

    return null;
  }

  @Override
  public ReturnError createBranchFrom(String srcBranch, String destBranch)
  {
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
  public Pair<String, ReturnError> getCurrentBranch()
  {
    try {
      return new ImmutablePair<>(repository.getBranch(), null);
    } catch (IOException e) {
      String message = String.format("Error getting current branch name: %s", e.getMessage());
      return new ImmutablePair<>(null, new ReturnErrorImpl(message));
    }
  }

  @Override
  public Pair<Boolean, ReturnError> hasUncommittedChanges()
  {
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

  public static class Builder implements SourceControlAgent.Builder
  {
    private File dir;

    public Builder setDir(File dir)
    {
      this.dir = Preconditions.checkNotNull(dir);
      return this;
    }

    public Pair<SourceControlAgent, ReturnError> build()
    {
      if (!dir.exists()) {
        String message = String.format("dir %s does not exist", dir.getAbsolutePath());
        return new ImmutablePair<>(null, new ReturnErrorImpl(message));
      }

      Repository repository;

      try {
        repository = new FileRepositoryBuilder()
          .readEnvironment()
          .findGitDir(dir).build();
      } catch (IOException e) {
        return new ImmutablePair<>(null,
          new ReturnErrorImpl(String.format("Error accessing repository: %s", e.getMessage())));
      }

      return new ImmutablePair<>(new GitSourceControlAgent(repository), null);
    }
  }
}
