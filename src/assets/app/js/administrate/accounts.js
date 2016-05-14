(function (AccountManagement, $, undefined) {
    AccountManagement.init = function () {
        $.get('/administrate/api/accounts', function (accounts) {
            console.log(accounts);
        });
    };
}(window.AccountManagement = window.AccountManagement || {}, jQuery));