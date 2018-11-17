import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;

class Graph {
    private ArrayList<LinkedList<Pair<Integer, ChangeCost>>> list;

    private int V = 0;
    private int E = 0;

    Graph(int n) {
        V = n;
        list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(new LinkedList<>());
        }
    }

    void addEdge(int src, int dst, double multipler, double cost, boolean isPercent) {
        ChangeCost costs = new ChangeCost(multipler, cost, isPercent);
        list.get(src).add(new Pair<>(dst, costs));
        E++;
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
