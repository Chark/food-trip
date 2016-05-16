(function (ArticleCategoryManagement, $, undefined) {

    var categoriesUrl = '/administrate/api/articles/categories';
    var categoriesTable;

    /**
     * Single article category management page.
     */
    ArticleCategoryManagement.initCategory = function () {

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
                {data: 'id'},
                {data: 'id'},
                {data: 'id'},
                {
                    data: 'id',
                    render: function (id) {
                        return 'top kek: ' + id;
                    }
                }
            ]
        });
        console.log(categoriesTable);
    };
}(window.ArticleCategoryManagement = window.ArticleCategoryManagement || {}, jQuery));