package com.google.sps;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.Random;

public class AnalyzedThread {
  public final static ImmutableList<String> titlesList =
      ImmutableList.of("Where is this supposed to go?", "This program needs to work better",
          "The wifi needs to be more consistent", "This could be better",
          "Maybe this could be structured a different way");
  public final static ImmutableList<Double> sentimentList =
      ImmutableList.of(-.3, -.5, -.1, -.2, -.5);
  public final static ImmutableList<Integer> upvotesList = ImmutableList.of(30, 50, 10, 5, 12);
  public final static ImmutableList<String> urlList =
      ImmutableList.of("http://www.youtube.com", "http://www.reddit.com", "http://www.facebook.com",
          "http://www.yahoo.com", "http://www.amazon.com");
  private final String threadTitle;
  private final double sentiment;
  private final int upvotes;
  private final int timestamp;
  private final String threadUrl;
  private final static Random rand = new Random();

  private AnalyzedThread(
      String threadTitle, double sentiment, int upvotes, int timestamp, String threadUrl) {
    this.threadTitle = threadTitle;
    this.sentiment = sentiment;
    this.upvotes = upvotes;
    this.timestamp = timestamp;
    this.threadUrl = threadUrl;
    // this.subject = subject;
  }

  public static AnalyzedThread createThread(
      String threadTitle, double sentiment, int upvotes, int timestamp, String threadUrl) {
    return new AnalyzedThread(threadTitle, sentiment, upvotes, timestamp, threadUrl);
  }

  public String title() {
    return threadTitle;
  }

  public double sentiment() {
    return sentiment;
  }

  public int upvotes() {
    return upvotes;
  }

  public int timeStamp() {
    return timestamp;
  }

  public String url() {
    return threadUrl;
  }

  public static String RandomTitle() {
    String StringValue = titlesList.get(rand.nextInt(4));
    return StringValue;
  }

  public static double RandomSentiment() {
    double SentimentValue = sentimentList.get(rand.nextInt(4));
    return SentimentValue;
  }

  public static int RandomUpvotes() {
    int UpvotesValue = upvotesList.get(rand.nextInt(4));
    return UpvotesValue;
  }

  public static String RandomUrl() {
    String UrlValue = urlList.get(rand.nextInt(4));
    return UrlValue;
  }

  public String toString() {
    return String.format("thread: %s, sentiment: %d, upvote: %d, timestamp: %d, url: %s",
        threadTitle, sentiment, upvotes, timestamp, threadUrl);
  }
}
