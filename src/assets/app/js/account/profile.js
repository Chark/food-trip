$(function () {

    var form = $('form[name=profileForm]');

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
});