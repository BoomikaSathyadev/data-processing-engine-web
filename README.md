# Data Processing Engine

Interactive DSA demo: BST tree, DFS & BFS, stack undo, Selection & Quick Sort.

**Live demo:** https://boomikasathyadev.github.io/data-processing-engine-web/

## Tech stack

| Part | Tech |
|------|------|
| Core logic + UI | **Java** (`java/Engine.java`, `App.java`, `TreePanel.java`) |
| Browser page | **HTML** only (`index.html`) |
| Algorithm JavaScript | **None** |

Runs as `app.jar` in the browser via [CheerpJ](https://leaningtech.com/cheerpj).

## Build before push

```bash
cd java
javac *.java
jar cfe ../app.jar App *.class
cd ..
```

Push: `index.html`, `app.jar`, `java/`

## GitHub Pages

Settings → Pages → branch **main** → folder **/(root)**
