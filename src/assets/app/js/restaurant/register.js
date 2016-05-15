(function (RestaurantRegister, $, undefined) {

    /**
     * Initialize restaurant registration page.
     */
    RestaurantRegister.init = function () {

        // Registration form.
        var form = $('form[name=registerForm]');

        // Validate registration form.
        form.validate({
            errorElement: 'small',
            rules: {
                email: {
                    maxlength: 64,
                    required: true
                },
                description: {
                    maxlength: 1024
                },
                name: {
                    minlength: 4,
                    maxlength: 64,
                    required: true
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
    
}(window.RestaurantRegister = window.RestaurantRegister || {}, jQuery));