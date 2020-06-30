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
package com.google.sps.data;
 
 public class RedditData{
     
 }
//import net.dean.jraw;
//import java.io.IOException;
 /**
public class RedditData{
  @SuppressWarnings("unchecked")
    public void getRedditPosts() throws IOException {
    RedditService service = retrofit.create(RedditService.class);
    Call<RedditData> call = service.getPosts("GoogleFi", "hot", 3);
 
    RedditData redditData = call.execute().body();
    List<Map<String, Object>> children = (List<Map<String, Object>>) redditData.data.get("children");
 
    List<RedditPost> redditPosts = new ArrayList<>();
    for (Map<String, Object> child : children) {
        RedditPost redditPost = new RedditPost();
        redditPost.title = (String) ((Map<String, Object>)child.get("data")).get("title");
        redditPost.content = (String) ((Map<String, Object>) child.get("data")).get("selftext");
        redditPost.url = (String) ((Map<String, Object>) child.get("data")).get("url");
        redditPost.id = (String) ((Map<String, Object>) child.get("data")).get("id");
    }
}
 
public static class RedditPost {
    String title;
    String content;
    String url;
    String id;
}
 
public static class RedditData {
    String kind;
    Map<String, Object> data;
 
    private Retrofit retrofit =
    new Retrofit.Builder()
    .baseUrl("http://www.reddit.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build();
 
    public interface RedditService {
        @GET("r/{subreddit}/{sorting}.json")
        Call<RedditData> getPosts(
        @Path("subreddit") String subreddit,
        @Path("sorting") String sorting,
        @Query("limit") int limit);
 
        @GET("r/{subreddit}/comments/{id}.json")
        Call<List<RedditData>> getComments(
        @Path("subreddit") String subreddit,
        @Path("id") String id);
    }
  }
}
*/