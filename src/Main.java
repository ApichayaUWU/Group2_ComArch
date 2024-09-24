import Simulator.BSimulator;
import Simulator.BehavioralSimulator;

import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {
        ReadAssembly RA = new ReadFile("src\\Combination.txt");
//        ReadAssembly RA = new ReadFile("src\\Multiply.txt");
//        ReadAssembly RA = new ReadFile("src\\example.txt");
        ArrayList<String> MC = RA.getMachineCode();
//        BSimulator BS = new BehavioralSimulator(MC,"src\\MultiplyOutput.txt");
        BSimulator BS = new BehavioralSimulator(MC,"src\\CombinationOutput.txt");
        BS.run();
    }
}