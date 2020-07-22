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

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube.CommentThreads;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.sps.servlets.YoutubeApi;
import com.google.sps.servlets.YoutubeApiException;
import java.math.BigInteger;
import java.util.List;

/** Data model for a singular Youtube post that will be added into a list of posts. */
public class YoutubePost {
  private final String title;
  private final String content;
  private final String url;
  private final String id;
  private static DateTime timeStamp;
  private final List<String> comments;
  private final List<Long> commentLikesList;
  private static BigInteger likeCount;

  public YoutubePost(String title, String content, String id, List<String> comments, List<Long> commentLikesList, DateTime timeStamp) {
    this.title = title;
    this.content = content;
    this.id = id;
    this.url = "https://www.youtube.com/watch?v=" + id;
    this.comments = comments;
    this.commentLikesList = commentLikesList;
    this.timeStamp = timeStamp;
    this.likeCount = likeCount;
  }

  public BigInteger getLikes() {
    return this.likeCount;
  }

  public List<String> getComments() {
    return this.comments;
  }

  public List<Long> getCommentLikes() {
    return this.commentLikesList;
  }

  public DateTime getTimeStamp() {
    return this.timeStamp;
  }

  public String getUrl(String id) {
    return this.url;
  }

  public String getUrl() {
    return this.url;
  }

  public String getID() {
    return this.id;
  }

  public String getContent() {
    return this.content;
  }

  public String getTitle() {
    return this.title;
  }

  @Override
  public String toString() {
    String post = ("title: " + this.title + "\n"
        + "Description: " + this.content + "\n"
        + "Id: " + this.id + "\n"
        + "Url: " + this.url + "\nComments: " + this.comments + this.commentLikesList);
    return post;
  }
}
