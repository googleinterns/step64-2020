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
      `/videos-sentiment?currentPage={currentPage}&postPerPage={postPerPage}`;
  fetch(url).then((response) => response.json()).then((threadInfoList) => {
    const threadList = document.getElementById('thread-container');
    threadList.innerHTML = '';
    numberOfPages = threadInfoList.numOfPages;
    createPageOptions();
    update();
    threadList.appendChild(loadList(threadInfoList));
  });
}

function loadList(list) {
  const div = document.createElement('div');
  div.id = 'Dividor';
  for (let i = 0; i < list.sentiment.length; i++) {
    const description = createDescription(list, i);
    const button = createTitleButton(list, i);
    div.appendChild(button);
    div.appendChild(description);
  }
  return div;
}

function createDescription(list, index) {
  const threadDescription = document.createElement('ul');
  const liDescription = document.createElement('li');
  liDescription.innerText = 'Description';
  liDescription.className = 'description-li';
  threadDescription.appendChild(liDescription);
  threadDescription.appendChild(
      createLiElement('Sentiment Value: ' + list.sentiment[index]));
  threadDescription.appendChild(createLiElement('Likes: ' + list.likes[index]));
  threadDescription.appendChild(linkListElement(list.url[index]));
  threadDescription.className = 'description';
  threadDescription.id = 'ul' + index;
  return threadDescription;
}

function createTitleButton(list, index) {
  const titleButton = document.createElement('button');
  titleButton.innerText = list.title[index];
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
