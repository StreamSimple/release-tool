package com.simplifi.it.rt.config.version;

import com.google.common.base.Preconditions;

public class VersionHandler
{
  private VersionIncrementer versionIncrementer;
  private VersionManipulator versionManipulator;

  public VersionHandler() {
  }

  public VersionHandler(VersionIncrementer versionIncrementer, VersionManipulator versionManipulator) {
    this.versionIncrementer = Preconditions.checkNotNull(versionIncrementer);
    this.versionManipulator = Preconditions.checkNotNull(versionManipulator);
  }

  public VersionIncrementer getVersionIncrementer() {
    return versionIncrementer;
  }

  public void setVersionIncrementer(VersionIncrementer versionIncrementer) {
    this.versionIncrementer = versionIncrementer;
  }

  public VersionManipulator getVersionManipulator() {
    return versionManipulator;
  }

  public void setVersionManipulator(VersionManipulator versionManipulator) {
    this.versionManipulator = versionManipulator;
  }

  @Override
  public String toString() {
    return "VersionHandler{" +
      "versionIncrementer=" + versionIncrementer +
      ", versionManipulator=" + versionManipulator +
      '}';
  }
}
