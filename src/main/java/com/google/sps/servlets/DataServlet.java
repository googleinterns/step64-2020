// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.cloud.language.v1.Sentiment;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import java.lang.Math;
import com.google.sps.servlets.YoutubeApi;
import com.google.sps.servlets.YoutubeApiException;
import com.google.sps.servlets.YoutubePost;
import java.io.Console;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 * Servlet responsible for storing Youtube Video and Displaying the details of the Youtube Video
 */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final String TIMESTAMP = "timestamp";
  private static final String TITLE = "title";
  private static final String SENTIMENT = "sentiment";
  private static final String UPVOTES = "upvotes";
  private static final String URL = "url";
  private  List<String> threadTitles = new ArrayList<String>();
  private  List<Double> threadSentiments = new ArrayList<Double>();
  private  List<Integer> threadUpvotes = new ArrayList<Integer>();
  private  List<String> threadUrls = new ArrayList<String>();
  private final JSONObject threadInfoList = new JSONObject();
  private final Gson gson = new Gson();
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private final Analyze analyze = new Analyze();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /*try {
      List<YoutubePost> newPost = YoutubeApi.getYoutubePost();
    } catch (YoutubeApiException e) {
      System.out.println("Error: Youtube api returning exception" + e);
      response.sendError(500, "An error occurred while fetching Youtube Posts");
      return;
    }*/
    int currentPage = convertToInt(request.getParameter("currentPage"));
    int postperPage = convertToInt(request.getParameter("postPerPage"));
    String order = request.getParameter("order");

    threadTitles.clear();
    threadSentiments.clear();
    threadUpvotes.clear();
    threadUrls.clear();
    
    threadInfoList.put("numOfPages", calculateNumOfPages(threadTitle.size()));
    threadInfoList.put(TITLE, threadTitles);
    threadInfoList.put(SENTIMENT, threadSentiments);
    threadInfoList.put(UPVOTES, threadUpvotes);
    threadInfoList.put(URL, threadUrls);

    response.setContentType("application/json;");
    response.getWriter().print(threadInfoList);

    /*// get sentiment properties from text
    Sentiment sentimentFromText = analyze.analyzeSentimentText("youtube comment text");
    double sentimentScoreText = sentimentFromText.getScore();
    double magnitudeScoreText = sentimentFromText.getMagnitude();
    // print the main subjects in the text
    analyze.analyzeEntitiesText("youtube comment text");
    // print the syntax in the text
    analyze.analyzeSyntaxText("youtube comment text");
    // print categories in text
    analyze.entitySentimentText("youtube comment text");*/
  }
  private int calculateNumOfPages(int postPerPage, int numOfPages) {
      return (int) Math.ceil(numOfPages/postPerPage);
  }
  private int convertToInt(String beingconverted) {
    int convertee;
    try {
      convertee = Integer.parseInt(beingconverted);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + beingconverted);
      return 1;
    }

    if (convertee < 1 || convertee > 10) {
      System.err.println("Number must be between 1 and 10");
      return 1;
    }
    return convertee;
  }
}
