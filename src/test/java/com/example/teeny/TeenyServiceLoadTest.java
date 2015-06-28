package com.example.teeny;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
public class TeenyServiceLoadTest {

  static final String BASE_URL = "http://www.pivotal.io/";
  static final int CREATE_RATE = 20;
  static final int GET_RATE = 200;
  static final int TEST_TIME_SEC = 2;
  static final int TEST_SUMMARY_SEC = 1;

  List<String> ids = new ArrayList<>();
  int getCount;

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
  public void loadTest() throws Exception {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    Runnable addUrl = () -> addUrl();
    Runnable getUrl = () -> getUrl();
    Runnable printStats = () -> printStats();

    ScheduledFuture<?> addFuture = scheduler.scheduleAtFixedRate(addUrl, 0, 1000 / CREATE_RATE, TimeUnit.MILLISECONDS);
    ScheduledFuture<?> getFuture = scheduler.scheduleAtFixedRate(getUrl, 0, 1000 / GET_RATE, TimeUnit.MILLISECONDS);
    ScheduledFuture<?> printFuture = scheduler.scheduleAtFixedRate(printStats, 1, TEST_SUMMARY_SEC, TimeUnit.SECONDS);

    ScheduledFuture<?> end = scheduler.schedule(() -> {
      addFuture.cancel(false);
      getFuture.cancel(false);
      printFuture.cancel(false);
    }, TEST_TIME_SEC, TimeUnit.SECONDS);
    end.get();

    ResponseEntity<Teeny[]> response = rest.getForEntity(postUrl, Teeny[].class, "false");
    Teeny[] sortedTeeny = response.getBody();
    Arrays.sort(sortedTeeny, (Teeny o1, Teeny o2) -> {
      return o1.getPopularity() - o2.getPopularity();
    });

    for (Teeny teeny : sortedTeeny) {
      System.out.println(teeny);
    }
  }

  private void addUrl() {
    String newUrl = BASE_URL + Long.toString(System.nanoTime());
    MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
    vars.add("url", newUrl);

    ResponseEntity<String> response = rest.postForEntity(postUrl, vars, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    synchronized (ids) {
      ids.add(response.getBody());
    }
  }

  private void getUrl() {
    String hash;
    synchronized (ids) {
      if (ids.size() == 0) {
        return;
      }
      int randomIndex = (int) Math.floor(Math.random() * ids.size());
      hash = ids.get(randomIndex);
      getCount++;
    }

    ResponseEntity<String> response = rest.getForEntity(getUrl, String.class, hash);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  private void printStats() {
    synchronized (ids) {
      System.out.format("URLs: %d    Gets: %d%n", ids.size(), getCount);
    }
  }
}