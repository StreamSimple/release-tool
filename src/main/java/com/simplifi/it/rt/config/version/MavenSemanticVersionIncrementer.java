package com.simplifi.it.rt.config.version;

import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.javautil.err.ReturnErrorImpl;

import java.util.Map;

public class MavenSemanticVersionIncrementer implements VersionIncrementer
{
  public static final MavenSemanticVersionIncrementer INSTANCE = new MavenSemanticVersionIncrementer();

  private MavenSemanticVersionIncrementer() {
  }

  public ReturnError validateVersion(String version) {
    return null;
  }

  @Override
  public String incrementVersion(String version) {
    return null;
  }

  @Override
  public boolean hasConfig() {
    return false;
  }

  @Override
  public ReturnError validateConfig(Map<String, String> config) {
    if (config.isEmpty()) {
      return null;
    }

    return new ReturnErrorImpl("There are no config parameters");
  }
}
