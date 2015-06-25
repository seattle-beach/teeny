package com.example.teeny;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TeenyNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static final TeenyNotFoundException ERROR = new TeenyNotFoundException();
}
