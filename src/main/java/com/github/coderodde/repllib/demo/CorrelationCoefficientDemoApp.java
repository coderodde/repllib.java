package com.github.coderodde.repllib.demo;

import com.github.coderodde.repllib.ReplAction;
import static com.github.coderodde.repllib.ReplAction.toDouble;
import static com.github.coderodde.repllib.ReplAction.toInt;
import com.github.coderodde.repllib.ReplInteger;
import com.github.coderodde.repllib.ReplKeyword;
import com.github.coderodde.repllib.ReplParser;
import com.github.coderodde.repllib.ReplReal;
import com.github.coderodde.repllib.ReplStatement;
import com.github.coderodde.repllib.ReplTokenDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CorrelationCoefficientDemoApp {

    private final List<DataPoint> dataPointList = new ArrayList<>();
    
    public void run() {
        final Scanner scanner = new Scanner(System.in);
        final ReplParser parser = buildParser();
        
        while (true) {
            try {
                System.out.print(">>> ");
                final String commandLine = scanner.nextLine();
                parser.parse(commandLine);
            } catch (final Exception ex) {
                System.err.printf("ERROR: %s\n", ex.getMessage());
            }
        }
    }
    
    public void addDataPoint(final double x, final double y) {
        dataPointList.add(new DataPoint(x, y));
    }
    
    public double computeCorrelation() {
        return computeTerm1() / (computeTerm2() * computeTerm3());
    }
    
    private double computeMeanX() {
        double sum = 0;
        
        for (final DataPoint dp : dataPointList) {
            sum += dp.x;
        }
        
        return sum / dataPointList.size();
    }
    
    private double computeMeanY() {
        double sum = 0;
        
        for (final DataPoint dp : dataPointList) {
            sum += dp.y;
        }
        
        return sum / dataPointList.size();
    }
    
    private double computeTerm1() {
        final double meanX = computeMeanX();
        final double meanY = computeMeanY();
        
        double sum = 0;
        
        for (final DataPoint dp : dataPointList) {
            sum += (dp.x - meanX) * (dp.y - meanY);
        }
        
        return sum;
    }
    
    private double computeTerm2() {
        final double meanX = computeMeanX();
        
        double sum = 0;
        
        for (final DataPoint dp : dataPointList) {
            sum += Math.pow(dp.x - meanX, 2.0);
        }
        
        return Math.sqrt(sum);
    }
    
    private double computeTerm3() {
        final double meanY = computeMeanY();
        
        double sum = 0;
        
        for (final DataPoint dp : dataPointList) {
            sum += Math.pow(dp.y - meanY, 2.0);
        }
        
        return Math.sqrt(sum);
    }
    
    public static void main(String[] args) {
        final CorrelationCoefficientDemoApp app = 
                new CorrelationCoefficientDemoApp();
        
        app.run();
    }
    
    private ReplParser buildParser() {
        final ReplParser parser = new ReplParser();
        
        final ReplKeyword keywordAdd         = new ReplKeyword("add");
        final ReplKeyword keywordData        = new ReplKeyword("data");
        final ReplKeyword keywordPoint       = new ReplKeyword("point");
        final ReplKeyword keywordSize        = new ReplKeyword("size");
        final ReplKeyword keywordPrint       = new ReplKeyword("print");
        final ReplKeyword keywordQuit        = new ReplKeyword("quit");
        final ReplKeyword keywordCoefficient = new ReplKeyword("coefficient");
        
        final ReplInteger replInteger = new ReplInteger();
        final ReplReal    replReal    = new ReplReal();
        
        final List<ReplTokenDescriptor> descriptorList = new ArrayList<>();
        
        descriptorList.add(keywordAdd);
        descriptorList.add(keywordData);
        descriptorList.add(keywordPoint);
        descriptorList.add(replReal);
        descriptorList.add(replReal);
        
        final ReplAction addDataPointAction = new ReplAction() {
            @Override
            public void act(List<Object> actionParameterList) {
                final double x = toDouble(actionParameterList.get(0));
                final double y = toDouble(actionParameterList.get(1));
                CorrelationCoefficientDemoApp.this.addDataPoint(x, y);
            }
        };
        
        final ReplStatement statementAddDataPoint = 
                new ReplStatement(descriptorList, 
                                  addDataPointAction);
        
        parser.addStatement(statementAddDataPoint);
        
        descriptorList.clear();
        descriptorList.add(keywordAdd);
        descriptorList.add(keywordPoint);
        descriptorList.add(replReal);
        descriptorList.add(replReal);
        
        final ReplStatement statementAddPoint = 
                new ReplStatement(descriptorList, addDataPointAction);
        
        parser.addStatement(statementAddPoint);
        
        descriptorList.clear();
        descriptorList.add(keywordAdd);
        descriptorList.add(replReal);
        descriptorList.add(replReal);
        
        final ReplStatement statementAdd = 
                new ReplStatement(descriptorList, addDataPointAction);
        
        parser.addStatement(statementAdd);
        
        descriptorList.clear();
        descriptorList.add(keywordSize);
        
        final ReplStatement statementSize = 
                new ReplStatement(descriptorList, new ReplAction() {
                    
            @Override
            public void act(List<Object> actionParameterList) {
                System.out.println(dataPointList.size());
            }
        }); 
        
        parser.addStatement(statementSize);
     
        descriptorList.clear();
        descriptorList.add(keywordCoefficient);
        
        final ReplStatement statementCoefficient = 
                new ReplStatement(descriptorList, new ReplAction() {
                    
            @Override
            public void act(List<Object> actionParameterList) {
                System.out.println(computeCorrelation());
            }
        });
        
        parser.addStatement(statementCoefficient);
        
        descriptorList.clear();
        descriptorList.add(keywordPrint);
        
        final ReplStatement statementPrintAll = 
                new ReplStatement(descriptorList, new ReplAction() {
                    
            @Override
            public void act(List<Object> actionParameterList) {
                for (final DataPoint dp : dataPointList) {
                    System.out.println(dp.toString());
                }
            }
        });
        
        parser.addStatement(statementPrintAll);
        
        descriptorList.clear();
        descriptorList.add(keywordPrint);
        descriptorList.add(replInteger);
        
        final ReplStatement statementPrintOne = 
                new ReplStatement(descriptorList, new ReplAction() {
                    
            @Override
            public void act(List<Object> actionParameterList) {
                final int index = toInt(actionParameterList.get(0));
                System.out.println(dataPointList.get(index).toString());
            }
        });
        
        parser.addStatement(statementPrintOne);
        
        descriptorList.clear();
        descriptorList.add(keywordPrint);
        descriptorList.add(replInteger);
        descriptorList.add(replInteger);
        
        final ReplStatement statementPrintRange = 
                new ReplStatement(descriptorList, new ReplAction() {
                    
            @Override
            public void act(List<Object> actionParameterList) {
                final int index0 = toInt(actionParameterList.get(0));
                final int index1 = toInt(actionParameterList.get(1));
                final List<DataPoint> view = dataPointList.subList(index0, 
                                                                   index1);
                
                for (final DataPoint dp : view) {
                    System.out.println(dp.toString());
                }
            }
        });
        
        parser.addStatement(statementPrintRange);
        
        descriptorList.clear();
        descriptorList.add(keywordQuit);
        
        final ReplStatement statementQuit = 
                new ReplStatement(descriptorList, new ReplAction() {
        
            @Override
            public void act(List<Object> actionParameterList) {
                System.out.println("\nBye!");
                System.exit(0);
            }
        });
        
        parser.addStatement(statementQuit);
        
        return parser;
    }
    
    private static final class DataPoint {
        final double x;
        final double y;
        
        DataPoint(final double x, final double y) {
            this.x = x;
            this.y = y;
        }
        
        public String toString() {
            return String.format("%f %f", x, y);
        }
    }
}
