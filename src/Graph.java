import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {
    private ArrayList<LinkedList<Pair<Integer, ChangeCost>>> list;
    public ArrayList<Pair<String, String>> nameOfCurrency = new ArrayList<>();

    private int V = 0;
    private int E = 0;

    public Graph(ArrayList<String> listOfCurrency) {
        V = listOfCurrency.size();
        list = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            list.add(new LinkedList<>());
            String[] splited = listOfCurrency.get(i).split(", ");

            if(splited.length == 4){
                splited[2] = splited[2] + " " + splited[3];
            }
            nameOfCurrency.add(new Pair<>(splited[2], splited[1]));
        }
    }

    void addEdge(int src, int dst, double multipler, double cost, boolean isPercent) {
        ChangeCost costs = new ChangeCost(multipler, cost, isPercent);
        list.get(src).add(new Pair<>(dst, costs));
        E++;
    }

    public int getCurrencyID(String shortName) {
        int i = 0;
        for (Pair currencyPair : nameOfCurrency) {
            if (currencyPair.getValue().equals(shortName)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public String getCurrencyShortName(int id) {
        return nameOfCurrency.get(id).getValue();
    }

    public ArrayList<LinkedList<Pair<Integer, ChangeCost>>> getList() {
        return list;
    }

    public void setList(ArrayList<LinkedList<Pair<Integer, ChangeCost>>> list) {
        this.list = list;
    }

    public int getV() {
        return V;
    }

    public void setV(int V) {
        this.V = V;
    }

    public int getE() {
        return E;
    }
}
