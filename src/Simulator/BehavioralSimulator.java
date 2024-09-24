package Simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BehavioralSimulator implements BSimulator{
    private final ArrayList<String> memory;
    private int line;
    private int counter;
    private final int[] reg = new int[8];
    FileWriter myWriter = new FileWriter("src\\output.txt");


    public BehavioralSimulator(ArrayList<String> memory) throws IOException {
        this.memory = memory;
        counter = 0;
        line = 0;
        Arrays.fill(reg, 0);
        printInitial();
    }

    private void printInitial() throws IOException {
        for (int i=0; i<memory.size() ; i++){
            String mem = extend32bits(memory.get(i));
            System.out.println("memory["+i+"]="+mem);
            myWriter.write("mem[ "+i+" ] "+mem+"\n");
        }
    }
    private void printState() throws IOException {
        System.out.println("@@@\n" +
                "state:\n" +
                "\tpc "+line+"\n" +
                "\tmemory:");
        myWriter.write("@@@\n" +
                "state:\n" +
                "\tpc "+line+"\n" +
                "\tinstructions "+counter+"\n" +
                "\tmemory:\n");
        for (int i=0; i<memory.size() ; i++){
            String mem = extend32bits(memory.get(i));
            System.out.println("\t\tmem[ "+i+" ] "+mem);
            myWriter.write("\t\tmem[ "+i+" ] "+mem+"\n");
        }
        System.out.println("\tregisters:");
        myWriter.write("\tregisters:\n");
        for (int i=0; i<reg.length ; i++){
            System.out.println("\t\treg[ "+i+" ] "+reg[i]);
            myWriter.write("\t\treg[ "+i+" ] "+reg[i]+"\n");
        }
        System.out.println("end state");
        myWriter.write("end state");
    }
    @Override
    public void run() throws IOException {
        run(0);
    }

    private void run(int line) throws IOException {
        printState();
        counter++;
        String MacCode = memory.get(line);
        String opCode = MacCode.substring(0,3);
        String regA = MacCode.substring(3,6);
        String regB = MacCode.substring(6,9);
        String destReg = MacCode.substring(22);
        String offsetField = MacCode.substring(9);
        if(opCode.equals("000")) add(regA,regB,destReg);
        if(opCode.equals("001")) nand(regA,regB,destReg);
        if(opCode.equals("010")) lw(regA,regB,offsetField);
        if(opCode.equals("011")) sw(regA,regB,offsetField);
        if(opCode.equals("100")) beq(regA,regB,offsetField);
        if(opCode.equals("101")) jalr(regA,regB);
        if(opCode.equals("110")) halt();
        if(opCode.equals("111")) noop();
    }

    private void add (String regA,String regB,String destReg) throws IOException { // R Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        int ValueD = Integer.parseInt(destReg,2);
        if(ValueD != 0) reg[ValueD] = reg[ValueA] + reg[ValueB];
        line++;
        run(line);
    }

    private void nand (String regA,String regB,String destReg) throws IOException { // R Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        int ValueD = Integer.parseInt(destReg,2);
        if(ValueD != 0) reg[ValueD] = ~(reg[ValueA] & reg[ValueB]); //nand
        line++;
        run(line);
    }

    private void lw (String regA,String regB,String offsetField) throws IOException { // I Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        int offset = convertNum(Integer.parseInt(offsetField,2));
        if(ValueB != 0) reg[ValueB] = convertNum(Integer.parseInt(memory.get(offset+reg[ValueA]),2));
        line++;
        run(line);
    }

    private void sw (String regA,String regB,String offsetField) throws IOException { // I Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        int offset = convertNum(Integer.parseInt(offsetField,2));
        String save = extendSignBit(reg[ValueB]);
        if (memory.size() <=  offset+reg[ValueA]) //extent memory size
            for(int i = memory.size();i<=offset+reg[ValueA];i++) memory.add("0");
        //int ValueM = convertNum(Integer.parseInt(memory.get(offset+reg[ValueA]),2));//find memory address
        memory.set(offset+reg[ValueA],save);
        line++;
        run(line);
    }

    private void beq (String regA,String regB,String offsetField) throws IOException { // I Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        int offset = convertNum(Integer.parseInt(offsetField,2));
        if(reg[ValueA] == reg[ValueB]) line = line + 1 + offset;
        else line++;
        run(line);
    }
    private void jalr(String regA , String regB) throws IOException { // J Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        if(ValueB != 0) reg[ValueB] = line + 1;
        if(ValueA == ValueB) line++;
        else line = reg[ValueA];
        run(line);
    }

    private void halt () throws IOException { // O Type
        line++;
        printFinal();
    }

    private void noop () throws IOException { // O Type
        line++;
        run(line);
    }

    private String extend32bits(String mem){
        StringBuilder sn = new StringBuilder();
        if(mem.length() == 25){
            sn.append("0000000");
        }else{
            if(mem.charAt(0) == '1') sn.append("1111111111111111");
            if(mem.charAt(0)=='0') sn.append("0000000000000000");
        }
        sn.append(mem);
        return sn.toString();
    }
    private String extendSignBit(int num){
        String s = Integer.toBinaryString(num);
        StringBuilder sn = new StringBuilder("0");
        if(s.length()==32) return s.substring(16); //ติดลบ
        else {
            sn.append("0".repeat(Math.max(0, 15 - s.length())));
        }
        sn.append(s);
        s = sn.toString();
        if(sn.toString().length() > 16){
            int n = sn.toString().length() - 16;
            s = s.substring(n);
        }
        return s;
    }

    private void printFinal() throws IOException {
        System.out.println("machine halted\n" +
                "total of "+ counter +" instructions executed\n" +
                "final state of machine:");
        myWriter.write("\nmachine halted\n" +
                "total of "+ counter +" instructions executed\n" +
                "final state of machine:\n");
        printState();
        myWriter.close();
    }

    private int convertNum(int num) {
        /* convert a 16-bit number into a 32-bit integer */
        if ((num & (1<<15)) > 0 ) {
            num -= (1<<16);
        }
        return num;
    }
}
