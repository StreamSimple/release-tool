package com.streamsimple.rt.config.version;

import com.google.common.base.Preconditions;
import com.streamsimple.javautil.err.ReturnError;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

public interface VersionManipulator
{
  Pair<String, ReturnError> getVersion();
  ReturnError replaceProjectVersion(String nextVersion);
  ReturnError replaceDependencyVersion(Artifact artifact, String version);

  interface Builder {
    Builder setProjectFile(File file);
    VersionManipulator build();
  }

  @Getter
  class Artifact {
    private final String groupId;
    private final String artifactId;

    public Artifact(String groupId,
                    String artifactId) {
      this.groupId = Preconditions.checkNotNull(groupId);
      this.artifactId = Preconditions.checkNotNull(artifactId);
    }
  }
}
