const { src, dest, watch, series, parallel } = require('gulp');
const ts = require('gulp-typescript');
const sass = require('gulp-sass')(require('sass'));
const terser = require('gulp-terser');
const postcss = require('gulp-postcss');
const autoprefixer = require('autoprefixer');
const sourcemaps = require('gulp-sourcemaps');
const cssnano = require('cssnano');
const replace = require('gulp-replace');
const merge = require('merge-stream');

var tsProject = ts.createProject('tsconfig.json');
var reporter = ts.reporter.fullReporter();

// File paths
const files = {
    scssPath: '../OpenRobertaServer/staticResources/app/scss/**/*.scss',
    cssPath: 'css/roberta.css',
    jsPath: 'src/**/*',
    nodePaths: [
        {
            moduleName: 'ace',
            bases: [
                {
                    base: '../OpenRobertaWeb/node_modules/ace-builds/src-min-noconflict',
                    //defines files to be copied over
                    src: [
                        //you may add '/**/*.js' instead of a {folder: '', file['']} structure or similar paths with wildcard
                        {
                            //the base folder is just an empty string, files may also be described as *.js if all files of a given folder should be copied
                            folderSrc: '',
                            folderDest: '',
                            files: ['ace.js', 'ext-language_tools.js', 'mode-c_cpp.js', 'mode-java.js', 'mode-python.js', 'mode-json.js'],
                        },
                    ],
                },
            ],
        },
        {
            moduleName: 'bootstrap',
            bases: [
                {
                    base: '../OpenRobertaWeb/node_modules/bootstrap',
                    src: [
                        {
                            folderSrc: 'dist/js',
                            folderDest: '',
                            files: ['bootstrap.bundle.min.js'],
                        },
                    ],
                },
                {
                    base: '../OpenRobertaWeb/node_modules/bootstrap-tagsinput',
                    src: [
                        {
                            folderSrc: 'dist',
                            folderDest: '',
                            files: ['bootstrap-tagsinput.min.js'],
                        },
                    ],
                },
                {
                    base: '../OpenRobertaWeb/node_modules/bootstrap-wysiwyg/',
                    src: [
                        {
                            folderSrc: 'js',
                            folderDest: '',
                            files: ['bootstrap-wysiwyg.min.js'],
                        },
                    ],
                },
                {
                    base: '../OpenRobertaWeb/node_modules/bootstrap-table',
                    src: [
                        {
                            folderSrc: 'dist',
                            folderDest: 'bootstrap-table-1.22.1-dist/js',
                            files: ['bootstrap-table.min.js', 'bootstrap-table-locale-all.min.js'],
                        },
                    ],
                },
            ],
        },
        {
            moduleName: 'dapjs',
            bases: [
                {
                    base: '../OpenRobertaWeb/node_modules/dapjs',
                    src: [
                        {
                            folderSrc: 'dist',
                            folderDest: '',
                            files: ['dap.umd.js'],
                        },
                    ],
                },
            ],
        },
    ],
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
        .pipe(
            sourcemaps.write('.', {
                includeContent: false,
                sourceRoot: '/',
            })
        )
        .pipe(dest('../OpenRobertaServer/staticResources/js/', { sourcemaps: '.' }));
}

function nodeTask() {
    const streams = files.nodePaths.map(function (module) {
        return module.bases.map(function (base) {
            return base.src.map(function (source) {
                if (typeof source === 'string') {
                    return src(base.base + source, { sourcemaps: true }) // set source and turn on sourcemaps
                        .pipe(dest('../OpenRobertaServer/staticResources/libs/' + module.moduleName, { sourcemaps: '.' }));
                } else if (source.folderSrc !== undefined && source.folderDest !== undefined && source.files !== undefined) {
                    return source.files.map(function (file) {
                        return src(base.base + '/' + source.folderSrc + '/' + file).pipe(
                            dest('../OpenRobertaServer/staticResources/libs/' + module.moduleName + '/' + source.folderDest, { sourcemaps: '.' })
                        );
                    });
                } else {
                    throw Error(
                        'make sure to define folder and files for each source or add a plain string as path, for copying from base folder leave this as an empty String'
                    );
                }
            });
        });
    });
    return merge(...streams);
}

// Cachebust
function cacheBustTask() {
    var cbString = new Date().getTime();
    return src(['index.html'], { allowEmpty: true })
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
exports.default = series(parallel(scssTask, cssTask, nodeTask, jsTask), cacheBustTask);

// Runs only css and js tasks simultaneously, cacheBustTask and ends in watchTask
exports.watch = series(parallel(cssTask, jsTask), cacheBustTask, watchTask);
