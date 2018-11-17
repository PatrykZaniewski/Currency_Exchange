public class Main {

    public static void main(String[] args) {
        Graph graf = new Graph(3);
        graf.addEdge(0, 1, 0.8889, 0.0001, true);
        graf.addEdge(0, 2, 1.0370, 0.0250, false);
        graf.addEdge(1, 2, 1.2795, 0.0000, true);

        ExchangeCurrency exchange = new ExchangeCurrency(graf);
        System.out.println(exchange.BellmanFord(0, 1000, 2));
    }
}
