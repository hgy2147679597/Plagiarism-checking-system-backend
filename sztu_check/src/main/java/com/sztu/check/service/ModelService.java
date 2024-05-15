package com.sztu.check.service;

import cn.hutool.core.lang.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @BelongsProject: Plagiarism-checking-system-backend
 * @BelongsPackage: com.sztu.sztu_check.service
 * @Author: wentianrui
 * @CreateTime: 2023-09-17  10:48
 * @Description: start method - similarity query     train method - retrain the model
 * @Version: 1.0
 */
@Service
@Slf4j
public class ModelService {
    @Value("${custom.docxDirectory}")
    private String docxDirectory;
    @Value("${custom.modelFilePath}")
    private String modelFile;
    @Value("${custom.pythonScriptPath}")
    String pythonScriptPath;
    @Value("${custom.pythonPath}")
    String pythonPath;
    private final static String PATTERN ="\\('([^']+)', (\\d+\\.\\d+)\\)";
    /**
     * @description: Input the article and vector dimensions to be queried, the minimum number of times, and the number of iterations so that the model performs a similarity query.
     * @author: wentianrui
     * @date:  9:40
     * @param: [test_docx_file, vectorSize, minCount, epochs]
     * @return: java.util.List<javafx.util.Pair<java.lang.String,java.lang.Double>>
     **/
    public List<Pair<String,Double>> start(String testDocxFile, Integer vectorSize, Integer minCount, Integer epochs) {
        log.info("[ModelService.start] pythonScriptPath: " + pythonScriptPath);
        ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, pythonScriptPath, "start", docxDirectory, modelFile, testDocxFile,
                Integer.toString(vectorSize), Integer.toString(minCount), Integer.toString(epochs));
        List<String> pythonOutput = getPythonOutput(processBuilder);
        if(pythonOutput==null){
            log.error("[ModelService.start] fail");
            return null;
        }
        List<Pair<String,Double>>documentSimilarities = string2DocumentSimilarity(pythonOutput.get(pythonOutput.size() - 1));
        log.info("[ModelService.start] get DocumentSimilarity success :"+documentSimilarities);
        return documentSimilarities;
    }
    /**
     * @description: Input dimension, minimum number of times, and number of training iterations to update the model by using all docx files for training.
     * @author: wentianrui
     * @date:  9:37
     * @param: [vectorSize, minCount, epochs]
     * @return: void
     **/
    public boolean train(Integer vectorSize, Integer minCount, Integer epochs) {
        ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, pythonScriptPath, "train", docxDirectory, modelFile, "",
                Integer.toString(vectorSize), Integer.toString(minCount), Integer.toString(epochs));
        List<String> pythonOutput = getPythonOutput(processBuilder);
        if (pythonOutput == null) {
            log.error("[ModelService.train] fail");
            return false;
        }
        log.info(String.valueOf(pythonOutput));
        log.info("[ModelService.train] success");
        return true;
    }
    /**
     * @description: Getting the output of a python script
     * @author: wentianrui
     * @date:  22:23
     * @param: [processBuilder]
     * @return: java.util.List<java.lang.String>
     **/
    public List<String> getPythonOutput(ProcessBuilder processBuilder) {
        try {
            processBuilder.redirectErrorStream(true);
            processBuilder.environment().put("PYTHONIOENCODING", "UTF-8");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> pythonOutput = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                pythonOutput.add(line);
            }
            int exitCode = process.waitFor();
            log.info("Python脚本已完成，退出码：" + exitCode);
            if (exitCode == 0) {
                return pythonOutput;
            } else {
                System.err.println("Python脚本执行失败");
                for (String outputLine : pythonOutput) {
                    System.err.println("Python错误输出: " + outputLine);
                }
            }
            return null;
        } catch (IOException | InterruptedException e) {
            log.error("[modelService.getPythonOutput] fail Exception:" + e.getMessage());
        }
        return null;
    }
    /**
     * @description: Converting the output obtained by a python script to a List collection
     * @author: wentianrui
     * @date:  22:24
     * @param: [input]
     * @return: java.util.List<cn.hutool.core.lang.Pair<java.lang.String,java.lang.Double>>
     **/
    public static List<Pair<String,Double>> string2DocumentSimilarity(String input){
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(input);
        List<Pair<String,Double>> resultList = new ArrayList<>();
        while (matcher.find()) {
            String documentName = matcher.group(1);
            double similarity = Double.parseDouble(matcher.group(2));
            Pair<String,Double> documentSimilarity = new Pair<>(documentName, similarity);
            resultList.add(documentSimilarity);
        }
        // 输出解析结果
        for (Pair<String,Double> doc : resultList) {
            log.info("Document: " + doc.getKey() + ", Similarity: " + doc.getValue());
        }
        return resultList;
    }
}
