package com.example.teeny;

import java.util.concurrent.atomic.AtomicInteger;

public class Teeny {
  String id;
  String url;

  final AtomicInteger accessCounter = new AtomicInteger();
  
  public Teeny() {
    this.id = null;
    this.url = null;
  }
  
  public Teeny(String url) {
    this.url = url;
    int hash = url.hashCode();
    hash = hash < 0 ? hash * -1 : hash;
    this.id = Integer.toString(hash, 36);
  }
  
  public String getId() {
    return id;
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
