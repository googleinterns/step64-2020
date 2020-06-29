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

import static com.google.common.truth.Truth.assertThat;
import com.google.cloud.language.v1.PartOfSpeech.Tag;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AnalyzeTest {

  private ByteArrayOutputStream bout;
  private PrintStream out;
  private Analyze analyze;

  @Before
  public void setUp() {
   analyze = new Analyze();
  }

  @Test
  public void analyzeCategoriesInTextReturnsExpectedResult() throws Exception {
    analyze.classifyText(
        "Android is a mobile operating system developed by Google, "
            + "based on the Linux kernel and designed primarily for touchscreen "
            + "mobile devices such as smartphones and tablets.");
    String got = bout.toString();
    assertThat(got).contains("Computers & Electronics");
  }

  @Test
  public void analyzeEntities_withEntities_returnsLarryPage() throws Exception {
    analyze.analyzeEntitiesText(
        "Larry Page, Google's co-founder, once described the 'perfect search engine' as"
            + " something that 'understands exactly what you mean and gives you back exactly what"
            + " you want.' Since he spoke those words Google has grown to offer products beyond"
            + " search, but the spirit of what he said remains.");
    String got = bout.toString();
    assertThat(got).contains("Larry Page");
  }

  @Test
  public void analyzeSentimentText_returnPositive() throws Exception {
    Sentiment sentiment =
        analyze.analyzeSentimentText(
            "Tom Cruise is one of the finest actors in hollywood and a great star!");
    assertThat(sentiment.getMagnitude()).isGreaterThan(0.0F);
    assertThat(sentiment.getScore()).isGreaterThan(0.0F);
  }

  @Test
  public void analyzeSentimentText_returnNegative() throws Exception {
    Sentiment sentiment =
        analyze.analyzeSentimentText("That was the worst performance I've seen in a while.");
    assertThat(sentiment.getMagnitude()).isGreaterThan(0.0F);
    assertThat(sentiment.getScore()).isLessThan(0.0F);
  }

@Test
  public void analyzeSentimentScore_returnNegative() throws Exception {
    float sentiment =
        analyze.getSentimentScore("That was the worst performance I've seen in a while.");
    assertThat(sentiment).isLessThan(0.0F);
  }

  @Test
  public void analyzeEntitySentimentTextReturnsExpectedResult() throws Exception {
    analyze.entitySentimentText(
        "Oranges, grapes, and apples can be "
            + "found in the cafeterias located in Mountain View, Seattle, and London.");
    String got = bout.toString();
    assertThat(got).contains("Seattle");
  }
 }
