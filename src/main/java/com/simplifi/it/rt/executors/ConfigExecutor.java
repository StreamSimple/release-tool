package com.simplifi.it.rt.executors;

import com.google.common.base.Preconditions;
import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.rt.config.ConfigFile;
import com.simplifi.it.rt.config.RepoConfig;;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigExecutor {
  private CommandExecutor commandExecutor;

  public ConfigExecutor(CommandExecutor commandExecutor) {
    this.commandExecutor = Preconditions.checkNotNull(commandExecutor);
  }

  public ReturnError executeBuild(ConfigFile configFile) {
    return execute(configFile, false);
  }

  public ReturnError executeRelease(ConfigFile configFile) {
    return execute(configFile, true);
  }

  private ReturnError execute(ConfigFile configFile, boolean releaseTrueBuildFalse) {
    List<RepoConfig> orderedRepoConfigs = configFile.toDAG().inOrderTraversal(RepoConfig.NameComparator.INSTANCE);
    orderedRepoConfigs = orderedRepoConfigs.stream().
      filter(repoConfig -> repoConfig.getCommand().isPresent()).collect(Collectors.toList());
    Collections.reverse(orderedRepoConfigs);

    for (RepoConfig repoConfig: orderedRepoConfigs) {
      // Execute build
      String workingDirectory = repoConfig.getPath();
      String command = repoConfig.getCommand().get();
      ReturnError returnError = commandExecutor.execute(workingDirectory, command);

      if (returnError != null) {
        return returnError;
      }

      // Execute release
      if (releaseTrueBuildFalse) {
        repoConfig.getReleaseConfig().ifPresent(releaseConfig -> {
          //releaseConfig.
        });
      }
    }

    return null;
  }
}
