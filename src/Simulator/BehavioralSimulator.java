package Simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BehavioralSimulator implements BSimulator {
    private final ArrayList<String> memory; // Memory storage for the machine
    private int line; // Program counter to keep track of the current line being executed
    private int counter; // Instruction execution count
    private final int[] reg = new int[8]; // Array to represent 8 registers
    FileWriter myWriter; // File writer for logging output

    // Constructor to initialize memory and setup file writer
    public BehavioralSimulator(ArrayList<String> memory, String src) throws IOException {
        this.memory = memory;
        myWriter = new FileWriter(src); // Initialize file writer for output
        counter = 0;
        line = 0;
        Arrays.fill(reg, 0); // Initialize all registers to zero
        printInitial(); // Print the initial state of the memory
    }

    // Method to print the initial memory state
    private void printInitial() throws IOException {
        for (int i = 0; i < memory.size(); i++) {
            String mem = extend32bits(memory.get(i));
            System.out.println("memory[" + i + "]=" + mem);
            myWriter.write("mem[ " + i + " ] " + mem + "\n");
        }
    }

    // Method to print the current state of the machine
    private void printState() throws IOException {
        System.out.println("@@@\n" +
                "state:\n" +
                "\tpc " + line + "\n" +
                "\tmemory:");
        myWriter.write("@@@\n" +
                "state:\n" +
                "\tpc " + line + "\n" +
                "\tmemory:\n");
        for (int i = 0; i < memory.size(); i++) {
            String mem = extend32bits(memory.get(i));
            System.out.println("\t\tmem[ " + i + " ] " + mem);
            myWriter.write("\t\tmem[ " + i + " ] " + mem + "\n");
        }
        System.out.println("\tregisters:");
        myWriter.write("\tregisters:\n");
        for (int i = 0; i < reg.length; i++) {
            System.out.println("\t\treg[ " + i + " ] " + reg[i]);
            myWriter.write("\t\treg[ " + i + " ] " + reg[i] + "\n");
        }
        System.out.println("end state");
        myWriter.write("end state");
    }

    // Main method to run the simulator
    @Override
    public void run() throws IOException {
        run(0); // Start execution at line 0
    }

    // Method to run the simulator from a given line
    private void run(int line) throws IOException {
        printState(); // Print the current state
        counter++; // Increment instruction count
        String MacCode = memory.get(line); // Get the current machine code
        String opCode = MacCode.substring(0, 3); // Extract the operation code
        String regA = MacCode.substring(3, 6); // Extract register A
        String regB = MacCode.substring(6, 9); // Extract register B
        String destReg = MacCode.substring(22); // Extract destination register
        String offsetField = MacCode.substring(9); // Extract offset for I-type instructions

        // Decode and execute the instruction based on the opcode
        if (opCode.equals("000")) add(regA, regB, destReg);
        if (opCode.equals("001")) nand(regA, regB, destReg);
        if (opCode.equals("010")) lw(regA, regB, offsetField);
        if (opCode.equals("011")) sw(regA, regB, offsetField);
        if (opCode.equals("100")) beq(regA, regB, offsetField);
        if (opCode.equals("101")) jalr(regA, regB);
        if (opCode.equals("110")) halt();
        if (opCode.equals("111")) noop();
    }

    // R-Type add instruction: reg[destReg] = reg[regA] + reg[regB]
    private void add(String regA, String regB, String destReg) throws IOException {
        int ValueA = Integer.parseInt(regA, 2);
        int ValueB = Integer.parseInt(regB, 2);
        int ValueD = Integer.parseInt(destReg, 2);
        if (ValueD != 0) reg[ValueD] = reg[ValueA] + reg[ValueB];
        line++;
        run(line);
    }

    // R-Type nand instruction: reg[destReg] = ~(reg[regA] & reg[regB])
    private void nand(String regA, String regB, String destReg) throws IOException {
        int ValueA = Integer.parseInt(regA, 2);
        int ValueB = Integer.parseInt(regB, 2);
        int ValueD = Integer.parseInt(destReg, 2);
        if (ValueD != 0) reg[ValueD] = ~(reg[ValueA] & reg[ValueB]); // nand operation
        line++;
        run(line);
    }

    // I-Type load word instruction: reg[regB] = memory[offset + reg[regA]]
    private void lw(String regA, String regB, String offsetField) throws IOException {
        int ValueA = Integer.parseInt(regA, 2);
        int ValueB = Integer.parseInt(regB, 2);
        int offset = convertNum(Integer.parseInt(offsetField, 2));
        if (ValueB != 0) reg[ValueB] = convertNum(Integer.parseInt(memory.get(offset + reg[ValueA]), 2));
        line++;
        run(line);
    }

    // I-Type store word instruction: memory[offset + reg[regA]] = reg[regB]
    private void sw(String regA, String regB, String offsetField) throws IOException {
        int ValueA = Integer.parseInt(regA, 2);
        int ValueB = Integer.parseInt(regB, 2);
        int offset = convertNum(Integer.parseInt(offsetField, 2));
        String save = extendSignBit(reg[ValueB]);
        // Extend memory if the offset exceeds current memory size
        if (memory.size() <= offset + reg[ValueA])
            for (int i = memory.size(); i <= offset + reg[ValueA]; i++) memory.add("0");
        memory.set(offset + reg[ValueA], save); // Store the value
        line++;
        run(line);
    }

    // I-Type branch equal instruction: if reg[regA] == reg[regB], branch to line + offset
    private void beq(String regA, String regB, String offsetField) throws IOException {
        int ValueA = Integer.parseInt(regA, 2);
        int ValueB = Integer.parseInt(regB, 2);
        int offset = convertNum(Integer.parseInt(offsetField, 2));
        if (reg[ValueA] == reg[ValueB]) line = line + 1 + offset;
        else line++;
        run(line);
    }

    // J-Type jump and link register instruction: reg[regB] = line + 1, line = reg[regA]
    private void jalr(String regA, String regB) throws IOException {
        int ValueA = Integer.parseInt(regA, 2);
        int ValueB = Integer.parseInt(regB, 2);
        if (ValueB != 0) reg[ValueB] = line + 1;
        if (ValueA == ValueB) line++;
        else line = reg[ValueA];
        run(line);
    }

    // O-Type halt instruction: Stop execution and print final state
    private void halt() throws IOException {
        line++;
        printFinal();
    }

    // O-Type no operation instruction: Do nothing
    private void noop() throws IOException {
        line++;
        run(line);
    }

    // Extend memory address to 32 bits for display
    private String extend32bits(String mem) {
        StringBuilder sn = new StringBuilder();
        if (mem.length() == 25) {
            sn.append("0000000");
        } else {
            if (mem.charAt(0) == '1') sn.append("1111111111111111");
            if (mem.charAt(0) == '0') sn.append("0000000000000000");
        }
        sn.append(mem);
        return sn.toString();
    }

    // Convert a 32-bit signed integer to a 16-bit binary representation
    private String extendSignBit(int num) {
        String s = Integer.toBinaryString(num);
        StringBuilder sn = new StringBuilder("0");
        if (s.length() == 32) return s.substring(16);
        else {
            sn.append("0".repeat(Math.max(0, 15 - s.length())));
        }
        sn.append(s);
        s = sn.toString();
        if (sn.toString().length() > 16) {
            int n = sn.toString().length() - 16;
            s = s.substring(n);
        }
        return s;
    }

    // Print the final state of the machine after halting
    private void printFinal() throws IOException {
        System.out.println("machine halted\n" +
                "total of " + counter + " instructions executed\n" +
                "final state of machine:");
        myWriter.write("\nmachine halted\n" +
                "total of " + counter + " instructions executed\n" +
                "final state of machine:\n");
        printState(); // Print the state one last time
        myWriter.close(); // Close the file writer
    }

    // Convert a 16-bit number into a 32-bit integer (sign-extend)
    private int convertNum(int num) {
        if ((num & (1 << 15)) > 0) { // Check if the sign bit is set
            num -= (1 << 16); // If so, make the number negative
        }
        return num;
    }
}
