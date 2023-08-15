package com.testcases.cmd;

import java.io.IOException;


public class VulnRuntime2 {
    private String cmd;

    public VulnRuntime2(String str) throws IOException {
        this.cmd = str;
        this.vuln1();

    }

    public void vuln1() throws IOException {
        Runtime.getRuntime().exec(this.cmd);
    }

}

