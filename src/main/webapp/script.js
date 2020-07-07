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

function addThreads() {  // eslint-disable-line
  const url = '/data';
  fetch(url).then((response) => response.json()).then((threadInfoList) => {
    const threadList = document.getElementById('thread-container');
    threadList.innerHTML = '';

    let i;
    for (i = 0; i < threadInfoList.length; i += 4) {
      // create description
      const ulid = 'ul' + i;
      const lidescription = document.createElement('LI');
      lidescription.innerText = 'Description';
      lidescription.className = 'descriptionli';
      const description = document.createElement('UL');
      description.appendChild(lidescription);
      description.appendChild(
          createListElement('sentiment:' + threadInfoList[i + 1]));
      description.appendChild(
          createListElement('upvotes: ' + threadInfoList[i + 2]));
      description.appendChild(linkListElement(threadInfoList[i + 3]));
      description.className = ('description');
      description.setAttribute('id', ulid);
      // create description
      const thread = createButtonElement(threadInfoList[i], i);
      threadList.appendChild(thread);
      threadList.appendChild(description);
    }
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

/** Creates a <button> element displaying text and  */
function createButtonElement(text, index) {
  const buttonid = 'button' + index;
  const buttonElement = document.createElement('button');
  buttonElement.innerText = text;
  buttonElement.className = 'thread';
  buttonElement.setAttribute('id', buttonid);
  return buttonElement;
}

/** Creates a <a> element that is appended to li element */
function linkListElement(url) {
  const aElement = document.createElement('a');
  const link = document.createElement('li');
  aElement.href = url;
  aElement.innerText = 'See the Thread on Reddit';
  link.appendChild(aElement);
  return link;
}
