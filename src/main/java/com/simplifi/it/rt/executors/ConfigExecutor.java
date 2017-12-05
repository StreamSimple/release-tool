package com.simplifi.it.rt.executors;

import com.google.common.base.Preconditions;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;
import com.simplifi.it.rt.config.ConfigFile;
import com.simplifi.it.rt.config.ProjectManager;
import com.simplifi.it.rt.config.RepoConfig;
import com.simplifi.it.rt.config.version.VersionIncrementerLoader;
import com.simplifi.it.rt.srcctl.SourceControlAgent;
import org.apache.commons.lang3.tuple.Pair;;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigExecutor {
  public static final String DEFAULT_RELEASE_BRANCH_PREFIX = "release-";
  public static final String DEFAULT_SRC_BRANCH = "master";
  public static final String DEFAULT_VERSION_HANDLER = VersionIncrementerLoader.Info.SEMANTIC.name();

  public enum Type {
    BUILD,
    RELEASE
  }

  private CommandExecutor commandExecutor;
  private SourceControlAgent.Builder agentBuilder;

  public ConfigExecutor(CommandExecutor commandExecutor,
                        SourceControlAgent.Builder agentBuilder) {
    this.commandExecutor = Preconditions.checkNotNull(commandExecutor);
    this.agentBuilder = Preconditions.checkNotNull(agentBuilder);
  }

  public ReturnError execute(ConfigFile configFile, Type type) {
    List<RepoConfig> orderedRepoConfigs = configFile.toDAG().inOrderTraversal(RepoConfig.NameComparator.INSTANCE);
    orderedRepoConfigs = orderedRepoConfigs
      .stream()
      .filter(repoConfig -> repoConfig.getCommand().isPresent())
      .collect(Collectors.toList());
    Collections.reverse(orderedRepoConfigs);

    for (RepoConfig repoConfig: orderedRepoConfigs) {
      ReturnError returnError = executeRepo(repoConfig, type);

      if (returnError != null) {
        return returnError;
      }
    }

    return null;
  }

  private ReturnError executeRepo(RepoConfig repoConfig, Type type) {
    // Execute build
    String workingDirectory = repoConfig.getPath();
    ProjectManager projectManager = ProjectManager.valueOf(repoConfig.getProjectType());
    String buildCommand = repoConfig.getCommand().orElse(projectManager.getDefaultBuildCommand());
    String srcBranch = repoConfig.getSrcBranch().orElse(DEFAULT_SRC_BRANCH);

    // Get the source control agent
    agentBuilder.setDir(new File(workingDirectory));
    Pair<SourceControlAgent, ReturnError> buildResult = agentBuilder.build();
    SourceControlAgent agent = buildResult.getLeft();
    ReturnError buildError = buildResult.getRight();

    if (buildError != null) {
      // Error building the agent
      return buildError;
    }

    // Check if the current ranch has uncommitted changes
    Pair<Boolean, ReturnError> uncommittedResult = agent.hasUncommittedChanges();
    Boolean hasUncommittedResult = uncommittedResult.getLeft();
    ReturnError uncommittedError = uncommittedResult.getRight();

    if (uncommittedError != null) {
      return uncommittedError;
    }

    if (hasUncommittedResult) {
      String message = String.format("The repo in %s has an uncommitted result", workingDirectory);
      return new ReturnErrorImpl(message);
    }

    // Checkout the branch we want
    ReturnError checkoutError = agent.checkoutBranch(srcBranch);

    if (checkoutError != null) {
      return checkoutError;
    }

    // Execute the build command
    ReturnError returnError = commandExecutor.execute(workingDirectory, buildCommand);

    if (returnError != null) {
      return returnError;
    }

    // Execute the release command
    if (type.equals(Type.RELEASE)) {
      repoConfig.getReleaseConfig().ifPresent(releaseConfig -> {
        String branchPrefix = releaseConfig.getBranchPrefix().orElse(DEFAULT_RELEASE_BRANCH_PREFIX);
        String releaseCommand = releaseConfig.getCommand().orElse(projectManager.getDefaultReleaseCommand());
        String versionHandlerName = releaseConfig.getVersionHandler().orElse(DEFAULT_VERSION_HANDLER);
      });
    }

    return null;
  }
}
