// Micro Frontends with Module Federation - Reference File

// === 1. Host (Shell) webpack.config.js concept ===
const hostConfig = {
  name: 'shell',
  remotes: {
    mfeApp: 'mfeApp@http://localhost:4201/remoteEntry.js'
  },
  shared: {
    '@angular/core': { singleton: true, strictVersion: true },
    '@angular/common': { singleton: true, strictVersion: true },
    '@angular/router': { singleton: true, strictVersion: true }
  }
};

// === 2. Remote (Micro Frontend) webpack.config.js concept ===
const remoteConfig = {
  name: 'mfeApp',
  filename: 'remoteEntry.js',
  exposes: {
    './Component': './src/app/remote-entry/entry.component.ts',
    './Routes': './src/app/remote-entry/entry.routes.ts'
  },
  shared: {
    '@angular/core': { singleton: true, strictVersion: true },
    '@angular/common': { singleton: true, strictVersion: true }
  }
};

// === 3. Host routing (lazy load remote) ===
const shellRoutes = [
  {
    path: 'mfe',
    loadChildren: () => import('mfeApp/Routes').then(m => m.routes)
  }
];

// === 4. Remote entry component ===
import { Component } from '@angular/core';

@Component({
  selector: 'app-mfe-entry',
  standalone: true,
  template: `<h2>Micro Frontend Loaded!</h2><router-outlet />`
})
export class MfeEntryComponent {}

// === 5. Communication between MFEs (Custom Events) ===
function emitEvent(name: string, data: any) {
  window.dispatchEvent(new CustomEvent(name, { detail: data }));
}
function listenEvent(name: string, handler: (e: CustomEvent) => void) {
  window.addEventListener(name, handler as EventListener);
}

console.log('Reference file: ejemplo-mfe.ts - Module Federation micro frontends for use in an Angular project');
