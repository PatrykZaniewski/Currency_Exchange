import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Stack;

public class ExchangeCurrency {

    private Graph graph;

    public ExchangeCurrency(Graph graph) {
        this.graph = graph;
    }

    double BellmanFord(int src, double amount, int dst) {
        int V = graph.getV(), E = graph.getE();
        double dist[] = new double[V];
        int prev[] = new int[V];

        for (int i = 0; i < V; i++) {
            prev[i] = i;
            dist[i] = Integer.MAX_VALUE;
        }
        dist[src] = (double) 1 / amount;

        for (int i = 1; i < V; i++) {
            //for (int j = 0; j < E; j++) {
                int u = 0;
                for (LinkedList<Pair<Integer, ChangeCost>> list : graph.getList()) {
                    for (Pair<Integer, ChangeCost> para : list) {
                        if (u != dst) {
                            int v = para.getKey();
                            double multipler = 1 / para.getValue().getMultipler();
                            double cost;
                            if (para.getValue().getIsPercent()) {
                                cost = 1 / (dist[u] * multipler) * para.getValue().getCost() / 100;
                            } else {
                                cost = para.getValue().getCost();
                            }
                            double afterExchange = 1 / (1 / (dist[u] * multipler) - cost);
                            if (dist[v] > afterExchange && afterExchange > 0) {
                                dist[v] = afterExchange;
                                prev[v] = u;
                            }
                        }
                    }
                    u++;
                }
            //}
        }

        for(int i = 0; i < dist.length; i++)
        {
            System.out.println(i + " " + 1/dist[i]);
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(dst);

        int u = dst;
        //TODO spróbować osiągnąć jednokrotne przejście po arbitrażu
        while (prev[u] != u) {
            u = prev[u];
            if (stack.search(u) > 0) {
                System.out.println(stack);

                return -1;
            }
            stack.push(u);
        }
        if (stack.peek() != src) {
            return 0;
        }
        double wynik = amount;
        while (stack.size() > 1) {
            u = stack.pop();
            System.out.print(graph.getCurrencyShortName(u) + " -> ");
            int v = stack.peek();
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
        }
        System.out.println(graph.getCurrencyShortName(dst));
        return wynik;
    }
}
