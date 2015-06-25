package com.example.teeny;

import java.util.concurrent.atomic.AtomicInteger;

public class Teeny {
  String hash;
  String url;

  final AtomicInteger accessCounter = new AtomicInteger();
  
  public Teeny(String url) {
    this.url = url;
    this.hash = "" + url.hashCode();
  }
  
  public String getString() {
    return hash;
  }
  
  public String getUrl() {
    accessCounter.incrementAndGet();
    return url;
  }
  
  public int getPopularity() {
    return accessCounter.intValue();
  }
  
  public static Teeny createTeeny(String url) {
    return new Teeny(url);
  }
}
