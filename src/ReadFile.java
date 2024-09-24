import java.io.*;
import java.util.ArrayList;

interface ReadAssembly{
    public void genarateMachineCode(String fileName0) throws IOException, SyntaxError,Exception ;
    public ArrayList<String> getMachineCode();
}
public class ReadFile implements ReadAssembly{
    private ParserAssembly PA ;
    public ReadFile(String fileName) throws Exception {
        genarateMachineCode(fileName);
    }
    @Override
    public void genarateMachineCode(String fileName)  throws IOException, SyntaxError,Exception {
        // ระบุชื่อไฟล์ Assembly ที่ต้องการอ่าน
//        fileName = "src\\Combination.txt";
        AssemblyTokenizerV2 tkz = new AssemblyTokenizerV2(fileName);
        tkz.printToken();
        PA = new ParserAssembly(tkz);
        PA.parseCode();
        PA.print();
    }

    @Override
    public ArrayList<String> getMachineCode() {
        return PA.getMachineCode();
    }
}