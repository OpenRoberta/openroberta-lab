### Typescript support for the OpenRoberta lab

### Getting Started

**Requirements**

-   Have node and npm installed globally

**Installing & Developing**

1. Install the dependencies using `npm i`
2. To let typescript compile using `npm run build`
3. Use typescript in watch mode using `npm run watch`

### Typescript support for the OpenRoberta lab (Eclipse)

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

### Typescript support for the OpenRoberta lab (Intellij Ultimate / Webstorm)

IntelliJ Ultimate supports typescript without third-party plugins.
But make sure the JavaScript and TypeScript bundled plugin is enabled on the **Settings/Preferences | Plugins page**

**Setup**

1. Start with [Getting Started](#getting-started)
2. Install the IntelliJ formatter from `Resources/formatter/openRobertaIdea.xml`
3. Go to **Settings/Preferences | Language & Frameworks | Typescript | Select Typescript Compiler (./node_modules/typescript)**

**Developing**
Our typescript compiler is configured to compile to `OpenRobertaServer/staticResources`.

1. Start open Roberta Server (either `./ora.sh start-from-git` or use the IDE Starter)
2. To compile with typescript click the Typescript Widget in the status bar choose **Compile | Compile All**
3. To compile everytime on change go to **Settings/Preferences | Language & Frameworks | Typescript** and select **Recompile on changes**
4. Everytime you changed a typescript file reload the page, since we don't have a live reloader

You can find additional information on [Jetbrain's own Typescript Manual](https://www.jetbrains.com/help/idea/compiling-typescript-to-javascript.html#ts_compiler_compile_code)

### Structure of the project

1. the sources are in directory `ts`. Each "component"/"framework", we have written, should be assembled in a directory below the directory `app`. This is necessary
   because the javascript resources loaded by the browser expect this structure (have a look into directory `OpenRobertaServer/staticResources/js`)
2. `node_modules` contains javascript, type declarations, ... .
3. the `neuralnetwork` component needs `d3` in a specific version (see `package.json`). Run `npm install --save` to install **all** dependencies locally.
4. `tsc` needs type declarations, for instance to know the signatures of javascript built-ins. Run `npm install --save @types/node` to install these.
5. **never** commit directory `node_modules`. It must be ignored in `.gitignore`.

### Migration from old staticResource system

1. Use tools to automate

```bash
amdtoes6 --dir ts --out ts
cjs-to-es6 ts
```

2. Replace
   `import (\S+) from '(\S+)';`
   with
   `import * as $1 from '$2';`
3. Fix individual errors
