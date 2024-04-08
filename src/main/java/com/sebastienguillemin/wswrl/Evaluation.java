package com.sebastienguillemin.wswrl;

import java.io.IOException;

import com.sebastienguillemin.wswrl.evaluation.EvaluationManager;

public class Evaluation {

    private static final String ONTOLOGY_FILENAME = "exampleOntology_short.ttl";

    public static void main(String[] args) throws IOException {
        EvaluationManager evaluationManager = new EvaluationManager(1, ONTOLOGY_FILENAME);
        evaluationManager.startEvaluation();
    }
}