package com.simplifi.it.rt.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.simplifi.it.rt.config.version.MavenVersionManipulator;
import com.simplifi.it.rt.config.version.VersionManipulator;

public enum ProjectManager
{
  MAVEN("mvn clean install",
    "mvn deploy -DskipTests",
    VersionManipulator.Builder.class);

  private String defaultBuildCommand;
  private String defaultReleaseCommand;
  private Class<VersionManipulator.Builder> versionManipulator;

  ProjectManager(String defaultBuildCommand,
                 String defaultReleaseCommand,
                 Class<VersionManipulator.Builder> versionManipulator) {
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

  public VersionManipulator.Builder getVersionManipulatorBuilder() {
    try {
      return versionManipulator.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      Throwables.propagate(e);
    }

    // Make the compiler happy
    return null;
  }
}
