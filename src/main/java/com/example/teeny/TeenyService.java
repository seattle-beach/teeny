package com.example.teeny;

import java.util.ArrayList;
import java.util.List;
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
  List<String> orderAndListTeeny(
      @RequestParam(value = "verbose", defaultValue = "true", required = false) boolean verbose) {
    List<String> result = new ArrayList<>();
    
    if (!verbose) {
      result.add("" + map.size());
    } else {
      for (Entry<String, Teeny> entry : map.entrySet()) {
        result.add(entry.getKey() + " :: "+ entry.getValue().getUrl());
      }
    }
    
    return result;
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(TeenyService.class, args);
  }
}