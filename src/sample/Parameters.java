package sample;

import java.util.ArrayList;

public class Parameters {


    private double mutationChance;
    private String parentChoice;
    private String parentChildRatio;
    private int generations;
    private ArrayList<City> cityList;
    private String reproductionMethod;
    private String mutationMethod;
    private int population;
    private boolean elitism;

    public boolean isElitism() {
        return elitism;
    }

    public void setElitism(boolean elitism) {
        this.elitism = elitism;
    }

    public double getMutationChance() {
        return mutationChance;
    }

    public void setMutationChance(String mutationChance) {

        this.mutationChance = Double.parseDouble(mutationChance.replace("%",""));
    }

    public String getParentChoice() {
        return parentChoice;
    }

    public void setParentChoice(String parentChoice) {
        this.parentChoice = parentChoice;
    }

    public String getParentChildRatio() {
        return parentChildRatio;
    }

    public void setParentChildRatio(String parentChildRatio) {
        this.parentChildRatio = parentChildRatio;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public ArrayList<City> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<City> cityList) {
        this.cityList = cityList;
    }

    public String getReproductionMethod() {
        return reproductionMethod;
    }

    public void setReproductionMethod(String reproductionMethod) {
        this.reproductionMethod = reproductionMethod;
    }

    public String getMutationMethod() {
        return mutationMethod;
    }

    public void setMutationMethod(String mutationMethod) {
        this.mutationMethod = mutationMethod;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

}
