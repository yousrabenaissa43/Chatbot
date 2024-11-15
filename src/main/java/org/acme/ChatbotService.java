package org.acme;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

public class ChatbotService {

    static ChatLanguageModel model = OpenAiChatModel.builder()
            .apiKey(System.getenv("OPENAI_API_KEY"))
            .modelName(GPT_4_O_MINI)
            .build();

    private static final String PROMPT_TEMPLATE = """
            You are a helpful assistant. Analyze the input sentence and extract the relevant features and values about a product in JSON format.
            The JSON should include:
            - type (e.g., chemise, pantalon, etc.)
            - couleur (color in French)
            - qualité (quality in French)
            - prix (price in dollars).

            Example: 
            Input: "Je veux une chemise blanche de haute qualité 50$."
            Output: {
              "type": "chemise",
              "couleur": "blanche",
              "qualité": "haute",
              "prix": "50$"
            }

            Input: "{{input}}"
            Output:
            """;





    static class ProductFeatureExtractionPrompt {

        @StructuredPrompt(PROMPT_TEMPLATE)
        static class ProductInput {

            private String input;
        }

        public static void main(String[] args) {

            ProductInput productInput = new ProductInput();
            productInput.input = "Je cherche un pantalon bleu de bonne qualité à 40$.";

            Prompt prompt = StructuredPromptProcessor.toPrompt(productInput);

            AiMessage aiMessage = model.generate(prompt.toUserMessage()).content();
            System.out.println(aiMessage.text());
        }
    }
}
