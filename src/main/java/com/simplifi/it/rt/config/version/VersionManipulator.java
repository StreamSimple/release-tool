package com.simplifi.it.rt.config.version;

public interface VersionManipulator {
  String getVersion();
  void replaceVersion(String nextVersion);
}
