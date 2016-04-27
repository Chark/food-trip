module.exports = function (grunt) {
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');

    grunt.initConfig({
        config: {
            APP_PATH: 'src/assets/app',
            DEPLOY_PATH: 'src/main/resources/static/compiled',
            VENDOR_PATH: 'src/assets/vendor'
        },

        uglify: {
            deps_js: {
                files: {
                    '<%= config.DEPLOY_PATH  %>/plugins.js': [
                        '<%= config.VENDOR_PATH  %>/jquery/dist/jquery.js',
                        '<%= config.VENDOR_PATH  %>/semantic/dist/semantic.js'
                    ]
                }
            }
        },
        concat: {
            css_deps: {
                files: {
                    '<%= config.DEPLOY_PATH  %>/plugins.css': [
                        '<%= config.VENDOR_PATH  %>/semantic/dist/semantic.css'
                    ]
                }
            },
            css_main: {
                src: '<%= config.APP_PATH  %>/css/**/*.css',
                dest: '<%= config.DEPLOY_PATH  %>/app.css'
            },
            js_main: {
                src: '<%= config.APP_PATH  %>/js/**/*.js',
                dest: '<%= config.DEPLOY_PATH  %>/app.js'
            }
        },
        copy: {
            fonts: {
                expand: true,
                cwd: '<%= config.VENDOR_PATH  %>/semantic/dist/themes/default/assets/fonts/',
                src: '**',
                dest: '<%= config.DEPLOY_PATH  %>/themes/default/assets/fonts',
                flatten: true,
                filter: 'isFile'
            }
        },
        watch: {
            options: {
                event: ['changed', 'added', 'deleted']
            },
            js_main: {
                files: '<%= config.APP_PATH  %>/js/**/*.js',
                tasks: ['concat:js_main'],
                options: {
                    spawn: false,
                    interrupt: true
                }
            },
            css_main: {
                files: '<%= config.APP_PATH  %>/css/**/*.css',
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
};