package com.example.teeny;

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
    map.put(teeny.getString(), teeny);
    return teeny.getString();
  }

  @RequestMapping("{hash}")
  String getActualUrl(@PathVariable String hash) {
    Teeny teeny = map.get(hash);
    if (teeny == null) {
      throw TeenyNotFoundException.ERROR;
    }
    return teeny.getUrl();
  }

  @RequestMapping(value = "{hash}", method = RequestMethod.DELETE)
  String deleteTeeny(@PathVariable String hash) {
    Teeny teeny = map.remove(hash);
    if (teeny == null) {
      throw TeenyNotFoundException.ERROR;
    }
    return teeny.getUrl();
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  String[] orderAndListTeeny() {
    // TODO
    return new String[] { "Popular urls" };
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(TeenyService.class, args);
  }
}