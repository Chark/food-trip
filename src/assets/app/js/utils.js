/**
 * All utilities used throughout the application javascript files.
 */
(function (Utils, $, undefined) {

    /**
     * Create a 'yes/no' label for a value.
     */
    Utils.yesNoLabel = function (value) {
        return Utils.label(value ? 'Yes' : 'No', value ? 'label-success' : 'label-danger');
    };

    /**
     * Create a small label using the 'span' tag.
     */
    Utils.label = function (text, clazz) {
        return '<span class="label ' + clazz + '">' + text + '</span>';
    };

    /**
     * Create a small button using the 'a' tag.
     */
    Utils.smallButton = function (text, clazz, id) {
        return Utils.urlButton(text, clazz, 'javascript:;', id);
    };

    /**
     * Create a url button.
     */
    Utils.urlButton = function (text, clazz, url, id) {
        return '<a href="' + url + '" class="btn btn-xs ' + clazz + '" data-id="' + id + '">' + text + '</a>';
    };
}(window.Utils = window.Utils || {}, jQuery));