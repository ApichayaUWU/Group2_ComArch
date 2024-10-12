package Assembler;

import java.io.*;
import java.util.ArrayList;

public class ReadFile implements ReadAssembly{
    private ParserAssembly PA ;
    public ReadFile(String fileName) throws Exception {
        genarateMachineCode(fileName);
    }
    @Override
    public void genarateMachineCode(String fileName)  throws IOException, SyntaxError,Exception {
        // ระบุชื่อไฟล์ Assembly ที่ต้องการอ่าน
//        fileName = "src\\Combination.txt";
        AssemblyTokenizer tkz = new AssemblyTokenizer(fileName);
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