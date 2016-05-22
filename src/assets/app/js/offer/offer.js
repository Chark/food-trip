
(function (Offers, $, undefined) {

    var offerUrl = '/restaurant/api/offers';
    var offerTable;
    var infoModal;

    /**
     * Initialize audit page.
     */
    Offers.initOffers = function () {


        /**
         * Initialize thread table.
         */

        offerTable = $('table.offers-table').DataTable({
            ajax: {
                url: offerUrl,
                dataSrc: ''
            },
            order: [
                [1, 'desc']
            ],
            columns: [
                {data: 'id'},
                {
                    data: 'headline'
                },
                {
                    data: 'description'
                },
                {
                    data: 'publicationDate',
                    render: function (e) {
                        return Utils.date(e);
                    }
                },
                {
                    data: 'validThrough'
                },
                {
                    data: 'id',
                    render: function(id){
                        return Utils.urlButton('Edit', 'btn-success edit', '/restaurant/offer/edit/' + id, id) + ' ' +
                            Utils.smallButton('Delete', 'btn-danger delete', id);
                    }
                }
            ]
        });


        offerTable.on('click', 'a.delete', function () {
            var id = $(this).data('id');
            bootbox.dialog({
                title: 'Delete offer',
                message: 'Are you sure you want to delete this offer?',
                buttons: {
                    yes: {
                        label: 'Delete',
                        className: 'btn-danger',
                        callback: function () {
                            $.delete(offerUrl + '/' + id, function () {
                                toastr.success('Deleted thread');
                                offerTable.ajax.reload();
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



}(window.Offers = window.Offers || {}, jQuery));



