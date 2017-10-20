package com.simplifi.it.rt.config.version;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Preconditions;

import java.io.IOException;

public class VersionIncrementerLoader
{
  public static final String TYPE = "type";
  public static final String CONFIG = "config";

  public enum Info {
    SEMANTIC(new SemanticVersionIncrementer.Builder(), SemanticVersionIncrementer.Config.class);

    private VersionIncrementer.Builder builder;
    private Class<? extends VersionIncrementer.Config> configClazz;

    Info(VersionIncrementer.Builder builder,
         Class<? extends VersionIncrementer.Config> configClazz) {
      this.builder = Preconditions.checkNotNull(builder);
      this.configClazz = Preconditions.checkNotNull(configClazz);
    }

    public VersionIncrementer.Builder getBuilder() {
      return builder;
    }

    public Class<? extends VersionIncrementer.Config> getConfigClazz() {
      return configClazz;
    }
  }

  private String type;
  private VersionIncrementer.Config config;

  public VersionIncrementerLoader() {
  }

  public VersionIncrementerLoader(String type,
                                  VersionIncrementer.Config config) {
    this.type = Preconditions.checkNotNull(type);
    this.config = Preconditions.checkNotNull(config);
  }

  public VersionIncrementer getVersionIncrementer() {
    return Info.valueOf(type)
      .builder
      .build(config);
  }

  @Override
  public String toString() {
    return "VersionIncrementerLoader{" +
      "type='" + type + '\'' +
      ", config=" + config +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    VersionIncrementerLoader that = (VersionIncrementerLoader) o;

    if (!type.equals(that.type)) return false;
    return config != null ? config.equals(that.config) : that.config == null;
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + (config != null ? config.hashCode() : 0);
    return result;
  }

  public static class Deserializer extends StdDeserializer<VersionIncrementerLoader>
  {
    public Deserializer(Class<?> vc) {
      super(vc);
    }

    public Deserializer(JavaType valueType) {
      super(valueType);
    }

    public Deserializer(StdDeserializer<VersionIncrementerLoader> src) {
      super(src);
    }

    @Override
    public VersionIncrementerLoader deserialize(JsonParser jp,
                                                DeserializationContext deserializationContext) throws IOException {
      JsonNode node = jp.getCodec().readTree(jp);

      ObjectMapper om = new ObjectMapper();
      om.registerModule(new Jdk8Module());

      String incrementerType = node.get(TYPE).asText();
      JsonNode configNode = node.get(CONFIG);
      Class<? extends VersionIncrementer.Config> versionIncrementerConfig =
        Info.valueOf(incrementerType).getConfigClazz();
      VersionIncrementer.Config config =
        om.readValue(om.writeValueAsString(configNode), versionIncrementerConfig);
      return new VersionIncrementerLoader(incrementerType, config);
    }
  }
}
