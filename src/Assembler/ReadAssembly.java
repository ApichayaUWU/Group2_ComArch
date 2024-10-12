package Assembler;

import java.io.IOException;
import java.util.ArrayList;

public interface ReadAssembly {
    public void genarateMachineCode(String fileName0) throws IOException, SyntaxError, Exception;

    public ArrayList<String> getMachineCode();
}
