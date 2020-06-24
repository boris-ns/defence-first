package rs.ac.uns.ftn.siemcentar.utils;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.siemcentar.constants.KieConstants;

import java.io.*;
import java.util.Date;

public class FileUtils {

    public static String readFile(MultipartFile file) throws Exception {
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
        return sb.toString();
    }

    public static void writeToKjarFile(MultipartFile file) throws Exception {
        String fileStr = readFile(file);
        String fileName = "new_rules_" + new Date().getTime();
        File f = new File(KieConstants.BASE_PATH_NEW_RULES + fileName + KieConstants.DRL_FILE_EXTENSION);
        f.createNewFile();
        try (PrintWriter out = new PrintWriter(f)) {
            out.println(fileStr);
        }
    }

    public static void writeToKjarFile(String data) throws Exception {
        String fileName = "new_rules_" + new Date().getTime();
        File f = new File(KieConstants.BASE_PATH_NEW_RULES + fileName + KieConstants.DRL_FILE_EXTENSION);
        f.createNewFile();
        try (PrintWriter out = new PrintWriter(f)) {
            out.println(data);
        }
    }
}
