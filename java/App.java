import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/** UI shell — all logic is in {@link Engine}. */
public class App extends JFrame {

    private final Engine engine = new Engine();
    private final TreePanel treePanel = new TreePanel(engine);
    private final JTextArea output = new JTextArea(6, 40);
    private final JTextField valueField = new JTextField(8);
    private boolean animating;

    public App() {
        super("Data Processing Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(15, 23, 36));
        setLayout(new BorderLayout(8, 8));

        JLabel title = new JLabel("Java core logic · HTML loader only · BST / DFS / BFS / Sorts");
        title.setForeground(new Color(230, 238, 248));
        title.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12));
        add(title, BorderLayout.NORTH);

        add(treePanel, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(8, 8));
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        controls.setOpaque(false);
        addBtn(controls, "Insert", e -> doInsert());
        controls.add(valueField);
        addBtn(controls, "DFS", e -> animate(engine.inorderNodes(), "DFS (inorder)"));
        addBtn(controls, "BFS", e -> animate(engine.bfsNodes(), "BFS (level-order)"));
        addBtn(controls, "Selection Sort", e -> log("Selection: " + Arrays.toString(engine.selectionSort())));
        addBtn(controls, "Quick Sort", e -> log("Quick: " + Arrays.toString(engine.quickSort())));
        addBtn(controls, "Undo", e -> doUndo());
        addBtn(controls, "Sample", e -> { engine.loadSample(); refresh("Sample loaded"); });
        addBtn(controls, "Clear", e -> { engine.clear(); treePanel.clearHighlight(); refresh("Cleared"); });
        south.add(controls, BorderLayout.NORTH);

        output.setEditable(false);
        output.setBackground(new Color(11, 18, 32));
        output.setForeground(new Color(148, 163, 184));
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        south.add(output, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        setSize(920, 620);
        setLocationRelativeTo(null);
        refresh("Ready.");
    }

    private void addBtn(JPanel p, String text, java.awt.event.ActionListener a) {
        JButton b = new JButton(text);
        b.setBackground(new Color(97, 218, 251));
        b.setForeground(new Color(5, 32, 50));
        b.addActionListener(a);
        p.add(b);
    }

    private void doInsert() {
        if (animating) return;
        try {
            int v = Integer.parseInt(valueField.getText().trim());
            if (!engine.insert(v)) { log("Memory full"); return; }
            valueField.setText("");
            treePanel.clearHighlight();
            refresh("Inserted " + v);
        } catch (NumberFormatException ex) {
            log("Enter a valid integer");
        }
    }

    private void doUndo() {
        if (animating) return;
        Integer v = engine.undo();
        if (v == null) log("Nothing to undo");
        else { treePanel.clearHighlight(); refresh("Removed " + v); }
    }

    private void animate(List<TreeNode> order, String label) {
        if (animating || order.isEmpty()) {
            log(order.isEmpty() ? "Tree is empty" : "Wait…");
            return;
        }
        animating = true;
        final int[] i = {0};
        Timer t = new Timer(400, e -> {
            if (i[0] < order.size()) {
                HashSet<TreeNode> set = new HashSet<>();
                for (int j = 0; j <= i[0]; j++) set.add(order.get(j));
                treePanel.setHighlighted(set);
                i[0]++;
            } else {
                ((Timer) e.getSource()).stop();
                log(label + ": " + order.stream().map(n -> String.valueOf(n.data)).collect(Collectors.joining(" ")));
                animating = false;
                treePanel.clearHighlight();
                treePanel.repaint();
            }
        });
        t.start();
    }

    private void refresh(String msg) {
        treePanel.repaint();
        output.setText(msg + "\n\nDFS:  " + engine.inorderValues() + "\nBFS:  " + engine.bfsValues()
                + "\nArray: " + engine.arr);
    }

    private void log(String msg) {
        output.setText(msg + "\n\n" + output.getText());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}
