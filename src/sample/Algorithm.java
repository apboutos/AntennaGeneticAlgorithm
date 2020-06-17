package sample;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Algorithm implements Runnable {


    private Parameters parameters;
    private ArrayList<Solution> populationList;
    private ArrayList<Solution> parentList;
    private ArrayList<Solution> childrenList;
    private double dmax;
    private Solution bestSolutionWithElitisism;
    private Solution bestSolutionWithoutElitisism;
    private Solution allTimeBestSolutionWithoutElitisism;
    private int populationSize;
    private Controller controller;
    private int generations;
    private int currentGeneration;
    private boolean endflag;

    public Algorithm(Controller controller,Parameters parameters){
        this.controller = controller;
        this.parameters = parameters;
        populationSize = parameters.getPopulation();
        dmax =(Solution.calculateMaximumDistance(parameters.getCityList()));
        generations = parameters.getGenerations();
        populationList = new ArrayList<Solution>();
        parentList = new ArrayList<>();
        childrenList = new ArrayList<>();
        bestSolutionWithElitisism = new Solution();
        bestSolutionWithoutElitisism = new Solution();
        allTimeBestSolutionWithoutElitisism = new Solution();
    }

    @Override
    public void run(){

        currentGeneration = 1;
        generateInitialPopulation();

        endflag = false;
        System.out.println(dmax);
        while(currentGeneration <= generations) {
            findBestSolution();
            updateGraphs();
            selectParents();
            generateChildren();
            currentGeneration++;
            selectNewPopulation();

        }
    }


    private void generateInitialPopulation(){

        populationList = new ArrayList<>();

        //Random type ----> x = (int)(Math.random()*((max-min)+1))+min;
        //produces random values in the [min,max] range.
        for(int i=0;i<populationSize;i++){
            Solution tmp = new Solution();
            tmp.setX1(Math.random()*10 + 40);
            tmp.setX2(Math.random()*10 + 40);
            tmp.setX3(Math.random()*10 + 40);
            tmp.setY1(Math.random()*10 + 40);
            tmp.setY2(Math.random()*10 + 40);
            tmp.setY3(Math.random()*10 + 40);
            tmp.evaluate(parameters.getCityList(),dmax);
            tmp.setGeneration(currentGeneration);
            populationList.add(tmp);
        }


    }

    /*
        Populates the list containing the parents with members from the population list.
        Each population member can be chosen multiple times as a parent.
     */

    private void selectParents(){

        switch (parameters.getParentChoice()){
            case "Roulette Wheel"      : rouletteWheelSelection();       break;
            case "Rank Roulette Wheel" : rankedRouletteWheelSelection(); break;
            case "Tournament"          : tournamentSelection();          break;
            default                    : rouletteWheelSelection();       break;
        }


        //TODO This is quick dirty fix to a bug that sometimes the parent list is not full at this point.
        while(parentList.size()<populationSize){
            if(parentList.size()<=0){

            }else{
                parentList.add(parentList.get(parentList.size()-1));

            }

        }
        /* DEBUG
        for(int i=0;i<parentList.size();i++){
            System.out.println("Parent " + i + " Evaluation " + parentList.get(i).getEvaluation());
        }*/

    }

    private void rouletteWheelSelection(){
        double totalFitnessScore = 0;
        double [] fitnessPercent = new double[populationSize];
        double [] cumulativeFitnessPercent = new double[populationSize];
        for(Solution i : populationList){
            totalFitnessScore = totalFitnessScore + i.getEvaluation();
        }
        for (int i=0;i<populationSize;i++){
            fitnessPercent[i] = 100 * populationList.get(i).getEvaluation() / totalFitnessScore;
        }
        cumulativeFitnessPercent[0] = fitnessPercent[0];
        for (int i=1;i<populationSize;i++){
            cumulativeFitnessPercent[i] = fitnessPercent[i] + cumulativeFitnessPercent[i-1];
        }
        parentList.clear();
        for(int i=0;i<populationSize;i++){
            double K = Math.random()*100 + 1;
            for (int j=0;j<populationSize;j++){
                if (K <= cumulativeFitnessPercent[j]){
                    parentList.add(populationList.get(j));
                    break;
                }
            }
        }
    }

    private void rankedRouletteWheelSelection(){
        double totalFitnessScore = 0;
        double[] fitnessPercent = new double[populationSize];
        double[] cumulativeFitnessPercent = new double[populationSize];
        Solution[] populationTable = new Solution[populationSize];
        for (int i = 0; i < populationSize; i++) {
            populationTable[i] = populationList.get(i);
            totalFitnessScore = totalFitnessScore + populationTable[i].getEvaluation();
        }
        for (int i = 0; i < populationSize; i++) {
            fitnessPercent[i] = 100 * populationTable[i].getEvaluation() / totalFitnessScore;
        }


        //Sort the two tables in descending order according to their fitness score.
        for(int i=0;i<populationSize;i++){
            int maxPos = i;
            for(int j=i;j<populationSize;j++){
                if(fitnessPercent[maxPos] <= fitnessPercent[j]){
                    maxPos = j;
                }
            }
            double tmp = fitnessPercent[i];
            fitnessPercent[i] = fitnessPercent[maxPos];
            fitnessPercent[maxPos] = tmp;

            Solution tmps = populationTable[i];
            populationTable[i] = populationTable[maxPos];
            populationTable[maxPos] = populationTable[i];
        }
        /*DEBUG
        for (int i = 0; i < populationSize; i++) {
            System.out.println("Solution " + i + " fitness " +(int)populationTable[i].getEvaluation() + " % " + fitnessPercent[i]);
        }*/

        //Calculate the new probabilities.
        double sum = 0;
        for(int i=1;i<=populationSize;i++){
            sum = sum + i;
        }
        for(int i=0;i<populationSize;i++){
            fitnessPercent[i] = (populationSize - (i+1) + 1)/sum;
        }

        /*DEBUG
        for (int i = 0; i < populationSize; i++) {
            System.out.println("Solution " + i + " fitness " +(int)populationTable[i].getEvaluation() + " % " + fitnessPercent[i]);
        }*/

        //Find the cumulative probabilities.
        cumulativeFitnessPercent[0] = fitnessPercent[0];
        for (int i=1;i<populationSize;i++){
            cumulativeFitnessPercent[i] = fitnessPercent[i] + cumulativeFitnessPercent[i-1];
        }
        /*DEBUG
        for (int i = 0; i < populationSize; i++) {
            System.out.println("Solution " + i + " fitness " +(int)populationTable[i].getEvaluation() + " % " + fitnessPercent[i] + " cum% " + cumulativeFitnessPercent[i]);
        }*/

        parentList.clear();

        //Pick the parents according to their cumulative probability.
        for(int i=0;i<populationSize;i++){
            double K = Math.random();
            for (int j=0;j<populationSize;j++){
                if (K <= cumulativeFitnessPercent[j]){
                    parentList.add(populationList.get(j));
                    break;
                }
            }
        }

        /* DEBUG
        for(int i=0;i<parentList.size();i++){
            System.out.println(i + "evaluation " + parentList.get(i).getEvaluation());
        }*/
    }


    private void tournamentSelection(){
        parentList.clear();
        for(int i=0;i<populationSize;i++){
            int s1 = (int)(Math.random()*populationSize);
            int s2 = (int)(Math.random()*populationSize);
            if(populationList.get(s1).getEvaluation() >= populationList.get(s2).getEvaluation()){
                parentList.add(populationList.get(s1));
            }
            else{
                parentList.add(populationList.get(s2));
            }
        }
    }

    private void findBestSolution(){

        if(parameters.isElitism()){

            for(Solution i : populationList){

                if(i.getEvaluation() > bestSolutionWithElitisism.getEvaluation()){
                    bestSolutionWithElitisism = i;
                }
            }
        }else{
            bestSolutionWithoutElitisism = populationList.get(0);
            for(Solution i : populationList){

                if(i.getEvaluation() > bestSolutionWithoutElitisism.getEvaluation()){
                    bestSolutionWithoutElitisism = i;
                }
            }
            if(bestSolutionWithoutElitisism.getEvaluation() > allTimeBestSolutionWithoutElitisism.getEvaluation()){
                allTimeBestSolutionWithoutElitisism = bestSolutionWithoutElitisism;
            }
        }


    }

    private void updateGraphs(){
        if(parameters.isElitism()){
            controller.updateScatterGraphWithBestSolution(bestSolutionWithElitisism);
            controller.updateResultsArea(createBestSolutionText(bestSolutionWithElitisism));
            controller.updateLineChartWithBestSolution(bestSolutionWithElitisism);
        }else{
            controller.updateLineChartWithBestSolution(bestSolutionWithoutElitisism);
            controller.updateScatterGraphWithBestSolution(allTimeBestSolutionWithoutElitisism);
            controller.updateResultsArea(createBestSolutionText(allTimeBestSolutionWithoutElitisism));
            //System.out.println(createBestSolutionText());
        }

    }

    public String createBestSolutionText(Solution bestSolution){
        StringBuilder sb = new StringBuilder();
        DecimalFormat format = new DecimalFormat("#.##");
        sb.append("Best solution :  [");
        sb.append(format.format(bestSolution.getX1()));
        sb.append(" ");
        sb.append(format.format(bestSolution.getY1()));
        sb.append("][");
        sb.append(format.format(bestSolution.getX2()));
        sb.append(" ");
        sb.append(format.format(bestSolution.getY2()));
        sb.append("][");
        sb.append(format.format(bestSolution.getX3()));
        sb.append(" ");
        sb.append(format.format(bestSolution.getY3()));
        sb.append("]");
        sb.append("\nCoverage: ");
        sb.append(format.format(bestSolution.getEvaluation()));
        sb.append("\nGeneration: ");
        sb.append(Double.toString(bestSolution.getGeneration()));
        return sb.toString();
    }

    private void generateChildren(){

        ArrayList<Solution> tmpParentList = (ArrayList<Solution>) parentList.clone();
        childrenList.clear();
        while(!tmpParentList.isEmpty()){
            int tmp = (int)(Math.random()*tmpParentList.size());
            int tmp2;
            do{
                tmp2 = (int)(Math.random()*tmpParentList.size());
            }while(tmp == tmp2);

            Solution[] parents = new Solution[2];
            parents[0] = tmpParentList.get(tmp);
            parents[1] = tmpParentList.get(tmp2);

            Solution[] children;
            switch (parameters.getReproductionMethod()){
                case "Linear Crossover"    : children = Solution.reproduceByLinearCrossover(parents,parameters.getCityList(),dmax); break;
                case "Arithmetic Crossover": children = Solution.reproduceByArithmeticCrossover(parents); break;
                default : children = Solution.reproduceByLinearCrossover(parents,parameters.getCityList(),dmax); break;
            }
            children[0].mutate(parameters.getMutationChance(),parameters.getMutationMethod());
            children[1].mutate(parameters.getMutationChance(),parameters.getMutationMethod());
            children[0].evaluate(parameters.getCityList(),dmax);
            children[1].evaluate(parameters.getCityList(),dmax);
            children[0].setGeneration(currentGeneration);
            children[1].setGeneration(currentGeneration);
            childrenList.add(children[0]);
            childrenList.add(children[1]);

            tmpParentList.remove(tmp);
            if(tmp<tmp2){
                tmpParentList.remove(tmp2-1);
            }else{
                tmpParentList.remove(tmp2);
            }

        }

        /*
        for(int i=0;i<childrenList.size();i++) {
            System.out.println("child " + i + " x1= " + childrenList.get(i).getX1() + " y1= " + childrenList.get(i).getY1() + " evaluation = " + childrenList.get(i).getEvaluation());
        }*/
    }

    //TODO MUST FIX very bad duplicate code implementation.
    private void selectNewPopulation() {
        double remainingPopulationSlots = populationSize;
        HashSet<Solution> parentSet = new HashSet<>();
        for(Solution i : parentList){
            parentSet.add(i);
        }
        parentList.clear();
        for(Solution i : parentSet){
            parentList.add(i);
        }
        parentSet = null;
        populationList.clear();

        if (parameters.isElitism()) {

            bestSolutionWithElitisism.setGeneration(currentGeneration);
            populationList.add(bestSolutionWithElitisism);
            remainingPopulationSlots--;
        }
        if (parameters.getParentChildRatio().equals("25:75")) {
            while (remainingPopulationSlots > 0) {
                if(remainingPopulationSlots > 0) {
                    if (!parentList.isEmpty()) {
                        populationList.add(parentList.get(0));
                        parentList.remove(0);
                        remainingPopulationSlots--;
                    }
                    if (!childrenList.isEmpty()) {
                        populationList.add(childrenList.get(0));
                        childrenList.remove(0);
                        remainingPopulationSlots--;
                    }
                    if (!childrenList.isEmpty()) {
                        populationList.add(childrenList.get(0));
                        childrenList.remove(0);
                        remainingPopulationSlots--;
                    }
                    if (!childrenList.isEmpty()) {
                        populationList.add(childrenList.get(0));
                        childrenList.remove(0);
                        remainingPopulationSlots--;
                    }
                }
            }
        }
        else  if (parameters.getParentChildRatio().equals("50:50")) {
            while (remainingPopulationSlots > 0) {
                if (remainingPopulationSlots > 0) {
                    if (!parentList.isEmpty()) {
                        populationList.add(parentList.get(0));
                        parentList.remove(0);
                        remainingPopulationSlots--;
                    }
                    if (!childrenList.isEmpty()) {
                        populationList.add(childrenList.get(0));
                        childrenList.remove(0);
                        remainingPopulationSlots--;
                    }
                    if (!childrenList.isEmpty()) {
                        populationList.add(childrenList.get(0));
                        childrenList.remove(0);
                        remainingPopulationSlots--;
                    }
                    if (!childrenList.isEmpty()) {
                        populationList.add(childrenList.get(0));
                        childrenList.remove(0);
                        remainingPopulationSlots--;
                    }
                }
            }
        }
        else if (parameters.getParentChildRatio().equals("75:25")) {
            while (remainingPopulationSlots > 0) {
                if (remainingPopulationSlots > 0) {
                    if (!parentList.isEmpty()) {
                        populationList.add(parentList.get(0));
                        parentList.remove(0);
                        remainingPopulationSlots--;
                    }
                    if (!childrenList.isEmpty()) {
                        populationList.add(childrenList.get(0));
                        childrenList.remove(0);
                        remainingPopulationSlots--;
                    }
                    if (!childrenList.isEmpty()) {
                        populationList.add(childrenList.get(0));
                        childrenList.remove(0);
                        remainingPopulationSlots--;
                    }
                    if (!childrenList.isEmpty()) {
                        populationList.add(childrenList.get(0));
                        childrenList.remove(0);
                        remainingPopulationSlots--;
                    }
                }
            }
        }
        for(Solution i : populationList){
            i.setGeneration(currentGeneration);
        }
        /*debug
        System.out.println("ParentSetSize = " + parentList.size());
        System.out.println("ChildrenListSize = " + childrenList.size());
        System.out.println("PopoulationListSize = " + populationList.size());*/
        /*
        System.out.println("Population List Generation : " + currentGeneration);
        for(int i=0;i<populationSize;i++){
            System.out.println(i + "  " + populationList.get(i).getEvaluation());

        }*/

    }

    public void setEndflag(boolean endflag){
        this.endflag = endflag;
    }

}

