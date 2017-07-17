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

  public ReturnError execute(ConfigFile configFile) {
    List<RepoConfig> orderedRepoConfigs = configFile.toDAG().inOrderTraversal(RepoConfig.NameComparator.INSTANCE);
    orderedRepoConfigs = orderedRepoConfigs.stream().
      filter(repoConfig -> repoConfig.getCommand().isPresent()).collect(Collectors.toList());
    Collections.reverse(orderedRepoConfigs);

    for (RepoConfig repoConfig: orderedRepoConfigs) {
      String workingDirectory = repoConfig.getPath();
      String command = repoConfig.getCommand().get();
      ReturnError returnError = commandExecutor.execute(workingDirectory, command);

      if (returnError != null) {
        return returnError;
      }
    }

    return null;
  }
}
