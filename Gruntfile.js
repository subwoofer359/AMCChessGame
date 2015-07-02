/*global module*/
module.exports = function (grunt) {
    "use strict";
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        jshint: {
            all: ['Gruntfile.js', 'src/main/webapp/jsfull/**/*.js', 'test/**/*.js', '!src/main/webapp/jsfull/**/bootstrap.min.js']
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
  	}

    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-qunit');
    grunt.loadNpmTasks('grunt-githooks');
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    
    grunt.registerTask('default', ['jshint', 'uglify', 'qunit', 'cssmin']);
    grunt.registerTask('QUnit', ['qunit']);
    grunt.registerTask('css_min', ['cssmin']);
};
