package app.prompts;

import app.pipeline.StepContext;

public class PromptFactory {

    public static String system() {
        return """
            You are an impartial deep research agent producing RFC-style briefings. 
            Use only UTF-8 characters. 
            Follow the workflow strictly: PLAN, GATHER, DRAFT, VERIFY, REPAIR, FINALIZE. 
            Provide evidence, cite sources, and keep internal reasoning private. 
            Output only the requested artifact.
            """;
    }

    public static String plan(StepContext context) {
        return """
            Task: Plan how to compare %s vs %s for the question: %s
            Output: A short numbered list of major steps and evaluation criteria.
            The answer has to be formatted as follows (new line separated):
            Plan:
             ...
            Evaluation criteria:
             ...
            Keep it high level. The phrase 'Evaluation criteria:' has to appear only once in the answer.
            No prose, no analysis yet.
            """.formatted(context.optionA, context.optionB, context.question);
    }

    public static String gather(StepContext context) {
        String criteriaList = String.join(", ", context.criteria);
        return """
            Task: Gather reliable, recent facts about %s and %s for each criterion: %s.
            Output: Bullet notes organized by criterion, with citations afterwards (domain + year + link).
            The citations should be formatted like this (new line separated):
            Citations:
             ...
            The answer should only have one instance of the phrase 'Citations:'.
            Do not draft conclusions.
            """.formatted(context.optionA, context.optionB, criteriaList);
    }

    public static String draft(StepContext context) {
        return """
            Task: Draft an RFC-style briefing comparing %s vs %s using the gathered notes below.
            Keep a neutral tone and include: Executive Summary, Background, Analysis by criteria, 
            Trade-offs, and Risks.
            
            Notes:
            %s
            
            Output: DRAFT ONLY (may contain TODOs).
            """.formatted(context.optionA, context.optionB, context.gathered);
    }

    public static String verify(StepContext context) {
        return """
            Task: Verify factual accuracy, outdated claims, missing perspectives, and weak citations in this draft:
            ---
            %s
            ---
            Output: Verification report listing issues and fixes. Do not rewrite the draft here.
            """.formatted(context.draft);
    }

    public static String repair(StepContext context) {
        return """
            Task: Apply the verification report to repair the knowledge base and notes.
            
            Report:
            %s
            
            Original notes:
            %s
            """.formatted(context.verificationReport, context.gathered);
    }

    public static String finalize(StepContext context) {
        return """
            Task: Produce the final, polished RFC-style briefing comparing %s and %s.
            Use the revised notes below. Include:
             - TL;DR
             - Background
             - Analysis by criteria
             - Trade-offs
             - Risks
             - Cost/Operational Considerations
             - Recommendation by use case
             - References section
            
            Resolve any remaining TODOs.
            
            Notes:
            %s
            """.formatted(context.optionA, context.optionB, context.repaired);
    }
}
