package org.example;

import java.io.*;

public class MultipleAppStarter {

    public static void main(String[] args) throws InterruptedException {
        int n = 11;
        boolean createProperties = false;
        if(createProperties)
            createAppProperties(n);
        else
            for (int i = 0; i < n; i++) {
                Thread.sleep(2000);
                String[] springArgs = new String[1];
                springArgs[0] = "--spring.config.location=classpath:application"+i+".properties";
                JavaServlet.main(springArgs);
            }
    }

    private static void createAppProperties(int n) {
        try {
            for (int i = 0; i < n; i++) {
                File file = new File("src/main/resources/application" + i + ".properties");
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                bw.write("server.port = " + (1000 + i));
                bw.newLine();
                bw.write("public.ip = localhost");
                bw.newLine();
                bw.write("bootstrap = localhost:8080");
                bw.newLine();
                bw.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
