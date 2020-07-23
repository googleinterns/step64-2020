// Copyright 2019 Google LLC
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
let currentPage = 1;
const postPerPage = 10;  // eslint-disable-line
let numberOfPages = 1;

function addThreads() {  // eslint-disable-line
  const url =
      `/videos-sentiment?currentPage=${currentPage}&postPerPage=${postPerPage}`;
  fetch(url).then((response) => response.json()).then((threadInfoList) => {
    const threadList = document.getElementById('thread-container');
    threadList.innerHTML = '';
    numberOfPages = Math.max(1, Math.ceil(threadInfoList.length / postPerPage));
    createPageOptions();
    update();
    threadList.appendChild(loadList(threadInfoList));
  });
}

function loadList(list) {
  const div = document.createElement('div');
  div.id = 'Dividor';
  list.forEach((element) => {
    const description = createDescription(element);
    const button = createTitleButton(element);
    div.appendChild(button);
    div.appendChild(description);
  });
  return div;
}

function createDescription(video) {
  const threadDescription = document.createElement('ul');
  const liDescription = document.createElement('li');
  liDescription.innerText = 'Description';
  liDescription.className = 'description-li';
  threadDescription.appendChild(liDescription);
  threadDescription.appendChild(
      createLiElement('Sentiment Value: ' + video.sentiment));
  threadDescription.appendChild(createLiElement('Likes: ' + video.like));
  threadDescription.appendChild(linkListElement(video.url));
  threadDescription.className = 'description';
  return threadDescription;
}

function createTitleButton(video) {
  const titleButton = document.createElement('button');
  titleButton.innerText = video.title;
  titleButton.className = 'thread';
  return titleButton;
}


function createLiElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

function linkListElement(url) {
  const liElement = document.createElement('li');
  const aElement = document.createElement('a');
  aElement.href = url;
  aElement.innerText = 'See Youtube Video';
  liElement.appendChild(aElement);
  return liElement;
}

class AnalyzedVideo {  // eslint-disable-line
  constructor(title, sentiment, like, url) {
    this.title = title;
    this.sentiment = sentiment;
    this.like = like;
    this.url = url;
  }
}

/** Retrieves the previous page */
function previous() {  // eslint-disable-line
  currentPage--;
  addThreads();
}

/** Retrieves the next page */
function next() {  // eslint-disable-line
  currentPage++;
  addThreads();
}

/** Retrieves the specificied page number */
function numberPage(pageNumber) {  // eslint-disable-line
  currentPage = pageNumber;
  addThreads();
}

/** Dis/enables previous and next button */
function update() {
  document.getElementById('next').disabled =
      currentPage >= numberOfPages ? true : false;
  document.getElementById('previous').disabled =
      currentPage == 1 ? true : false;
}

/** Create a option for page selector */
function createPageOptions() {
  const select = document.getElementById('pageNumber');
  select.innerHTML = '';
  for (let i = 1; i <= numberOfPages; i++) {
    const pageOption = document.createElement('option');
    pageOption.appendChild(document.createTextNode(i));
    pageOption.value = i;
    select.appendChild(pageOption);
  }
  const amountOfPages = document.getElementById('amountOfPages');
  amountOfPages.innerHTML = '';
  amountOfPages.appendChild(document.createTextNode(' of ' + currentPage));
}
