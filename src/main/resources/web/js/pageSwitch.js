function setChangePageEvents(next, previous, pageInputField, form){
    next.addEventListener('click', function (e) {
        e.preventDefault();
        var pageInput = pageInputField;
        var currPage = parseInt(pageInput.value || '0');
        pageInput.value = currPage + 1;

        form.submit();
    });

    previous.addEventListener('click', function (e) {
        e.preventDefault();
        var pageInput = pageInputField;
        var currPage = parseInt(pageInput.value || '0');

        if (currPage - 1 < 0) return;

        pageInput.value = currPage - 1;

        form.submit();
    });
}