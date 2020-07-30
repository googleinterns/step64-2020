package com.google.sps.servlets;

import java.io.IOException;
import com.google.api.client.util.DateTime;

/** DataModel for a Youtube Post that's been analyzed by the NLP API */
@AutoValue
abstract class AnalyzedVideo {
  abstract String title();
  abstract DateTime timestamp();
  abstract double sentiment();
  abstract int likes();
  abstract String url();

  public static AnalyzedVideo create(String title, DateTime timestamp double sentiment, int likes, String url) {
    return new AutoValue_AnalyzedVideo(title, timestamp sentiment, likes, url);
  }
}
