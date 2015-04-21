module.exports = function (grunt) {
    "use strict";
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        jshint: {
            all: ['Gruntfile.js', ' src/main/webapp/js/**/*.js', 'test/**/*.js']
        },
        qunit: {
            all: ['src/main/webapp/WEB-INF/Tests/Qunit.html']
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-qunit');
    grunt.registerTask('default', ['jshint', 'qunit']);
    grunt.registerTask('QUnit', ['qunit']);
};
