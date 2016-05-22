(function (ThreadCategoryModerate, $, undefined) {

    var threadUrl = '/moderate/api/threads/categories';
    var threadCategoryTable;
    var infoModal;

    /**
     * Initialize audit page.
     */
    ThreadCategoryModerate.initThreadCategoryModerate = function () {


        /**
         * Initialize thread table.
         */

        threadCategoryTable = $('table.thread-categories-table').DataTable({
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
                    data: 'name'
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
                    data: 'editDate',
                    render: function (e) {
                        if(e == null){
                            return "Not edited";
                        }
                        return Utils.date(e);
                    }
                },
                {
                    data: 'id',
                    render: function(id){
                        return Utils.urlButton('Edit', 'btn-success edit', '/moderate/threads/categories/edit/' + id, id) + ' ' +
                            Utils.smallButton('Delete', 'btn-danger delete', id);
                    }
                }
            ]
        });

        threadCategoryTable.on('click', 'a.delete', function () {
            var id = $(this).data('id');
            bootbox.dialog({
                title: 'Delete thread category',
                message: 'Are you sure you want to delete this thread category?',
                buttons: {
                    yes: {
                        label: 'Delete',
                        className: 'btn-danger',
                        callback: function () {
                            $.delete(threadUrl + '/' + id, function () {
                                toastr.success('Deleted thread category');
                                threadCategoryTable.ajax.reload();
                            }).fail(function () {
                                toastr.error('Failed to delete thread category');
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



}(window.ThreadCategoryModerate = window.ThreadCategoryModerate || {}, jQuery));
