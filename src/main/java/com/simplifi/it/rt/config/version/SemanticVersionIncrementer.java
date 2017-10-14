package com.simplifi.it.rt.config.version;

import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.javautil.err.ReturnErrorImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticVersionIncrementer implements VersionIncrementer
{
  public static final SemanticVersionIncrementer INSTANCE = new SemanticVersionIncrementer();
  public static final String VERSION_REGEX_STRING = "(\\d)\\.(\\d)\\.(\\d)(-SNAPSHOT)?";
  public static final Pattern VERSION_REGEX = Pattern.compile(VERSION_REGEX_STRING);

  private SemanticVersionIncrementer() {
  }

  public ReturnError validateVersion(String version) {
    if (!VERSION_REGEX.matcher(version).matches()) {
      return new ReturnErrorImpl("Version: \"" +
        version +
        "\" does not match the regex\"" +
        VERSION_REGEX_STRING +
        "\"");
    }

    return null;
  }

  @Override
  public String incrementVersion(String version,
                                 VersionIncrementer.IncrementType type,
                                 ReleaseType releaseType) {
    Matcher matcher = VERSION_REGEX.matcher(version);
    boolean matches = matcher.matches();

    if (!matches) {
      throw new IllegalArgumentException();
    }

    String major = matcher.group(1);
    String minor = matcher.group(2);
    String patch = matcher.group(3);

    long majorLong = Long.parseLong(major);
    long minorLong = Long.parseLong(minor);
    long patchLong = Long.parseLong(patch);

    switch (type) {
      case MAJOR: {
        majorLong++;
        break;
      }
      case MINOR: {
        minorLong++;
        break;
      }
      case PATCH: {
        patchLong++;
        break;
      }
    }

    String newVersionString = majorLong + "." + minorLong + "." + patchLong;

    if (releaseType == ReleaseType.SNAPSHOT) {
      newVersionString = newVersionString + "-SNAPSHOT";
    }

    return newVersionString;
  }

  static class Builder implements VersionIncrementer.Builder {
    @Override
    public VersionIncrementer build(Config config) {
      return INSTANCE;
    }
  }
}
