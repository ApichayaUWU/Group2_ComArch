import java.io.*;
import java.util.*;

public class AssemblyTokenizerV1 {
    // อ่านไฟล์และแปลงเป็นโทเคน
    public static List<String[]> tokenizeAssemblyFile(String filename) throws IOException {
        List<String[]> tokensList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        // อ่านแต่ละบรรทัดในไฟล์
        while ((line = reader.readLine()) != null) {
            // ตรวจสอบว่าบรรทัดไม่ว่าง
            if (!line.trim().isEmpty()) {
                // ตัดคำในแต่ละบรรทัดและเพิ่มลงในรายการ
                String[] tokens = tokenizeLine(line);
                if (tokens.length > 0) {
                    tokensList.add(tokens);
                }
            }
        }
        reader.close();
        return tokensList;
    }

    // คำแต่ละบรรทัดและเก็บ comment ที่แยกด้วยช่องว่าง
    public static String[] tokenizeLine(String line) {
        // ตัดคำโดยใช้ช่องว่างและแท็บ
        String[] allTokens = line.trim().split("\\s+");

        // ถ้าเป็นบรรทัดว่างให้ข้าม
        if (allTokens.length == 0) {
            return new String[0];
        }

        // สมมติว่าโทเคนก่อน comment จะเป็นโทเคนที่ 1-3 (เช่น lw 0 1) ที่เหลือคือคอมเมนต์
        // นำ instruction ออกมา ส่วนที่เหลือถือเป็นคอมเมนต์
        List<String> mainTokens = new ArrayList<>();
        List<String> commentTokens = new ArrayList<>();

        // แบ่งโทเคนหลักออกจากคอมเมนต์
        for (int i = 0; i < allTokens.length; i++) {
            if (i < 4) { // โทเคนหลัก 4 ตัวแรก (label, instruction, และ operands)
                mainTokens.add(allTokens[i]);
            } else { // โทเคนที่เหลือถือเป็น comment
                commentTokens.add(allTokens[i]);
            }
        }

        // ถ้ามี comment ให้เพิ่มเป็นโทเคนสุดท้าย
        if (!commentTokens.isEmpty()) {
            String comment = String.join(" ", commentTokens); // รวม comment ที่เหลือ
            mainTokens.add(comment);
        }

        return mainTokens.toArray(new String[0]);
    }

}
