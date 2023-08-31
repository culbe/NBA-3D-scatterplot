import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;

public class InitStats {

    public static void main(String[] args) {
        PrintWriter procWriter;
        Scanner keyboard = new Scanner(System.in);
        String id;
        String URLid;
        final String NAME_NUMBER = "01";

        try {
            // System.out.println("Enter the name of a player");
            // String name = keyboard.nextLine();
            String name = args[0];
            name = name.toLowerCase();
            id = nameToID(name);
            System.out.println(id);
            URLid = id.charAt(0) + "/" + id + NAME_NUMBER;
            System.out.println(URLid);
            keyboard.close();
        } catch (Exception e) {
            System.out.println("Bad input. Exiting...");
            return;
        }

        File proc = new File("processed/" + id);
        try {
            if (proc.delete()) {
                proc.createNewFile();
            }
            procWriter = new PrintWriter(proc);
        } catch (Exception e) {
            System.out.println("Processed print writer error");
            return;
        }

        URL url;
        InputStream inputStream = null;
        BufferedReader bufferedReader;
        String line;
        try {
            for (int i = 2003; i < 2023; i++) {
                try {
                    Thread.sleep((long)(Math.random()*500)+5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                url = new URL("https://www.basketball-reference.com/players/" + URLid + "/gamelog/" + i);
                System.out.println("Getting url of year: " + url.toString());
                inputStream = url.openStream(); // throws an IOException
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.substring(0, Math.min(line.length(), 20)).contains("<tr ")){
                        Scanner scanner = new Scanner(line);
                        String l;
                        int trb, ast, pts;
                        while (scanner.hasNext()) {
                            l = scanner.next();
                            if (l.contains("trb")) {
                                l = scanner.next();
                                trb = Integer.parseInt(l.substring(1, l.indexOf("<")));
                                l = scanner.next();
                                l = scanner.next();
                                l = scanner.next();
                                l = scanner.next();
                                ast = Integer.parseInt(l.substring(1, l.indexOf("<")));
                                for (int j = 0; j < 20; j++) {
                                    l = scanner.next();
                                }
                                pts = Integer.parseInt(l.substring(1, l.indexOf("<")));
                                procWriter.print(pts + " " + trb + " " + ast + " ");
                            }
                        }
                        scanner.close();
                    }
                }
            }
            procWriter.close();
        } catch (Exception e) {
            procWriter.close();
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ioe) {
            }
        }
    }

    public static String nameToID(String name) {
        String[] names = name.split(" ");
        String last5 = names[1].substring(0, Math.min(5, names[1].length()));
        String first2 = names[0].substring(0, Math.min(2, names[1].length()));
        return last5 + first2;
    }
}
