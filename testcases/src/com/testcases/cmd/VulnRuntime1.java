package com.testcases.cmd;

import java.io.IOException;


public class VulnRuntime1 {

    public VulnRuntime1(String str) throws IOException {
        Runtime.getRuntime().exec(str);
    }

}

