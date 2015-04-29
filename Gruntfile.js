/*global module*/
module.exports = function (grunt) {
    "use strict";
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        jshint: {
            all: ['Gruntfile.js', 'src/main/webapp/js/**/*.js', 'test/**/*.js', '!src/main/webapp/js/**/bootstrap.min.js']
        },
        qunit: {
            all: ['src/main/webapp/WEB-INF/Tests/Qunit.html']
        },
        githooks: {
            all: {
                options: {
            // Target-specific options go here 
                },
                'pre-commit' : 'jshint qunit'
            }
        }

    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-qunit');
    grunt.loadNpmTasks('grunt-githooks');
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.registerTask('default', ['jshint', 'qunit']);
    grunt.registerTask('QUnit', ['qunit']);
};
