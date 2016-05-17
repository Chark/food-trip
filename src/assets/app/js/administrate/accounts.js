(function (AccountManagement, $, undefined) {

    var accountsUrl = '/administrate/api/accounts';
    var accountTable;

    /**
     * Single account management page.
     */
    AccountManagement.initAccount = function () {

        /**
         * Initialize authority select.
         */
        $('select[name=authorities]').select2({
            placeholder: 'Select account authorities',
            theme: 'bootstrap'
        });

        var form = $('form[name=accountForm]');
        var newAccount = form.data('account-id') <= 0;

        /**
         * Form validation.
         */
        form.validate({
            errorElement: 'small',
            rules: {
                username: {
                    minlength: 4,
                    maxlength: 64,
                    required: true
                },
                email: {
                    required: true
                },
                password: {
                    required: newAccount,
                    minlength: 4,
                    maxlength: 64
                },
                repeatPassword: {
                    equalTo: 'input[name=password]'
                },
                bio: {
                    maxlength: 1024
                },
                name: {
                    maxlength: 64
                },
                firstName: {
                    maxlength: 64
                },
                phone: {
                    maxlength: 64
                },
                website: {
                    maxlength: 64
                }
            },
            highlight: function (element) {
                $(element).closest('.form-group').addClass('has-error');
            },
            success: function (element) {
                element.closest('.form-group').removeClass('has-error');
            }
        });

    };

    /**
     * All accounts management page.
     */
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
                    render: function (id, type, full) {
                        var locked = !full.accountNonLocked;
                        return Utils.urlButton('Edit', 'btn-success edit', '/administrate/accounts/' + id, id) + ' ' +
                            Utils.smallButton(
                                locked ? 'Unlock' : 'Lock',
                                (locked ? 'btn-success' : 'btn-danger') + ' lock',
                                id,
                                'data-locked-to="' + !locked + '"');
                    }
                }
            ]
        });

        /**
         * Delete specified user account.
         */
        accountTable.on('click', 'a.lock', function () {

            // What state to set the locked to.
            var locked = $(this).data('locked-to');
            var id = $(this).data('id');

            bootbox.dialog({
                title: (locked ? 'Lock' : 'Unlock') + ' account',
                message: 'Are you sure you want to ' + (locked ? 'lock' : 'unlock') + ' this account?',
                buttons: {
                    yes: {
                        label: locked ? 'Lock' : 'Unlock',
                        className: 'btn-danger',
                        callback: function () {
                            $.post(accountsUrl + '/' + id + '/locked?locked=' + locked, function () {
                                toastr.success('Changed locked state');
                                accountTable.ajax.reload(null, false);
                            }).fail(function () {
                                toastr.error('Failed to change the locked state for user account');
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