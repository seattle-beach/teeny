package com.example.teeny;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TeenyService.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class TeenyServiceTest {
  String url1 = "www.yahoo.com";
  String url1Hash = "" + url1.hashCode();
  String url2 = "www.google.com";
  String url2Hash = "" + url2.hashCode();
  
  @Value("${local.server.port}")
  int port;
  
  @Test
  public void shouldCreateOneTeeny() {
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
    String postUrl = "http://localhost:" + port;
    vars.add("url", url1);
    
    RestTemplate rest = new TestRestTemplate();
    ResponseEntity<String> response = rest.postForEntity(postUrl, vars, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(url1Hash, response.getBody());
  }

  @Test
  public void shouldReturnOneTeeny() {
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();

    String postUrl = "http://localhost:" + port;
    vars.add("url", url1);
    
    RestTemplate rest = new TestRestTemplate();
    ResponseEntity<String> response = rest.postForEntity(postUrl, vars, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(url1Hash, response.getBody());
    
    String getUrl = "http://localhost:" + port + "/{id}" ;
    response = rest.getForEntity(getUrl, String.class, url1Hash);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(url1, response.getBody());
  }
  
  @Test
  public void shouldAdd2TeenyAndReturnValidTeeny() {
    RestTemplate rest = new TestRestTemplate();
    String postUrl = "http://localhost:" + port;
    
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
    vars.add("url", url1);
    ResponseEntity<String> response = rest.postForEntity(postUrl, vars, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(url1Hash, response.getBody());
    
    vars.clear();
    vars.add("url", url2);
    response = rest.postForEntity(postUrl, vars, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(url2Hash, response.getBody());
    
    String getUrl = "http://localhost:" + port + "/{id}" ;
    response = rest.getForEntity(getUrl, String.class, url1Hash);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(url1, response.getBody());

    response = rest.getForEntity(getUrl, String.class, url2Hash);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(url2, response.getBody());
    
    assertNotEquals(url2, url1);
  }
  
  @Test
  public void shouldAllowDuplicateEntry() {
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
    RestTemplate rest = new TestRestTemplate();
    
    String postUrl = "http://localhost:" + port;
    vars.add("url", url1);
    
    ResponseEntity<String> response = rest.postForEntity(postUrl, vars, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(url1Hash, response.getBody());
    
    response = rest.postForEntity(postUrl, vars, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(url1Hash, response.getBody());
  }
  
  @Test
  public void getShouldNotFail() {
    String getUrl = "http://localhost:" + port;
    
    RestTemplate rest = new TestRestTemplate();
    ResponseEntity<String> response = rest.getForEntity(getUrl, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("[\"Popular urls\"]", response.getBody());
  }
}