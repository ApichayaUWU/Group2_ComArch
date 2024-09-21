import java.io.*;
import java.util.*;

public class ReadFile {
    public static void main(String[] args)  throws IOException, SyntaxError,Exception {
        // ระบุชื่อไฟล์ Assembly ที่ต้องการอ่าน
        String filename = "src\\example.txt";
        AssemblyTokenizerV2 tkz = new AssemblyTokenizerV2(filename);
        tkz.printToken();
        ParserAssembly pa = new ParserAssembly(tkz);
        pa.parseCode();
        pa.print();
//        List<String> tokenizedAssembly = tkz.getTokensList();
//        System.out.println(tkz.peek());
//        System.out.println(tkz.consume());
//        System.out.println(tkz.peek());
        // ใช้ StringBuilder เพื่อรวม String[] แต่ละบรรทัดเข้าด้วยกัน
//            StringBuilder src = new StringBuilder();
//
//            for (String tokens : tokenizedAssembly) {
//                // แปลง String[] ให้เป็น String และเชื่อมด้วยเครื่องหมาย !
//                String line = String.join(",", tokens);
//                src.append(line);
//            }

        // แสดงผลรวมของทุกบรรทัดที่เชื่อมกัน



    }
}