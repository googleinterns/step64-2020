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
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoStatistics;
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
  private static final String APPLICATION_NAME = "Capstone Project";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final Object SERVICE_LOCK = new Object();
  private static YouTube youtubeService;
  private static final String SNIPPET = "snippet";
  private static final Long MAX_VIDEO_RESULTS = 5L;
  private static final Long MAX_COMMENT_RESULTS = 3L;

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
    String id;
    String key = System.getenv("DEVELOPER_KEY");
    try {
      // Returns top posts relevant to GoogleFi and feeds those Ids into video response
      YouTube.Search.List searchRequest = getService()
                                              .search()
                                              .list(SNIPPET)
                                              .setType("video")
                                              .setMaxResults(MAX_VIDEO_RESULTS)
                                              .setOrder("relevance")
                                              .setQ("google fi");
      SearchListResponse searchResponse = searchRequest.setKey(key).execute();
      for (SearchResult searchResult : searchResponse.getItems()) {
        VideoListResponse response = getService()
                                         .videos()
                                         .list(SNIPPET)
                                         .setKey(key)
                                         .setId(searchResult.getId().getVideoId())
                                         .execute();
        if (response.getItems().size() == 0) {
          continue;
        }
        Video video = response.getItems().get(0);

        YouTube.CommentThreads.List request = getService()
                                                  .commentThreads()
                                                  .list(SNIPPET)
                                                  .setVideoId(video.getId())
                                                  .setTextFormat("plainText");
        CommentThreadListResponse commentsResponse =
            request.setKey(key).setOrder("relevance").setMaxResults(MAX_COMMENT_RESULTS).execute();
        List<CommentData> commentList = new ArrayList();
        for (CommentThread commentThread : commentsResponse.getItems()) {
          CommentSnippet snippet = commentThread.getSnippet().getTopLevelComment().getSnippet();
          commentList.add(new CommentData(snippet.getTextDisplay(), snippet.getLikeCount()));
        }

        YouTube.Videos.List likeList =
            getService().videos().list("statistics").setId(searchResult.getId().getVideoId());

        VideoListResponse likeListResponse = likeList.setKey(key).execute();
        BigInteger videoLikes = likeListResponse.getItems().get(0).getStatistics().getLikeCount();
        System.out.println("likes" + videoLikes);

        YoutubePost newPost =
            new YoutubePost(video.getSnippet().getTitle(), video.getSnippet().getDescription(),
                video.getId(), video.getSnippet().getPublishedAt(), commentList, videoLikes);
        list.add(newPost);
      }
      return list;
    } catch (GeneralSecurityException | IOException e) {
      System.out.println("Error: Youtube api returning exception" + e);
      throw new YoutubeApiException("Youtube Api could not get service", e);
    }
  }
}
