package com.testcases.cmd;

import java.io.IOException;


public class VulnProcessBuilder1 {

    public VulnProcessBuilder1(String str) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(str);
        Process process = processBuilder.start();
    }

}
