(function (ArticleManagement, $, undefined) {

    var articlesUrl = '/administrate/api/articles';
    var articlesTable;

    /**
     * Single article category management page.
     */
    ArticleManagement.initArticle = function () {

        var form = $('form[name=articleForm]');
        var newAccount = form.data('article-id') <= 0;

        /**
         * Form validation.
         */
        form.validate({
            errorElement: 'small',
            rules: {
                title: {
                    minlength: 4,
                    maxlength: 64,
                    required: true
                },
                shortDescription: {
                    maxlength: 1024
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
     * All article categories management page.
     */
    ArticleManagement.initArticles = function () {
        /**
         * Initialize the article category data table.
         */
        articlesTable = $('table.article-table').DataTable({
            ajax: {
                url: articlesUrl,
                dataSrc: ''
            },
            columns: [
                {data: 'id'},
                {data: 'title'},
                {data: 'shortDescription'},
                {
                    data: 'creationDate',
                    render: function (date) {
                        return Utils.date(date);
                    }
                },
                {
                    data: 'id',
                    render: function (id) {
                        return Utils.urlButton('Edit', 'btn-success edit', '/administrate/articles/' + id, id) + ' ' +
                            Utils.smallButton('Delete', 'btn-danger delete', id);
                    }
                }
            ]
        });

        /**
         * Delete specified user account.
         */
        articlesTable.on('click', 'a.delete', function () {
            var id = $(this).data('id');
            bootbox.dialog({
                title: 'Delete article',
                message: 'Are you sure you want to delete this article?',
                buttons: {
                    yes: {
                        label: 'Delete',
                        className: 'btn-danger',
                        callback: function () {
                            $.delete(articlesUrl + '/' + id, function () {
                                toastr.success('Deleted article');
                                articlesTable.ajax.reload();
                            }).fail(function () {
                                toastr.error('Failed to delete article');
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
}(window.ArticleManagement = window.ArticleManagement || {}, jQuery));