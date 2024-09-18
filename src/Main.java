import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // ระบุชื่อไฟล์ Assembly ที่ต้องการอ่าน
            String filename = "src\\example.txt";
            List<String[]> tokenizedAssembly = AssemblyTokenizerV1.tokenizeAssemblyFile(filename);

            // แสดงผลโทเคนที่ถูกตัดแบบ list
//            for (String[] tokens : tokenizedAssembly) {
////                System.out.println(Arrays.toString(tokens));
////            }
            // แสดงผลโทเคนที่ถูกตัดเป็น String
            for (String[] tokens : tokenizedAssembly) {
                // รวมค่าใน tokens ให้เป็น String เดียว
                System.out.println(String.join(",", tokens));
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
