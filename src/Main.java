import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {

    private static Graph graph;

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
        //TODO wczytywanie pliku jako argument
        DataRead read = new DataRead("src/test.txt");
        graph = read.readFile();
        if (graph == null) {
            System.out.println("Program zostal przerwany w czasie wczytywania danych.");
        } else {

            System.out.println("Aby uzyskać pomoc dotyczącą działania programu wpisz 'POMOC'.");

            while (true) {
                ExchangeCurrency exchange = new ExchangeCurrency(graph);
                //FindArbitration arbitration = new FindArbitration(graf);
                Scanner odczyt = new Scanner(System.in);
                String text = odczyt.nextLine();
                String[] splited = text.split(" ");

                if (splited.length == 1 && text.length() > 0) {
                    if (splited[0].equals("wyjscie") || splited[0].equals("Wyjscie") || splited[0].equals("WYJSCIE")) {
                        break;
                    }
                    if (splited[0].equals("POMOC")) {
                        System.out.println("Aby wyznaczyc arbitraz podaj jeden argument - ilość waluty.");
                        System.out.println("Aby wyznaczyć najkorzystniejszą ścieżkę wymiany waluty podaj 3 argumenty oddzielone białym znakiem - skrót waluty wejściowej, ilość waluty, skrót waluty wyjściowej.");
                        System.out.println("Aby zakończyć działanie programu wpisz WYJSCIE.");
                    } else {
                        try {
                            double stringToDouble = Double.parseDouble(splited[0]);
                            //TODO arbitraz
                        } catch (NumberFormatException e) {
                            System.out.println("Błąd 09: Podany argument arbitrazu zawiera znaki inne niż cyfry.");
                        }
                    }
                } else if (splited.length == 2) {
                    System.out.println("Błąd 10: Brak jednego z argumentow potrzebnych do wyszukania korzystnej wymiany waluty.");
                } else if (splited.length > 2) {
                    boolean firstArgErr = false;
                    boolean secondArgErr = false;
                    double amount;
                    if (splited.length > 3) {
                        System.out.println("Ostrzeżenie 02: Podano wiecej argumentow niz wymaga tego algorytm wyliczania najkorzystniejszej sciezki wymiany walut. Zostaly wczytane tylko pierwsze 3 argumenty.");
                    }
                    if (!isAlpha(splited[0])) {
                        firstArgErr = true;
                        System.out.println("Błąd 07: Pierwszy z argumentow zawiera znaki inne niz litery.");
                    }
                    if (!isAlpha(splited[2])) {
                        secondArgErr = true;
                        System.out.println("Błąd 08: Trzeci z argumentow zawiera znaki inne niz litery.");
                    }
                    try {
                        amount = Double.parseDouble(splited[1]);
                        if (amount < 0) {
                            System.out.println("Błąd 06: Kwota wymiany mniejsza od 0.");
                        } else {
                            if (!secondArgErr && !firstArgErr) {
                                boolean firstArgNotExist = false;
                                boolean secondArgNotExist = false;
                                int src = graph.getCurrencyID(splited[0]);
                                int dst = graph.getCurrencyID(splited[2]);
                                if (src == -1) {
                                    firstArgNotExist = true;
                                    System.out.println("Błąd 04: Waluta wejściowa nie istnieje w pliku.");
                                }
                                if (dst == -1) {
                                    secondArgNotExist = true;
                                    System.out.println("Błąd 05: Waluta wyjściowa nie istnieje w pliku.");
                                }
                                if (!firstArgNotExist && !secondArgNotExist) {
                                    //System.out.println(src + " " + amount + " "+ dst);
                                    double wynik = exchange.BellmanFord(src, amount, dst);
                                    if (wynik == 0) {
                                        System.out.println("Nie mozna wymienic walut gdyz nie istnieje miedzy nimi bezposrednie albo posrednie polaczenie.");
                                    } else if (wynik == -1) {
                                        System.out.println("Na sciezce wymiany istnieje arbitraz wymiany walut co prowadzi do osiagniecia nieskonczonego wyniku.");
                                    } else {
                                        DecimalFormat df = new DecimalFormat("###.##");
                                        System.out.println("Wynik końcowy wymiany: " + df.format(wynik));
                                    }
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Błąd 12: Drugi z argumentow zawiera znaki inne niz cyfry.");
                    }
                }
            }
        }
    }
}


