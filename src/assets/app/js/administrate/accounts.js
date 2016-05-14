(function (AccountManagement, $, undefined) {

    var accountsUrl = '/administrate/api/accounts';
    var accountTable;

    AccountManagement.initAccount = function () {

    };

    AccountManagement.initAccounts = function () {

        /**
         * Initialize the account data table.
         */
        accountTable = $('table.account-table').DataTable({
            ajax: {
                url: accountsUrl,
                dataSrc: ''
            },
            order: [
                [3, 'desc']
            ],
            columns: [
                {data: 'id'},
                {data: 'prettyUsername'},
                {data: 'email'},
                {
                    data: 'registrationDate',
                    render: function (date) {
                        return Utils.date(date);
                    }
                },
                {
                    data: 'enabled',
                    render: function (enabled) {
                        return Utils.yesNoLabel(enabled);
                    }
                },
                {
                    data: 'id',
                    render: function (id) {
                        return Utils.urlButton('Edit', 'btn-success edit', '/administrate/accounts/' + id, id) + ' ' +
                            Utils.smallButton('Delete', 'btn-danger delete', id);
                    }
                }
            ]
        });

        /**
         * Delete specified user account.
         */
        accountTable.on('click', 'a.delete', function () {
            var id = $(this).data('id');
            bootbox.dialog({
                title: 'Delete account',
                message: 'Are you sure you want to delete this account?',
                buttons: {
                    yes: {
                        label: 'Delete',
                        className: 'btn-danger',
                        callback: function () {
                            $.delete(accountsUrl + '/' + id, function () {
                                toastr.success('Deleted user account');
                                accountTable.ajax.reload();
                            }).fail(function () {
                                toastr.error('Failed to delete user account');
                            });
                        }
                    },
                    no: {
                        label: 'Cancel',
                        className: 'btn-success'
                    }
                }
            });
        });
    };
}(window.AccountManagement = window.AccountManagement || {}, jQuery));