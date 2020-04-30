package services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class AuditService {
    private final String filename;

    public AuditService() {
        this.filename = "csv/audit.csv";
    }

    public void saveFunctionCall(String functionName) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String lineToAppend = functionName + "," + timestamp;
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(this.filename, true))){
            wr.write(lineToAppend);
            wr.newLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
