package com.simplifi.it.rt.config.version;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.simplifi.it.javautil.err.ReturnError;
import com.simplifi.it.rt.config.Configurable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class VersionHandler
{
  public static final String VERSION_INCREMENTER_NODE_NAME = "incrementer";
  public static final String VERSION_MANIPULATOR_NODE_NAME = "manipulator";
  public static final String TYPE = "type";
  public static final String CONFIG = "config";

  public static final String MAVEN_SEMANTIC_VERSION_INCREMENTER_NAME = "MavenSem";
  public static final Map<String, VersionIncrementer> VERSION_INCREMENTERS;

  static {
    Map<String, VersionIncrementer> versionIncrementers = Maps.newHashMap();
    versionIncrementers.put(MAVEN_SEMANTIC_VERSION_INCREMENTER_NAME, MavenSemanticVersionIncrementer.INSTANCE);
    VERSION_INCREMENTERS = Collections.unmodifiableMap(versionIncrementers);
  }

  public static final String MAVEN_VERSION_MANIPULATOR_NAME = "Maven";
  public static final Map<String, VersionManipulator> VERSION_MANIPULATORS;

  static {
    Map<String, VersionManipulator> versionManipulators = Maps.newHashMap();
    versionManipulators.put(MAVEN_VERSION_MANIPULATOR_NAME, MavenVersionManipulator.INSTANCE);
    VERSION_MANIPULATORS = Collections.unmodifiableMap(versionManipulators);
  }

  private VersionIncrementer versionIncrementer;
  private VersionManipulator versionManipulator;

  private Map<String, String> incrementerConfig;
  private Map<String, String> manipulatorConfig;

  public VersionHandler() {
  }

  public VersionHandler(VersionIncrementer versionIncrementer,
                        Map<String, String> incrementerConfig,
                        VersionManipulator versionManipulator,
                        Map<String, String> manipulatorConfig) {
    this.versionIncrementer = Preconditions.checkNotNull(versionIncrementer);
    this.incrementerConfig = Preconditions.checkNotNull(incrementerConfig);
    this.versionManipulator = Preconditions.checkNotNull(versionManipulator);
    this.manipulatorConfig = Preconditions.checkNotNull(manipulatorConfig);

    Preconditions.checkArgument(versionIncrementer.validateConfig(incrementerConfig) == null);
    Preconditions.checkArgument(versionManipulator.validateConfig(manipulatorConfig) == null);
  }

  public VersionIncrementer getVersionIncrementer() {
    return versionIncrementer;
  }

  public void setVersionIncrementer(VersionIncrementer versionIncrementer) {
    this.versionIncrementer = versionIncrementer;
  }

  public VersionManipulator getVersionManipulator() {
    return versionManipulator;
  }

  public void setVersionManipulator(VersionManipulator versionManipulator) {
    this.versionManipulator = versionManipulator;
  }

  @Override
  public String toString() {
    return "VersionHandler{" +
      "versionIncrementer=" + versionIncrementer +
      ", versionManipulator=" + versionManipulator +
      '}';
  }

  public static class Deserializer extends StdDeserializer<VersionHandler>
  {
    public Deserializer(Class<?> vc) {
      super(vc);
    }

    public Deserializer(JavaType valueType) {
      super(valueType);
    }

    public Deserializer(StdDeserializer<VersionHandler> src) {
      super(src);
    }

    @Override
    public VersionHandler deserialize(JsonParser jp,
                                      DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
      JsonNode node = jp.getCodec().readTree(jp);

      ObjectMapper om = new ObjectMapper();
      om.registerModule(new Jdk8Module());

      validateVersionNode(jp, node, VERSION_INCREMENTER_NODE_NAME);
      validateVersionNode(jp, node, VERSION_MANIPULATOR_NODE_NAME);

      JsonNode incrementerNode = node.get(VERSION_INCREMENTER_NODE_NAME);
      JsonNode manipulatorNode = node.get(VERSION_MANIPULATOR_NODE_NAME);

      String incrementerType = incrementerNode.get(TYPE).asText();
      String manipulatorType = manipulatorNode.get(TYPE).asText();

      VersionIncrementer versionIncrementer = VERSION_INCREMENTERS.get(incrementerType);
      VersionManipulator versionManipulator = VERSION_MANIPULATORS.get(manipulatorType);

      Map<String, String> incrementerConfig = validateConfig(incrementerNode, versionIncrementer);
      Map<String, String> manipulatorConfig = validateConfig(manipulatorNode, versionManipulator);

      return new VersionHandler(versionIncrementer, incrementerConfig, versionManipulator, manipulatorConfig);
    }

    private static void validateVersionNode(JsonParser jp, JsonNode node, String versionNode) throws JsonProcessingException {

      if (!node.has(VERSION_INCREMENTER_NODE_NAME)) {
        throw new JsonParseException(jp, "Field " + VERSION_INCREMENTER_NODE_NAME + " is required.");
      } else {
        JsonNode subNode = node.get(VERSION_INCREMENTER_NODE_NAME);
        JsonParser subJp = subNode.traverse();

        if (!subNode.has(TYPE)) {
          throw new JsonParseException(subJp, "Field " + TYPE + " is required.");
        }
      }
    }

    private static Map<String, String> validateConfig(JsonNode node, Configurable configurable) throws JsonProcessingException {
      JsonParser jp = node.traverse();

      if (configurable.hasConfig()) {
        if (node.has(CONFIG)) {
          Map<String, String> config = Configurable.toMap(node.get(CONFIG));
          ReturnError returnError = configurable.validateConfig(config);

          if (returnError != null) {
            throw new JsonParseException(jp, returnError.getMessage());
          }

          return config;
        } else {
          throw new JsonParseException(jp, "Configuration was expected, but no configuration was specified.");
        }
      } else {
        if (node.has(CONFIG)) {
          throw new JsonParseException(jp, "Configuration was not expected, configuration was specified.");
        } else {
          return Maps.newHashMap();
        }
      }
    }
  }
}
