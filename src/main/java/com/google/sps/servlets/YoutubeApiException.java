package com.google.sps.servlets;

import java.lang.Exception;

/**This error will be thrown when the Youtube Api experiences a problem
retrieving the request. */

public class YoutubeApiException extends Exception {
  public YoutubeApiException(String errorMessage, Throwable error) {
    super(errorMessage, error);
  }
}
