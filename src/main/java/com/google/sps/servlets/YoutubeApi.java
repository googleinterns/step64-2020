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
import com.google.sps.servlets.YoutubeApiException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

public class YoutubeApi {
  private static final String DEVELOPER_KEY = "AIzaSyCSxCO3BeXFt1lYAou94rtlyQhinc470So";
  private static final String APPLICATION_NAME = "Capstone Project";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static YouTube YOUTUBE_SERVICE;

  private static YouTube getService() throws GeneralSecurityException, IOException {
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  public static List<YoutubePost> getYoutubePost() throws YoutubeApiException {
    List<YoutubePost> list = new ArrayList();
    String id = "Ks-_Mh1QhMc";
    try {
      if (YOUTUBE_SERVICE == null) {
        YOUTUBE_SERVICE = getService();
      }
      VideoListResponse response =
          YOUTUBE_SERVICE.videos().list("snippet").setKey(DEVELOPER_KEY).setId(id).execute();
      Video video = response.getItems().get(0);
      YoutubePost newPost = new YoutubePost(
          video.getSnippet().getTitle(), video.getSnippet().getDescription(), video.getId());
      list.add(newPost);
      return list;
    } catch (GeneralSecurityException | IOException | YoutubeApiException e) {
      System.out.println("Error: Youtube api returning exception" + e);
      if (YOUTUBE_SERVICE == null) {
        throw new YoutubeApiException("Youtube Api could not get service", e);
      }
    }
    return list;
  }
}
