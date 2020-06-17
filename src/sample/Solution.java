package sample;

import sample.Utilities.IntegerTooBigException;

import javax.xml.crypto.Data;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Solution {

    private double x1,x2,x3;
    private double y1,y2,y3;

    private double evaluation;

    private int generation;

    private static final double maxPositionValue = 100.0;
    private static final double minPositionValue = 0.0;

    public Solution(){
        x1 = 0;
        x2 = 0;
        x3 = 0;
        y1 = 0;
        y2 = 0;
        y3 = 0;
        evaluation = 0;
    }

    public Solution(double x1,double x2,double x3,double y1,double y2,double y3){
        this.x1 = truncate(x1);
        this.x2 = truncate(x2);
        this.x3 = truncate(x3);
        this.y1 = truncate(y1);
        this.y2 = truncate(y2);
        this.y3 = truncate(y3);
        evaluation = 0;

    }

    public void mutate(double mutationChance, String mutationMethod){
        double tmp = Math.random();
        if(tmp <= (mutationChance/100)){
            switch (mutationMethod){
                case "Uniform" : uniformMutation(); break;
            }
        }
    }

    private void uniformMutation(){

        // Gaussian * variance + meanValue
        Random random = new Random();
        double newValue = random.nextGaussian()*16.6 + 50.0;
        if(newValue < 0) newValue = 0;
        if(newValue > 100) newValue = 100;
        int gene = random.nextInt(6);
        switch (gene){
            case 0 : this.x1 = newValue; break;
            case 1 : this.x2 = newValue; break;
            case 2 : this.x3 = newValue; break;
            case 3 : this.y1 = newValue; break;
            case 4 : this.y2 = newValue; break;
            case 5 : this.y3 = newValue; break;
        }
    }

    public double evaluate(ArrayList<City> cityList,double dMAX){

        double[] remainingPopulationWithBadSignalList = new double[cityList.size()];
        double totalPopulationCoverage = 0;
        for (int i=0;i<cityList.size();i++){
            remainingPopulationWithBadSignalList[i] = cityList.get(i).getPopulation();
        }
        for(int i=0;i<cityList.size();i++){
            double d = Math.sqrt(Math.pow((x1 - cityList.get(i).getX()),2) + Math.pow((y1 - cityList.get(i).getY()),2));
            double c = cityList.get(i).getPopulation()*dMAX/(20*d+0.00001);
            remainingPopulationWithBadSignalList[i] = remainingPopulationWithBadSignalList[i] - c;
            if(remainingPopulationWithBadSignalList[i] > 0) {
                d = Math.sqrt(Math.pow((x2 - cityList.get(i).getX()),2) + Math.pow((y2 - cityList.get(i).getY()),2));
                c = cityList.get(i).getPopulation()*dMAX / (20 * d + 0.00001);
                remainingPopulationWithBadSignalList[i] = remainingPopulationWithBadSignalList[i] - c;
            }
            if(remainingPopulationWithBadSignalList[i] > 0) {
                d = Math.sqrt(Math.pow((x3 - cityList.get(i).getX()),2) + Math.pow((y3 - cityList.get(i).getY()),2));
                c = cityList.get(i).getPopulation()*dMAX / (20 * d + 0.00001);
                remainingPopulationWithBadSignalList[i] = remainingPopulationWithBadSignalList[i] - c;
            }
            if(remainingPopulationWithBadSignalList[i] < 0){
                remainingPopulationWithBadSignalList[i] = 0;
            }
            totalPopulationCoverage = totalPopulationCoverage + cityList.get(i).getPopulation() - remainingPopulationWithBadSignalList[i];
        }
        evaluation = totalPopulationCoverage;
        return evaluation;
    }

    public static Solution[] reproduceByLinearCrossover(Solution[] parents,ArrayList<City> cityList,double dmax){

        Solution[] tmp = new Solution[3];
        tmp[0] = new Solution();
        tmp[1] = new Solution();
        tmp[2] = new Solution();

        tmp[0].setX1(0.5*parents[0].getX1() + 0.5*parents[1].getX1());
        tmp[0].setX2(0.5*parents[0].getX2() + 0.5*parents[1].getX2());
        tmp[0].setX3(0.5*parents[0].getX3() + 0.5*parents[1].getX3());
        tmp[0].setY1(0.5*parents[0].getY1() + 0.5*parents[1].getY1());
        tmp[0].setY2(0.5*parents[0].getY2() + 0.5*parents[1].getY2());
        tmp[0].setY3(0.5*parents[0].getY3() + 0.5*parents[1].getY3());
        tmp[0].evaluate(cityList,dmax);
        tmp[0].setGeneration(parents[0].getGeneration());
        tmp[0].checkBounds();

        tmp[1].setX1(1.5*parents[0].getX1() - 0.5*parents[1].getX1());
        tmp[1].setX2(1.5*parents[0].getX2() - 0.5*parents[1].getX2());
        tmp[1].setX3(1.5*parents[0].getX3() - 0.5*parents[1].getX3());
        tmp[1].setY1(1.5*parents[0].getY1() - 0.5*parents[1].getY1());
        tmp[1].setY2(1.5*parents[0].getY2() - 0.5*parents[1].getY2());
        tmp[1].setY3(1.5*parents[0].getY3() - 0.5*parents[1].getY3());
        tmp[1].evaluate(cityList,dmax);
        tmp[1].setGeneration(parents[0].getGeneration());
        tmp[1].checkBounds();

        tmp[2].setX1(1.5*parents[1].getX1() - 0.5*parents[0].getX1());
        tmp[2].setX2(1.5*parents[1].getX2() - 0.5*parents[0].getX2());
        tmp[2].setX3(1.5*parents[1].getX3() - 0.5*parents[0].getX3());
        tmp[2].setY1(1.5*parents[1].getY1() - 0.5*parents[0].getY1());
        tmp[2].setY2(1.5*parents[1].getY2() - 0.5*parents[0].getY2());
        tmp[2].setY3(1.5*parents[1].getY3() - 0.5*parents[0].getY3());
        tmp[2].evaluate(cityList,dmax);
        tmp[2].setGeneration(parents[0].getGeneration());
        tmp[2].checkBounds();

        //Keep the two best children
        Solution[] children = new Solution[2];
        if(tmp[0].getEvaluation() > tmp[1].getEvaluation()){
            children[0] = tmp[0];
            if(tmp[1].getEvaluation() > tmp[2].getEvaluation()){
                children[1] = tmp[1];
            }
            else{
                children[1] = tmp[2];
            }
        }
        else{
            children[0] = tmp[1];
            if(tmp[0].getEvaluation() > tmp[2].getEvaluation()){
                children[1] = tmp[0];
            }
            else{
                children[1] = tmp[2];
            }

        }
        return children;
    }

    public static Solution[] reproduceByArithmeticCrossover(Solution[] parents){

        Solution[] children = new Solution[2];
        children[0] = new Solution();
        children[1] = new Solution();

        double alpha;
        alpha = generateAlpha(parents[0].getX1(),parents[1].getX1());

        children[0].setX1(alpha*parents[0].getX1() + (1 - alpha)*parents[1].getX1());
        children[0].setX2(alpha*parents[0].getX2() + (1 - alpha)*parents[1].getX2());
        children[0].setX3(alpha*parents[0].getX3() + (1 - alpha)*parents[1].getX3());
        children[0].setY1(alpha*parents[0].getY1() + (1 - alpha)*parents[1].getY1());
        children[0].setY2(alpha*parents[0].getY2() + (1 - alpha)*parents[1].getY2());
        children[0].setY3(alpha*parents[0].getY3() + (1 - alpha)*parents[1].getY3());
        children[0].checkBounds();
        alpha = generateAlpha(parents[1].getX1(),parents[0].getX1());

        children[1].setX1(alpha*parents[1].getX1() + (1 - alpha)*parents[0].getX1());
        children[1].setX2(alpha*parents[1].getX2() + (1 - alpha)*parents[0].getX2());
        children[1].setX3(alpha*parents[1].getX3() + (1 - alpha)*parents[0].getX3());
        children[1].setY1(alpha*parents[1].getY1() + (1 - alpha)*parents[0].getY1());
        children[1].setY2(alpha*parents[1].getY2() + (1 - alpha)*parents[0].getY2());
        children[1].setY3(alpha*parents[1].getY3() + (1 - alpha)*parents[0].getY3());
        children[1].checkBounds();
        return children;
    }

    public static double generateAlpha(double parent1,double parent2){


        double upperLimit;
        double lowerLimit;
        double alpha;
        if(parent1 > parent2){
            upperLimit = (maxPositionValue - parent2) / (parent1 - parent2);
            lowerLimit = (minPositionValue - parent2) / (parent1 - parent2);
        }
        else if(parent1 < parent2){
            upperLimit = (maxPositionValue - parent1) / (parent2 - parent1);
            lowerLimit = (minPositionValue - parent1) / (parent2 - parent1);
        }
        else{
            return Math.random();
        }
        //Random type ----> x = (int)(Math.random()*((max-min)+1))+min;
        //produces random values in the [min,max] range.

        do{
            alpha = Math.random()*(upperLimit-lowerLimit) + lowerLimit;
        }
        while(alpha >= upperLimit || alpha <= lowerLimit);
        /*
        System.out.println("upperLimit = " + upperLimit);
        System.out.println("lowerLimit = " + lowerLimit);
        System.out.println("alpha " + alpha);
        */
        return alpha;
    }

    private void checkBounds(){
        if(x1 > 100){
            x1 = 100;
        }
        if(x1 < 0){
            x1 = 0;
        }
        if(x2 > 100){
            x2 = 100;
        }
        if(x2 < 0){
            x2 = 0;
        }
        if(x3 > 100){
            x3 = 100;
        }
        if(x3 < 0){
            x3 = 0;
        }
        if(y1 > 100){
            y1 = 100;
        }
        if(y1 < 0){
            y1 = 0;
        }
        if(y2 > 100){
            y2 = 100;
        }
        if(y2 < 0){
            y2 = 0;
        }
        if(y3 > 100){
            y3 = 100;
        }
        if(y3 < 0){
            y3 = 0;
        }
    }

    //TODO NOT TESTED
    public static double calculateMaximumDistance(ArrayList<City> cityList){

        double maximumDistance = 0;
        double distance = 0;
        for(int i=0;i<cityList.size();i++){
            for(int j=0;j<cityList.size();j++){
                if(i!=j)
                    distance = Math.sqrt(Math.pow(cityList.get(i).getX() - cityList.get(j).getX(),2) + Math.pow(cityList.get(i).getY() - cityList.get(j).getY(),2));
                if(distance > maximumDistance){
                    maximumDistance = distance;
                }
            }
        }
        return maximumDistance;
    }

    /*
        The String returned will be composed by the first half of parent1 and the second half of parent2.
     */

    public static String merge(String parent1, String parent2){

        StringBuilder sb = new StringBuilder();

        try {
            String s1 = complete(parent1);
            String s2 = complete(parent2);
            sb.append(s1,0,16);
            sb.append(s2,16,32);
        } catch (IntegerTooBigException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String complete(String string) throws IntegerTooBigException {
        //if(string.length() > 32) throw new IntegerTooBigException();
        StringBuilder sb = new StringBuilder();
        if(string.length() < 32){
            for(int i=0;i<32 - string.length();i++){
                sb.append("0");
            }
        }
        sb.append(string);
        return sb.toString();
    }

    public static String binaryToGrey(String binary){

        StringBuilder sb = new StringBuilder();
        sb.append(binary.charAt(0));
        for (int i=1;i<binary.length();i++){
            sb.append(XOR(binary.charAt(i-1),binary.charAt(i)));
        }
        return sb.toString();
    }

    public static String greyToBinary(String grey){

        StringBuilder sb = new StringBuilder();
        sb.append(grey.charAt(0));
        for (int i=1;i<grey.length();i++){
            sb.append(XOR(sb.charAt(i-1),grey.charAt(i)));
        }
        return sb.toString();
    }

    public static char XOR (char x, char y){
        if(x == '0' && y == '0') return '0';
        if(x == '1' && y == '0') return '1';
        if(x == '0' && y == '1') return '1';
        return '0';
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = truncate(x1);

    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = truncate(x2);
    }

    public double getX3() {
        return x3;
    }

    public void setX3(double x3) {
        this.x3 = truncate(x3);
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = truncate(y1);
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = truncate(y2);
    }

    public double getY3() {
        return y3;
    }

    public void setY3(double y3) {
        this.y3 = truncate(y3);
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public double truncate(double a){
        if (a >= 0){
            return Math.floor(a*100)/100;
        }
        else{
            return Math.ceil(a*100)/100;
        }
    }


}
