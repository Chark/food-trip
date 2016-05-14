/**
 * Go one page back.
 */
function pageBack() {
    window.history.back();
}

/**
 * Various configurations.
 */
$(function () {

    // csrf token is present in the header on each page.
    var token = $('meta[name="_csrf"]').attr('content');
    var header = 'X-CSRF-TOKEN';

    // Append csrf token to all ajax calls.
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var url = window.location;

    // Highlight navigation bars based on current page.
    $('ul.nav a[href="' + url + '"]')
        .parent()
        .addClass('active');

    $('ul.nav a').filter(function () {
        return url.href.indexOf(this.href) == 0;
    }).parent().addClass('active');

    /**
     * Instead of using long ajax post methods, a simpler version to just post json.
     */
    jQuery['postJSON'] = function (url, data, callback) {

        // Shift arguments if data argument was omitted.
        if (jQuery.isFunction(data)) {
            callback = data;
            data = undefined;
        }

        // Data must be json, in-case it was not make it a json string.
        if (typeof data !== 'string' || data instanceof String) {
            data = JSON.stringify(data);
        }

        return jQuery.ajax({
            url: url,
            type: 'POST',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: data,
            success: callback
        });
    };

    /**
     * Register additional ajax functions.
     */
    $.each(['put', 'delete'], function (i, method) {
        $[method] = function (url, data, callback, type) {
            if ($.isFunction(data)) {
                type = type || callback;
                callback = data;
                data = undefined;
            }

            return $.ajax({
                url: url,
                type: method,
                dataType: type,
                data: data,
                success: callback
            });
        };
    });

    // Initialize toastr.
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "newestOnTop": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
});