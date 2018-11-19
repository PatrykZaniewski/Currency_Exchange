import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {
    private ArrayList<LinkedList<Pair<Integer, ChangeCost>>> list;
    private ArrayList<Pair<String, String>> nameOfCurrency = new ArrayList<>();

    private int V = 0;
    private int E = 0;

    public Graph(int n) {
        V = n;
        list = new ArrayList<>();
        char x = 'A';
        for (int i = 0; i < n; i++) {
            list.add(new LinkedList<>());
            StringBuilder sb = new StringBuilder();
            sb.append(x);
            String y = sb.toString();
            nameOfCurrency.add(new Pair<>(y + y + y, y));
            x++;
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

    public void setV(int v) {
        V = v;
    }

    public int getE() {
        return E;
    }
}
