package com.simplifi.it.rt.config.version;

import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;

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
      String message = String.format("Version: \"%s\" does not match the regex \"%s\"", version, VERSION_REGEX_STRING);
      return new ReturnErrorImpl(message);
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

    String newVersionString = String.format("%s.%s.%s", majorLong, minorLong, patchLong);

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
