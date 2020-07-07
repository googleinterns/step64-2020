package com.google.sps.servlets;

import java.io.IOException;
import java.util.Random;

public class AnalyzedThread {
  public static String[] titlesArray = {"Where is this supposed to go?",
      "This program needs to work better", "The wifi needs to be more consistent",
      "This could be better", "Maybe this could be structured a different way"};
  public static double[] sentimentArray = {-.3, -.5, -.1, -.2, -.5};
  public static int[] upvotesArray = {30, 50, 10, 5, 12};
  public static String[] urlArray = {"http://www.youtube.com", "http://www.reddit.com",
      "http://www.facebook.com", "http://www.yahoo.com", "http://www.amazon.com"};
  private static String threadTitle;
  private static double sentiment;
  private static int upvotes;
  private static int timestamp;
  private static String threadUrl;
  private static Random rand = new Random();
  // private static String subject;

  public AnalyzedThread() {}

  private AnalyzedThread(String threadTitle, double sentiment, int upvotes, int timestamp,
      String threadUrl /*, String subject*/) {
    this.threadTitle = threadTitle;
    this.sentiment = sentiment;
    this.upvotes = upvotes;
    this.timestamp = timestamp;
    this.threadUrl = threadUrl;
    // this.subject = subject;
  }

  public AnalyzedThread setThread(
      String threadTitle, double sentiment, int upvotes, int timestamp, String threadUrl) {
    return new AnalyzedThread(threadTitle, sentiment, upvotes, timestamp, threadUrl);
  }

  public void setThreadTitle(String title) {
    this.threadTitle = threadTitle;
  }
  public String title() {
    return threadTitle;
  }
  public void setSentiment(double sentiment) {
    this.sentiment = sentiment;
  }
  public double sentiment() {
    return sentiment;
  }
  public void setUpvotes(int upvotes) {
    this.upvotes = upvotes;
  }
  public int upvotes() {
    return upvotes;
  }
  public void setTimestamp(int timestamp) {
    this.timestamp = timestamp;
  }
  public int timeStamp() {
    return timestamp;
  }
  public void setthreadUrl(String threadUrl) {
    this.threadUrl = threadUrl;
  }
  public String url() {
    return threadUrl;
  }
  /*
  public void setSubject(String subject){
      this.subject = subject;
  }
  public String subject(){
      return subject;
  }*/
  public static String RandomTitle() {
    String StringValue = titlesArray[rand.nextInt(4)];
    return StringValue;
  }
  public static double RandomSentiment() {
    double SentimentValue = sentimentArray[rand.nextInt(4)];
    return SentimentValue;
  }
  public static int RandomUpvotes() {
    int UpvotesValue = upvotesArray[rand.nextInt(4)];
    return UpvotesValue;
  }
  public static String RandomUrl() {
    String UrlValue = urlArray[rand.nextInt(4)];
    return UrlValue;
  }
  public String toString() {
    return String.format("thread: %s, sentiment: %d, upvote: %d, timestamp: %d, url: %s",
        threadTitle, sentiment, upvotes, timestamp, threadUrl);
  }
}
