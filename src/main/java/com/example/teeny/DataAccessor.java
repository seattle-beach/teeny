package com.example.teeny;

public interface DataAccessor {
  public void addTeeny(Teeny teeny);
  public Teeny accessTeeny(String id);
  public Teeny removeTeeny(String id);
  public int size();
  public Teeny[] getAll();
  public void clear();
}
