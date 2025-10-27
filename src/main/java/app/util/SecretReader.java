package app.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecretReader {
    private final Map<String, String> secrets;
    public SecretReader(Map<String,String> secrets){
        this.secrets = secrets;
    }

    /**
     * Creates a SecretReader from the file referenced in the path.
     * @param path - The path to the targeted file.
     * @return a SecretReader object representation of the file.
     * @throws IOException - The file has to be there.
     */
    public static SecretReader load(Path path) throws IOException{
        List<String> lines = Files.readAllLines(path);
        return new SecretReader(parse(lines));
    }

    /**
     * Parses a list of strings into key-value pairs separated by '='.
     * Ignores empty lines and lines starting with '#'.
     * Trims whitespace around keys and values.
     *
     * @param lines list of strings to parse (each in the form "key=value")
     * @return map containing parsed key-value pairs
     */
    private static Map<String,String> parse(List<String> lines){
        Map<String, String> map = new HashMap<>();
        for(String l : lines){
            if(!l.isEmpty() && !l.startsWith("#")){
                int i = l.indexOf('=');
                if(i > 0) map.put(l.substring(0,i).trim(),l.substring(i+1).trim());
            }
        }
        return map;
    }
    public String get(String key){
        return secrets.get(key);
    }

}
