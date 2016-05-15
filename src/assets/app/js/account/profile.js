(function (Profile, $, undefined) {

    /**
     * Initialize profile page.
     */
    Profile.init = function () {

        /**
         * Accept or ignore an invitation.
         */
        function invitation(id, accept) {
            $.post('/profile/api/invitation/' + id + '?accept=' + accept, function () {
                location.reload();
            }).fail(function () {
                toastr.error('Failed to process this action');
            });
        }

        // Profile edit form.
        var form = $('form[name=profileForm]');

        // Validate profile edit form.
        form.validate({
            errorElement: 'small',
            rules: {
                email: {
                    required: true
                },
                password: {
                    minlength: 4,
                    maxlength: 64
                },
                repeatPassword: {
                    equalTo: 'input[name=password]'
                },
                bio: {
                    maxlength: 1024
                },
                name: {
                    maxlength: 64
                },
                firstName: {
                    maxlength: 64
                },
                phone: {
                    maxlength: 64
                },
                website: {
                    maxlength: 64
                }
            },
            highlight: function (element) {
                $(element).closest('.form-group').addClass('has-error');
            },
            success: function (element) {
                element.closest('.form-group').removeClass('has-error');
            }
        });

        /**
         * Accept or decline invitation.
         */
        $('.invitations > .invitation').click(function () {

            var id = $(this).data('id');
            bootbox.dialog({
                title: 'Invitation',
                message: 'What should we do with this invitation',
                buttons: {
                    accept: {
                        label: 'Accept',
                        className: 'btn-success',
                        callback: function () {
                            invitation(id, true);
                        }
                    },
                    ignore: {
                        label: 'Ignore',
                        className: 'btn-warning',
                        callback: function () {
                            invitation(id, false);
                        }
                    },
                    cancel: {
                        label: 'Cancel',
                        className: 'btn-default'
                    }
                }
            });
        });
    };
}(window.Profile = window.Profile || {}, jQuery));