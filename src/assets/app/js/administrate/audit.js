(function (Audit, $, undefined) {

    var auditUrl = '/administrate/api/audit';
    var auditTable;
    var infoModal;

    /**
     * Initialize audit page.
     */
    Audit.initAudit = function () {

        // Row colors based on message colors.
        var rowColors = {
            RED: 'danger',
            GREEN: 'success',
            BLUE: '',
            YELLOW: 'warning'
        };

        // Find the information modal.
        infoModal = $('.info-modal');

        /**
         * Initialize audit table.
         */
        auditTable = $('table.audit-table').DataTable({
            ajax: {
                url: auditUrl,
                dataSrc: ''
            },
            order: [
                [1, 'desc']
            ],
            rowCallback: function (row, data, index) {
                $(row).addClass(rowColors[data.color]);
            },
            columns: [
                {data: 'id'},
                {
                    data: 'creationDate',
                    render: function (date) {
                        return Utils.date(date);
                    }
                },
                {
                    data: 'username',
                    render: function (username) {
                        return Utils.emptyLabel(username);
                    }
                },
                {data: 'message'},
                {
                    data: 'id',
                    render: function (id) {
                        return Utils.smallButton('Details', 'details btn-success', id);
                    }
                }
            ]
        });

        /**
         * Audit view details button.
         */
        auditTable.on('click', 'a.details', function () {
            var id = $(this).data('id');

            $.get(auditUrl + '/' + id, function (auditMessage) {
                infoModal.find('.audit-id').text(auditMessage.id);
                infoModal.find('.audit-message').text(auditMessage.message);
                infoModal.find('.audit-creation-date').text(Utils.date(auditMessage.creationDate));

                // Hide or show account details if account is not null.
                var account = auditMessage.account;

                if (account) {
                    infoModal.find('.audit-user-id').text(account.id);
                    infoModal.find('.audit-username').html(
                        '<a href="/administrate/accounts/' + account.id + '">' + account.username + '</a>');

                    infoModal.find('.audit-email').text(account.email);
                    infoModal.find('.account-details').show();
                } else {
                    infoModal.find('.account-details').hide();
                }
                infoModal.modal();
            })
        });
    };

}(window.Audit = window.Audit || {}, jQuery));