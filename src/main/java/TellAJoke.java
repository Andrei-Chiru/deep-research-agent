import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseOutputItem;
import com.openai.models.responses.ResponseOutputMessage;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

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

        String apiKey = map.get("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY missing in .env");
        }

        OpenAIClient client = OpenAIOkHttpClient.builder().apiKey(apiKey).build();
        ChatCompletionCreateParams cp = ChatCompletionCreateParams.builder()
                .addUserMessage("tell me a short programming joke")
                .model("gpt-4o-mini")
                .build();

        ChatCompletion cc = client.chat().completions().create(cp);
        String text = cc.choices().get(0).message().content().orElse(null);
        System.out.println(text);
    }
}
