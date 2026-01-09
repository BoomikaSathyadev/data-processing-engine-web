# Data Processing Engine (Web)

This is a browser-based mini project built to practice and visualize core
Data Structures and Algorithms concepts. Instead of limiting everything to
console output, this project shows how DSA logic can be implemented and
interacted with through a simple web interface.

The goal of this project is to clearly understand how algorithms work
internally and how they can be applied in a real, interactive environment.

## What this project does
- Allows you to insert numbers into a Binary Search Tree (BST)
- Displays the BST using DFS (Inorder Traversal)
- Supports undoing the last insertion using a Stack
- Sorts the inserted values using Selection Sort
- Shows live statistics like number of BST nodes, array size, and stack size

## Concepts used
This project focuses on the following DSA concepts:
- Binary Search Tree (insert, delete, inorder traversal)
- Depth First Search (DFS – inorder)
- Stack (array-based, used for undo operation)
- Selection Sort

## Technologies used
- HTML
- CSS
- JavaScript (Vanilla)

No external libraries or frameworks are used.

## Why this project
Most DSA programs are written only for console output.  
This project was created to:
- Strengthen DSA fundamentals
- Visualize how data structures behave
- Connect theoretical concepts with practical usage
- Build confidence in implementing logic beyond textbooks

## How to run
1. Clone or download this repository
2. Open the `index.html` file in any modern web browser
3. Start inserting values and interacting with the application

## Notes
- Inorder traversal of a BST always displays values in sorted order
- Selection Sort works on a copy of the inserted data
- Stack is used to track and undo the most recent insertion
