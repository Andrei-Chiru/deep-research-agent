package app.pipeline;

import app.llm.OpenAITransport;
import app.prompts.PromptFactory;
import app.store.ArtifactStore;

import java.util.List;

public class StepRunner {
    private final OpenAITransport llm;
    private final ArtifactStore store;

    public StepRunner(OpenAITransport llm, ArtifactStore store) {
        this.llm = llm;
        this.store = store;
    }
    public StepContext runAll(StepContext context){
        //1) PLAN
        context.plan = llm.complete(PromptFactory.plan(context),2000);
        String criteria = "Evaluation criteria:";
        context.criteria = List.of(
                context.plan
                .substring(context.plan.indexOf(criteria) + criteria.length())
                .split("\\R"));


        System.out.println("I planned how I will answer the question");
        store.save(Step.PLAN,context.plan);
        context.criteria = ArtifactStore.extractCriteria(context.plan);

        // 2) GATHER
        context.gathered = llm.complete(PromptFactory.gather(context), 6000);
        String citations = "Citations:";
        context.sources = List.of(
                context.gathered
                        .substring(context.gathered.indexOf(citations) + citations.length())
                        .split("\\R"));
        System.out.println("I gathered information on the task");
        store.save(Step.GATHER, context.gathered);

        // 3) DRAFT
        context.draft = llm.complete(PromptFactory.draft(context), 7000);
        System.out.println("I have created a draft answer");
        store.save(Step.DRAFT, context.draft);

        // 4) VERIFY
        context.verificationReport = llm.complete(PromptFactory.verify(context), 2000);
        System.out.println("I verified the claims and information in the draft.");
        store.save(Step.VERIFY, context.verificationReport);

        // 5) REPAIR
        context.repaired = llm.complete(PromptFactory.repair(context), 6000);
        System.out.println("I repaired the knowledge base");
        store.save(Step.REPAIR, context.repaired);

        // 6) FINALIZE
        context.finalBriefing = llm.complete(PromptFactory.finalize(context), 10000);
        System.out.println("I created the analysis");
        store.save(Step.FINALIZE, context.finalBriefing);
        store.saveContext(context);

        return context;
    }
}
