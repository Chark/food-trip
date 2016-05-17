(function (RestaurantProfile, $, undefined) {

    // Row colors based on message colors, should reuse?
    var rowColors = {
        RED: 'danger',
        GREEN: 'success',
        BLUE: '',
        YELLOW: 'warning'
    };

    var invitationsUrl = '/restaurant/api/invitations';
    var auditUrl = '/restaurant/api/audit';

    var invitationsTable;
    var auditTable;

    var inviteModal;

    /**
     * Reload all tables on the page.
     */
    function reloadTables () {

        // First arg is the callback, second one prevents paging reset.
        invitationsTable.ajax.reload(null, false);
        auditTable.ajax.reload(null, false);
    }

    /**
     * Initialize the restaurant profile page.
     */
    RestaurantProfile.init = function () {

        // Init accounts data table.
        $('.accounts-table').DataTable();

        // Invite new user modal.
        inviteModal = $('.invite-modal');

        /**
         * Initialize invitations table.
         */
        invitationsTable = $('table.invitations-table').DataTable({
            ajax: {
                url: invitationsUrl,
                dataSrc: ''
            },
            order: [
                [0, 'desc']
            ],
            columns: [
                {data: 'id'},
                {data: 'username'},
                {
                    data: 'id',
                    render: function (id) {
                        return Utils.smallButton('Delete', 'btn-danger delete', id);
                    }
                }
            ]
        });

        /**
         * Restaurant audit table.
         */
        auditTable = $('table.audit-table').DataTable({
            ajax: {
                url: auditUrl,
                dataSrc: ''
            },
            order: [
                [1, 'desc']
            ],
            rowCallback: function (row, data) {
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
                {data: 'action'},
                {data: 'message'},
                {data: 'username'}
            ]
        });

        /**
         * Delete invitation.
         */
        invitationsTable.on('click', 'a.delete', function () {
            var id = $(this).data('id');
            $.delete(invitationsUrl + '/' + id, function () {
                toastr.success('Deleted invitation');

                // Need to update the audit log as well as invitations table.
                reloadTables();
            }).fail(function () {
                toastr.error('Could not delete invitation');
            });
        });

        /**
         * Open up invitation modal.
         */
        $('.invite-account').click(function () {
            inviteModal.find('input').val('');
            inviteModal.modal();
        });

        /**
         * Submit new invitation.
         */
        $('form[name=inviteForm]').submit(function (e) {

            var username = $(this).serializeObject().username;
            $.post(invitationsUrl + '/invite?username=' + username, function () {
                toastr.success('Invited user to join this restaurant');
                inviteModal.modal('hide');

                // Reload both audit and invite table.
                reloadTables();
            }).fail(function () {
                toastr.error('Could not invite user to join this restaurant, maybe you\'re inviting yourself?');
            });
            e.preventDefault();
        });
    };

}(window.RestaurantProfile = window.RestaurantProfile || {}, jQuery));