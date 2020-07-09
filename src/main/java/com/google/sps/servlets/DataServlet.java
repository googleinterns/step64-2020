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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private final Gson gson = new Gson();
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private static List <String> threadTitles = new ArrayList <String>();
  private static List <Double> threadSentiments = new ArrayList <Double>();
  private static List <Integer> threadUpvotes = new ArrayList <Integer>();
  private static List <String> threadUrls = new ArrayList <String>();
  private static JSONObject threadInfoList = new JSONObject();
  private static final String TIMESTAMP = "timestamp";
  private static final String TITLE = "title";
  private static final String SENTIMENT = "sentiment";
  private static final String UPVOTES = "upvotes";
  private static final String URL = "url";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    for (int i = 0; i < 15; i++) {
      threadTitles.add(AnalyzedThread.RandomTitle());
      threadSentiments.add(AnalyzedThread.RandomSentiment());
      threadUpvotes.add(AnalyzedThread.RandomUpvotes());
      threadUrls.add(AnalyzedThread.RandomUrl());
    }
    
    String titles = gson.toJson(threadTitles);
    String sentiments = gson.toJson(threadSentiments);
    String upvotes = gson.toJson(threadUpvotes);
    String urls = gson.toJson(threadUrls);


    threadInfoList.put(TITLE, titles);
    threadInfoList.put(SENTIMENT, sentiments);
    threadInfoList.put(UPVOTES, upvotes);
    threadInfoList.put(URL, urls);

    System.out.print(threadInfoList.toString());

    response.setContentType("application/json;");
    response.getWriter().println(threadInfoList);
  }
}
