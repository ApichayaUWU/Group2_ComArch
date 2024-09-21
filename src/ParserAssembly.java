import java.util.ArrayList;
import java.util.List;
public class ParserAssembly {
    private Tokenizer tkz;
    private static final List<String> AssemblyCommand = CreateAssemblyCommand();
    private ArrayList<String> machineCode = new ArrayList<>();
    private String currentCode ;
    private ArrayList<String> label = new ArrayList<>();
    private ArrayList<Integer> address = new ArrayList<Integer>();
    public ParserAssembly(Tokenizer tkz){
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
        while(tkz.hasNextToken()) {
            if (AssemblyCommand.contains(tkz.peek())) {
                parseCommand();
            } else {
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
                parseReg();
            }
            parseComment();
            if(tkz.peek().equals("!")) {
                parseGapRType();
            }
        }else if(tkz.peek().equals("nand")){
            currentCode = "001";
            tkz.consume();
            for(int i=0; i<3; i++){
                parseReg();
            }
            parseComment();
            if(tkz.peek().equals("!")) {
                parseGapRType();
            }
        }else if(tkz.peek().equals("lw")){
            currentCode = "010";
            tkz.consume();
            for(int i=0; i<2; i++) {
                parseReg();
            }
            parseOffsetField();
            parseComment();
        }else if(tkz.peek().equals("sw")){
            currentCode = "011";
            tkz.consume();
            for(int i=0; i<2; i++) {
                parseReg();
            }
            parseOffsetField();
            parseComment();
        }else if(tkz.peek().equals("beq")){
            currentCode = "100";
            tkz.consume();
            for(int i=0; i<2; i++) {
                parseReg();
            }
            parseOffsetField();
            parseComment();
        }else if(tkz.peek().equals("jalr")){
            currentCode = "101";
            tkz.consume();
            for(int i=0; i<2; i++){
                parseReg();
            }
            parseComment();
            if(tkz.peek().equals("!")) {
                parseGapJType();
            }
        }else if(tkz.peek().equals("halt")){
            currentCode = "110";
            tkz.consume();
            parseComment();
            if(tkz.peek().equals("!")) {
                parseGapOType();
            }
        }else if(tkz.peek().equals("noop")){
            currentCode = "111";
            tkz.consume();
            parseComment();
            if(tkz.peek().equals("!")) {
                parseGapOType();
            }
        }
    }

    public void parseLabel() throws Exception{
        if(!label.contains(tkz.peek())){
            label.add(tkz.peek());
            address.add(machineCode.size());
            tkz.consume();
            if(tkz.peek().equals(".fill")){
                tkz.consume();
                parseFill();
            }
        }else{
            throw new Exception("repeat label");
        }
    }

    public void parseFill() throws Exception{
        if(isNumber(tkz.peek())) {
            int num = Integer.parseInt(tkz.peek());
            if (num <= 32767 && num >= -32768) {
                String value = TwosComplement(num);
                currentCode = currentCode + value;
                machineCode.add(currentCode);
                while(!tkz.peek().equals("!")){
                    tkz.consume();
                }
            }
        }else {
            throw new Exception("wrong fill label");
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
        if(isNumber(tkz.peek())){
            int num = Integer.parseInt(tkz.peek());
            if(num <= 32767 && num >= -32768){
                String offset = TwosComplement(num);
                currentCode = currentCode + offset;
                tkz.consume();
            }
        }else if(label.contains(tkz.peek())){
            currentCode = currentCode + tkz.peek();
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
        int pc = 0;
        int index = 0;
        while(pc < machineCode.size()){
            String code = machineCode.get(pc);
            if(!(code.length() > 16)) return;
            while(index < label.size()){
                String var = label.get(index);
                if(code.contains(var)){
                    int adLabel = address.get(index);
                    if(code.charAt(0) == '0'){
                        String offset = TwosComplement(adLabel);
                        code = code.replace(var, offset);
                    }else if(code.charAt(0) == '1'){
                        int offsetNum = adLabel-(pc+1);
                        String offset = TwosComplement(offsetNum);
                        code = code.replace(var, offset);
                    }
                }
                index++;
            }
            pc++;
        }
    }
    public void parseGapRType() throws Exception{
        StringBuilder code = new StringBuilder(currentCode);
        code.insert(8,"0000000000000");
        currentCode = code.toString();
        machineCode.add(currentCode);
        currentCode = "";
        tkz.consume();
    }

    public void parseGapJType() throws Exception{
        currentCode = currentCode + "0000000000000000";
        machineCode.add(currentCode);
        currentCode = "";
        tkz.consume();
    }

    public void parseGapOType() throws Exception{
        currentCode = currentCode + "0000000000000000000000";
        machineCode.add(currentCode);
        currentCode = "";
        tkz.consume();
    }

    public void parseGap31_25()throws Exception{

    }

    public void parseComment() throws Exception{
        while (!tkz.peek().equals("!")){
            tkz.consume();
        }
    }

    private boolean isNumber(String str) {
        //check string is number
        int size = str.length();
        if((str.charAt(0) == '-')) {
            for (int i = 1; i < size; i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }
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
        return Integer.toBinaryString(number).substring(3);
//        if(type.equals("reg")){
//            int size = binaryString.length();
//            if(size < 3){
//                int i = 3 - size;
//                while (i > 0){
//                    binaryString.insert(0, "0");
//                    i--;
//                }
//            }else if(size > 3){
//                throw new Exception("incorrect reg");
//            }else return binaryString.toString();
//        }else if(type.equals("offsetField")){
//            int size = binaryString.length();
//            if(size < 16){
//                int i = 16 - size;
//                while (i > 0){
//                    binaryString.insert(0, "0");
//                    i--;
//                }
//            }else if(size > 16){
//                throw new Exception("incorrect value");
//            }else return binaryString.toString();
//        }else{
//            throw new Exception("incorrect command");
//        }
    }

    public String TwosComplement(Integer number)throws Exception{
        return Integer.toBinaryString(number).substring(16);
//        System.out.println(binaryString);
//        String TwosCom = binaryString.replace('1','2');
//        System.out.println("line 1 "+TwosCom);
//        TwosCom = TwosCom.replace('0','1');
//        System.out.println("line 2 "+TwosCom);
//        TwosCom = TwosCom.replace('2','0');
//        System.out.println("line 3 " + TwosCom);
//        int num = Integer.parseInt(TwosCom,2);
//        System.out.println(num);
//        num = num + 1;
//        System.out.println("num" + num);
////        binaryString = Integer.toBinaryString(num);
////        System.out.println(binaryString);
//        StringBuilder binaryS = new StringBuilder(Integer.toBinaryString(num));
//        System.out.println("BS"+binaryS);
//        int size = binaryS.length();
//        System.out.println(binaryS);
//        if(size < 16){
//            int i = 16 - size;
//            String fillValue;
//            if(binaryS.charAt(0) == '1'){
//                fillValue = "1";
//            }else if(binaryS.charAt(0) == '0') {
//                fillValue = "0";
//            }else throw new Exception("unknown");
//            while (i > 0){
//                binaryS.insert(0, fillValue);
//                i--;
//            }
//        }else if(size > 16){
//            throw new Exception("incorrect value");
//        }else return binaryS.toString();
//

    }
}
