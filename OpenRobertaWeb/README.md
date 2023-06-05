### Typescript support for the OpenRoberta lab

### Getting Started

**Requirements**

- Have node and npm installed globally

**Installing**

1. Go into the OpenRobertaLab folder
2. Install all the dependencies use `npm i`
2. Run typesript compiler and gulp process use `npx gulp`

**Developing**
Compile typescript (and roberta.css) in watch mode use `npx gulp watch`

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

You can find additional information
on [Jetbrain's own Typescript Manual](https://www.jetbrains.com/help/idea/compiling-typescript-to-javascript.html#ts_compiler_compile_code)

### Structure of the project

1. the sources are in directory `ts`. Each "component"/"framework", we have written, should be assembled in a directory below the directory `app`. This is
   necessary
   because the javascript resources loaded by the browser expect this structure (have a look into directory `OpenRobertaServer/staticResources/js`)
2. `node_modules` contains javascript, type declarations, ... .
3. the `neuralnetwork` component needs `d3` in a specific version (see `package.json`). Run `npm install --save` to install **all** dependencies locally.
4. `tsc` needs type declarations, for instance to know the signatures of javascript built-ins. Run `npm install --save @types/node` to install these.
5. **never** commit directory `node_modules`. It must be ignored in `.gitignore`.

### Migration from very old staticResource system

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
