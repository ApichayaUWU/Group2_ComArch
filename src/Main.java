import Simulator.BSimulator;
import Simulator.BehavioralSimulator;

import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {
//        ReadAssembly RA = new ReadFile("src\\Combination.txt");
        ReadAssembly RA = new ReadFile("src\\fib.txt");
//        ReadAssembly RA = new ReadFile("src\\Multiply.txt");
        ArrayList<String> MC = RA.getMachineCode();
//        BSimulator BS = new BehavioralSimulator(MC,"src\\CombinationOutput.txt");
        BSimulator BS = new BehavioralSimulator(MC,"src\\fibOutput.txt");
//        BSimulator BS = new BehavioralSimulator(MC,"src\\MultiplyOutput.txt");
        BS.run();
        String s = Integer.toBinaryString(-5);
        System.out.println(s);
    }
}