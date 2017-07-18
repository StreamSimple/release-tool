package com.simplifi.it.rt.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.simplifi.it.javautil.err.ReturnError;

import java.util.Iterator;
import java.util.Map;

public interface Configurable
{
  boolean hasConfig();
  ReturnError validateConfig(Map<String, String> config);

  static Map<String, String> toMap(JsonNode node) throws JsonParseException
  {
    Map<String, String> config = Maps.newHashMap();
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();

    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> entry = iterator.next();
      String key = entry.getKey();
      JsonNode valueNode = entry.getValue();
      JsonParser valueParser = valueNode.traverse();

      if (!valueNode.isValueNode()) {
        throw new JsonParseException(valueParser, "Non value node.");
      }

      config.put(key, valueNode.asText());
    }

    return config;
  }
}
