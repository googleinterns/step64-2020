package com.google.sps.servlets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

public class YoutubeApi {
  private static final String DEVELOPER_KEY = "AIzaSyCSxCO3BeXFt1lYAou94rtlyQhinc470So";
  private static final String APPLICATION_NAME = "API code samples";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  public static YouTube getService() throws GeneralSecurityException, IOException {
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }
  public static YoutubePost getYoutubePost() throws GeneralSecurityException, IOException {
    try {
      YoutubePost newPost = new YoutubePost();
      YouTube youtubeService = getService();
      VideoListResponse response = youtubeService.videos()
                                       .list("snippet")
                                       .setKey(DEVELOPER_KEY)
                                       .setId("Ks-_Mh1QhMc")
                                       .execute();
      Video video = response.getItems().get(0);
      newPost.content = video.getSnippet().getDescription();
      newPost.title = video.getSnippet().getTitle();
      newPost.id = video.getId();
      return newPost;
    } catch (GeneralSecurityException | IOException e) {
      System.out.println("Error: Youtube api returning exception" + e);
    }
    return null;
  }
}