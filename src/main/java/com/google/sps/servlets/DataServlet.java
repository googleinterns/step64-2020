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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that returns some example content.
 * comments data
 */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private final Gson gson = new Gson();
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private static final String TIMESTAMP = "timestamp";
  private static final String TITLE = "title";
  private static final String SENTIMENT = "sentiment";
  private static final String UPVOTES = "upvotes";
  private static final String URL = "url";
  private final Analyze analyze = new Analyze();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<Object> threads = new ArrayList<Object>();

    for (int i = 0; i < 15; i++) {
      threads.add(AnalyzedThread.RandomTitle());
      threads.add(AnalyzedThread.RandomSentiment());
      threads.add(AnalyzedThread.RandomUpvotes());
      threads.add(AnalyzedThread.RandomUrl());
    }

    String json = gson.toJson(threads);
    response.setContentType("application/json;");
    response.getWriter().println(json);

    // get sentiment properties from text
    Sentiment sentimentFromText = analyze.analyzeSentimentText("youtube comment text");
    double sentimentScoreText = sentimentFromText.getScore();
    double magnitudeScoreText = sentimentFromText.getMagnitude();
    // print the main subjects in the text
    analyze.analyzeEntitiesText("youtube comment text");
    // print the syntax in the text
    analyze.analyzeSyntaxText("youtube comment text");
    // print categories in text
    analyze.entitySentimentText("youtube comment text");
  }
}
