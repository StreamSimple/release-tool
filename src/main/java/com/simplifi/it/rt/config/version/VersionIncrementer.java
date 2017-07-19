package com.simplifi.it.rt.config.version;

import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.rt.config.Configurable;

public interface VersionIncrementer extends Configurable
{
  ReturnError validateVersion(String version);
  String incrementVersion(String version, IncrementType type);

  enum IncrementType {
    MAJOR, MINOR, PATCH
  }
}
