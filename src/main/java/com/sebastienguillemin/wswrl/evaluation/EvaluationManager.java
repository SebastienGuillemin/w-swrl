package com.sebastienguillemin.wswrl.evaluation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class EvaluationManager {
    private int tasksCount;
    private String taskType;
    private File ontologyFile;
    private long[] times;

    public EvaluationManager(int tasksCount, String taskType, File ontologyFile) {
        this.tasksCount = tasksCount;
        this.taskType = taskType;
        this.ontologyFile = ontologyFile;
        this.times = new long[tasksCount];
    }

    public void startEvaluation() throws IOException {
        System.out.println(String.format("Running evaluation with %s tasks for each engine.", this.tasksCount));

        try {
            File file = new File("executionTimes.csv");

            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName() + ".\n");
                Files.write(file.toPath(), ("Time (ms),Engine,Inferred Axioms,Ontology").getBytes());
            } else {
                System.out.println("File already exists, keeping previous content.\n");
            }
            System.out.println();

            EvaluationTask evaluationTask;
            EngineName engineName = EngineName.valueOf(this.taskType);

            for (int i = 0; i < this.tasksCount; i++) {
                System.out.println(String.format("Running %s engines(%s/%s) - Active threads %s", this.taskType, (i + 1), this.tasksCount, Thread.activeCount()));

                evaluationTask = new EvaluationTask(this.ontologyFile, engineName);
                evaluationTask.start();
                evaluationTask.join();
                this.times[i] = evaluationTask.getExecutionTimeMilli();
                Files.write(
                    file.toPath(),
                    String.format(System.lineSeparator() + "%s,%s,%s,%s", this.times[i], this.taskType,evaluationTask.getInferredAxiomsCount(), this.ontologyFile.toPath().getFileName()).getBytes(),
                    StandardOpenOption.APPEND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
