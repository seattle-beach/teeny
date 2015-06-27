package com.example.teeny;

import java.util.Map.Entry;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;

@Component
@Primary
public class GeodeDataAccessor implements DataAccessor {
  String LOCATOR = System.getProperty("locator");
  String SERVER = System.getProperty("server");
  int PORT = Integer.getInteger("port", 1527);
  String REGION_TYPE = System.getProperty("region_type", ClientRegionShortcut.LOCAL.name());

  ClientCache cache;
  Region<String, String> teenyRegion;
  Region<String, Integer> statsRegion;

  public GeodeDataAccessor() {
    if (LOCATOR != null) {
      cache = new ClientCacheFactory().addPoolLocator(LOCATOR, PORT).create();
    } else if (SERVER != null) {
      cache = new ClientCacheFactory().addPoolServer(LOCATOR, PORT).create();
    } else {
      cache = new ClientCacheFactory().create();
    }

    ClientRegionFactory<String, String> teenyFactory = cache.createClientRegionFactory(REGION_TYPE);
    teenyRegion = teenyFactory.create("teeny");

    ClientRegionFactory<String, Integer> statsFactory = cache.createClientRegionFactory(REGION_TYPE);
    statsRegion = statsFactory.create("stats");
  }

  @Override
  public void addTeeny(Teeny teeny) {
    teenyRegion.put(teeny.getId(), teeny.getUrl());
    statsRegion.put(teeny.getId(), 0);
  }

  @Override
  public Teeny accessTeeny(String id) {
    String url = teenyRegion.get(id);
    int count = statsRegion.get(id);
    statsRegion.put(id, ++count);
    return new Teeny(url, count);
  }

  @Override
  public int size() {
    return teenyRegion.size();
  }

  @Override
  public Teeny[] getAll() {
    Teeny[] result = new Teeny[size()];
    int i = 0;
    for (Entry<String, String> entry : teenyRegion.entrySet()) {
      String url = entry.getValue();
      int count = statsRegion.get(entry.getKey());
      result[i++] = new Teeny(url, count);
    }
    return result;
  }

  @Override
  public void clear() {
    teenyRegion.clear();
    statsRegion.clear();
  }

  @Override
  public Teeny removeTeeny(String id) {
    String url = teenyRegion.remove(id);
    return new Teeny(url);
  }
}
