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

import com.google.sps.servlets.YoutubeApiException;

// Data model for a singular Youtube post that will be added into a list of posts.

public class YoutubePost {
  private final String title;
  private final String content;
  private static String url;
  private final String id;

  public YoutubePost(String title, String content, String id) throws YoutubeApiException {
    this.title = title;
    this.content = content;
    this.id = id;
    url = getUrl(id);
  }

  public static String getUrl(String id) {
    url = ("https://www.youtube.com/watch?v=" + id);
    return url;
  }

  @Override
  public String toString() {
    String post = ("title: " + title + "\n"
        + "Description: " + content + "\n"
        + "Id: " + id + "\n"
        + "Url: " + url);
    return post;
  }
}
