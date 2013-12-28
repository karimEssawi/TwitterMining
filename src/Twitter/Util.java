/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

/**
 *
 * @author Karim
 */
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Util {
    
    public static void writeStringToFile(String filePathAndName, String stringToBeWritten) throws IOException{
        try
        {
            String filename= filePathAndName;
            boolean append = true;
            FileWriter fw = new FileWriter(filename,append);
            fw.write(stringToBeWritten);//appends the string to the file
            fw.write("\n");
            fw.close();
        }
        catch(IOException e)
        {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    public static String[] readKeyWords(String fileName){
        String[] keyWords = null;
        List<String> lines;
        File f = new File(fileName);

//        URL url = Util.class.getClass().getResource(fileName);
//        File file;
//        try{
//            file = new File(url.toURI());
//            lines = Files.readAllLines(Paths.get(file.getCanonicalPath()), Charset.defaultCharset());
//            keyWords = lines.toArray(new String[lines.size()]);
//        }catch(IOException | URISyntaxException e){e.printStackTrace();}
        
        try{
//            lines = Files.readAllLines(Paths.get(f.getCanonicalPath()), Charset.defaultCharset());
            lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
            keyWords = lines.toArray(new String[lines.size()]);
        }catch(IOException e){e.printStackTrace();}
        
        return keyWords;
    }
}
