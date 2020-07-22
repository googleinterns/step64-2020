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

/**
 * Servlet responsible for storing Youtube Video and Displaying the details of the Youtube Video
 */
@WebServlet("/videos-sentiment")
public class DataServlet extends HttpServlet {
  private static final String TIMESTAMP = "timestamp";
  private static final String TITLE = "title";
  private static final String SENTIMENT = "sentiment";
  private static final String LIKES = "likes";
  private static final String URL = "url";
  private static final String NUMOFPAGES = "numOfPages";

  private final JSONObject threadInfo = new JSONObject();
  private final Gson gson = new Gson();
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private final Analyze analyze = new Analyze();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    for (YoutubePost post : newPosts) {
      String title = post.getTitle();
      double sentiment = analyze.getSentimentScore(post.getContent());
      int likes = Math.random()*100;
      String url = post.getUrl();
      AnalyzedVideo tempVideo = AnalyzedVideo.create(title, sentiment, likes, url);
      threadInfo.add(tempVideo);
    }

    int numOfPages = Math.max(1, ((int) Math.ceil(threadTitles.size() / postPerPage)));
    threadInfo = createCurrentPage(currentPage, postPerPage, threadInfo);

    

    response.setContentType("application/json;");
    response.getWriter().print(threadInfo);
  }

  private List<AnalyzedVideo> createCurrentPage(int currentPage, int postPerPage, List <AnalyzedVideo> threadInfo) {
    int start = (currentPage - 1) * postPerPage;
    int end = Math.min(threadTitles.size(), (currentPage * postPerPage));
    threadInfo = threadInfo.subList(start, end);
  }

  private int convertToInt(String beingconverted) {
    int convertee = 0;
    try {
      convertee = Integer.parseInt(beingconverted);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + beingconverted);
      return 1;
    } catch (IllegalArgumentException e) {
      System.err.println("The argument passed is not acceptable: " + beingconverted);
      return 1;
    }
    return convertee;
  }
}
