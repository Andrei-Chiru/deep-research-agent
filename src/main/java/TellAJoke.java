import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessage;

import static java.util.stream.Collectors.toList;

public class TellAJoke {
    public static void main(String[] args) throws Exception {
        // read .env from project root
        List<String> lines = Files.readAllLines(Path.of(".env"), StandardCharsets.UTF_8);
        Map<String,String> map = new HashMap<>();
        for (String l : lines) {
            if (!l.isBlank() && !l.startsWith("#")) {
                int i = l.indexOf('=');
                if (i > 0) map.put(l.substring(0, i).trim(), l.substring(i + 1).trim());
            }
        }

        // get the key
        String apiKey = map.get("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY missing in .env");
        }

        // connect to the openai client
        OpenAIClient client = OpenAIOkHttpClient.builder().apiKey(apiKey).build();

        // give a prompt
        ChatCompletionCreateParams.Builder createParamsBuilder = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_5)
                .maxCompletionTokens(2048)
                .addUserMessage("Tell me a short joke");

        // get response
        List<ChatCompletionMessage> messages =
                client.chat().completions().create(createParamsBuilder.build()).choices().stream()
                        .map(ChatCompletion.Choice::message)
                        .collect(toList());

        messages.stream().flatMap(message -> message.content().stream()).forEach(System.out::println);

        System.out.println("\n-----------------------------------\n");

        // add message to history
        messages.forEach(createParamsBuilder::addMessage);
        createParamsBuilder
                .addUserMessage("Tell me another one!");

        // get response
        messages =
                client.chat().completions().create(createParamsBuilder.build()).choices().stream()
                        .map(ChatCompletion.Choice::message)
                        .collect(toList());

        messages.stream().flatMap(message -> message.content().stream()).forEach(System.out::println);

        System.out.println("\n-----------------------------------\n");
    }
}
