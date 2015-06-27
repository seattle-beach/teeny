package com.example.teeny;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class LocalMapDataAccessor implements DataAccessor {
  ConcurrentHashMap<String, Teeny> map = new ConcurrentHashMap<>();

  @Override
  public void addTeeny(Teeny teeny) {
    map.put(teeny.getId(), teeny);
  }

  @Override
  public Teeny accessTeeny(String id) {
    map.get(id).incrementPopularity();
    return map.get(id);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public Teeny[] getAll() {
    Teeny[] result = new Teeny[map.size()];
    int i = 0;
    for (Entry<String, Teeny> entry : map.entrySet()) {
      result[i++] = entry.getValue();
    }
    return result;
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Teeny removeTeeny(String id) {
    return map.remove(id);
  }
}
