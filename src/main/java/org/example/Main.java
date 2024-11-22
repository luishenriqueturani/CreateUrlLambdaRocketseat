package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Map<String, String> handleRequest(Map<String, Object> stringObjectMap, Context context) {
    String body = stringObjectMap.get("body").toString();

    Map<String, String> bodymap;
    try {
      bodymap = objectMapper.readValue(body, Map.class);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao converter o body: " + e.getMessage());
    }

    String url = bodymap.get("originalUrl");
    String expirationTime = bodymap.get("expirationTime");

    if (url == null) {
      throw new RuntimeException("O campo url não foi informado");
    }

    if (expirationTime == null) {
      throw new RuntimeException("O campo expirationTime não foi informado");
    }

    String newUrl = UUID.randomUUID().toString().substring(0, 8);

    Map<String, String> response = new HashMap<>();
    response.put("code", newUrl);

    return response;
  }
}