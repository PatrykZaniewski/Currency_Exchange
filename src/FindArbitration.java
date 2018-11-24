import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Stack;

public class FindArbitration {
    private double dist[];
    private Graph graph;

    FindArbitration(Graph graph) {
        this.graph = graph;
    }

    double BellmanFord(int src, double amount) {
        int V = graph.getV(), E = graph.getE();
        dist = new double[V];
        int[] prev = new int[V];

        for (int i = 0; i < V; i++) {
            prev[i] = i;
            dist[i] = Integer.MAX_VALUE;
        }

        dist[src] = (double) 1 / amount;

        for (int i = 1; i < V; i++) {
            int u = 0;
            for (LinkedList<Pair<Integer, ChangeCost>> list : graph.getList()) {
                for (Pair<Integer, ChangeCost> para : list) {
                    int v = para.getKey();
                    double multipler = 1 / para.getValue().getMultipler();
                    double cost;
                    if (para.getValue().getIsPercent()) {
                        cost = 1 / (dist[u] * multipler) * para.getValue().getCost() / 100;
                    } else {
                        cost = para.getValue().getCost();
                    }
                    double afterExchange = 1 / (1 / (dist[u] * multipler) - cost);
                    if (dist[v] > afterExchange) {
                        dist[v] = afterExchange;
                        prev[v] = u;
                    }
                }
                u++;
            }
        }

        int u = 0;
        Stack<Integer> test = new Stack<>();
        int str = 0;
        int moves = 0;
        for (LinkedList<Pair<Integer, ChangeCost>> list : graph.getList()) {
            for (Pair<Integer, ChangeCost> para : list) {
                if (dist[u] != Integer.MAX_VALUE) {
                    if (hasCycle(u, para.getKey(), para.getValue())) {
                        test.clear();
                        int x = u;
                        moves = 0;
                        while (x != para.getKey() && moves <= E) {
                            test.add(x);
                            x = prev[x];
                            moves++;
                        }
                        test.add(para.getKey());
                        if (moves <= E) {
                            str = 1;
                            break;
                        }
                    }
                }
            }
            if (str == 1) break;
            u++;
        }
        if(moves > E)
        {
            return 0;
        }
        double wynik = amount;
        while (test.size() > 0) {
            System.out.print(graph.getCurrencyShortName(u) + " -> ");
            int v = test.peek();
            LinkedList<Pair<Integer, ChangeCost>> lista = graph.getList().get(u);
            for (Pair x : lista) {
                if ((int) x.getKey() == v) {
                    ChangeCost change = (ChangeCost) x.getValue();
                    double multipler = change.getMultipler();
                    double cost = change.getCost();
                    if (change.getIsPercent()) {
                        wynik = wynik * multipler;
                        wynik = wynik - (wynik * cost / 100);
                    } else {
                        wynik = wynik * multipler - cost;
                    }
                }
            }
            u = test.pop();
        }
        return wynik;
    }

    boolean hasCycle(int src, int dst, ChangeCost changeCost) {
        double cost;
        if (changeCost.getIsPercent()) {
            cost = 1 / (dist[src] * changeCost.getMultipler()) * changeCost.getCost() / 100;
        } else {
            cost = changeCost.getCost();
        }
        double afterExchange = 1 / (1 / (dist[src] * changeCost.getMultipler()) - cost);
        return dist[dst] > afterExchange;
    }
}
