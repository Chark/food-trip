/**
 * Go one page back.
 */
function pageBack() {
    window.history.back();
}

$(function () {

    // csrf token is present in the header on each page.
    var token = $('meta[name="_csrf"]').attr('content');
    var header = 'X-CSRF-TOKEN';

    // Append csrf token to all ajax calls.
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var url = window.location;

    // Highlight navbars based on current page.
    $('ul.nav a[href="'+ url +'"]')
        .parent()
        .addClass('active');

    $('ul.nav a').filter(function() {
        return this.href == url;
    }).parent().addClass('active');
});