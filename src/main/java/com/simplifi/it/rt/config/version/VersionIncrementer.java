package com.simplifi.it.rt.config.version;

import com.streamsimple.javautil.err.ReturnError;

public interface VersionIncrementer
{
  ReturnError validateVersion(String version);
  String incrementVersion(String version, IncrementType type, ReleaseType releaseType);

  enum IncrementType {
    MAJOR, MINOR, PATCH
  }

  enum ReleaseType {
    RELEASE, SNAPSHOT
  }

  interface Config {
  }

  interface Builder {
    VersionIncrementer build(Config config);
  }
}
