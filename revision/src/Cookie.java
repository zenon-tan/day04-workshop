package revision.src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Cookie {

    private String fileName;

    public Cookie(String fileName) {
        this.fileName = fileName;
    }

    public String getRandomCookie() {

        List<String> cookieList = readFile();
        Random random = new Random();
        int randomNo = random.nextInt(cookieList.size() - 1);

        String randomCookie = cookieList.get(randomNo);
        
        return randomCookie;

    }

    public List<String> readFile() {

        List<String> cookieList = new LinkedList<String>();

        try {
            
            BufferedReader bf = new BufferedReader(new FileReader(this.fileName));
            String line;
            while(null != (line = bf.readLine())) {
                line = line.trim();
                cookieList.add(line);
            }

            bf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cookieList;

    }
    
}
