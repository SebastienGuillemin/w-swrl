package com.sebastienguillemin.wswrl.evaluation;

import java.io.File;
import java.nio.file.Files;

/**
 * Evaluation program.
 */
public class Evaluation {
    /**
     * Start the evaluation program. Evaluation results will be save in a file nales
     * 'evaluationResults.csv' at the root of the project.
     * 
     * @param args Must contains three elements : the number of evaluation tasks
     *             (greater than 0), the type of task ('SWRL' or 'WSWRL'), and the
     *             path to the ontology filename to proceed (a .ttl file).
     * @throws Exception If an exception occurs while running the evaluation
     *                   program.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println(
                    "Need 3 arguments:" +
                            "\n - Number of evaluation task (greater than 0)." +
                            "\n - Type of task ('SWRL' or 'WSWRL')." +
                            "\n - The path to the ontology filename to proceed (a .ttl file).");
            System.exit(0);
        }

        int tasksCount = processTasksCount(args);
        String taskType = processTaskType(args);
        File ontologyFile = processOntologyPath(args);

        EvaluationManager evaluationManager = new EvaluationManager(tasksCount, taskType, ontologyFile);
        evaluationManager.startEvaluation();
    }

    private static int processTasksCount(String[] args) {
        try {
            int tasksCount = Integer.parseInt(args[0]);

            if (tasksCount <= 0)
                throw new NumberFormatException();

            return tasksCount;
        } catch (NumberFormatException e) {
            System.out.println("'" + args[0] + "' is not a valid number of tasks (must be greater than 0).");
            System.exit(1);
            return -1;
        }
    }

    private static String processTaskType(String[] args) {
        String taskType = args[1];

        if (!taskType.equals("SWRL") && !taskType.equals("WSWRL")) {
            System.out.println("'" + taskType + "' is not a valid type of task (must be 'SWRL' or 'WSWRL').");
            System.exit(1);
        }
        return taskType;
    }

    private static File processOntologyPath(String[] args) throws Exception {
        String ontologyPath = args[2];

        File ontologyFile = new File(ontologyPath);

        if (!ontologyFile.exists() || ontologyFile.isDirectory()) {
            System.out.println(ontologyPath + " does not exist.");
            System.exit(1);
        }

        String mimeType = Files.probeContentType(ontologyFile.toPath());
        if (!mimeType.equals("text/turtle")) {
            System.out.println("'" + ontologyPath + "' is not a Turtle file.");
            System.exit(1);
        }

        return ontologyFile;
    }
}