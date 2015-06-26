package com.example.teeny;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
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
  
  String postUrl;
  String getUrl;
  
  RestTemplate rest = new TestRestTemplate();

  @Before
  public void setup() {
    postUrl = "http://localhost:" + port;
    getUrl = "http://localhost:" + port + "/{id}";
    rest.delete(postUrl);
  }

  @Test
  public void shouldCreateOneTeeny() {
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
    vars.add("url", url1);

    postUrlAndVerifyHash(vars, url1Hash);
  }

  @Test
  public void shouldReturnOneTeeny() {
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();

    vars.add("url", url1);
    postUrlAndVerifyHash(vars, url1Hash);
    verifyUrl(url1Hash, url1);
  }

  @Test
  public void shouldReturnValidCount() {
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
    vars.add("url", url1);
    postUrlAndVerifyHash(vars, url1Hash);
    verifyCount(rest, postUrl, 1);

    vars.clear();
    vars.add("url", url2);
    postUrlAndVerifyHash(vars, url2Hash);
    verifyCount(rest, postUrl, 2);
  }

  @Test
  public void shouldDeleteTeeny() {
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
    vars.add("url", url1);
    postUrlAndVerifyHash(vars, url1Hash);
    verifyCount(rest, postUrl, 1);
    
    vars.clear();
    vars.add("url", url2);
    postUrlAndVerifyHash(vars, url2Hash);
    verifyCount(rest, postUrl, 2);
    
    rest.delete(getUrl, url1Hash);
    verifyCount(rest, postUrl, 1);
    
    rest.delete(getUrl, url2Hash);
    verifyCount(rest, postUrl, 0);
  }
  
  @Test
  public void shouldAdd2TeenyAndReturnValidTeeny() {
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
    vars.add("url", url1);
    postUrlAndVerifyHash(vars, url1Hash);

    vars.clear();
    vars.add("url", url2);
    postUrlAndVerifyHash(vars, url2Hash);

    verifyUrl(url1Hash, url1);
    verifyUrl(url2Hash, url2);

    assertNotEquals(url2, url1);
  }

  @Test
  public void shouldAllowDuplicateEntry() {
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
    vars.add("url", url1);

    postUrlAndVerifyHash(vars, url1Hash);
    verifyCount(rest, postUrl, 1);

    postUrlAndVerifyHash(vars, url1Hash);
    verifyCount(rest, postUrl, 1);
  }

  @Test
  public void emptyMapShouldNotFail() {
    String getUrl = "http://localhost:" + port;

    RestTemplate rest = new TestRestTemplate();
    ResponseEntity<String> response = rest.getForEntity(getUrl, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("[]", response.getBody());
  }
  
  private void postUrlAndVerifyHash(MultiValueMap<String, String> vars, String hash) {
    ResponseEntity<String> response = rest.postForEntity(postUrl, vars, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(hash, response.getBody());
  }

  private void verifyUrl(String hash, String url) {
    ResponseEntity<String> response = rest.getForEntity(getUrl, String.class, hash);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(url, response.getBody());
  }

  private void verifyCount(RestTemplate rest, String postUrl, int count) {
    ResponseEntity<String> response;
    response = rest.getForEntity(postUrl + "/?verbose={verbose}", String.class, "false");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("[\"" + count + "\"]", response.getBody());
  }
}