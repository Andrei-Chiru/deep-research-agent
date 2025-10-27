package app.store;

import app.pipeline.Step;
import app.pipeline.StepContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ArtifactStore {
    private final Path root;
    private final ObjectMapper om = new ObjectMapper().findAndRegisterModules();
    public ArtifactStore(Path root){
        this.root = root;
    }
    public void save(Step step, String content){
        try{
            Files.createDirectories(root);
            Files.writeString(root.resolve(step.name().toLowerCase()+".md"), content);
        } catch (IOException e){
            System.out.println("Couldnt save content "+ content);
        }
    }
    public void saveContext(StepContext context){
        try{
            Files.createDirectories(root);
            om.writeValue(root.resolve("context.json").toFile(), context);
        } catch (IOException e){
            System.out.println("Couldnt save context");
        }
    }

    public static List<String> extractCriteria(String planText) {
        // naive: lines that look like criteria bullets
        return planText.lines()
                .filter(l -> l.matches("(?i).*(criterion|criteria|:).*") == false)
                .filter(l -> l.matches("^\\s*[-*\\d.].+"))
                .map(l -> l.replaceFirst("^\\s*[-*\\d.]+\\s*", "").trim())
                .limit(10)
                .toList();
    }
}
