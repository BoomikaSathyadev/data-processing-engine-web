Data Processing Engine
A browser-based DSA visualizer that brings core data structures and algorithms to life through an interactive web interface — with the same logic also implemented in Java for backend/console use.
🔗 Live Demo: https://boomikasathyadev.github.io/data-processing-engine-web/

What This Project Does
Most DSA practice stays limited to console output. This project goes further — every algorithm is interactive, animated, and visible in the browser so you can actually see how data structures behave, not just read about them.

Features

Binary Search Tree (BST) — insert and delete nodes with a live SVG tree visualization that updates in real time
DFS Traversal — animated inorder traversal with step-by-step highlighting
BFS Traversal — animated level-order traversal across the tree
Stack-based Undo — undo the last insertion using a stack, mirroring how undo works in real applications
Selection Sort — visual step-by-step sort on inserted values
Quick Sort — visual quick sort with partition highlighting
Live Stats — BST node count, array size, and stack size update after every operation


Concepts Covered
ConceptImplementationBinary Search TreeInsert, delete, inorder traversalDepth First Search (DFS)Inorder traversal with animationBreadth First Search (BFS)Level-order traversal with animationStackArray-based stack for undo operationsSelection SortIn-place sort with visual pass highlightingQuick SortRecursive sort with partition visualization

Tech Stack
LayerTechUIHTML, CSS, Vanilla JavaScriptVisualizationSVG (no external libraries)Core Logic (Java)java/Engine.java
No external libraries or frameworks — all logic written from scratch.

Run Locally
Web (browser)
bashgit clone https://github.com/BoomikaSathyadev/data-processing-engine-web.git
cd data-processing-engine-web
# Open index.html in any modern browser
Java (core logic)
bashcd java
javac *.java
java Engine

Why This Project
Built to bridge the gap between DSA theory and practical implementation. Understanding why an inorder traversal produces sorted output is one thing — watching it animate node by node makes it stick. The Java implementation runs the same logic independently, reinforcing that the algorithms work beyond the browser environment.
