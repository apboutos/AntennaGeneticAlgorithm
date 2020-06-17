package sample;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void evaluate() {
    }

    @org.junit.jupiter.api.Test
    void reproduceByMiddleCrossover() {

        int s1x1 = 1150;  //binary : 0000000000000000|0000010001111110
        int s1x2 = 2000;  //binary : 0000000000000000|0000011111010000
        int s1x3 = 3150;  //binary : 0000000000000000|0000110001001110

        int s1y1 = 252000;//binary : 0000000000000011|1101100001100000
        int s1y2 = 58;    //binary : 0000000000000000|0000000000111010
        int s1y3 = 8325;  //binary : 0000000000000000|0010000010000101

        int s2x1 = 7898;    //binary : 0000000000000000|0001111011011010
        int s2x2 = 7777;    //binary : 0000000000000000|0001111001100001
        int s2x3 = 456789;  //binary : 0000000000000110|1111100001010101

        int s2y1 = 252445;  //binary : 0000000000000011|1101101000011101
        int s2y2 = 5845;    //binary : 0000000000000000|0001011011010101
        int s2y3 = 832535;  //binary : 0000000000001100|1011010000010111

        Solution s1 = new Solution(s1x1, s1x2, s1x3, s1y1, s1y2, s1y3);
        Solution s2 = new Solution(s2x1, s2x2, s2x3, s2y1, s2y2, s2y3);
        Solution[] parents = {s1, s2};//new Solution[2];
        Solution[] children = Solution.reproduceByArithmeticCrossover(parents);

        assertEquals(7898, children[0].getX1());     //0000000000000000|0001111011011010
        assertEquals(7777, children[0].getX2());     //0000000000000000|0001111001100001
        assertEquals(63573, children[0].getX3());    //0000000000000000|1111100001010101
        assertEquals(252445, children[0].getY1());   //0000000000000011|1101101000011101
        assertEquals(5845, children[0].getY2());     //0000000000000000|0001011011010101
        assertEquals(46103, children[0].getY3());    //0000000000000000|1011010000010111

        assertEquals(1150, children[1].getX1());     //0000000000000000|0000010001111110
        assertEquals(2000, children[1].getX2());     //0000000000000000|0000011111010000
        assertEquals(396366, children[1].getX3());   //0000000000000110|0000110001001110
        assertEquals(252000, children[1].getY1());   //0000000000000011|1101100001100000
        assertEquals(58, children[1].getY2());       //0000000000000000|0000000000111010
        assertEquals(794757, children[1].getY3());   //0000000000001100|0010000010000101
    }

    @Test
    void merge() {

        int parent1 = 1150;  //binary : 00000000000000000000010001111110
        int parent2 = 2000;  //binary : 00000000000000000000011111010000

        int s1x1 = 1150;  //binary : 0000000000000000|0000010001111110
        int s1x2 = 2000;  //binary : 0000000000000000|0000011111010000
        int s1x3 = 3150;  //binary : 0000000000000000|0000110001001110


        int s1y1 = 252000;//binary : 0000000000000011|1101100001100000
        int s1y2 = 58;    //binary : 0000000000000000|0000000000111010
        int s1y3 = 8325;  //binary : 0000000000000000|0010000010000101

        int s2x1 = 7898;    //binary : 0000000000000000|0001111011011010
        int s2x2 = 7777;    //binary : 0000000000000000|0001111001100001
        int s2x3 = 456789;  //binary : 0000000000000110|1111100001010101

        int s2y1 = 252445;  //binary : 0000000000000011|1101101000011101
        int s2y2 = 5845;    //binary : 0000000000000000|0001011011010101
        int s2y3 = 832535;  //binary : 0000000000001100|1011010000010111


        try {
            assertEquals("00000000000000000000011111010000", Solution.merge(Integer.toBinaryString(parent1), (Integer.toBinaryString(parent2))));
            assertEquals("00000000000000000000010001111110", Solution.merge(Integer.toBinaryString(parent2), Integer.toBinaryString(parent1)));
            assertEquals("00000000000000000001111011011010", Solution.merge(Integer.toBinaryString(s1x1), (Integer.toBinaryString(s2x1))));
            assertEquals("00000000000000000001111001100001", Solution.merge(Integer.toBinaryString(s1x2), (Integer.toBinaryString(s2x2))));
            assertEquals("00000000000000001111100001010101", Solution.merge(Integer.toBinaryString(s1x3), (Integer.toBinaryString(s2x3))));
            assertEquals("00000000000000111101101000011101", Solution.merge(Integer.toBinaryString(s1y1), (Integer.toBinaryString(s2y1))));
            assertEquals("00000000000000000001011011010101", Solution.merge(Integer.toBinaryString(s1y2), (Integer.toBinaryString(s2y2))));
            assertEquals("00000000000000001011010000010111", Solution.merge(Integer.toBinaryString(s1y3), (Integer.toBinaryString(s2y3))));

            assertEquals("00000000000000000000010001111110", Solution.merge((Integer.toBinaryString(s2x1)), (Integer.toBinaryString(s1x1))));
            assertEquals("00000000000000000000011111010000", Solution.merge((Integer.toBinaryString(s2x2)), (Integer.toBinaryString(s1x2))));
            assertEquals("00000000000001100000110001001110", Solution.merge((Integer.toBinaryString(s2x3)), (Integer.toBinaryString(s1x3))));
            assertEquals("00000000000000111101100001100000", Solution.merge((Integer.toBinaryString(s2y1)), (Integer.toBinaryString(s1y1))));
            assertEquals("00000000000000000000000000111010", Solution.merge((Integer.toBinaryString(s2y2)), (Integer.toBinaryString(s1y2))));
            assertEquals("00000000000011000010000010000101", Solution.merge((Integer.toBinaryString(s2y3)), (Integer.toBinaryString(s1y3))));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void complete() {

        try {
            assertEquals("00000000000000000000000000001001", Solution.complete("1001"));
            assertEquals("00000000000000000000000000000000", Solution.complete(""));

        } catch (Exception e) {
            fail();
        }
        try {
            Solution.complete("000000000000000000000000000000000000000");

        } catch (Exception e) {
            return;
        }


    }

    @Test
    void generateRandomNumbers(){

        double[] table = new double[100];
        for(int i=0;i<100;i++){
            table[i] = Math.random()*100;
            System.out.println(i + " " + table[i]);
        }
    }

    @Test
    void uniformMutation() {
        Random random = new Random();
        int counter = 0;
        for (int i = 0; i < 10000; i++) {
            double newValue = random.nextGaussian() * 16.6 + 50.0;
            if (newValue < 0) {
                newValue = 0;
                counter++;
            }
            if (newValue > 100) {
                newValue = 100;
                counter++;
            }
            System.out.println(newValue);
        }
        System.out.println("Values out of bounds " + counter);

    }


    @Test
    void rankedRouletteSelection() {

        int populationSize = 100;
        ArrayList<Solution> populationList = new ArrayList<>();
        ArrayList<Solution> parentList = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Solution tmp = new Solution();
            tmp.setX1((Math.random() * ((100) + 1)));
            tmp.setX2((Math.random() * ((100) + 1)));
            tmp.setX3((Math.random() * ((100) + 1)));
            tmp.setY1((Math.random() * ((100) + 1)));
            tmp.setY2((Math.random() * ((100) + 1)));
            tmp.setY3((Math.random() * ((100) + 1)));
            tmp.setEvaluation(Math.random() * 1000);
            populationList.add(tmp);
        }

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
        /*
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

        /*
        for (int i = 0; i < populationSize; i++) {
            System.out.println("Solution " + i + " fitness " +(int)populationTable[i].getEvaluation() + " % " + fitnessPercent[i]);
        }*/

        //Find the cumulative probabilities.
        cumulativeFitnessPercent[0] = fitnessPercent[0];
        for (int i=1;i<populationSize;i++){
            cumulativeFitnessPercent[i] = fitnessPercent[i] + cumulativeFitnessPercent[i-1];
        }

        for (int i = 0; i < populationSize; i++) {
            System.out.println("Solution " + i + " fitness " +(int)populationTable[i].getEvaluation() + " % " + fitnessPercent[i] + " cum% " + cumulativeFitnessPercent[i]);
        }

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

        for(int i=0;i<parentList.size();i++){
            System.out.println(i + "evaluation " + parentList.get(i).getEvaluation());
        }

    }

    @Test
    void reproduceByArithmeticCrossover() {



        //Solution s1 = new Solution(31.22, 28.33, 16.18, 21.32, 91.9, 5.1);
        //Solution s2 = new Solution(28.1, 7.77, 31.2, 68.1, 45.89, 66.44);

        Solution s3 = new Solution(12.23, 0.02, 99.44, 45.63, 65.8, 71.38);
        Solution s4 = new Solution(75.79, 29.74, 56.21, 89.98, 2.11, 51.12);

        Solution[] table = {s3, s4};
        System.out.println("alpha = " + Solution.generateAlpha(table[0].getX1(),table[1].getX1()));

        Solution[] children;
        children = Solution.reproduceByArithmeticCrossover(table);
        System.out.println(children[0].getX1() + " " + children[0].getX2() + " " + children[0].getX3() + " " +
                children[0].getY1() + " " + children[0].getY2() + " " + children[0].getY3() + " ");
        System.out.println(children[1].getX1() + " " + children[1].getX2() + " " + children[1].getX3() + " " +
                children[1].getY1() + " " + children[1].getY2() + " " + children[1].getY3() + " ");

    }
    @Test
    void binaryToGrey(){

        int parent1 = 1150;  //binary : 00000000000000000000010001111110
        int parent2 = 2000;  //binary : 00000000000000000000011111010000

        int s1x1 = 1150;  //binary : 0000000000000000|0000010001111110
        int s1x2 = 2000;  //binary : 0000000000000000|0000011111010000
        int s1x3 = 3150;  //binary : 0000000000000000|0000110001001110


        int s1y1 = 252000;//binary : 0000000000000011|1101100001100000
        int s1y2 = 58;    //binary : 0000000000000000|0000000000111010
        int s1y3 = 8325;  //binary : 0000000000000000|0010000010000101

        int s2x1 = 7898;    //binary : 0000000000000000|0001111011011010
        int s2x2 = 7777;    //binary : 0000000000000000|0001111001100001
        int s2x3 = 456789;  //binary : 0000000000000110|1111100001010101

        int s2y1 = 252445;  //binary : 0000000000000011|1101101000011101
        int s2y2 = 5845;    //binary : 0000000000000000|0001011011010101
        int s2y3 = 832535;  //binary : 0000000000001100|1011010000010111

        assertEquals("1101",Solution.binaryToGrey("1001"));
        assertEquals("00000000000000000000011001000001",Solution.binaryToGrey("00000000000000000000010001111110"));
        assertEquals("00000000000000000000010000111000",Solution.binaryToGrey("00000000000000000000011111010000"));
        assertEquals("00000000000000000000101001101001",Solution.binaryToGrey("00000000000000000000110001001110"));
        assertEquals("00000000000000100011010001010000",Solution.binaryToGrey("00000000000000111101100001100000"));
        assertEquals("00000000000000000000000000100111",Solution.binaryToGrey("00000000000000000000000000111010"));
        assertEquals("00000000000000000001000101010001",Solution.binaryToGrey("00000000000000000001111001100001"));

    }

    @Test
    void greyToBinary(){

        assertEquals("1001",Solution.greyToBinary("1101"));
        assertEquals("00000000000000000000010001111110",Solution.greyToBinary("00000000000000000000011001000001"));
        assertEquals("00000000000000000000011111010000",Solution.greyToBinary("00000000000000000000010000111000"));
        assertEquals("00000000000000000000110001001110",Solution.greyToBinary("00000000000000000000101001101001"));
        assertEquals("00000000000000111101100001100000",Solution.greyToBinary("00000000000000100011010001010000"));
        assertEquals("00000000000000000000000000111010",Solution.greyToBinary("00000000000000000000000000100111"));
        assertEquals("00000000000000000001111001100001",Solution.greyToBinary("00000000000000000001000101010001"));

    }
    @org.junit.jupiter.api.Test
    void calculateMaximumDistance() {
    }




}