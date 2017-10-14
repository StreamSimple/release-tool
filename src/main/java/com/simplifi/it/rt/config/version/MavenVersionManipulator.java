package com.simplifi.it.rt.config.version;

import com.simplifi.it.javautil.err.ReturnError;
import org.apache.commons.lang3.tuple.Pair;

public class MavenVersionManipulator implements VersionManipulator
{
  public static final MavenVersionManipulator INSTANCE = new MavenVersionManipulator();

  private MavenVersionManipulator() {
  }

  @Override
  public Pair<String, ReturnError> getVersion() {
    return null;
  }

  @Override
  public ReturnError replaceVersion(String nextVersion) {
    return null;
  }
}
