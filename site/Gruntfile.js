module.exports = function (grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        uglify: {
            build: {
                src: 'src/main/site/assets/js/global.js',
                dest: 'src/main/site/assets/build/js/global.min.js'
            }
        },

        imagemin: {
            dynamic: {
                files: [{
                    expand: true,
                    cwd: 'src/main/site/assets/img/',
                    src: ['**/*.{png,jpg,gif}'],
                    dest: 'src/main/site/assets/build/img/'
                }]
            }
        },

        less: {
            production: {
                options: {
                    paths: ["src/main/site/assets/less"],
                    cleancss: true
                },
                files: {
                    "src/main/site/assets/build/css/style.min.css": "src/main/site/assets/less/style.less"
                }
            }
        },

        watch: {
            options: {
                livereload: true
            },
            scripts: {
                files: ['src/main/site/assets/js/*.js'],
                tasks: ['uglify'],
                options: {
                    spawn: false
                }
            },
            css: {
                files: ['src/main/site/assets/less/*.less'],
                tasks: ['less'],
                options: {
                    spawn: false
                }
            },
            images: {
                files: ['src/main/site/assets/img/**/*.{png,jpg,gif}'],
                tasks: ['imagemin'],
                options: {
                    spawn: false
                }
            }
        },
    });

    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-imagemin');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('default', 'watch');
    grunt.registerTask('imgs', 'imagemin');
    grunt.registerTask('build', ['imagemin', 'less', 'uglify']);
};
