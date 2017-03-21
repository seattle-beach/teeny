package com.example.teeny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration(exclude = { VelocityAutoConfiguration.class })
@ComponentScan
public class TeenyService {
  @Autowired
  DataAccessor da;

  @RequestMapping(value = "/", method = RequestMethod.POST)
  String createTeeny(@RequestParam(value = "url") String url) {
    Teeny teeny = Teeny.createTeeny(url);
    da.addTeeny(teeny);
    return teeny.getId();
  }

  @RequestMapping("{hash}")
  String getActualUrl(@PathVariable String hash) {
    Teeny teeny = da.accessTeeny(hash);
    if (teeny == null) {
      throw TeenyNotFoundException.ERROR;
    }
    return teeny.getUrl();
  }

  @RequestMapping(value = "{hash}", method = RequestMethod.DELETE)
  void deleteTeeny(@PathVariable String hash) {
    Teeny teeny = da.removeTeeny(hash);
    if (teeny == null) {
      throw TeenyNotFoundException.ERROR;
    }
  }

  @RequestMapping(value = "/", method = RequestMethod.DELETE)
  void clearTeeny() {
    da.clear();
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  Object orderAndListTeeny(@RequestParam(value = "verbose", defaultValue = "true", required = false) boolean verbose) {
    if (!verbose) {
      Meta meta = new Meta();
      meta.count = da.size();
      return meta;
    }

    return da.getAll();
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