function fetchSpeakerImage(id) {
    const url = `/speakers/${id}`;
    fetch(url)
        .then(response => response.text())
        .then(html => {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        const speakerImgDiv = doc.querySelector('.speaker-img');

        // append caption:
        const caption = document.createElement('span');
        caption.classList.add('caption');
        caption.textContent = 'visit speaker profile';
        speakerImgDiv.appendChild(caption);

        // remove selector:
        const selectElement = speakerImgDiv.querySelector('select');
        if (selectElement) {
            selectElement.remove();
        }

        // replace div class
        const wrapper = document.createElement('div');
        wrapper.classList.add('small-speaker-img');
        wrapper.innerHTML = speakerImgDiv.innerHTML;

        document.querySelector('a.speaker-image').innerHTML = '';
        document.querySelector('a.speaker-image').appendChild(wrapper);
    })
        .catch(error => {
        console.error(error);
    });
}