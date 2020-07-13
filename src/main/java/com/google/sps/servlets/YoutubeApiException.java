package com.google.sps.servlets;

import java.lang.Exception;

public class YoutubeApiException extends Exception {
  public YoutubeApiException(String errorMessage, Throwable error) {
    super(errorMessage, error);
  }
}