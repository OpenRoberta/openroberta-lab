const { src, dest, watch, series, parallel } = require('gulp');
const ts = require('gulp-typescript');
const sass = require('gulp-sass')(require('sass'));
const terser = require('gulp-terser');
const postcss = require('gulp-postcss');
const autoprefixer = require('autoprefixer');
const sourcemaps = require('gulp-sourcemaps');
const cssnano = require('cssnano');
const replace = require('gulp-replace');

var tsProject = ts.createProject('tsconfig.json');
var reporter = ts.reporter.fullReporter();

// File paths
const files = {
    scssPath: '../OpenRobertaServer/staticResources/app/scss/**/*.scss',
    cssPath: 'css/roberta.css',
    jsPath: 'src/**/*'
};

// Sass task: compiles the style.scss file into style.css
function scssTask() {
    return src(files.scssPath, { sourcemaps: true }) // set source and turn on sourcemaps
        .pipe(sass()) // compile SCSS to CSS
        .pipe(postcss([autoprefixer(), cssnano()])) // PostCSS plugins
        .pipe(dest('../OpenRobertaServer/staticResources/css', { sourcemaps: '.' })); // put final CSS in dist folder with sourcemap
}
function cssTask() {
    return src(files.cssPath, { sourcemaps: true }) // set source and turn on sourcemaps
        .pipe(postcss([autoprefixer(), cssnano()])) // PostCSS plugins
        .pipe(dest('../OpenRobertaServer/staticResources/css', { sourcemaps: '.' })); // put final CSS in dist folder with sourcemap
}

// JS task: concatenates and uglifies JS files to script.js
function jsTask() {
    return tsProject
        .src()
        .pipe(sourcemaps.init())
        .pipe(tsProject(reporter))
        .pipe(terser())
        .pipe(sourcemaps.write('.', {
            includeContent: false,
            sourceRoot: '/'
        }))
        .pipe(dest('../OpenRobertaServer/staticResources/js/', { sourcemaps: '.' }));
}

// Cachebust
function cacheBustTask() {
    var cbString = new Date().getTime();
    return src(['index.html'], { 'allowEmpty': true })
        .pipe(replace(/cb=\d+/g, 'cb=' + cbString))
        .pipe(dest('.'));
}

// Watch task: watch CSS and JS files for changes
function watchTask() {
    watch(
        [files.cssPath, files.jsPath],
        { interval: 1000, usePolling: true }, //Makes docker work
        series(parallel(cssTask, jsTask), cacheBustTask)
    );
}

// Runs the scss and js tasks simultaneously then runs cacheBust
exports.default = series(parallel(scssTask, cssTask, jsTask), cacheBustTask);

// Runs only css and js tasks simultaneously, cacheBustTask and ends in watchTask
exports.watch = series(parallel(cssTask, jsTask), cacheBustTask, watchTask);
