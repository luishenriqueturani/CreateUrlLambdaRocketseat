package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final S3Client s3Client = S3Client.builder().build();

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

      LocalDateTime expirationPluss7Days = LocalDateTime.now().plusDays(7);

      Instant instant = expirationPluss7Days.atZone(ZoneId.systemDefault()).toInstant();

      expirationTime = String.valueOf(instant.toEpochMilli());
    }

    Long expirationTimeInSeconds = Long.parseLong(expirationTime);

    String newUrl = UUID.randomUUID().toString().substring(0, 8);

    UrlData urlData = new UrlData(url, expirationTimeInSeconds);

    try {
      //transforma em json
      String json = objectMapper.writeValueAsString(urlData);

      //cria a conexão com o bucket do s3
      PutObjectRequest request = PutObjectRequest.builder()
          .bucket("java-lambda-rocketseat-url-storage")
          .key(newUrl + ".json")
          .contentType("application/json")
          .build();

      //envia para o bucket
      s3Client.putObject(request, RequestBody.fromString(json));

    } catch (Exception e) {
      throw new RuntimeException("Erro ao enviar para o bucket: " + e.getMessage());
    }

    Map<String, String> response = new HashMap<>();
    response.put("code", newUrl);

    return response;
  }
}