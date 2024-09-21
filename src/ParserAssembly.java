import java.util.ArrayList;
import java.util.List;
public class ParserAssembly {
    private Token tkz;
    private static final List<String> AssemblyCommand = CreateAssemblyCommand();
    private ArrayList<String> machineCoode = new ArrayList<>();
    private String currentCode ;
    public ParserAssembly(){
//        this.tkz = tkz;
        this.currentCode = "";
    }

    public void parseCode() throws Exception{
        if(tkz.hasNextToken()){
            this.parseInstruction();
        }
    }

    public void parseInstruction() throws Exception{
        if(AssemblyCommand.contains(tkz.peek())){
            parseCommand();
        }else{
            parseLable();
        }
    }

    public void parseCommand() throws Exception{
        if(tkz.peek().equals("add")){
            currentCode = "000";
            tkz.consume();
            parseReg();
        }else if(tkz.peek().equals("nand")){
            currentCode = "001";
            tkz.consume();
            parseReg();
        }else if(tkz.peek().equals("lw")){
            currentCode = "010";
            tkz.consume();
            parseReg();
        }else if(tkz.peek().equals("sw")){
            currentCode = "011";
            tkz.consume();
            parseReg();
        }else if(tkz.peek().equals("beq")){
            currentCode = "100";
            tkz.consume();
            parseReg();
        }else if(tkz.peek().equals("jalr")){
            currentCode = "101";
            tkz.consume();
            parseReg();
        }else if(tkz.peek().equals("halt")){
            currentCode = "110";
            tkz.consume();
            parseInstruction();
        }else if(tkz.peek().equals("noop")){
            currentCode = "111";
            tkz.consume();
            parseInstruction();
        }
    }

    public void parseLable() throws Exception{

    }

    public void parseFill() throws Exception{

    }
    public void parseReg() throws Exception{
        if(isNumber(tkz.peek())){
            if(tkz.peek().charAt(0) == '-'){
                throw new Exception("incorrect reg");
            }else {
                int reg = Integer.parseInt(tkz.peek());
                if(reg < 7){
                    String r = FillBinary(reg,"reg");
                    currentCode = currentCode + r;
                }
            }

        }
    }

    public void parseOffsetField() throws Exception{
        if(isNumber(tkz.peek())){
            if(tkz.peek().charAt(0) == '-'){
                int num = Integer.parseInt(tkz.peek());
                String offset = TwosComplement(num,true);
                currentCode = currentCode + offset;
            }else {
                int num = Integer.parseInt(tkz.peek());
                if(num < 7){
                    String offset = TwosComplement(num,false);
                    currentCode = currentCode + offset;
                }
            }

        }

    }

    public void parseGapRType() throws Exception{

    }

    public void parseGapJType() throws Exception{

    }

    public void parseGapOType() throws Exception{

    }

    public void parseGap31_25()throws Exception{

    }

    private boolean isNumber(String str) {
        //check string is number
        int size = str.length();
        for (int i = 0; i < size; i++) {
            if((i == 0) && (str.charAt(i) == '-')) {
                i++;
            }
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return size > 0;
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
