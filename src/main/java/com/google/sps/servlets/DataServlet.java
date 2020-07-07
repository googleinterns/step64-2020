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
  private static String TIMESTAMP = "timestamp";
  private static String TITLE = "title";
  private static String SENTIMENT = "sentiment";
  private static String UPVOTES = "upvotes";
  private static String URL = "url";
  // private static String SUBJECTTAG = "subjecttag";
  // private static String SUBJECTTAGLIST = "subjecttaglist";
  // private static String CUSTOMTAGLIST = "customtaglist";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // response.sendRedirect("/index.html");
    /*String settitle = "Orange";
    int settimestamp = 90;
    int setupvotes = 32;
    double setsentiment = -.3;
    String seturl = "https://google.com";
    //String setsubjecttag = "";


    //Create Enity for the analyzed thread
    Entity threadEntity = new Entity("Thread");
    threadEntity.setProperty(TITLE, settitle);
    threadEntity.setProperty(UPVOTES, setupvotes);
    threadEntity.setProperty(SENTIMENT, setsentiment);
    threadEntity.setProperty(TIMESTAMP, settimestamp);
    threadEntity.setProperty(URL,seturl);
    //threadEntity.setProperty(SUBJECTTAG, subjecttag);

    datastore.put(threadEntity);
    Query query = new Query("Thread");*/

    // PreparedQuery results = datastore.prepare(query);
    List<Object> threads = new ArrayList<Object>();

    /*for(Entity entity : results.asIterable()){
        String title = ((String) entity.getProperty(TITLE));
        System.out.println(title);
        int upvotes = ((int) entity.getProperty(UPVOTES));
        System.out.println(upvotes);
        double sentiment = ((double) entity.getProperty(SENTIMENT));
        System.out.println(sentiment);
        String url = ((String) entity.getProperty(URL));
        System.out.println(url);
        /*Script does not handle subject tags yet*///String subject = ((String)entity.getProperty(SUBJECTTAG));
    /*threads.add(title);
    threads.add(upvotes);
    threads.add(sentiment);
    threads.add(url);
    //threads.add(subject);
}*/

    for (int i = 0; i < 15; i++) {
      threads.add(AnalyzedThread.RandomTitle());
      threads.add(AnalyzedThread.RandomSentiment());
      threads.add(AnalyzedThread.RandomUpvotes());
      threads.add(AnalyzedThread.RandomUrl());
    }

    String json = gson.toJson(threads);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
