package com.google.sps.servlets;

import com.google.auto.value.AutoValue;
import java.io.IOException;

/** Represents a Video that's been analyzed by the NLP API */
@AutoValue
abstract class AnalyzedVideo {
  abstract String title();
  abstract double sentiment();
  abstract int likes();
  abstract String url();

  public static AnalyzedVideo create(String title, double sentiment, int likes, String url) {
    return new AutoValue_AnalyzedVideo(title, sentiment, likes, url);
  }
}
