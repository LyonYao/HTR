
var gulp = require('gulp');
var jshint = require('gulp-jshint');
var del = require('del');
var templateCache = require('gulp-angular-templatecache');
var cssmin = require('gulp-cssmin');
var inject = require('gulp-inject');
var wiredep  = require('wiredep').stream;

gulp.task('default',['devIndex'], function() {
});

var paths = {
    js: ['./scripts/app.route.js', './scripts/app.modules.js', './scripts/**/*.js'],
    css: ['./styles/**/*.css'],
    templates: './scripts/templates.js',
    buildjs: ['./build/**/*.js'],
    buildcss: ['./build/**/*.css']
};

gulp.task('jshint', function() {
    gulp.src(paths.js)
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});

gulp.task('clean', function() {
    // You can use multiple globbing patterns as you would with `gulp.src`
    return del(['./build', paths.templates]);
});

gulp.task('template', function () {
    return gulp.src('./views/**/*.html')
        .pipe(templateCache({module: 'htrApp'}))
        .pipe(gulp.dest('./scripts'))
});

gulp.task('js', function() {
    if (deployEnvironment == Environment.DEV) { // DEV
        return gulp.src(paths.js)
            .pipe(concat('all.js'))
            .pipe(gulp.dest('./build'));
    } else { // PROD
        return gulp.src(pathsb.js)
            .pipe(sourcemaps.init())
            .pipe(stripDebug())
            .pipe(uglify())
            .pipe(concat('all.min.js'))
            .pipe(sourcemaps.write())
            .pipe(gulp.dest('./build'));
    }
});

gulp.task('deployCSS', function() {
    return gulp.src(paths.css)
        .pipe(cssmin())
        .pipe(concat('all.css'))
        .pipe(gulp.dest('./build'));
});

gulp.task('devIndex', ['clean', 'jshint'], function () {
    // It's not necessary to read the files (will speed up things), we're only after their paths:
    return gulp.src('./index.html')
        .pipe(inject(gulp.src(paths.js, {read: false}), {relative: true}))
        .pipe(inject(gulp.src(paths.css, {read: false}), {relative: true}))
        .pipe(wiredep())
        .pipe(gulp.dest('./'));
});

gulp.task('deployIndex', ['clean', 'jshint', 'template', 'deployJS', 'deployCSS'], function () {
    // It's not necessary to read the files (will speed up things), we're only after their paths:
    return gulp.src('./index.html')
        .pipe(inject(gulp.src(paths.buildjs, {read: false}), {relative: true}))
        .pipe(inject(gulp.src(paths.buildcss, {read: false}), {relative: true}))
        .pipe(wiredep())
        .pipe(gulp.dest('./'));
});