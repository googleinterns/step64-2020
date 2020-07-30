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

package com.google.sps.servlets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.client.util.DateTime;
import java.util.Date;
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
import com.google.sps.servlets.YoutubeApi;
import com.google.sps.servlets.YoutubeApiException;
import com.google.sps.servlets.YoutubePost;
import java.io.Console;
import java.io.IOException;
import java.lang.Math;
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
import java.math.BigInteger;

/**
 * Servlet responsible for storing Youtube Video and Displaying the details of the Youtube Video
 */
@WebServlet("/videos-sentiment")
public class DataServlet extends HttpServlet {
  private static final String TIMESTAMP = "Timestamp";
  private static final String LAST_UPDATE = "Last Update";
  private static final String TITLE = "Titles";
  private static final String SENTIMENT = "Sentiments";
  private static final String LIKES ="Likes"
  private static final String VIDEO = "Video";
  private static final String ID = "Id";

  private final JSONObject threadInfo = new JSONObject();
  private final Gson gson = new Gson();
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private final Analyze analyze = new Analyze();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(VIDEO);
    PreparedQuery results = datastore.prepare(query);

    List<YoutubePost> newPosts;
    try {
      newPosts = YoutubeApi.getYoutubePost();
    } catch (YoutubeApiException e) {
      System.out.println("Error: Youtube api returning exception" + e);
      response.sendError(500, "An error occurred while fetching Youtube Posts");
      return;
    }

    List<AnalyzedVideo> threadInfo = new ArrayList<AnalyzedVideo>();
    int currentPage = convertToInt(request.getParameter("currentPage"));
    int postPerPage = convertToInt(request.getParameter("postPerPage"));
    long currentTimestamp = System.currentTimeMillis();
    for (YoutubePost post : newPosts) {
      String title = post.getTitle();
      String id = post.getID();
      String url = post.getUrl();
      long timeStamp = post.getTimeStamp().getValue();
      int likes = (int) Math.random()*100;
      double sentiment = analyze.getSentimentScore(post.getContent());
      
      Entity videoEntity = new Entity(VIDEO);
      videoEntity.setProperty(ID, id);
      videoEntity.setProperty(TITLE, title);
      videoEntity.setProperty(LIKES, likes);
      videoEntity.setProperty(SENTIMENT, sentiment);
      videoEntity.setProperty(URL, url);
      videoEntity.setProperty(SENTIMENT, seniment);
      videoEntity.setProperty(TIMESTAMP, timeStamp); 
      videoEntity.setProperty(LAST_UPDATE, currentTimestamp);
      datastore.put(videoEntity);
    }
    
    for(Entity entity : results.asIterable()){
      String title = entit
      int like
      double sentiment 
      String url
      DateTime timeStamp
      
      AnalyzedVideo currentVideo =
      threadInfo.add(currentVideo);
    }

    threadInfo = createCurrentPage(currentPage, postPerPage, threadInfo);

    response.setContentType("application/json;");
    response.getWriter().print(gson.toJson(threadInfo));
  }

  private List<AnalyzedVideo> createCurrentPage(
      int currentPage, int postPerPage, List<AnalyzedVideo> threadInfo) {
    int start = (currentPage - 1) * postPerPage;
    int end = Math.min(threadInfo.size(), (currentPage * postPerPage));
    threadInfo = threadInfo.subList(start, end);
    return threadInfo;
  }

  private int convertToInt(String beingconverted) {
    int convertee = 0;
    try {
      convertee = Integer.parseInt(beingconverted);
    } catch (NumberFormatException e) {
      System.err.println("Error: Argument is returning: " + beingconverted);
      throw new IllegalArgumentException("Could not convert to int", e);
    }
    return convertee;
  }
}
