package app;

import app.llm.OpenAITransport;
import app.pipeline.StepContext;
import app.pipeline.StepRunner;
import app.prompts.PromptFactory;
import app.store.ArtifactStore;
import app.util.SecretReader;
import com.openai.models.ChatModel;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.io.UncheckedIOException;
public class ResearchAgent {
    public static void main(String[] args){
        System.out.println("Hello! I am Brutus - a deep research AI agent that can analise choices in brief RFC-style. What do you want me to analyse?");
        Scanner scanner = new Scanner(System.in);
        String question = scanner.nextLine().trim();

        SecretReader secretReader = null;
        try {
            secretReader = SecretReader.load(Path.of(".env"));
        }catch (IOException e) {
            throw new UncheckedIOException("Failed to read .env", e);
        }
        String apiKey = secretReader.get("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY missing in .env");
        }

        OpenAITransport llm = new OpenAITransport(apiKey,ChatModel.GPT_5_MINI, PromptFactory.system());
        boolean anotherAnalysis = false;
        do{
            boolean providedTwoChoices = false;
            StepContext context = null;
            do{
                String reply = llm.completeOnce("Given this task: " + question + " give me the two choices comma separated and nothing else",300);
                int i = reply.indexOf(',');
                if(i>0){
                    context = new StepContext(question,reply.substring(0,i).trim(),reply.substring(i+1).trim());
                    providedTwoChoices = true;
                    System.out.println("Here are the two choices: "+ context.optionA + " and " + context.optionB + ".");
                }
                else{
                    System.out.println("Please give me to analyse two choices!");
                }
            }while (!providedTwoChoices);
            ArtifactStore store = new ArtifactStore(Path.of("runs", context.optionA.toLowerCase() + "_vs_" + context.optionB.toLowerCase()));
            StepRunner runner = new StepRunner(llm,store);
            runner.runAll(context);
            System.out.println("Do you want me to analyse another choice? (Yes/No)");
            String continuation = scanner.nextLine().trim();
            if(continuation.equals("Yes")){
                anotherAnalysis = true;
                llm.resetHistory();
            }
        }while (anotherAnalysis);
    }

}
