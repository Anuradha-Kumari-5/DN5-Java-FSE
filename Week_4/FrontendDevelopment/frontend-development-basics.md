# Frontend Development Basics – React, Angular & Vue.js Overview

A quick-reference guide comparing the three most widely used frontend JavaScript frameworks/libraries: **React**, **Angular**, and **Vue.js**.

---

## Table of Contents

- [Introduction](#introduction)
- [React](#react)
- [Angular](#angular)
- [Vue.js](#vuejs)
- [Side-by-Side Comparison](#side-by-side-comparison)
- [Which One Should You Choose?](#which-one-should-you-choose)
- [Learning Resources](#learning-resources)

---

## Introduction

Modern web applications need a structured way to manage UI state, handle user interaction, and update the DOM efficiently. React, Angular, and Vue.js all solve this problem, but with different philosophies:

- **React** — a UI library (not a full framework) focused on building component trees with a virtual DOM
- **Angular** — a complete, opinionated framework with everything built in (routing, forms, HTTP client, DI)
- **Vue.js** — a progressive framework designed to be incrementally adoptable, blending ideas from both React and Angular

---

## React

**Type:** Library (view layer only)
**Created by:** Meta (Facebook)
**Language:** JavaScript / JSX (optionally TypeScript)

### Core Concepts

- **Components** — reusable, self-contained UI pieces (function or class-based)
- **JSX** — syntax extension that lets you write HTML-like markup inside JavaScript
- **Virtual DOM** — React builds an in-memory tree, diffs it against the previous version, and patches only what changed
- **Props** — data passed from parent to child components (read-only)
- **State** — internal, mutable data managed within a component via hooks
- **Hooks** — functions like `useState`, `useEffect`, `useContext` that let function components manage state and side effects
- **Unidirectional data flow** — data flows down via props; events flow up via callbacks

### Minimal Example

```jsx
import { useState } from "react";

function Counter() {
  const [count, setCount] = useState(0);

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>Increment</button>
    </div>
  );
}

export default Counter;
```

### Ecosystem

- **Routing:** React Router
- **State management:** Redux, Zustand, Recoil, Context API
- **Meta-frameworks:** Next.js, Remix
- **Styling:** CSS Modules, styled-components, Tailwind CSS

### Strengths

- Huge ecosystem and community
- Flexible — you choose your own routing/state/build tools
- Strong job market demand
- Great for highly interactive, component-driven UIs

### Trade-offs

- "Just a library" — many architectural decisions (routing, state, forms) are left to the developer
- JSX has a learning curve for beginners
- Frequent ecosystem churn (new libraries/patterns)

---

## Angular

**Type:** Full-fledged framework
**Created by:** Google
**Language:** TypeScript (built-in)

### Core Concepts

- **Components** — TypeScript classes decorated with `@Component`, paired with an HTML template
- **Modules (NgModules)** — organize the app into cohesive blocks of functionality
- **Templates** — HTML extended with Angular-specific syntax (`*ngIf`, `*ngFor`, `{{ }}` interpolation)
- **Directives** — instructions attached to DOM elements (structural like `*ngFor`, or attribute-based like `[ngClass]`)
- **Two-way data binding** — `[(ngModel)]` keeps the UI and component state in sync automatically
- **Dependency Injection (DI)** — services are injected into components rather than instantiated manually
- **RxJS** — reactive programming with Observables is baked into HTTP calls, forms, and routing

### Minimal Example

```typescript
import { Component } from '@angular/core';

@Component({
  selector: 'app-counter',
  template: `
    <p>Count: {{ count }}</p>
    <button (click)="increment()">Increment</button>
  `
})
export class CounterComponent {
  count = 0;

  increment() {
    this.count++;
  }
}
```

### Ecosystem

- **Routing:** Angular Router (built-in)
- **State management:** NgRx, Akita, or plain services + RxJS
- **HTTP client:** `HttpClient` (built-in)
- **Forms:** Template-driven and Reactive Forms (built-in)
- **CLI:** Angular CLI scaffolds, builds, tests, and lints out of the box

### Strengths

- Everything needed is included — routing, forms, HTTP, testing utilities
- TypeScript by default improves maintainability at scale
- Strong conventions make large teams more consistent
- Long-term support backed by Google, popular in enterprise settings

### Trade-offs

- Steeper learning curve (modules, DI, RxJS, decorators all at once)
- More boilerplate than React or Vue for small projects
- Larger bundle size out of the box

---

## Vue.js

**Type:** Progressive framework
**Created by:** Evan You (independent, community-driven)
**Language:** JavaScript / TypeScript

### Core Concepts

- **Single File Components (SFCs)** — `.vue` files combine template, script, and style in one file
- **Template syntax** — HTML-based templates with directives like `v-if`, `v-for`, `v-bind`, `v-model`
- **Reactivity system** — Vue tracks dependencies and automatically updates the DOM when reactive data changes
- **Composition API** — `ref`, `reactive`, `computed`, `watch` (introduced in Vue 3), similar in spirit to React hooks
- **Options API** — the original, more structured way of defining `data`, `methods`, `computed`, `watch` (still supported)
- **Two-way binding** — `v-model` synchronizes form inputs with component state

### Minimal Example

```vue
<template>
  <div>
    <p>Count: {{ count }}</p>
    <button @click="count++">Increment</button>
  </div>
</template>

<script setup>
import { ref } from "vue";
const count = ref(0);
</script>
```

### Ecosystem

- **Routing:** Vue Router (official)
- **State management:** Pinia (current standard), Vuex (legacy)
- **Meta-framework:** Nuxt.js
- **Build tooling:** Vite (created by the Vue team, now used far beyond Vue)

### Strengths

- Gentle learning curve — plain HTML/CSS/JS knowledge transfers directly
- Single File Components keep related code colocated and readable
- Official libraries (router, state) reduce "which library do I pick" fatigue
- Can be adopted incrementally — drop it into part of an existing page or go full SPA

### Trade-offs

- Smaller job market and ecosystem than React or Angular (though growing steadily)
- Fewer large-scale enterprise case studies compared to Angular
- Two APIs (Options vs Composition) can be confusing to newcomers reading mixed tutorials

---

## Side-by-Side Comparison

| Aspect                  | React                          | Angular                          | Vue.js                          |
|-------------------------|---------------------------------|-----------------------------------|-----------------------------------|
| Type                    | Library                        | Full framework                    | Progressive framework            |
| Language                | JS / JSX (TS optional)         | TypeScript (default)              | JS / TS (optional)               |
| Learning curve          | Moderate                       | Steep                              | Gentle                            |
| DOM approach            | Virtual DOM                    | Real DOM + change detection        | Virtual DOM (reactive tracking)  |
| Data binding            | One-way (props down, events up)| Two-way (`ngModel`)                | Two-way (`v-model`) + one-way    |
| State management        | Redux / Zustand / Context      | NgRx / services                    | Pinia / Vuex                      |
| Routing                 | React Router (3rd party)       | Angular Router (built-in)          | Vue Router (official)             |
| CLI tooling             | Create React App / Vite        | Angular CLI (built-in)             | Vue CLI / Vite (official)         |
| Component format        | JSX in `.jsx`/`.tsx`            | Class + template in `.ts`/`.html`  | Single File Component `.vue`     |
| Backed by               | Meta                            | Google                             | Community / independent          |
| Typical use case        | Flexible SPAs, large ecosystems | Enterprise apps, large teams       | Rapid development, incremental adoption |

---

## Which One Should You Choose?

- **Choose React** if you want maximum flexibility, the largest job market, and don't mind assembling your own toolchain (or using a meta-framework like Next.js).
- **Choose Angular** if you're building a large enterprise application, want everything built-in with strong conventions, and your team is comfortable with TypeScript and RxJS from day one.
- **Choose Vue.js** if you want the gentlest learning curve, prefer official first-party libraries over ecosystem shopping, and want the option to adopt it incrementally into an existing project.

There's no universally "best" choice — all three can build production-grade, performant applications. The right pick depends on team experience, project scale, and how much structure you want the framework to enforce versus how much flexibility you want to retain.

---

## Learning Resources

- React: [react.dev](https://react.dev)
- Angular: [angular.dev](https://angular.dev)
- Vue.js: [vuejs.org](https://vuejs.org)

---

*This document is intended as a quick-start reference. Contributions and corrections welcome via pull request.*
