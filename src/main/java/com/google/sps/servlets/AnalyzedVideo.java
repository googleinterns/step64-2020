package com.google.sps.servlets;

import java.io.IOException;
import com.google.api.client.util.DateTime;
import com.google.auto.value.AutoValue;

/** DataModel for a Youtube Post that's been analyzed by the NLP API */
@AutoValue
abstract class AnalyzedVideo {
  abstract String title();
  abstract String timestamp();
  abstract double sentiment();
  abstract long likes();
  abstract String url();

  public static AnalyzedVideo create(String title, String timestamp, double sentiment, long likes, String url){
    return new AutoValue_AnalyzedVideo(title, timestamp, sentiment, likes, url);
  }
}
