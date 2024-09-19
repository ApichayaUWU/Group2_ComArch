package Simulator;

import java.util.ArrayList;

public class BehavioralSimulator implements BSimulator{
    private ArrayList<String> memory = new ArrayList<String>();
    private int line;
    private int couter;
    private int[] reg = new int[8];


    public BehavioralSimulator(ArrayList<String> memory){
        this.memory = memory;
        couter = 0;
        line = 0;
        for (int i=0; i< reg.length ; i++){
            reg[i] = 0;
        }
        printInitial();
    }

    private void printInitial(){
        for (int i=0; i<memory.size() ; i++){
            //System.out.println("memory["+i+"]="+memory.get(i));
            System.out.println("memory["+i+"]="+Integer.parseInt(memory.get(i),2));
        }
    }
    private void printState(){
        System.out.println("@@@\n" +
                "state:\n" +
                "\tpc "+line+"\n" +
                "\tmemory:");
        for (int i=0; i<memory.size() ; i++){
            //System.out.println("\t\tmem[ "+i+" ] "+memory.get(i));
            System.out.println("\t\tmem[ "+i+" ] "+Integer.parseInt(memory.get(i),2));
        }
        System.out.println("\tregisters:");
        for (int i=0; i<reg.length ; i++){
            System.out.println("\t\treg[ "+i+" ] "+reg[i]);
        }
        System.out.println("end state");
    }
    @Override
    public void run() {
        run(0);
    }

    private void run(int line){
        printState();
        couter++;
        String MacCode = memory.get(line);
        String opCode = MacCode.substring(0,3);
        String regA = MacCode.substring(3,6);
        String regB = MacCode.substring(6,9);
        String destReg = MacCode.substring(22);;
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

    private void add (String regA,String regB,String destReg){ // R Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        int ValueD = Integer.parseInt(destReg,2);
        reg[ValueD] = reg[ValueA] + reg[ValueB];
        line++;
        run(line);
    }

    private void nand (String regA,String regB,String destReg){ // R Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        int ValueD = Integer.parseInt(destReg,2);
        reg[ValueD] = ~(reg[ValueA] & reg[ValueB]); //nand
        line++;
        run(line);
    }

    private void lw (String regA,String regB,String offsetField){ // I Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        int offset = binaryStringToInteger(offsetField);
        reg[ValueB] = binaryStringToInteger(memory.get(offset+reg[ValueA]));
        line++;
        run(line);
    }

    private void sw (String regA,String regB,String offsetField){ // I Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        int offset = binaryStringToInteger(offsetField);
        String save = Integer.toBinaryString(reg[ValueB]);
        int ValueM = binaryStringToInteger(memory.get(offset+reg[ValueA])); //find memory address
        if (memory.size() <=  ValueM) //extent memory size
            for(int i = memory.size();i<=ValueM;i++) memory.add("0");
        memory.set(ValueM,save);
        line++;
        run(line);
    }

    private void beq (String regA,String regB,String offsetField){ // I Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        int offset = binaryStringToInteger(offsetField);
        if(reg[ValueA] == reg[ValueB]) line = line + 1 + offset;
        else line++;
        run(line);
    }
    private void jalr(String regA , String regB){ // J Type
        int ValueA = Integer.parseInt(regA,2);
        int ValueB = Integer.parseInt(regB,2);
        reg[ValueB] = line + 1;
        if(ValueA == ValueB) line++;
        else line = reg[ValueA];
        run(line);
    }

    private void halt (){ // O Type
        line++;
        printFinal();
    }

    private void noop (){ // O Type
        line++;
        run(line);
    }

    private void printFinal(){
        System.out.println("machine halted\n" +
                "total of "+couter+" instructions executed\n" +
                "final state of machine:");
        printState();
    }

    private int binaryStringToInteger(String binaryString) {
        // ตรวจสอบว่าเป็นจำนวนลบหรือไม่
        if (binaryString.charAt(0) == '1') {
            // กลับบิตทั้งหมด
            String inverted = binaryString.replace('0', '2').replace('1', '0').replace('2', '1');
            // บวก 1
            int decimal = Integer.parseInt(inverted, 2) + 1;
            // ทำค่าให้เป็นลบ
            return -decimal;
        } else {
            // ถ้าเป็นจำนวนบวก แปลงตามปกติ
            return Integer.parseInt(binaryString, 2);
        }
    }
}
