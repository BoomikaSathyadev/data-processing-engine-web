import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

public class TreePanel extends JPanel {

    private static final int NODE_W = 44, NODE_H = 36, GAP_X = 56, GAP_Y = 72, PAD = 28;

    private final Engine engine;
    private Set<TreeNode> highlighted = new HashSet<>();

    public TreePanel(Engine engine) {
        this.engine = engine;
        setBackground(new Color(11, 18, 32));
        setPreferredSize(new Dimension(640, 280));
    }

    public void setHighlighted(Set<TreeNode> nodes) {
        highlighted = nodes != null ? new HashSet<>(nodes) : new HashSet<>();
        repaint();
    }

    public void clearHighlight() {
        highlighted.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (engine.root == null) {
            g2.setColor(new Color(148, 163, 184));
            g2.setFont(getFont().deriveFont(13f));
            String msg = "Insert values or load sample";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
            g2.dispose();
            return;
        }

        List<LayoutItem> items = layout(engine.root);
        Map<TreeNode, LayoutItem> pos = new HashMap<>();
        for (LayoutItem it : items) pos.put(it.node, it);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(148, 163, 184, 120));
        drawEdges(g2, engine.root, pos);

        for (LayoutItem it : items) {
            int px = PAD + it.x * GAP_X + NODE_W / 2;
            int py = PAD + it.y * GAP_Y + NODE_H / 2;
            int rx = px - NODE_W / 2, ry = py - NODE_H / 2;
            if (highlighted.contains(it.node)) {
                g2.setColor(new Color(19, 78, 74));
                g2.fillRoundRect(rx, ry, NODE_W, NODE_H, 10, 10);
                g2.setColor(new Color(52, 211, 153));
            } else {
                g2.setColor(new Color(11, 18, 32));
                g2.fillRoundRect(rx, ry, NODE_W, NODE_H, 10, 10);
                g2.setColor(new Color(97, 218, 251));
            }
            g2.drawRoundRect(rx, ry, NODE_W, NODE_H, 10, 10);
            g2.setColor(new Color(230, 238, 248));
            g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
            String s = String.valueOf(it.node.data);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(s, px - fm.stringWidth(s) / 2, py + fm.getAscent() / 2 - 2);
        }
        g2.dispose();
    }

    private void drawEdges(Graphics2D g2, TreeNode node, Map<TreeNode, LayoutItem> pos) {
        if (node == null) return;
        LayoutItem p = pos.get(node);
        int px = PAD + p.x * GAP_X + NODE_W / 2;
        int py = PAD + p.y * GAP_Y + NODE_H / 2;
        if (node.left != null) {
            LayoutItem c = pos.get(node.left);
            g2.drawLine(px, py + 14, PAD + c.x * GAP_X + NODE_W / 2, PAD + c.y * GAP_Y + NODE_H / 2 - 14);
            drawEdges(g2, node.left, pos);
        }
        if (node.right != null) {
            LayoutItem c = pos.get(node.right);
            g2.drawLine(px, py + 14, PAD + c.x * GAP_X + NODE_W / 2, PAD + c.y * GAP_Y + NODE_H / 2 - 14);
            drawEdges(g2, node.right, pos);
        }
    }

    private List<LayoutItem> layout(TreeNode root) {
        List<LayoutItem> items = new ArrayList<>();
        walk(root, 0, items, new int[] {0});
        return items;
    }

    private void walk(TreeNode node, int depth, List<LayoutItem> items, int[] counter) {
        if (node == null) return;
        walk(node.left, depth + 1, items, counter);
        items.add(new LayoutItem(node, counter[0]++, depth));
        walk(node.right, depth + 1, items, counter);
    }

    private static class LayoutItem {
        final TreeNode node;
        final int x, y;
        LayoutItem(TreeNode node, int x, int y) { this.node = node; this.x = x; this.y = y; }
    }
}
