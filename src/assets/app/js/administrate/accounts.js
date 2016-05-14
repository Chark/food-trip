(function (Accounts, $, undefined) {
    Accounts.init = function () {
        $.get('/administrate/api/accounts', function (accounts) {
            console.log(accounts);
        });
    };
}(window.Accounts = window.Accounts || {}, jQuery));