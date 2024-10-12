package Assembler;

import java.io.IOException;
import java.util.ArrayList;

public interface ReadAssembly {
    void genarateMachineCode(String fileName0) throws IOException, SyntaxError, Exception;

    ArrayList<String> getMachineCode();
}
