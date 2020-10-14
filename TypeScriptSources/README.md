### typescript support for the OpenRoberta lab (Eclipse)

we plan to change the way, we work with typescript. Thus, this may be outdated soon.

1. install our Javascript formatter as found in the Resources directory
2. install the plugin 'TypeScript IDE' (in spite of its strange name it is a plugin). It includes an editor and ts compiler. Restart eclipse. The settings are ok.
   the 'TypeScript IDE' has a nice and really fast ts editor. It is declared outdated on the market place. The propsed wild web stuff did not work for us.
3. the file `package.json` declares on which javascript frameworks we depend.
4. the file `tsconfig.json` contains settings for tsc when web content to be consumed by a browser should be generated.
   the file `tsconfig-umd.json` contains settings for tsc when content to be consumed by node should be generated.
   for the full power of this config file see https://www.typescriptlang.org/docs/handbook/tsconfig-json.html .

Workflow, when using Eclipse (for Version: 2019-12 (4.14.0), does not work with the actual Eclipse. e.g. 2010-09 due to missing js and ts support :-<):

1. right click the project -> Typescript and add the 'tsconfig.json' to the typescript build path. On every save the compiler runs (incrementally).
2. for compilation of all files ('clean update' right click on the file 'tsconfig.json' and 'run as compile typescript'.
3. https://github.com/angelozerr/typescript.java/wiki/Getting-Started contains a lot of valuable information about the plugin

4. the editor supports 'goto dec', 'completion' and much more (see the wiki). Note (again): On every save the compiler runs (incrementally) and shows all errors
   in the 'problem view'. Click and analyse.
5. sometimes run the tsc-configuration to achieve consistent generated javascript files.

Pitfalls:
1: 'goto dec' and others may opens a NEW editor. Thus a file may be accessed by more than one editor.
   BE CAREFUL: NEVER edit the same source at the same time with two editors. Changes will be LOST in almost allcases!
2: as with Java nature, if you open a .ts-file as simple resource instead as a ts resource, on save the incremental compilation will NOT be done.

PERSONAL REMARK: I have activated 'codelens'. It is EXPERIMENTAL. It gives reference counts of components and a used-by functionality when clicking on the
    references (small italic font).
   
### Structure of the project

1. the ts sources are in directory `ts`. Each "component"/"framework", we have written, should be assembled in a directory below the directory `app`. This is necessary
   because the javascript resources loaded by the browser expect this structure (have a look into directory `OpenRobertaServer/staticResources/js`)
2. `node_modules` contains javascript, type declarations, ... .
3. the `neuralnetwork` component needs `d3` in a specific version (see `package.json`).  Run `npm install --save` to install **all** dependencies locally.
4. `tsc` needs type declarations, for instance to know the signatures of javascript built-ins. Run `npm install --save @types/node` to install these.
5. **never** commit directory `node_modules`. It must be ignored in `.gitignore`.  