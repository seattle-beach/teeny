package com.example.teeny;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class TeenyService {
  ConcurrentHashMap<String, Teeny> map = new ConcurrentHashMap<>();

  @RequestMapping(value = "/", method = RequestMethod.POST)
  String createTeeny(@RequestParam(value = "url") String url) {
    Teeny teeny = Teeny.createTeeny(url);
    map.put(teeny.getId(), teeny);
    return teeny.getId();
  }

  @RequestMapping("{hash}")
  String getActualUrl(@PathVariable String hash) {
    Teeny teeny = map.get(hash);
    if (teeny == null) {
      throw TeenyNotFoundException.ERROR;
    }
    teeny.incrementPopularity();
    return teeny.getUrl();
  }

  @RequestMapping(value = "{hash}", method = RequestMethod.DELETE)
  void deleteTeeny(@PathVariable String hash) {
    Teeny teeny = map.remove(hash);
    if (teeny == null) {
      throw TeenyNotFoundException.ERROR;
    }
  }

  @RequestMapping(value = "/", method = RequestMethod.DELETE)
  void clearTeeny() {
    map.clear();
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  Object orderAndListTeeny(@RequestParam(value = "verbose", defaultValue = "true", required = false) boolean verbose) {

    if (!verbose) {
      Meta meta = new Meta();
      meta.count = map.size();
      return meta;
    }

    Teeny[] result = new Teeny[map.size()];
    int i = 0;
    for (Entry<String, Teeny> entry : map.entrySet()) {
      result[i++] = entry.getValue();
    }

    return result;
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(TeenyService.class, args);
  }

  public static class Meta {
    private int count;

    public int getCount() {
      return count;
    }
  }
}