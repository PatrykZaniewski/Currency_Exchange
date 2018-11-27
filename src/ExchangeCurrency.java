import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Stack;

public class ExchangeCurrency {

    private Graph graph;

    public ExchangeCurrency(Graph graph) {
        this.graph = graph;
    }

    double exchange(int src, double amount, int dst) {
        int numberOfVertexes = graph.getNumberOfVertexes();
        double dist[] = new double[numberOfVertexes];
        int prevVertex[] = new int[numberOfVertexes];

        for (int i = 0; i < numberOfVertexes; i++) {
            prevVertex[i] = i;
            dist[i] = Double.MAX_VALUE;
        }

        dist[src] = (double) 1 / amount;

        for (int i = 1; i < numberOfVertexes; i++) {
            int currentVertex = 0;
            for (LinkedList<Pair<Integer, ChangeCost>> list : graph.getListOfNeighbor()) {
                for (Pair<Integer, ChangeCost> pair : list) {
                    if (currentVertex != dst) {
                        int v = pair.getKey();
                        double multipler = 1 / pair.getValue().getMultipler();
                        double cost;
                        if (pair.getValue().getIsPercent()) {
                            cost = 1 / (dist[currentVertex] * multipler) * pair.getValue().getCost() / 100;
                        } else {
                            cost = pair.getValue().getCost();
                        }
                        double afterExchange = 1 / (1 / (dist[currentVertex] * multipler) - cost);
                        if (dist[v] > afterExchange && afterExchange > 0) {
                            dist[v] = afterExchange;
                            prevVertex[v] = currentVertex;
                        }
                    }
                }
                currentVertex++;
            }
        }

        /*for (int i = 0; i < dist.length; i++) {
            System.out.println(i + " " + prevVertex[i] + " " + 1 / dist[i]);
        }*/

        Stack<Integer> stack = new Stack<>();
        stack.push(dst);

        int u = dst;
        while (prevVertex[u] != u) {
            u = prevVertex[u];
            if (stack.search(u) > 0) {
                /*for(int i = stack.size() - 1; i >= 0; i--)
                {
                    System.out.print(graph.getCurrencyShortName(stack.get(i)));
                    if(i > 0){
                        System.out.print(" -> ");
                    }
                }
                System.out.println();
                exchange(src, amount, stack.get(stack.size() - 2));*/
                return -1;
            }
            stack.push(u);
        }
        if (stack.peek() != src) {
            return 0;
        }
        double result = amount;
        while (stack.size() > 1) {
            u = stack.pop();
            System.out.print(graph.getCurrencyShortName(u) + " -> ");
            int v = stack.peek();
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
        }
        System.out.println(graph.getCurrencyShortName(dst));
        return result;
    }
}
