package sample;

public class City {

    private int number;
    private int population;
    private double x;
    private double y;

    public City(){

    }
    public City(int number,int population,double x,double y){
        this.number = number;
        this.population = population;
        this.x = x;
        this.y = y;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
