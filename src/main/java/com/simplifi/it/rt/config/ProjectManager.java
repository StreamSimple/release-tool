package com.simplifi.it.rt.config;

import com.google.common.base.Preconditions;
import com.simplifi.it.rt.config.version.MavenVersionManipulator;
import com.simplifi.it.rt.config.version.VersionManipulator;

public enum ProjectManager
{
  MAVEN("mvn clean install",
    "mvn deploy -DskipTests",
    MavenVersionManipulator.INSTANCE);

  private String defaultBuildCommand;
  private String defaultReleaseCommand;
  private VersionManipulator versionManipulator;

  ProjectManager(String defaultBuildCommand,
                 String defaultReleaseCommand,
                 VersionManipulator versionManipulator) {
    this.defaultBuildCommand = Preconditions.checkNotNull(defaultBuildCommand);
    this.defaultReleaseCommand = Preconditions.checkNotNull(defaultReleaseCommand);
    this.versionManipulator = Preconditions.checkNotNull(versionManipulator);
  }

  public String getDefaultBuildCommand() {
    return defaultBuildCommand;
  }

  public String getDefaultReleaseCommand() {
    return defaultReleaseCommand;
  }

  public VersionManipulator getVersionManipulator() {
    return versionManipulator;
  }
}
