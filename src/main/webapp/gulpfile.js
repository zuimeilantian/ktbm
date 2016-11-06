const gulp = require('gulp'),
	  connect = require('gulp-connect');

gulp.task('serve', function(){
	connect.server({
    	root: 'app',
    	livereload: true,
    	port: 29090
    });
	gulp.watch('app/**/*.*', ['reload']);
});
gulp.task('reload', function(){
	gulp.src('app/**/*.*')
    	.pipe(connect.reload());
});