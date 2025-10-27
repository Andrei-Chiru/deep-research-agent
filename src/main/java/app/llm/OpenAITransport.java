package app.llm;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionAssistantMessageParam;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionSystemMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;

import java.util.ArrayList;
import java.util.List;

public class OpenAITransport {
    private final OpenAIClient client;
    private final ChatModel model;
    private final List<ChatCompletionMessageParam> messages = new ArrayList<>();

    public OpenAITransport(String apiKey, ChatModel model, String system) {
        this.client = OpenAIOkHttpClient.builder().apiKey(apiKey).build();
        this.model = model;
        messages.add(
                ChatCompletionMessageParam.ofSystem(
                        ChatCompletionSystemMessageParam
                        .builder()
                        .content(system)
                        .build())
        );
    }

    /**
     * The agent answers the user's query. The message from the user and the agent are stored.
     * @param user - What the user asks
     * @param maxTokens - Maximum cost of processing the message
     * @return String representing the reply of the agent.
     */
    public String complete(String user, int maxTokens) {
        //add user message
        messages.add(
                ChatCompletionMessageParam.ofUser(
                        ChatCompletionUserMessageParam
                        .builder()
                        .content(user)
                        .build())
        );

        //give history to agent
        ChatCompletionCreateParams params = ChatCompletionCreateParams
                .builder()
                .model(model)
                .messages(messages)
                .maxCompletionTokens(maxTokens)
                .build();

        ChatCompletion chat = client.chat().completions().create(params);

        String reply = chat.choices().getFirst().message().content().get();

        // add assistant messages too
        messages.add(
                ChatCompletionMessageParam.ofUser(
                        ChatCompletionUserMessageParam
                        .builder()
                        .content(reply)
                        .build())
        );

        return reply;
    }

    /**
     * The agent answers the user's query. The query and the agent's response are not stored.
     * @param user - What the user asks
     * @param maxTokens - Maximum cost of processing the message
     * @return String representing the reply of the agent.
     */
    public String completeOnce(String user, int maxTokens){
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)
                .addUserMessage(user)
                .maxCompletionTokens(maxTokens)
                .build();

        ChatCompletion chat = client.chat().completions().create(params);

        return chat.choices().getFirst().message().content().get();
    }

    /**
     * Clears the history of the agent.
     */
    public void resetHistory() {
        ChatCompletionMessageParam system = messages.get(0);
        messages.clear();
        messages.add(system);
    }
}
