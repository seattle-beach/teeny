package com.example.teeny;

import java.util.concurrent.atomic.AtomicInteger;

public class Teeny {
  String id;
  String url;

  final AtomicInteger popularity = new AtomicInteger();

  public Teeny() {
    this(null);
  }

  public Teeny(String url) {
    this(url, 0);
  }

  public Teeny(String url, int count) {
    if (url == null) {
      return;
    }
    this.url = url;
    int hash = url.hashCode();
    hash = hash < 0 ? hash * -1 : hash;
    this.id = Integer.toString(hash, 36);
    this.popularity.set(count);
  }

  public String getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  public int getPopularity() {
    return popularity.intValue();
  }

  public void incrementPopularity() {
    popularity.incrementAndGet();
  }

  public static Teeny createTeeny(String url) {
    return new Teeny(url);
  }

  @Override
  public String toString() {
    return "Teeny [id=" + id + ", url=" + url + ", popularity=" + popularity + "]";
  }
}
