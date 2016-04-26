module.exports = function (grunt) {
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');

    grunt.initConfig({
        uglify: {
            deps_js: {
                files: {
                    'src/main/resources/static/compiled/plugins.js': [
                        'src/main/resources/static/vendor/jquery/dist/jquery.js',
                        'src/main/resources/static/vendor/semantic/dist/semantic.js'
                    ]
                }
            }
        },

        concat: {
            css_deps: {
                files: {
                    'src/main/resources/static/compiled/plugins.css': [
                        'src/main/resources/static/vendor/semantic/dist/semantic.css'
                    ]
                }
            },
            css_main: {
                src: 'src/main/resources/static/css/**/*.css',
                dest: 'src/main/resources/static/compiled/app.css'
            },
            js_main: {
                src: 'src/main/resources/static/js/**/*.js',
                dest: 'src/main/resources/static/compiled/app.js'

            }
        },

        watch: {
            js_main: {
                files: 'src/main/resources/static/js/**/*.js',
                tasks: ['concat:js_main'],
                options: {
                    spawn: false,
                    interrupt: true
                }
            },
            css_main: {
                files: 'src/main/resources/static/css/**/*.css',
                tasks: ['concat:css_main'],
                options: {
                    spawn: false,
                    interrupt: true
                }
            }
        }


    });
    grunt.registerTask('default', 'Do something interesting.', function () {
        grunt.log.writeln("");
        grunt.log.writeln("");

        grunt.log.writeln("If you are starting this project for the first time or you want to initialize this projects dependencies and other required files type:");
        grunt.log.ok(["grunt init"]);
        grunt.log.writeln("");
        grunt.log.writeln("If you want to watch files type: ");
        grunt.log.ok(["grunt watch"]);

        grunt.log.writeln("");
        grunt.log.writeln("");
    });


    grunt.registerTask('init', ['uglify:deps_js', 'concat:js_main', 'concat:css_deps', 'concat:css_main']);
    grunt.registerTask('watch', ['watch']);
};