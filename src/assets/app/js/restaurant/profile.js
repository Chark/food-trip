(function (RestaurantProfile, $, undefined) {

    var invitationsUrl = '/restaurant/api/invitations';
    var invitationsTable;
    var inviteModal;

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
         * Delete invitation.
         */
        invitationsTable.on('click', 'a.delete', function () {
            var id = $(this).data('id');
            $.delete(invitationsUrl + '/' + id, function () {
                toastr.success('Deleted invitation');
                invitationsTable.ajax.reload();
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
                invitationsTable.ajax.reload();
            }).fail(function () {
                toastr.error('Could not invite user to join this restaurant, maybe you\'re inviting yourself?');
            });
            e.preventDefault();
        });
    };

}(window.RestaurantProfile = window.RestaurantProfile || {}, jQuery));