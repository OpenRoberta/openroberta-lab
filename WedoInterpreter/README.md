### typescript support for developing the WeDo interpreter (Eclipse)

1. install the plugin 'TypeScript IDE' (in spite of its strange name it is a plugin). It includes an editor and ts compiler. Restart eclipse.
2. the settings are ok (including format + save options)
3. the tsconfig.json contains settings for tsc. For the full power of this config file see https://www.typescriptlang.org/docs/handbook/tsconfig-json.html .
Simple settings are, e.g.:
```javascript
{
    "compilerOptions": {
        "outDir": "<directory-for-generated-js-sources>",
        "rootDir": "<directory-with-ts-sources>",
        "target": "es5",
        "moduleResolution": "node",
        "noUnusedLocals": true,
        "module": "umd"
    },
    "compileOnSave": true,
    "buildOnSave": true
}
```
4. right click the project -> Typescript and add the 'tsconfig.json' to the typescript build path. On every save the compiler runs (incrementally).
5. for compilation of all files ('clean update' right click on the file 'tsconfig.json' and 'run as compile typescript'.
6. https://github.com/angelozerr/typescript.java/wiki/Getting-Started contains a lot of valuable information about the plugin

7. the editor supports 'goto dec', 'completion' and much more (see the wiki). Note (again): On every save the compiler runs (incrementally) and shows all errors
   in the 'problem view'. Click and analyse.
8. sometimes run the tsc-configuration to achieve consistent generated javascript files.

9. PITFALL 1: 'goto dec' and others may opens a NEW editor. Thus a file may be accessed by more than one editor.
   BE CAREFUL: NEVER edit the same source at the same time with two editors. Changes will be LOST in almost allcases!
10. PITFALL 2: as with Java nature, if you open a .ts-file as simple resource instead as a ts resource, on save the incremental compilation will NOT be done.
10. PERSONAL REMARK: I have activated 'codelens'. It is EXPERIMENTAL. It gives reference counts of components and a used-by functionality when clicking on the
    references (small italic font).
   
The 'TypeScript IDE' is a nice and really fast ts editor and tsc plugin

### javascript support  for developing the WeDo interpreter (OUTDATED! WE USE TYPESCRIPT!)

1. project -> right click -> properties
    * convert to faceted form
    * add Javascript facet
    * activate Javascript builder in facets
    * in Javascript add formatter + save actions (tbd)
    * add the base directory /js to the include path, add **/*.js as inclusion pattern
    
2. in preferences -> Javascript -> Runtimes
    * add the node location
    * select the node.js runtime
    * error: this is resetted to chromium by eclipse

3. open a js file with the Javascript editor
    * outline ok
    * call hierarchy ok
    * completion ok
    * goto definition fails

4. debug/run
    * run as ... ok
    * debug as ... ok when stepping or hitting a breakpoint in the "main" file
    * showing variables etc is ok
    * fails to hit a breakpoint in a "non-main" file
    * but stepping down into that file and then seting a breakpoint is ok
    * the breakpoint vanishes randomly(?)
    * process must be cancelled explicitly
