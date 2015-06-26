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
    this.id = "" + url.hashCode();
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
