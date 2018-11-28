import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Stack;

public class FindArbitration {
    private double dist[];
    private Graph graph;
    private int prevVertex[];

    FindArbitration(Graph graph) {
        this.graph = graph;
    }

    private boolean hasCycle(int src, int dst, ChangeCost changeCost) {
        double cost;
        if (changeCost.getIsPercent()) {
            cost = 1 / (dist[src] * changeCost.getMultipler()) * changeCost.getCost() / 100;
        } else {
            cost = changeCost.getCost();
        }
        double afterExchange = 1 / (1 / (dist[src] * changeCost.getMultipler()) - cost);
        return dist[dst] > afterExchange;
    }

    double arbitration(int src, double amount) {
        int V = graph.getNumberOfVertexes();
        int E = graph.getNumberOfEdges();
        dist = new double[V];
        prevVertex = new int[V];

        for (int i = 0; i < V; i++) {
            prevVertex[i] = i;
            dist[i] = Integer.MAX_VALUE;
        }

        dist[src] = (double) 1 / amount;

        for (int i = 1; i < V; i++) {
            int u = 0;
            for (LinkedList<Pair<Integer, ChangeCost>> list : graph.getListOfNeighbor()) {
                for (Pair<Integer, ChangeCost> pair : list) {
                    int v = pair.getKey();
                    double multipler = 1 / pair.getValue().getMultipler();
                    double cost;
                    if (pair.getValue().getIsPercent()) {
                        cost = 1 / (dist[u] * multipler) * pair.getValue().getCost() / 100;
                    } else {
                        cost = pair.getValue().getCost();
                    }
                    double afterExchange = 1 / (1 / (dist[u] * multipler) - cost);
                    if (dist[v] > afterExchange) {
                        dist[v] = afterExchange;
                        prevVertex[v] = u;
                    }
                }
                u++;
            }
        }

        int u = 0;
        Stack<Integer> stackOfCurrency = new Stack<>();
        int str = 0;
        int moves = 0;
        for (LinkedList<Pair<Integer, ChangeCost>> list : graph.getListOfNeighbor()) {
            for (Pair<Integer, ChangeCost> para : list) {
                if (dist[u] != Integer.MAX_VALUE) {
                    if (hasCycle(u, para.getKey(), para.getValue())) {
                        stackOfCurrency.clear();
                        int x = u;
                        moves = 0;
                        while (x != para.getKey() && moves <= E) {
                            stackOfCurrency.add(x);
                            x = prevVertex[x];
                            moves++;
                        }
                        stackOfCurrency.add(para.getKey());
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

        if(moves > E || dist[src] >= 1 / amount || stackOfCurrency.empty())
        {
            return 0;
        }

        double result = amount;
        Stack<Integer> secondStackOfCurrency = (Stack<Integer>) stackOfCurrency.clone();

        int u2 = u;
        while (stackOfCurrency.size() > 0) {
            int v = stackOfCurrency.peek();
            LinkedList<Pair<Integer, ChangeCost>> lista = graph.getListOfNeighbor().get(u);
            for (Pair<Integer, ChangeCost> pair : lista) {
                if (pair.getKey() == v) {
                    ChangeCost change = pair.getValue();
                    double multipler = change.getMultipler();
                    double cost = change.getCost();
                    if (change.getIsPercent()) {
                        result = result * multipler;
                        result = result - (result * cost / 100);
                    } else {
                        result = result * multipler - cost;
                    }
                }
            }
            u = stackOfCurrency.pop();
        }
        if(result < amount)
        {
            return 0;
        }
        else
        {
            while(secondStackOfCurrency.size() > 0)
            {
                System.out.print(graph.getCurrencyShortName(u2) + " -> ");
                u2 = secondStackOfCurrency.pop();
            }
            System.out.println(graph.getCurrencyShortName(u2) + " ");
            return result;
        }
    }
}
