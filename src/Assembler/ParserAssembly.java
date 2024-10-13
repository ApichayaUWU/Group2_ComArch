package Assembler;

import Assembler.Tokenizer;

import java.util.ArrayList;
import java.util.List;
public class ParserAssembly {
    private Tokenizer tkz;
    private static final List<String> AssemblyCommand = CreateAssemblyCommand();
    private ArrayList<String> machineCode = new ArrayList<>();
    private String currentCode ;
    private ArrayList<String> label = new ArrayList<>();
    private ArrayList<Integer> address = new ArrayList<Integer>();

    public ParserAssembly(Tokenizer tkz) throws Exception {
        // receive Tokenizer tkz and initial currentCode for assign machine code to machineCode
        this.tkz = tkz;
        this.currentCode = "";
        //start read token and convert assembly language to machine code
        this.parseCode();
    }

    public ArrayList<String> getMachineCode(){
        //send ArrayList<String> machineCode that contain code in file
        return machineCode;
    }
    public void parseCode() throws Exception{
        //read token if file not empty
        if(tkz.hasNextToken()){
            this.parseInstruction();
        }
    }

    public void parseInstruction() throws Exception{
        //read token until last token
        while(tkz.hasNextToken()) {
            if (AssemblyCommand.contains(tkz.peek())) {
                //if token is command in instruction name in assembly language
                parseCommand();
            }else {
                //if token is not command assume this token is label
                parseLabel();
            }
        }
        //when last token fill label with address follow syntax of assembly language
        fillLabel();
    }

    public void parseCommand() throws Exception{
        //add opcode to bit 22-24 in currentCode of each name of instruction
        if(tkz.peek().equals("add")){
            currentCode = "000";
            tkz.consume();
            for(int i=0; i<3; i++){
                //add regA regB destReg follow assembly language
                parseReg();
            }
            //add bit 3-15 with 0
            parseGapRType();
            //ignore token After this, until the end of the line
            parseComment();
        }else if(tkz.peek().equals("nand")){
            currentCode = "001";
            tkz.consume();
            for(int i=0; i<3; i++){
                //add regA regB destReg follow assembly language
                parseReg();
            }
            //add bit 3-15 with 0
            parseGapRType();
            //ignore token After this, until the end of the line
            parseComment();
        }else if(tkz.peek().equals("lw")){
            currentCode = "010";
            tkz.consume();
            //add regA regB offsetField follow assembly language
            for(int i=0; i<2; i++) {
                parseReg();
            }
            parseOffsetField();
            parseComment();
        }else if(tkz.peek().equals("sw")){
            currentCode = "011";
            tkz.consume();
            //add regA regB offsetField follow assembly language
            for(int i=0; i<2; i++) {
                parseReg();
            }
            parseOffsetField();
            parseComment();
        }else if(tkz.peek().equals("beq")){
            currentCode = "100";
            tkz.consume();
            //add regA regB offsetField follow assembly language
            for(int i=0; i<2; i++) {
                parseReg();
            }
            parseOffsetField();
            parseComment();
        }else if(tkz.peek().equals("jalr")){
            currentCode = "101";
            tkz.consume();
            //add regA regB follow assembly language
            for(int i=0; i<2; i++){
                parseReg();
            }
            parseComment();
            //add bit 0-15 with 0
            parseGapJType();
        }else if(tkz.peek().equals("halt")){
            currentCode = "110";
            tkz.consume();
            parseComment();
            //add bit 0-21 with 0
            parseGapOType();
        }else if(tkz.peek().equals("noop")){
            currentCode = "111";
            tkz.consume();
            parseComment();
            //add bit 0-21 with 0
            parseGapOType();
        }
    }

    public void parseLabel() throws Exception{
        // check label duplicate
        if(!label.contains(tkz.peek())){
            //add to label for remember declare label
            label.add(tkz.peek());
            //add to address for remember address in this code
            address.add(machineCode.size());
            tkz.consume();
            if(tkz.peek().equals(".fill")){
                //command fill value in label
                tkz.consume();
                parseFill();
            }else if(AssemblyCommand.contains(tkz.peek())){
                //if next token is command go to read command
                parseCommand();
            }else{
                //when syntax is error use command that not in syntax
                throw new Exception("command not found");
            }
        }else{
            //declare repeat label
            throw new Exception("repeat label");
        }
    }

    public void parseFill() throws Exception{
        if(isNumber(tkz.peek())) {
            int num = Integer.parseInt(tkz.peek());
            if (num <= 32767 && num >= -32768) {
                //change value from decimal to binary
                String value = TwosComplement(num);
                //add it in code
                currentCode = currentCode + value;
                machineCode.add(currentCode);
                currentCode = "";
                tkz.consume();
                parseComment();
            }else{
                //check if number is over 15 bit
                throw new Exception("number not match");
            }
        } else {
            //add label to code
            currentCode = currentCode + tkz.consume();
            machineCode.add(currentCode);
            currentCode = "";
            parseComment();

        }
    }
    public void parseReg() throws Exception{
        //check is it register
        if(isNumber(tkz.peek())){
            //check register must be positive
            if(tkz.peek().charAt(0) == '-'){
                throw new Exception("incorrect reg");
            }else {
                //change register to binary
                int reg = Integer.parseInt(tkz.peek());
                //check if register is exist
                if(reg <= 7){
                    String r = FillBinary(reg);
                    currentCode = currentCode + r;
                    tkz.consume();
                }else{
                    throw new Exception("wrong register");
                }
            }
        }else{
            throw new Exception("register must be number");
        }
    }

    public void parseOffsetField() throws Exception{
        //check offsetField is number or label
        if(isNumber(tkz.peek())){
            int num = Integer.parseInt(tkz.peek());
            //check is number over 15 bit
            if(num <= 32767 && num >= -32768){
                //change number to binary
                String offset = TwosComplement(num);
                //add code in machineCode list
                currentCode = currentCode + offset;
                machineCode.add(currentCode);
                currentCode = "";
                tkz.consume();
            }else{
                throw new Exception("can not use this number");
            }
        }else {
            //add label to code and add code
            currentCode = currentCode + tkz.peek();
            machineCode.add(currentCode);
            currentCode = "";
            tkz.consume();
        }
    }

    public void fillLabel() throws Exception{
        //start at address 0 for fill label with address follow syntax
        int pc = 0;
        while(pc < machineCode.size()){
            int index = 0;
            String code = machineCode.get(pc);
            while(index < label.size()){
                String var = label.get(index);
                //check if this address is for command or for assign in label
                if(Character.isDigit(code.charAt(0))){
                    //select specific label "string" that add before
                    String variable = code.substring(9);
                    //check is this line machineCode have label in declare label
                    if(variable.equals(var)){
                        //replace label with address in from machine code
                        int adLabel = address.get(index);
                        //if case J type or I type
                        if (code.charAt(0) == '1') {
                            int offsetNum = adLabel-(pc+1);
                            String offset = TwosComplement(offsetNum);
                            machineCode.set(pc,code.replace(var, offset));
                        } else if(code.charAt(0) == '0') {
                            String offset = TwosComplement(adLabel);
                            machineCode.set(pc,code.replace(var, offset));
                        }
                    }
                }else{
                    //case assign label with another label
                    if(code.equals(var)){
                        int adLabel = address.get(index);
                        String offset = TwosComplement(adLabel);
                        machineCode.set(pc,code.replace(var, offset));
                    }
                }
                index++;
            }
            code = machineCode.get(pc);
            if(!isNumber(code)){
                throw new Exception("use undefine label");
            }
            pc++;
        }

    }
    public void parseGapRType() throws Exception{
        //add 0 for R type
        StringBuilder code = new StringBuilder(currentCode);
        code.insert(9,"0000000000000");
        currentCode = code.toString();
        machineCode.add(currentCode);
        currentCode = "";
    }

    public void parseGapJType() throws Exception{
        //add 0 for J type
        currentCode = currentCode + "0000000000000000";
        machineCode.add(currentCode);
        currentCode = "";
    }

    public void parseGapOType() throws Exception{
        //add 0 for 0 type
        currentCode = currentCode + "0000000000000000000000";
        machineCode.add(currentCode);
//        System.out.println("what add 0 type" + currentCode);
        currentCode = "";
    }

    public void parseComment() throws Exception{
        //delete token command
        while (!tkz.peek().equals("!")){
            tkz.consume();
        }
        tkz.consume();
    }

    private boolean isNumber(String str) {
        //check string is number
        int size = str.length();
        if((str.charAt(0) == '-')||Character.isDigit(str.charAt(0))) {
            for (int i = 1; i < size; i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }
        }else{
            return false;
        }
        return true;
    }
    private static List<String> CreateAssemblyCommand(){
        //add list of command that usable
        List<String> list = new ArrayList<>();
        list.add("add");
        list.add("nand");
        list.add("lw");
        list.add("sw");
        list.add("beq");
        list.add("jalr");
        list.add("halt");
        list.add("noop");
        return list;
    }

    public String FillBinary(Integer number) throws Exception{
        //fill binary number with 0 for correct syntax
        StringBuilder binaryString = new StringBuilder(Integer.toBinaryString(number));
        int size = binaryString.length();
        if(size < 3){
            int i = 3 - size;
            while (i > 0){
                binaryString.insert(0, "0");
                i--;
            }
        }else if(size > 3) {
            throw new Exception("incorrect reg");
        }
        return binaryString.toString();
    }

    public String TwosComplement(Integer number)throws Exception{
        //function for change decimal to binary
        if(number >= 0){
            StringBuilder binaryString = new StringBuilder(Integer.toBinaryString(number));
            int size = binaryString.length();
            if(size < 16){
                int i =16 - size;
                while (i > 0){
                    binaryString.insert(0, "0");
                    i--;
                }
            }else if(size > 16) {
                throw new Exception("incorrect offsetField");
            }
            return binaryString.toString();
        }else{
            return Integer.toBinaryString(number).substring(16);
        }
    }
    public void print(){
        //function for print check
        System.out.println(label.size());
        System.out.println(label);
        System.out.println(address.size());
        System.out.println(address);
        System.out.println(machineCode.size());
        System.out.println(machineCode);
    }

}
