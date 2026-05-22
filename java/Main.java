import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final Engine engine = new Engine();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n DATA PROCESSING ENGINE ");
            System.out.println("1. Insert");
            System.out.println("2. DFS (inorder)");
            System.out.println("3. BFS (level-order)");
            System.out.println("4. Selection sort");
            System.out.println("5. Quick sort");
            System.out.println("6. Undo");
            System.out.println("7. Sample data");
            System.out.println("8. Exit");
            System.out.print("Choice: ");

            if (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.println("Invalid input.");
                continue;
            }

            switch (sc.nextInt()) {
                case 1 -> {
                    System.out.print("Value: ");
                    if (engine.insert(sc.nextInt())) System.out.println("Inserted.");
                    else System.out.println("Full.");
                }
                case 2 -> print("DFS", engine.inorderValues());
                case 3 -> print("BFS", engine.bfsValues());
                case 4 -> System.out.println("Selection: " + Arrays.toString(engine.selectionSort()));
                case 5 -> System.out.println("Quick: " + Arrays.toString(engine.quickSort()));
                case 6 -> {
                    Integer v = engine.undo();
                    System.out.println(v == null ? "Nothing to undo." : "Removed " + v);
                }
                case 7 -> {
                    engine.loadSample();
                    System.out.println("Sample loaded.");
                }
                case 8 -> {
                    System.out.println("Bye.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void print(String label, java.util.List<Integer> values) {
        System.out.println(label + ": " + (values.isEmpty() ? "(empty)" : values));
    }
}
