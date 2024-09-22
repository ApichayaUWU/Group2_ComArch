import java.util.ArrayList;

public class test {
    public static void main(String[] args) throws Exception {
        ReadAssembly RA = new ReadFile("src\\tonnamTest.txt");
        ArrayList<String> MC = RA.getMachineCode();
//        System.out.println(MC);

    }
}
