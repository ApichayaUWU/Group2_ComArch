import Assembler.ReadAssembly;
import Assembler.ReadFile;
import Simulator.BSimulator;
import Simulator.BehavioralSimulator;

import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {
        ReadAssembly RA = new ReadFile("src\\divide.txt");
//        Assembler.ReadAssembly RA = new Assembler.ReadFile("src\\Multiply.txt");
        ArrayList<String> MC = RA.getMachineCode();
//        Assembler.ReadAssembly RA = new Assembler.ReadFile("src\\Multiply.txt");

        BSimulator BS = new BehavioralSimulator(MC,"src\\divideOutput.txt");
//        BSimulator BS = new BehavioralSimulator(MC,"src\\MultiplyOutput.txt");
        BS.run();
    }
}