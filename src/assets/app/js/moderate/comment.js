(function (CommentModerate, $, undefined) {

    var commentUrl = '/moderate/api/comments';
    var commentTable;
    var infoModal;

    /**
     * Initialize audit page.
     */
    CommentModerate.initCommentModerate = function () {


        /**
         * Initialize thread table.
         */

        commentTable = $('table.comment-table').DataTable({
            ajax: {
                url: commentUrl,
                dataSrc: ''
            },
            order: [
                [1, 'desc']
            ],
            columns: [
                {data: 'id'},
                {
                    data: 'account.prettyUsername',
                    render: function (username) {
                        return Utils.emptyLabel(username);
                    }
                },
                {
                    data: 'creationDate',
                    render: function (e) {
                        return Utils.date(e);
                    }
                },
                {
                    data: 'rating'
                },
                {
                    data: 'text'
                },
                {
                    data: 'hidden',
                    render: function (e) {
                        return Utils.yesNoLabel(e);
                    }
                },
                {
                    data: 'id',
                    render: function(id){
                        return Utils.urlButton('Edit', 'btn-success edit', '/moderate/comments/edit/' + id, id) + ' ' +
                            Utils.smallButton('Delete', 'btn-danger delete', id);
                    }
                }
            ]
        });

        commentTable.on('click', 'a.delete', function () {
            var id = $(this).data('id');
            bootbox.dialog({
                title: 'Delete comment',
                message: 'Are you sure you want to delete this comment?',
                buttons: {
                    yes: {
                        label: 'Delete',
                        className: 'btn-danger',
                        callback: function () {
                            $.delete(commentUrl + '/' + id, function () {
                                toastr.success('Comment delete');
                                commentTable.ajax.reload();
                            }).fail(function () {
                                toastr.error('Failed to delete comment');
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


}(window.CommentModerate = window.CommentModerate || {}, jQuery));
