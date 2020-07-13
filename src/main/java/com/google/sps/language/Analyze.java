/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.sps;
import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Uses the Natural Language API to perform entity, sentiment and syntax
 * analysis.
 */
public class Analyze {
  /** Identifies entities in a given string. */
  public void analyzeEntitiesText(String text) throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
      analyzeEntities(doc, language);
    }
  }

  /** Identifies entities in the contents of the object at the given GCS. */
  public void analyzeEntitiesFile(String gcsUri) throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      // set the GCS Content URI path to the file to be analyzed
      Document doc =
          Document.newBuilder().setGcsContentUri(gcsUri).setType(Type.PLAIN_TEXT).build();
      analyzeEntities(doc, language);
    }
  }

  private void analyzeEntities(Document doc, LanguageServiceClient language) throws IOException {
    AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
                                         .setDocument(doc)
                                         .setEncodingType(EncodingType.UTF16)
                                         .build();

    AnalyzeEntitiesResponse response = language.analyzeEntities(request);

    // Print the response
    for (Entity entity : response.getEntitiesList()) {
      System.out.printf("Entity: %s\n", entity.getName());
      System.out.printf("Salience: %.3f\n", entity.getSalience());
      System.out.println("Metadata: ");
      for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
        System.out.printf("%s : %s", entry.getKey(), entry.getValue());
      }
      for (EntityMention mention : entity.getMentionsList()) {
        System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
        System.out.printf("Content: %s\n", mention.getText().getContent());
        System.out.printf("Type: %s\n\n", mention.getType());
      }
    }
  }

  /** Identifies the sentiment in a given string. */
  public Sentiment analyzeSentimentText(String text) throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
      return analyzeSentiment(doc, language);
    }
  }

  /** Gets Sentiment from the contents of the GCS hosted file. */
  public Sentiment analyzeSentimentFile(String gcsUri) throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc =
          Document.newBuilder().setGcsContentUri(gcsUri).setType(Type.PLAIN_TEXT).build();
      return analyzeSentiment(doc, language);
    }
  }

  private Sentiment analyzeSentiment(Document doc, LanguageServiceClient language)
      throws IOException {
    AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
    Sentiment sentiment = response.getDocumentSentiment();
    if (sentiment == null) {
      System.out.println("No sentiment found");
    } else {
      System.out.printf("Sentiment magnitude : %.3f\n", sentiment.getMagnitude());
      System.out.printf("Sentiment score : %.3f\n", sentiment.getScore());
    }
    return sentiment;
  }

  /** Analyzes syntax in a given string. */
  public List<Token> analyzeSyntaxText(String text) throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
      return analyzeSyntax(doc, language);
    }
  }

  /** Gets the syntax of the GCS hosted file. */
  public List<Token> analyzeSyntaxFile(String gcsUri) throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc =
          Document.newBuilder().setGcsContentUri(gcsUri).setType(Type.PLAIN_TEXT).build();
      return analyzeSyntax(doc, language);
    }
  }

  private List<Token> analyzeSyntax(Document doc, LanguageServiceClient language)
      throws IOException {
    AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                                       .setDocument(doc)
                                       .setEncodingType(EncodingType.UTF16)
                                       .build();
    // analyze the syntax in the given text
    AnalyzeSyntaxResponse response = language.analyzeSyntax(request);
    // print the response
    for (Token token : response.getTokensList()) {
      System.out.printf("\tText: %s\n", token.getText().getContent());
      System.out.printf("\tBeginOffset: %d\n", token.getText().getBeginOffset());
      System.out.printf("Lemma: %s\n", token.getLemma());
      System.out.printf("PartOfSpeechTag: %s\n", token.getPartOfSpeech().getTag());
      System.out.printf("\tAspect: %s\n", token.getPartOfSpeech().getAspect());
      System.out.printf("\tCase: %s\n", token.getPartOfSpeech().getCase());
      System.out.printf("\tForm: %s\n", token.getPartOfSpeech().getForm());
      System.out.printf("\tGender: %s\n", token.getPartOfSpeech().getGender());
      System.out.printf("\tMood: %s\n", token.getPartOfSpeech().getMood());
      System.out.printf("\tNumber: %s\n", token.getPartOfSpeech().getNumber());
      System.out.printf("\tPerson: %s\n", token.getPartOfSpeech().getPerson());
      System.out.printf("\tProper: %s\n", token.getPartOfSpeech().getProper());
      System.out.printf("\tReciprocity: %s\n", token.getPartOfSpeech().getReciprocity());
      System.out.printf("\tTense: %s\n", token.getPartOfSpeech().getTense());
      System.out.printf("\tVoice: %s\n", token.getPartOfSpeech().getVoice());
      System.out.println("DependencyEdge");
      System.out.printf("\tHeadTokenIndex: %d\n", token.getDependencyEdge().getHeadTokenIndex());
      System.out.printf("\tLabel: %s\n\n", token.getDependencyEdge().getLabel());
    }

    return response.getTokensList();
  }

  /** Detects categories in a given string using the Language API. */
  public void classifyText(String text) throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      // set content to the text string
      Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
      classify(doc, language);
    }
  }

  /** Detects categories in a GCS hosted file using the Language API. */
  public void classifyFile(String gcsUri) throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      // set the GCS content URI path
      Document doc =
          Document.newBuilder().setGcsContentUri(gcsUri).setType(Type.PLAIN_TEXT).build();
      classify(doc, language);
    }
  }

  private void classify(Document doc, LanguageServiceClient language) throws IOException {
    ClassifyTextRequest request = ClassifyTextRequest.newBuilder().setDocument(doc).build();
    // detect categories in the given file
    ClassifyTextResponse response = language.classifyText(request);

    for (ClassificationCategory category : response.getCategoriesList()) {
      System.out.printf(
          "Category name : %s, Confidence : %.3f\n", category.getName(), category.getConfidence());
    }
  }

  /** Detects the entity sentiments in the given string using the Language API. */
  public void entitySentimentText(String text) throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
      entitySentiment(doc, language);
    }
  }

  /** Identifies the entity sentiments in the the GCS hosted file using the Language API. */
  public void entitySentimentFile(String gcsUri) throws IOException {
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc =
          Document.newBuilder().setGcsContentUri(gcsUri).setType(Type.PLAIN_TEXT).build();
      entitySentiment(doc, language);
    }
  }

  private void entitySentiment(Document doc, LanguageServiceClient language) throws IOException {
    AnalyzeEntitySentimentRequest request = AnalyzeEntitySentimentRequest.newBuilder()
                                                .setDocument(doc)
                                                .setEncodingType(EncodingType.UTF16)
                                                .build();
    // Detect entity sentiments in the given file
    AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
    // Print the response
    for (Entity entity : response.getEntitiesList()) {
      System.out.printf("Entity: %s\n", entity.getName());
      System.out.printf("Salience: %.3f\n", entity.getSalience());
      System.out.printf("Sentiment : %s\n", entity.getSentiment());
      for (EntityMention mention : entity.getMentionsList()) {
        System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
        System.out.printf("Content: %s\n", mention.getText().getContent());
        System.out.printf("Magnitude: %.3f\n", mention.getSentiment().getMagnitude());
        System.out.printf("Sentiment score : %.3f\n", mention.getSentiment().getScore());
        System.out.printf("Type: %s\n\n", mention.getType());
      }
    }
  }
}
