package Simulator;

import java.util.ArrayList;

public class BehavioralSimulator implements BSimulator{
    private ArrayList<String> memory = new ArrayList<String>();
    private int couter;
    private int[] reg = new int[8];

    public BehavioralSimulator(ArrayList<String> memory){
        this.memory = memory;
        couter = 0;
        for (int i=0; i< reg.length ; i++){
            reg[i] = 0;
        }
        printInitial();
    }

    private void printInitial(){
        for (int i=0; i<memory.size() ; i++){
            System.out.println("memory["+i+"]="+memory.get(i));
        }
    }
    private void printState(){

    }
    @Override
    public void run() {
        run(0);
    }

    private void run(int line){
        String MacCode = memory.get(line);
        String opCode = MacCode.substring(0,3);
        String regA = MacCode.substring(3,6);
        String regB = MacCode.substring(6,9);
        String destReg = MacCode.substring(22);;
        String offsetField = MacCode.substring(9);

    }

    private void add (String regA,String regB,String destReg){ // R Type

    }

    private void nand (String regA,String regB,String destReg){ // R Type

    }

    private void lw (String regA,String regB,String offsetField){ // I Type

    }

    private void sw (String regA,String regB,String offsetField){ // I Type

    }

    private void beq (String regA,String regB,String offsetField){ // I Type

    }

    private void halt (){ // O Type

    }

    private void noop (){ // O Type

    }
}
