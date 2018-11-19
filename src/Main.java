import java.text.DecimalFormat;
import java.util.Scanner;


public class Main {

    private static boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Graph graf = new Graph(4);
        graf.addEdge(0, 1, 0.8889, 0.0001, true);
        graf.addEdge(1, 2, 1.2795, 0.0000, true);
        graf.addEdge(2, 3, 0.8, 0.0250, false);
        graf.addEdge(2, 1, 0.7, 0.0250, false);

        while (true) {
            ExchangeCurrency exchange = new ExchangeCurrency(graf);
            //FindArbitration arbitration = new FindArbitration(graf);
            Scanner odczyt = new Scanner(System.in);
            String text = odczyt.nextLine();
            String[] splited = text.split(" ");

            if (splited.length == 1) {
                if (splited[0].equals("wyjscie") || splited[0].equals("Wyjscie") || splited[0].equals("WYJSCIE")) {
                    break;
                }
                try {
                    double stringToDouble = Double.parseDouble(splited[0]);
                    //TODO arbitraz
                } catch (NumberFormatException e) {
                    System.out.println("Podany argument arbitrazu zawiera znaki inne niż cyfry.");
                }
            } else if (splited.length == 2) {
                System.out.println("Brak jednego z argumentow potrzebnych do wyszukania korzystnej wymiany waluty");
            } else if (splited.length > 2) {
                boolean firstArgErr = false;
                boolean secondArgErr = false;
                double amount = 0;
                if (splited.length > 3) {
                    System.out.println("Podano wiecej argumentow niz wymaga tego algorytm wyliczania najkorzystniejszej sciezki wymiany walut. Zostaly wczytane tylko pierwsze 3 argumenty");
                }
                if (!isAlpha(splited[0])) {
                    firstArgErr = true;
                    System.out.println("Pierwszy z argumentow zawiera znaki inne niz litery");
                }
                if (!isAlpha(splited[2])) {
                    secondArgErr = true;
                    System.out.println("Trzeci z argumentow zawiera znaki inne niz litery");
                }
                try {
                    amount = Double.parseDouble(splited[1]);
                    if (amount < 0) {
                        System.out.println("Ilosc waluty nie moze byc ujemna");
                    } else {
                        if (!secondArgErr && !firstArgErr) {
                            boolean firstArgNotExist = false;
                            boolean secondArgNotExist = false;
                            int src = graf.getCurrencyID(splited[0]);
                            int dst = graf.getCurrencyID(splited[2]);
                            if (src == -1) {
                                firstArgNotExist = true;
                                System.out.println("Waluta wejsciowa nie istnieje w pliku");
                            }
                            if (dst == -1) {
                                secondArgNotExist = true;
                                System.out.println("Waluta wyjściowa nie istnie w pliku");
                            }
                            if (!firstArgNotExist && !secondArgNotExist) {
                                double wynik = exchange.BellmanFord(src, amount, dst);
                                if (wynik == 0) {
                                    System.out.println("Nie mozna wymienic walut gdyz nie istnieje miedzy nimi bezposrednie albo posrednie polaczenie");
                                } else if (wynik == -1) {
                                    System.out.println("Na sciezce wymiany istnieje arbitraz wymiany walut co prowadzi do osiagniecia nieskonczonego wyniku");
                                } else {
                                    DecimalFormat df = new DecimalFormat("###.##");
                                    System.out.println("Wynik końcowy wymiany: " + df.format(wynik));
                                }
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Drugi z argumentow zawiera znaki inne niz cyfry ");
                }
            }
        }
    }
}


