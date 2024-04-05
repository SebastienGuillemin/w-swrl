package com.sebastienguillemin.wswrl.evaluation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import com.sebastienguillemin.wswrl.evaluation.EvaluationTask.EngineName;

public class EvaluationManager {
    private int tasksCount;
    private String ontologyFilename;
    private long[] swrlTimes;
    private long[] wswrlTimes;

    public EvaluationManager(int tasksCount, String ontologyFilename) {
        this.ontologyFilename = ontologyFilename;
        this.tasksCount = tasksCount;
        this.swrlTimes = new long[tasksCount];
        this.wswrlTimes = new long[tasksCount];
    }

    public void startEvaluation() throws IOException {
        System.out.println(String.format("Running evaluation with %s tasks for each engine.", this.tasksCount));

        try {
            File file = new File("executionTimes.csv");

            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName() + ".\n");
                Files.write(file.toPath(), ("Time (ms),Engine").getBytes());
            } else {
                System.out.println("File already exists, keeping previous content.\n");
            }

            EvaluationTask evaluationTask;
            // for (int i = 0; i < this.tasksCount; i++) {
            //     System.out.println(String.format("Running SWRL engines(%s/%s) - Active threads %s", (i + 1),
            //             this.tasksCount, Thread.activeCount()));

            //     evaluationTask = new EvaluationTask(this.ontologyFilename, EngineName.SWRL);
            //     evaluationTask.start();
            //     evaluationTask.join();
            //     this.swrlTimes[i] = evaluationTask.getExecutionTimeMilli();
            //     Files.write(file.toPath(), String.format(System.lineSeparator() + "%s,%s", this.swrlTimes[i], "SWRL").getBytes(),
            //             StandardOpenOption.APPEND);
            // }

            System.out.println();

            for (int i = 0; i < this.tasksCount; i++) {
                System.out.println(String.format("Running W-SWRL engines(%s/%s) - Active threads %s", (i + 1),
                        this.tasksCount, Thread.activeCount()));

                evaluationTask = new EvaluationTask(this.ontologyFilename, EngineName.WSWRL);
                evaluationTask.start();
                evaluationTask.join();
                this.wswrlTimes[i] = evaluationTask.getExecutionTimeMilli();
                Files.write(file.toPath(), String.format(System.lineSeparator() + "%s,%s", this.swrlTimes[i], "WSWRL").getBytes(),
                        StandardOpenOption.APPEND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
