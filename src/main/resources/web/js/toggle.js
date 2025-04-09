function toggle(button, Json, label) {
    const buttons = document.querySelectorAll('.tertiary');
    buttons.forEach(queryButton => {
        if (queryButton !== button) {
            queryButton.setAttribute('data-active', "false");        // toggle other active attributes off
            queryButton.textContent = 'Show ' + queryButton.getAttribute('data-label'); // toggle other button lables
            switch (queryButton.getAttribute('data-label')) {
                case "Sentiment":
                    document.querySelector(".full-sentiment").style.display = "none";
                    break;
                case "Entities":
                    document.querySelector(".entity-legend").style.display = "none";
                    break;
            }
        }
    });

    if (button.getAttribute('data-active') === "false") {
        button.setAttribute('data-active', "true");
        button.textContent = 'Hide ' + label;

        let paragraphs = document.querySelectorAll('.speech-container p');
        paragraphs.forEach((paragraph) => {
            let pClass = paragraph.getAttribute('class');
            if (pClass === "speaker_introduction" || pClass === "comment") {
                return; // Skip these paragraphs
            }
            let original = paragraph.getAttribute('data-original').trim();

            switch (label) {
                case "Sentiment":
                    document.querySelector(".full-sentiment").style.display = "block";
                    break;
                case "Entities":
                    document.querySelector(".entity-legend").style.display = "block";
                    break;
            }
            // span wrap analysis
            spanWrap(Json,label.toLowerCase());

        });
    } else {
        switch (label) {
            case "Sentiment":
                document.querySelector(".full-sentiment").style.display = "none";
                break;
            case "Entities":
                document.querySelector(".entity-legend").style.display = "none";
                break;
        }
        // revert to original text
        let paragraphs = document.querySelectorAll('.speech-container p');
        paragraphs.forEach((paragraph) => {
            let original = paragraph.getAttribute('data-original').trim();
            paragraph.innerHTML = original;
        });

        button.setAttribute('data-active', "false");
        button.textContent = 'Show '+ label;
    }
}
