package app.pipeline;

import java.util.ArrayList;
import java.util.List;

public class StepContext {
    public final String optionA;
    public final String optionB;
    public final String question;

    public List<String> criteria = new ArrayList<>();
    public List<String> sources = new ArrayList<>();

    public String plan;                  // step 1 output
    public String gathered;              // step 2 output
    public String draft;                 // step 3 output
    public String verificationReport;    // step 4 output
    public String repaired;              // step 5 output
    public String finalBriefing;         // step 6 output
    public StepContext(String question, String optionA, String optionB){
        this.optionA = optionA;
        this.optionB = optionB;
        this.question = question;
    }
}
