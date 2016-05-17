(function (Audit, $, undefined) {

    var auditUrl = '/administrate/api/audit';
    var auditTable;
    var userChart;
    var infoModal;

    /**
     * Initialize audit page.
     */
    Audit.initAudit = function (accountArray) {

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

        /**
         * Initialize user chart.
         */


        userChart = new Chart($('.user-chart'), {
            type: 'line',
            data: {
                labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
                datasets: [
                    {
                        label: 'New accounts per day of the week',
                        fill: false,
                        lineTension: 0,
                        backgroundColor: "rgba(75,192,192,0.4)",
                        borderColor: "rgba(75,192,192,1)",
                        pointBorderColor: "rgba(75,192,192,1)",
                        pointHoverBackgroundColor: "rgba(75,192,192,1)",
                        pointHoverBorderColor: "rgba(220,220,220,1)",
                        pointHitRadius: 10,
                        data: accountArray
                    }
                ]
            }
        });
    };

}(window.Audit = window.Audit || {}, jQuery));