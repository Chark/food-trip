(function (ArticleCategoryManagement, $, undefined) {

    var categoriesUrl = '/administrate/api/articles/categories';
    var categoriesTable;

    /**
     * Single article category management page.
     */
    ArticleCategoryManagement.initCategory = function () {

        var form = $('form[name=articleCategoryForm]');
        var newAccount = form.data('article-category-id') <= 0;

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
                description: {
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
    ArticleCategoryManagement.initCategories = function () {
        /**
         * Initialize the article category data table.
         */
        categoriesTable = $('table.article-category-table').DataTable({
            ajax: {
                url: categoriesUrl,
                dataSrc: ''
            },
            columns: [
                {data: 'id'},
                {data: 'title'},
                {data: 'description'},
                {
                    data: 'creationDate',
                    render: function (date) {
                        return Utils.date(date);
                    }
                },
                {
                    data: 'id',
                    render: function (id) {
                        return Utils.urlButton('Edit', 'btn-success edit', '/administrate/articles/categories/' + id, id) + ' ' +
                            Utils.smallButton('Delete', 'btn-danger delete', id);
                    }
                }
            ]
        });

        /**
         * Delete specified user account.
         */
        categoriesTable.on('click', 'a.delete', function () {
            var id = $(this).data('id');
            bootbox.dialog({
                title: 'Delete article category',
                message: 'Are you sure you want to delete this article category?',
                buttons: {
                    yes: {
                        label: 'Delete',
                        className: 'btn-danger',
                        callback: function () {
                            $.delete(categoriesUrl + '/' + id, function () {
                                toastr.success('Deleted article category');
                                accountTable.ajax.reload();
                            }).fail(function () {
                                toastr.error('Failed to delete article category');
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
}(window.ArticleCategoryManagement = window.ArticleCategoryManagement || {}, jQuery));