/**
 * UI + browser demo (JavaScript).
 * Core algorithms: java/Engine.java and java/TreeNode.java
 */

// --- mirrors java/Engine.java (for static GitHub Pages demo) ---
const MAX = 100;
const stack = [];
let root = null;
let arr = [];
let animating = false;

function Node(data) {
  this.data = data;
  this.left = null;
  this.right = null;
}

function push(x) {
  if (stack.length >= MAX) return false;
  stack.push(x);
  return true;
}

function pop() {
  return stack.length ? stack.pop() : null;
}

function insertBST(node, data) {
  if (!node) return new Node(data);
  if (data < node.data) node.left = insertBST(node.left, data);
  else if (data > node.data) node.right = insertBST(node.right, data);
  return node;
}

function findMin(node) {
  while (node && node.left) node = node.left;
  return node;
}

function deleteNode(node, key) {
  if (!node) return null;
  if (key < node.data) node.left = deleteNode(node.left, key);
  else if (key > node.data) node.right = deleteNode(node.right, key);
  else {
    if (!node.left && !node.right) return null;
    if (!node.left) return node.right;
    if (!node.right) return node.left;
    const t = findMin(node.right);
    node.data = t.data;
    node.right = deleteNode(node.right, t.data);
  }
  return node;
}

function inorderTraversal(node, out) {
  if (!node) return;
  inorderTraversal(node.left, out);
  out.push(node);
  inorderTraversal(node.right, out);
}

function bfsTraversal(node) {
  const order = [];
  if (!node) return order;
  const q = [node];
  while (q.length) {
    const cur = q.shift();
    order.push(cur);
    if (cur.left) q.push(cur.left);
    if (cur.right) q.push(cur.right);
  }
  return order;
}

function selectionSort(a) {
  const copy = a.slice();
  for (let i = 0; i < copy.length - 1; i++) {
    let min = i;
    for (let j = i + 1; j < copy.length; j++) if (copy[j] < copy[min]) min = j;
    [copy[min], copy[i]] = [copy[i], copy[min]];
  }
  return copy;
}

async function quickSortAnimated(a, onStep) {
  const copy = a.slice();
  async function partition(lo, hi) {
    const pivot = copy[hi];
    await onStep({ arr: copy.slice(), pivotIdx: hi, phase: "pivot" });
    let i = lo - 1;
    for (let j = lo; j < hi; j++) {
      await onStep({ arr: copy.slice(), i, j, pivotIdx: hi, phase: "compare" });
      if (copy[j] < pivot) {
        i++;
        [copy[i], copy[j]] = [copy[j], copy[i]];
        await onStep({ arr: copy.slice(), i, j, pivotIdx: hi, phase: "swap" });
      }
    }
    [copy[i + 1], copy[hi]] = [copy[hi], copy[i + 1]];
    await onStep({ arr: copy.slice(), pivotIdx: i + 1, phase: "swap" });
    return i + 1;
  }
  async function sort(lo, hi) {
    if (lo >= hi) return;
    const p = await partition(lo, hi);
    await sort(lo, p - 1);
    await sort(p + 1, hi);
  }
  if (copy.length) await sort(0, copy.length - 1);
  return copy;
}

// --- UI ---
const el = (id) => document.getElementById(id);
const msg = (t) => { el("messages").textContent = t; };
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

function countNodes(node) {
  return node ? 1 + countNodes(node.left) + countNodes(node.right) : 0;
}

function setAnimating(on) {
  animating = on;
  ["btn-insert", "btn-dfs", "btn-bfs", "btn-sort-sel", "btn-sort-quick", "btn-undo", "btn-clear", "btn-sample"]
    .forEach((id) => { el(id).disabled = on; });
}

function layoutTree(node) {
  const items = [];
  let idx = 0;
  function walk(n, depth) {
    if (!n) return;
    walk(n.left, depth + 1);
    items.push({ node: n, x: idx++, y: depth });
    walk(n.right, depth + 1);
  }
  walk(node, 0);
  return items;
}

function renderTree(highlightSet = new Set(), newNode = null) {
  const svg = el("tree-svg");
  const empty = el("tree-empty");
  const wrap = el("tree-wrap");
  if (!root) {
    svg.innerHTML = "";
    svg.style.display = "none";
    empty.style.display = "block";
    return;
  }
  empty.style.display = "none";
  svg.style.display = "block";
  const items = layoutTree(root);
  const NODE_W = 44, NODE_H = 36, GAP_X = 56, GAP_Y = 72, PAD = 28;
  const width = Math.max(320, items.length * GAP_X + PAD * 2);
  const height = (Math.max(...items.map((i) => i.y)) + 1) * GAP_Y + PAD * 2 + NODE_H;
  svg.setAttribute("width", width);
  svg.setAttribute("height", height);
  svg.setAttribute("viewBox", `0 0 ${width} ${height}`);
  const pos = new Map();
  items.forEach((it) => pos.set(it.node, {
    px: PAD + it.x * GAP_X + NODE_W / 2,
    py: PAD + it.y * GAP_Y + NODE_H / 2,
    node: it.node,
    data: it.node.data,
  }));
  let html = "";
  function edges(n) {
    if (!n) return;
    const p = pos.get(n);
    if (n.left) {
      const c = pos.get(n.left);
      html += `<line class="edge" x1="${p.px}" y1="${p.py + 14}" x2="${c.px}" y2="${c.py - 14}" />`;
      edges(n.left);
    }
    if (n.right) {
      const c = pos.get(n.right);
      html += `<line class="edge" x1="${p.px}" y1="${p.py + 14}" x2="${c.px}" y2="${c.py - 14}" />`;
      edges(n.right);
    }
  }
  edges(root);
  items.forEach((it) => {
    const { px, py, data, node } = pos.get(it.node);
    let cls = "node-box";
    if (newNode === node) cls += " new-node";
    if (highlightSet.has(node)) cls += " visit";
    html += `<g><rect class="${cls}" x="${px - NODE_W / 2}" y="${py - NODE_H / 2}" width="${NODE_W}" height="${NODE_H}" rx="8"/>
      <text class="node-text" x="${px}" y="${py + 5}" text-anchor="middle">${data}</text></g>`;
  });
  svg.innerHTML = html;
  wrap.scrollLeft = (width - wrap.clientWidth) / 2;
}

function renderSortChips(values, state = {}) {
  const box = el("sort-chips");
  if (!values.length) { box.innerHTML = ""; return; }
  box.innerHTML = values.map((v, i) => {
    let c = "chip";
    if (state.pivotIdx === i) c += " pivot";
    else if (state.compare && (state.compare.i === i || state.compare.j === i)) c += " compare";
    else if (state.swap && (state.swap.i === i || state.swap.j === i)) c += " swap";
    return `<span class="${c}">${v}</span>`;
  }).join("");
}

function updateUI(opts = {}) {
  const inorder = [], bfs = bfsTraversal(root);
  inorderTraversal(root, inorder);
  el("out-inorder").textContent = inorder.length ? inorder.map((n) => n.data).join(" ") : "(empty)";
  el("out-bfs").textContent = bfs.length ? bfs.map((n) => n.data).join(" ") : "(empty)";
  el("out-array").textContent = arr.length ? "[" + arr.join(", ") + "]" : "[]";
  el("out-stack").textContent = stack.length ? "[" + [...stack].reverse().join(", ") + "]" : "[]";
  el("stat-nodes").textContent = countNodes(root);
  el("stat-array").textContent = arr.length;
  el("stat-stack").textContent = stack.length;
  if (!opts.skipTree) renderTree(new Set(), opts.newNode || null);
  if (!opts.skipChips && arr.length) renderSortChips(arr);
}

async function animateTraversal(nodes, label) {
  if (!nodes.length) { msg("Tree is empty"); return; }
  setAnimating(true);
  const visited = new Set();
  for (let i = 0; i < nodes.length; i++) {
    visited.add(nodes[i]);
    renderTree(visited);
    await sleep(380);
  }
  msg(label + ": " + nodes.map((n) => n.data).join(" "));
  setAnimating(false);
  renderTree();
}

el("btn-insert").addEventListener("click", async () => {
  if (animating) return;
  const v = Number(el("value").value);
  if (!Number.isFinite(v)) { msg("Enter a valid number"); return; }
  if (arr.length >= MAX) { msg("Memory full"); return; }
  root = insertBST(root, v);
  arr.push(v);
  push(v);
  el("value").value = "";
  const nodes = [];
  inorderTraversal(root, nodes);
  updateUI({ newNode: nodes.find((n) => n.data === v) });
  await sleep(400);
  updateUI();
  msg("Inserted " + v);
});

el("btn-dfs").addEventListener("click", async () => {
  const order = [];
  inorderTraversal(root, order);
  await animateTraversal(order, "DFS (inorder)");
});

el("btn-bfs").addEventListener("click", async () => {
  await animateTraversal(bfsTraversal(root), "BFS (level-order)");
});

el("btn-sort-sel").addEventListener("click", async () => {
  if (animating || !arr.length) { msg(arr.length ? "Wait…" : "No data"); return; }
  setAnimating(true);
  const copy = arr.slice();
  for (let i = 0; i < copy.length - 1; i++) {
    let min = i;
    for (let j = i + 1; j < copy.length; j++) {
      renderSortChips(copy, { compare: { i: min, j } });
      await sleep(100);
      if (copy[j] < copy[min]) min = j;
    }
    [copy[min], copy[i]] = [copy[i], copy[min]];
    renderSortChips(copy, { swap: { i, j: min } });
    await sleep(180);
  }
  el("out-sorted-sel").textContent = "[" + selectionSort(arr).join(", ") + "]";
  renderSortChips(copy);
  msg("Selection sort done");
  setAnimating(false);
});

el("btn-sort-quick").addEventListener("click", async () => {
  if (animating || !arr.length) { msg(arr.length ? "Wait…" : "No data"); return; }
  setAnimating(true);
  const sorted = await quickSortAnimated(arr, async (s) => {
    const st = { pivotIdx: s.pivotIdx };
    if (s.phase === "compare") st.compare = { i: s.i, j: s.j };
    if (s.phase === "swap") st.swap = { i: s.i, j: s.j };
    renderSortChips(s.arr, st);
    await sleep(120);
  });
  el("out-sorted-quick").textContent = "[" + sorted.join(", ") + "]";
  renderSortChips(sorted);
  msg("Quick sort done");
  setAnimating(false);
});

el("btn-undo").addEventListener("click", () => {
  if (animating) return;
  const v = pop();
  if (v === null) { msg("Nothing to undo"); return; }
  root = deleteNode(root, v);
  const i = arr.indexOf(v);
  if (i !== -1) arr.splice(i, 1);
  msg("Removed " + v);
  updateUI();
});

el("btn-clear").addEventListener("click", () => {
  if (animating) return;
  root = null;
  arr.length = 0;
  stack.length = 0;
  el("out-sorted-sel").textContent = "(not sorted)";
  el("out-sorted-quick").textContent = "(not sorted)";
  el("sort-chips").innerHTML = "";
  msg("Cleared");
  updateUI();
});

el("btn-sample").addEventListener("click", () => {
  if (animating) return;
  root = null;
  arr.length = 0;
  stack.length = 0;
  [50, 30, 70, 20, 40, 60, 80].forEach((v) => {
    root = insertBST(root, v);
    arr.push(v);
    push(v);
  });
  el("out-sorted-sel").textContent = "(not sorted)";
  el("out-sorted-quick").textContent = "(not sorted)";
  msg("Sample loaded");
  updateUI();
});

el("value").addEventListener("keydown", (e) => {
  if (e.key === "Enter") el("btn-insert").click();
});

updateUI();
