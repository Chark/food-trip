(function (Newsletter, $, undefined) {

    var newsletterurl = '/moderate/api/newsletters';
    var newsletterTable;
    var infoModal;



    /**
     * Initialize audit page.
     */
    Newsletter.initNewsletter = function () {


        /**
         * Initialize thread table.
         */

        newsletterTable = $('table.newsletter-table').DataTable({
            ajax: {
                url: newsletterurl,
                dataSrc: ''
            },
            order: [
                [1, 'desc']
            ],
            columns: [
                {data: 'id'},
                {
                    data: 'title'
                },
                {
                    data: 'description'
                },
                {
                    data: 'creationDate',
                    render: function (e) {
                        return Utils.date(e);
                    }
                },
                {
                    data: 'expirationDate'
                },
                {
                    data: 'account.prettyUsername',
                    render: function (username) {
                        return Utils.emptyLabel(username);
                    }
                },
                {
                    data: 'viewCount'
                },
                {
                    data: 'published',
                    render: function (e) {
                        return Utils.yesNoLabel(e);
                    }
                },
                {
                    data: 'edited',
                    render: function (e) {
                        return Utils.yesNoLabel(e);
                    }
                },
                {
                    data: 'id',
                    render: function (id) {
                        return Utils.urlButton('Edit', 'btn-success edit', '/moderate/newsletters/edit/' + id, id) + ' ' +
                            Utils.smallButton('Delete', 'btn-danger delete', id);
                    }
                }
            ]
        });

        newsletterTable.on('click', 'a.delete', function () {
            var id = $(this).data('id');
            bootbox.dialog({
                title: 'Delete newsletter',
                message: 'Are you sure you want to delete this newsletter?',
                buttons: {
                    yes: {
                        label: 'Delete',
                        className: 'btn-danger',
                        callback: function () {
                            $.delete(newsletterurl + '/' + id, function () {
                                toastr.success('Newsletter deleted');
                                newsletterTable.ajax.reload();
                            }).fail(function () {
                                toastr.error('Failed to delete newsletter');
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


}(window.Newsletter = window.Newsletter || {}, jQuery));
