function spanWrap(Json, label) {
//    label = String(label);
    let offset = 0;
    document.querySelectorAll('.speech-container p').forEach((paragraph,paragraphIndex) => {
        let pClass = paragraph.getAttribute('class');
        if (pClass === "speaker_introduction" || pClass === "comment"){
            // speaker introductions and comments are not part of the nlp analysis
            return;
        }
        let original = paragraph.getAttribute('data-original').trim();
        let pBegin = offset;
        let pEnd = offset + original.length;
        let result = '';
        let datavals = "";
        let lastIndex = 0;
        Json.forEach((item, index) => {
            if (index===0 && label==="sentiment") {
                // case 0, full sentiment
                return;
            }
            if(index!=0){
                if (Json[index - 1].end === item.begin) {
                    // last sentences end overlaps with this sentences begin. creates offsets.
                    return;
                }
            }
            if (pBegin <= item.begin && pEnd >= item.end) {
                // case 1, item inside paragraph
                let highlighted = original.substring(item.begin - offset, item.end - offset);

                result += original.substring(lastIndex, item.begin - offset);
                result += generateSpan(item, label, highlighted);
                lastIndex = item.end - offset;
            }
            else if (pBegin <= item.begin && pEnd > item.begin){
                // case 2, item starts in paragraph
                let highlighted = original.substring(item.begin - offset);

                result += original.substring(lastIndex, item.begin - offset);
                result += generateSpan(item, label, highlighted);
                lastIndex = pEnd - offset;
            }
            else if (pBegin < item.end && pEnd >= item.end){
                // case 3, item ends in paragraph
                let highlighted = original.substring(pBegin-offset, item.end-offset);

                result += original.substring(lastIndex, item.end - offset);
                result += generateSpan(item, label, highlighted);
                lastIndex = item.end - offset;
            }
        });
        result += original.substring(lastIndex);
        paragraph.innerHTML = result;
        offset+=original.length;
        if (paragraphIndex>0) {
            // paragraph ends add to offset, except the first
            offset+=1;
        }
    });
}

function generateSpan(item, label, highlighted) {
    switch (label) {
        case "pos":
            return '<span class="'+label+'" data-'+label+'="'+item.PosValue + " " + item.coarseValue+'">' + highlighted + '</span>';
        case "entities":
            return '<span class="'+label +' '+ item.value.toLowerCase()+'" data-'+label+'="'+item.value+'">' + highlighted + '</span>';
        case "sentiment":
            let icon = sentimentIcon(item.sentiment);
            return highlighted+'<span class="'+label+'" data-'+label+'="'+item.sentiment+'"><sup>'+icon+'</sup></span>';
        default:
            return '';
    }
}
function sentimentIcon(sentiment) {
    switch (true) {
        case sentiment > 0:
            return '<i style="color:rgb('+255*(1-sentiment)+',255,'+255*(1-sentiment)+');" class="fa-regular fa-face-smile"></i>';
        case sentiment < 0:
            return '<i style="color:rgb(255,'+255*(1-Math.abs(sentiment))+','+255*(1-Math.abs(sentiment))+');" class="fa-regular fa-face-frown"></i>';
        case sentiment === 0:
            return '<i style="color:rgb(255,255,255);" class="fa-regular fa-face-meh"></i>';
        default:
            return "";
    }
}