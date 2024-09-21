import java.io.*;
import java.util.*;

public class ReadFile {
    public static void readfile(String[] args) {
        try {
            // ระบุชื่อไฟล์ Assembly ที่ต้องการอ่าน
            String filename = "src\\example.txt";
            List<String[]> tokenizedAssembly = AssemblyTokenizerV2.tokenizeAssemblyFile(filename);

            // ใช้ StringBuilder เพื่อรวม String[] แต่ละบรรทัดเข้าด้วยกัน
            StringBuilder src = new StringBuilder();

            for (String[] tokens : tokenizedAssembly) {
                // แปลง String[] ให้เป็น String และเชื่อมด้วยเครื่องหมาย !
                String line = String.join(",", tokens);
                src.append(line);
                src.append(" ! "); // เพิ่ม ! หลังจากแต่ละบรรทัด
            }

            // แสดงผลรวมของทุกบรรทัดที่เชื่อมกัน
            System.out.println(src.toString());

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
