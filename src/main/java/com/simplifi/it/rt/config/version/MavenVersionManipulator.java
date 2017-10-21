package com.simplifi.it.rt.config.version;

import com.google.common.base.Preconditions;
import com.simplifi.it.javautil.err.ReturnError;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

public class MavenVersionManipulator implements VersionManipulator
{
  private File pomFile;

  private MavenVersionManipulator(File pomFile) {
    this.pomFile = Preconditions.checkNotNull(pomFile);
  }

  @Override
  public Pair<String, ReturnError> getVersion() {
    return null;
  }

  @Override
  public ReturnError replaceProjectVersion(String nextVersion) {
    return null;
  }

  @Override
  public ReturnError replaceDependencyVersion(Artifact artifact, String version) {
    return null;
  }

  public static class Builder implements VersionManipulator.Builder {
    private File pomFile;

    @Override
    public VersionManipulator.Builder setProjectFile(File file) {
      pomFile = Preconditions.checkNotNull(file);
      return this;
    }

    @Override
    public VersionManipulator build() {
      return new MavenVersionManipulator(pomFile);
    }
  }
}
