package com.google.sps.servlets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.CommentThreads;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.sps.servlets.YoutubeApiException;
import java.io.IOException;
import java.lang.System;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**The Youtube Api handles getting the request from Youtube by
getting the service then populates the YoutubePost class with
the video responses. */
public class YoutubeApi {
  private static final String DEVELOPER_KEY = "AIzaSyCSxCO3BeXFt1lYAou94rtlyQhinc470So";
  private static final String APPLICATION_NAME = "Capstone Project";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final Object SERVICE_LOCK = new Object();
  private static YouTube youtubeService;
  private static String timeStamp;

  private static YouTube getService() throws GeneralSecurityException, IOException {
    if (youtubeService == null) {
      synchronized (SERVICE_LOCK) {
        if (youtubeService == null) {
          final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
          youtubeService = new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                               .setApplicationName(APPLICATION_NAME)
                               .build();
        }
      }
    }
    return youtubeService;
  }

  public static List<YoutubePost> getYoutubePost() throws YoutubeApiException {
    List<YoutubePost> list = new ArrayList();
    List<CommentData> commentList = new ArrayList();
    String id = "Ks-_Mh1QhMc";
    try {
      VideoListResponse response =
          getService().videos().list("snippet").setKey(DEVELOPER_KEY).setId(id).execute();
      Video video = response.getItems().get(0);
      /**Retrieving comments before creating new post */

      YouTube.CommentThreads.List request = getService()
                                                .commentThreads()
                                                .list("snippet")
                                                .setVideoId(video.getId())
                                                .setTextFormat("plainText");
      CommentThreadListResponse commentsResponse =
          request.setKey(DEVELOPER_KEY).setMaxResults(15L).execute();
      for (CommentThread commentThread : commentsResponse.getItems()) {
        CommentSnippet snippet = commentThread.getSnippet().getTopLevelComment().getSnippet();
        commentList.add(new CommentData(snippet.getTextDisplay(), snippet.getLikeCount()));
      }
      /**Constructor model (Title, Description, Video id, Video comments, video comment likes, Video
       * publish time stamp, Video likes) */
      YoutubePost newPost =
          new YoutubePost(video.getSnippet().getTitle(), video.getSnippet().getDescription(),
              video.getId(), video.getSnippet().getPublishedAt(), commentList);
      list.add(newPost);
      System.out.println(newPost);
      return list;
    } catch (GeneralSecurityException | IOException e) {
      System.out.println("Error: Youtube api returning exception" + e);
      throw new YoutubeApiException("Youtube Api could not get service", e);
    }
  }
}
