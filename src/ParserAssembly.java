
import java.util.ArrayList;
import java.util.List;
public class ParserAssembly {
    private Tokenizer tkz;
    private static final List<String> AssemblyCommand = CreateAssemblyCommand();
    private ArrayList<String> machineCode = new ArrayList<>();
    private String currentCode ;
    private ArrayList<String> label = new ArrayList<>();
    private ArrayList<Integer> address = new ArrayList<Integer>();

    public ParserAssembly(Tokenizer tkz)  {
        this.tkz = tkz;
        this.currentCode = "";
    }

    public ArrayList<String> getMachineCode(){
        return machineCode;
    }
    public void parseCode() throws Exception{
        if(tkz.hasNextToken()){
            this.parseInstruction();
        }
    }

    public void parseInstruction() throws Exception{
        int i=0;
        while(tkz.hasNextToken()) {
            if (AssemblyCommand.contains(tkz.peek())) {
//                System.out.println("command   " + tkz.peek() +"  " + i++);
                parseCommand();
            }else {
//                System.out.println("label" + tkz.peek());
                parseLabel();
            }
        }
        fillLabel();
    }

    public void parseCommand() throws Exception{
        if(tkz.peek().equals("add")){
            currentCode = "000";
            tkz.consume();
            for(int i=0; i<3; i++){
//                System.out.println("add   " + i +tkz.peek());
                parseReg();
            }
            parseGapRType();
//            System.out.println("your here ?" + tkz.peek());
            parseComment();
//            if(tkz.peek().equals("!")) {
//
//            }
        }else if(tkz.peek().equals("nand")){
            currentCode = "001";
            tkz.consume();
            for(int i=0; i<3; i++){
//                System.out.println("nand   " + i +tkz.peek());
                parseReg();
            }
            parseGapRType();
            parseComment();

        }else if(tkz.peek().equals("lw")){
            currentCode = "010";
            tkz.consume();
            for(int i=0; i<2; i++) {
//                System.out.println("lw   " + i +tkz.peek());
                parseReg();
            }
            parseOffsetField();
            parseComment();
        }else if(tkz.peek().equals("sw")){
            currentCode = "011";
            tkz.consume();
            for(int i=0; i<2; i++) {
//                System.out.println("sw   " + i +tkz.peek());
                parseReg();
            }
            parseOffsetField();
            parseComment();
        }else if(tkz.peek().equals("beq")){
            currentCode = "100";
            tkz.consume();
            for(int i=0; i<2; i++) {
//                System.out.println("beq   " + i +tkz.peek());
                parseReg();
            }
            parseOffsetField();
            parseComment();
        }else if(tkz.peek().equals("jalr")){
            currentCode = "101";
            tkz.consume();
            for(int i=0; i<2; i++){
//                System.out.println("jalr   " + i +tkz.peek());
                parseReg();
            }
            parseComment();
            parseGapJType();
        }else if(tkz.peek().equals("halt")){
            currentCode = "110";
            tkz.consume();
//            System.out.println("halt      " + tkz.peek());
            parseComment();
            parseGapOType();
        }else if(tkz.peek().equals("noop")){
            currentCode = "111";
            tkz.consume();
//            System.out.println("noop      " + tkz.peek());
            parseComment();
            parseGapOType();
//            System.out.println("after noop   " + tkz.peek());
        }
    }

    public void parseLabel() throws Exception{
//        System.out.println("is this label" + tkz.peek());
        if(!label.contains(tkz.peek())){
            label.add(tkz.peek());
            address.add(machineCode.size());
//            System.out.println("label     " + label.getLast() + "address  " + address.getLast());
            tkz.consume();
//            System.out.println(tkz.peek());
            if(tkz.peek().equals(".fill")){
                tkz.consume();
//                System.out.println("filllllllllllllll "+tkz.peek());
                parseFill();
            }else if(AssemblyCommand.contains(tkz.peek())){
                parseCommand();
            }else{
                throw new Exception("command not found");
            }
        }else{
//            System.out.println("why"+tkz.peek());
            throw new Exception("repeat label");
        }
    }

    public void parseFill() throws Exception{
        if(isNumber(tkz.peek())) {
            int num = Integer.parseInt(tkz.peek());
            if (num <= 32767 && num >= -32768) {
                String value = TwosComplement(num);
                currentCode = currentCode + value;
//                System.out.println("is number fill   " + currentCode);
                machineCode.add(currentCode);
                currentCode = "";
//                System.out.println("fill number?  " +tkz.peek());
                tkz.consume();
                parseComment();
            }else{
                throw new Exception("number not match");
            }
        } else {
//            System.out.println("fill label whit label " + tkz.peek());
            currentCode = currentCode + tkz.consume();
//            System.out.println("what add  " + currentCode);
            machineCode.add(currentCode);
            currentCode = "";
            parseComment();

        }
    }
    public void parseReg() throws Exception{
        if(isNumber(tkz.peek())){
            if(tkz.peek().charAt(0) == '-'){
                throw new Exception("incorrect reg");
            }else {
                int reg = Integer.parseInt(tkz.peek());
                if(reg <= 7){
                    String r = FillBinary(reg);
                    currentCode = currentCode + r;
                    tkz.consume();
                }else{
                    throw new Exception("wrong register");
                }
            }
        }
    }

    public void parseOffsetField() throws Exception{
//        System.out.println(isNumber(tkz.peek()));
        if(isNumber(tkz.peek())){
//            System.out.println("number offset  " + tkz.peek());
            int num = Integer.parseInt(tkz.peek());
            if(num <= 32767 && num >= -32768){
                String offset = TwosComplement(num);
                currentCode = currentCode + offset;
                machineCode.add(currentCode);
//                System.out.println("what add offset num" + currentCode);
                currentCode = "";
                tkz.consume();
            }else{
                throw new Exception("can not read number");
            }
        }else {
//            System.out.println("label offset   " + tkz.peek());
            currentCode = currentCode + tkz.peek();
//            System.out.println("what add in label offset  " + currentCode);
            machineCode.add(currentCode);
            currentCode = "";
            tkz.consume();
//            int index = label.indexOf(tkz.peek());
//            int value = address.get(index);
//            if (value <= 32767 && value >= -32768) {
//                String offset = TwosComplement(value);
//                currentCode = currentCode + offset;
//            }
        }
    }

    public void fillLabel() throws Exception{
//        System.out.println("helllll"+machineCode.size()+label.size()+address.size());
        int pc = 0;
        while(pc < machineCode.size()){
            int index = 0;
            String code = machineCode.get(pc);
//            System.out.println("code for fill   "+code);
            while(index < label.size()){
                String var = label.get(index);
//                System.out.println("youuusnpaspcoa'"+var+code);
                if(Character.isDigit(code.charAt(0))){
                    String variable = code.substring(9);
//                    System.out.println("what substring  " + variable + "what check equals " + var);
                    if(variable.equals(var)){
                        int adLabel = address.get(index);
                        if (code.charAt(0) == '1') {
//                        System.out.println("you 11111" + var );
                            int offsetNum = adLabel-(pc+1);
//                        System.out.println("offsetNum   "+offsetNum);
                            String offset = TwosComplement(offsetNum);
//                        System.out.println("offset   "+offset);
                            machineCode.set(pc,code.replace(var, offset));
                        } else if(code.charAt(0) == '0') {
//                        System.out.println("you 0000" + var);
                            String offset = TwosComplement(adLabel);
                            machineCode.set(pc,code.replace(var, offset));
                        }
                    }
                }else{
                    if(code.equals(var)){
//                        System.out.println("you fill with label " + var);
                        int adLabel = address.get(index);
                        String offset = TwosComplement(adLabel);
                        machineCode.set(pc,code.replace(var, offset));
                    }
                }
                index++;
            }
            code = machineCode.get(pc);
            if(!isNumber(code)){
//                System.out.println("what code" + code);
                throw new Exception("use undefine label");
            }
            pc++;
        }

    }
    public void parseGapRType() throws Exception{
        StringBuilder code = new StringBuilder(currentCode);
        code.insert(9,"0000000000000");
        currentCode = code.toString();
        machineCode.add(currentCode);
//        System.out.println("what add R type" + currentCode);
        currentCode = "";
    }

    public void parseGapJType() throws Exception{
        currentCode = currentCode + "0000000000000000";
        machineCode.add(currentCode);
//        System.out.println("what add J type" + currentCode);
        currentCode = "";
    }

    public void parseGapOType() throws Exception{
        currentCode = currentCode + "0000000000000000000000";
        machineCode.add(currentCode);
//        System.out.println("what add 0 type" + currentCode);
        currentCode = "";
    }

    public void parseGap31_25()throws Exception{

    }

    public void parseComment() throws Exception{
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
                throw new Exception("incorrect reg");
            }
            return binaryString.toString();
        }else{
            return Integer.toBinaryString(number).substring(16);
        }
    }
    public void print(){
        System.out.println(label.size());
        System.out.println(label);
        System.out.println(address.size());
        System.out.println(address);
        System.out.println(machineCode.size());
        System.out.println(machineCode);
    }

}
