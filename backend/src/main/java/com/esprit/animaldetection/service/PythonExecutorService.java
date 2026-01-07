package com.esprit.animaldetection.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Service
public class PythonExecutorService {

    @Value("${app.python.script.dir}")
    private String pythonScriptDir;

    public String executeDetection(String imagePath, String modelType, double threshold, boolean showDescriptions) {
        try {
            // Use virtual environment Python
            String pythonExe = pythonScriptDir + ".venv/Scripts/python.exe";
            
            // Build Python command
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonExe,
                    pythonScriptDir + "detection_script.py",
                    imagePath,
                    modelType,
                    String.valueOf(threshold),
                    String.valueOf(showDescriptions)
            );
            
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            
            // Read output
            StringBuilder output = new StringBuilder();
            String jsonOutput = null;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.debug("Python output: {}", line);
                    // Find the JSON line (starts with {)
                    if (line.trim().startsWith("{")) {
                        jsonOutput = line;
                    }
                }
            }
            
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script failed with exit code: " + exitCode);
            }
            
            if (jsonOutput == null) {
                throw new RuntimeException("No JSON output from Python script");
            }
            
            return jsonOutput;
            
        } catch (Exception e) {
            log.error("Error executing Python script", e);
            throw new RuntimeException("Error executing detection: " + e.getMessage());
        }
    }
}
