package com.microwise.msp.hardware.service.impl;

import com.microwise.msp.hardware.service.LogAnalysisService;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
@Service
public class LogAnalysisServiceImpl implements LogAnalysisService {


    public boolean doLogAnalysis(String[] cmdArray) {
        try {
            for (String command : cmdArray) {
                Process process = Runtime.getRuntime().exec( new String[]{"/bin/sh", "-c", command});
                InputStreamReader ir = new InputStreamReader(process
                        .getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                String line;

                int exitCode = process.waitFor();
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                if (exitCode != 0) {
                      return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
