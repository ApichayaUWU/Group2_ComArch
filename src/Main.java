import Simulator.BSimulator;
import Simulator.BehavioralSimulator;

import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {
        ReadAssembly RA = new ReadFile("src\\tonnamTest.txt");
        ArrayList<String> MC = RA.getMachineCode();
//        MC.set(3,"0001110010000000000000111");
//        BSimulator BS = new BehavioralSimulator(MC);
//        BS.run();
    }
}