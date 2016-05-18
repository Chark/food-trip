
(function (ThreadModerate, $, undefined) {

    var threadUrl = '/moderate/api/threads';
    var threadTable;
    var infoModal;

    /**
     * Initialize audit page.
     */
    ThreadModerate.initThreadModerate = function () {


        /**
         * Initialize thread table.
         */

        threadTable = $('table.thread-table').DataTable({
            ajax: {
                url: threadUrl,
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
                    data: 'viewCount'
                },
                {
                    data: 'creationDate',
                    render: function (e) {
                        return Utils.date(e);
                    }
                },
                {
                    data: 'account.prettyUsername',
                    render: function (username) {
                        return Utils.emptyLabel(username);
                    }
                },
                {
                    data: 'editDate',
                    render: function (e) {
                        return Utils.date(e);
                    }
                },
                {
                    data: 'id',
                    render: function(id){
                        return Utils.urlButton('Edit', 'btn-success edit', '/moderate/threads/edit/' + id, id) + ' ' +
                            Utils.smallButton('Delete', 'btn-danger delete', id);
                    }
                }
            ]
        });

        threadTable.on('click', 'a.delete', function () {
            var id = $(this).data('id');
            bootbox.dialog({
                title: 'Delete thread',
                message: 'Are you sure you want to delete this thread?',
                buttons: {
                    yes: {
                        label: 'Delete',
                        className: 'btn-danger',
                        callback: function () {
                            $.delete(threadUrl + '/' + id, function () {
                                toastr.success('Deleted thread');
                                threadTable.ajax.reload();
                            }).fail(function () {
                                toastr.error('Failed to delete thread');
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



}(window.ThreadModerate = window.ThreadModerate || {}, jQuery));
