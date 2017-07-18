package com.simplifi.it.rt.config.version;

import com.simplifi.it.rt.config.Configurable;

public interface VersionIncrementer extends Configurable
{
  String incrementVersion(String version);
}
