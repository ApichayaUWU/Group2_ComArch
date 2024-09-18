import Simulator.BSimulator;
import Simulator.BehavioralSimulator;

import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Opt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        // Press Ctrl+R or click the green arrow button in the gutter to run the code.
        for (int i = 1; i <= 5; i++) {

            // Press Ctrl+D to start debugging your code. We have set one breakpoint
            // for you, but you can always add more by pressing Cmd+F8.
            System.out.println("i = " + i);
        }
        ArrayList<String> memory = new ArrayList<>();
        memory.add("0100000010000000000000111");
        memory.add("0100010100000000000000011");
        memory.add("0000010100000000000000001");
        memory.add("1000000010000000000000010");
        memory.add("1000000001111111111111101");
        memory.add("1110000000000000000000000");
        memory.add("1100000000000000000000000");
        memory.add("0000000000000101");
        memory.add("1111111111111111");
        memory.add("0000000000000010");
        BSimulator sim = new BehavioralSimulator(memory);
        sim.run();
    }
}