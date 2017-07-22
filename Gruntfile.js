/*global module*/
module.exports = function (grunt) {
    "use strict";
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        env: {
            coverage: {
                APP_DIR_FOR_CODE_COVERAGE: 'test/coverage/instrument/app/'
            }
        },
        jshint: {
            all: ['Gruntfile.js', 'src/main/webapp/jsfull/**/*.js', 'test/**/*.js', '!src/main/webapp/jsfull/**/bootstrap.min.js']
        },
        qunit: {
            all: ['src/main/webapp/WEB-INF/Tests/Qunit.html'],
            options: {
                '--web-security': 'no',
                console: true,
                coverage: {
                    disposeCollector: true,
                    src: ['src/main/webapp/jsfull/*.js', '!src/main/webapp/jsfull/sinon*.js'],
                    instrumentedFiles: 'test/coverage/instrument',
                    htmlReport: 'report/coverage',
                    coberturaReport: 'report/',
                    linesThresholdPct: 84
                }
            }
        },
        githooks: {
            all: {
                options: {
            // Target-specific options go here 
                },
                'pre-commit' : 'jshint qunit'
            }
        },
        cssmin: {
            options: {
                shorthandCompacting: true,
                roundingPrecision: -1
            },
            target: {
                files: [{
                    expand: true,
                    cwd: 'src/main/webapp/cssfull',
                    src: ['*.css', '!*.min.css'],
                    dest: 'src/main/webapp/css',
                    ext: '.css'
                }]
            }
        },
        uglify: {
            mangle: {
                except: ['jQuery', 'Backbone']
            },
            my_target: {
                files: [{
                    expand: true,
                    cwd: 'src/main/webapp/jsfull',
                    src: ['**/*.js', '!*.min.js'],
                    dest: 'src/main/webapp/js'
                }]
            }
        },
        concat:{
            dist: {
                src: ['src/main/webapp/jsfull/Pieces/chessPiece.js',
                        'src/main/webapp/jsfull/Pieces/pawnPiece.js',
                        'src/main/webapp/jsfull/Pieces/bishopPiece.js',
                        'src/main/webapp/jsfull/Pieces/knightPiece.js',
                        'src/main/webapp/jsfull/Pieces/rookPiece.js',
                        'src/main/webapp/jsfull/Pieces/queenPiece.js',
                        'src/main/webapp/jsfull/Pieces/kingPiece.js'
                    ],
                dest: 'src/main/webapp/jsfull/AChessPieces.js'
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-githooks');
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-qunit-istanbul');

    grunt.registerTask('default', ['concat', 'jshint', 'uglify', 'qunit', 'cssmin']);
    grunt.registerTask('QUnit', ['qunit']);
    grunt.registerTask('css_min', ['cssmin']);
    grunt.registerTask('createModule', ['concat']);
};
