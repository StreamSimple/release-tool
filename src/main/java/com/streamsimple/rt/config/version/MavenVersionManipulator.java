package com.streamsimple.rt.config.version;

import com.google.common.base.Preconditions;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MavenVersionManipulator implements VersionManipulator
{
  private File pomFile;

  private MavenVersionManipulator(File pomFile) {
    this.pomFile = Preconditions.checkNotNull(pomFile);
  }

  @Override
  public Pair<String, ReturnError> getVersion() {
    try {
      MavenXpp3Reader reader = new MavenXpp3Reader();
      Model model = reader.read(new FileReader(pomFile));
      return new ImmutablePair<>(model.getVersion(), null);
    } catch (XmlPullParserException | IOException e) {
      String message =
        String.format("Failed to read pom at %s with error: %s", pomFile.getAbsolutePath(), e.getMessage());
      return new ImmutablePair<>(null, new ReturnErrorImpl(message));
    }
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
