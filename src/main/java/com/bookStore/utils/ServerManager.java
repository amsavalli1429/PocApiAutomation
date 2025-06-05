package com.bookStore.utils;

import com.bookStore.config.ConfigReader;
import io.restassured.RestAssured;

import java.io.*;

public class ServerManager {

    private static Process serverProcess;

    
    public static void startServer() {
        if (isCIEnvironment()) {
            System.out.println("CI mode detected — server startup is skipped.");
            ExtentReportUtil.step("INFO", "Skipping server startup in CI environment.");
            return;
        }

        try {
            System.out.println("🔄 Starting FastAPI server...");
            ExtentReportUtil.step("INFO", "Starting FastAPI server...");

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "uvicorn main:app --reload");
            pb.directory(getBackendDirectory());
            serverProcess = pb.start();

            streamServerLogs(serverProcess);
            waitUntilServerIsReady();

        } catch (IOException | InterruptedException e) {
            ExtentReportUtil.step("FAIL", "Server startup failed: " + e.getMessage());
            throw new RuntimeException("❌ Failed to start server.", e);
        }
    }

    
    public static void stopServer() {
        if (serverProcess != null) {
            serverProcess.destroy();
            System.out.println("🛑 FastAPI server stopped.");
            ExtentReportUtil.step("INFO", "FastAPI server stopped.");
        }
    }

    
    private static void waitUntilServerIsReady() throws InterruptedException {
        int retries = 10;
        for (int i = 0; i < retries; i++) {
            if (isServerRunning()) {
                System.out.println("✅ FastAPI server is up!");
                ExtentReportUtil.step("PASS", "FastAPI server is running.");
                return;
            }
            Thread.sleep(1000);
        }
        throw new RuntimeException("❌ Server did not start within expected time.");
    }

    private static boolean isServerRunning() {
        try {
            RestAssured.baseURI = ConfigReader.getBaseUri();
            return RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .get("/docs")
                    .getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private static void streamServerLogs(Process process) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[FastAPI] " + line);
                }
            } catch (IOException e) {
                System.err.println("❗ Error reading server logs: " + e.getMessage());
            }
        }).start();
    }

    private static File getBackendDirectory() {
        String baseDir = System.getProperty("user.dir");
        return new File(baseDir + File.separator + "bookstore-main" + File.separator + "bookstore");
    }

    private static boolean isCIEnvironment() {
        return System.getenv("CI") != null;
    }
}
