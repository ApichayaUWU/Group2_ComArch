import java.util.ArrayList;
import java.util.List;
public class ParserAssembly {
    private Token tkz;
    private static final List<String> AssemblyCommand = CreateAssemblyCommand();
    private ArrayList<String> machineCoode = new ArrayList<>();
    private String currentCode ;
    public ParserAssembly(Token tkz){
        this.tkz = tkz;
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
            Integer reg = Integer.parseInt(tkz.peek());
            if(reg < 7){
                String binaryString = Integer.toBinaryString(reg);
                String s = new String(String.valueOf(binaryString));
                currentCode = currentCode + s;
            }
        }
    }

    public void parseImm() throws Exception{

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
    private String fillBinary(){

    }
}
