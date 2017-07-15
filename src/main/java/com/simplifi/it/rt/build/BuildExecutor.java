package com.simplifi.it.rt.build;

import com.google.common.base.Preconditions;
import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.rt.command.CommandExecutor;

import java.util.List;
import java.util.stream.Collectors;

public class BuildExecutor {
  private CommandExecutor commandExecutor;

  public BuildExecutor(CommandExecutor commandExecutor) {
    this.commandExecutor = Preconditions.checkNotNull(commandExecutor);
  }

  public ReturnError execute(BuildConfig buildConfig) {
    List<RepoConfig> orderedRepoConfigs = buildConfig.toDAG().inOrderTraversal(RepoConfig.NameComparator.INSTANCE);
    orderedRepoConfigs = orderedRepoConfigs.stream().
      filter(repoConfig -> repoConfig.getCommand().isPresent()).collect(Collectors.toList());

    for (RepoConfig repoConfig: orderedRepoConfigs) {
      String workingDirectory = repoConfig.getPath();
      String command = repoConfig.getName(); // TODO replace with command not name
      ReturnError returnError = commandExecutor.execute(workingDirectory, command);

      if (returnError != null) {
        return returnError;
      }
    }

    return null;
  }
}
