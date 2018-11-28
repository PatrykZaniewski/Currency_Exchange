import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {

    private static Graph graph;
    private static DataRead read;
    private static ExchangeCurrency exchange;
    private static FindArbitration arbitration;

    private static boolean isAlpha(String stringToCheck) {
        char charsArray[] = stringToCheck.toCharArray();
        for (char c : charsArray) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Blad 01: Nie podano argumentu wejsciowego - nazwy pliku z danymi.");
        } else {
            if (args.length > 1) {
                System.out.println("Ostrzenie 01: Podano wiecej niz jeden argument startowy. Wszystkie argumenty poza pierwszym zostana pominiete.");
            }
            read = new DataRead(args[0]);
            graph = read.readFile();
            if (graph == null) {
                System.out.println("Program zostal przerwany w czasie wczytywania danych.");
            } else {
                System.out.println("Aby uzyskac pomoc dotyczaca dzialania programu wpisz 'POMOC'.");
                while (true) {
                    exchange = new ExchangeCurrency(graph);
                    arbitration = new FindArbitration(graph);
                    Scanner scannerRead = new Scanner(System.in);
                    String textConsolRead = scannerRead.nextLine();
                    String[] splittedConsolRead = textConsolRead.split(" ");

                    if (splittedConsolRead.length == 1 && textConsolRead.length() > 0) {
                        if (splittedConsolRead[0].equals("wyjscie") || splittedConsolRead[0].equals("Wyjscie") || splittedConsolRead[0].equals("WYJSCIE")) {
                            break;
                        }
                        if (splittedConsolRead[0].equals("POMOC") || splittedConsolRead[0].equals("Pomoc") || splittedConsolRead[0].equals("pomoc")) {
                            System.out.println("Aby wyznaczyc arbitraz podaj jeden argument - ilosc waluty.");
                            System.out.println("Aby wyznaczyc najkorzystniejsza sciezke wymiany waluty podaj 3 argumenty oddzielone bialym znakiem - skrot waluty wejsciowej, ilosc waluty, skrot waluty wyjsciowej.");
                            System.out.println("Aby wyznaczyc dzialanie programu wpisz WYJSCIE.");
                        } else {
                            try {
                                double stringToDouble = Double.parseDouble(splittedConsolRead[0]);
                                double result = 0;
                                if (stringToDouble >= 0) {
                                    for (int i = 0; i < graph.getNumberOfVertexes(); i++) {
                                        result = arbitration.arbitration(i, stringToDouble);
                                        if (result > 0) {
                                            DecimalFormat df = new DecimalFormat("###.##");
                                            System.out.println("Wynik koncowy arbitrazu: " + df.format(result));
                                            break;
                                        }
                                    }
                                    if (result <= 0) {
                                        System.out.println("Nie znaleziono abitrazu z podana kwota lub arbitraz nie jest mozliwy.");
                                    }
                                } else {
                                    System.out.println("Blad 09: Kwota arbitrazu mniejsza od 0.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Blad 10: Podany argument arbitrazu zawiera znaki inne niż cyfry.");
                            }
                        }
                    } else if (splittedConsolRead.length == 2) {
                        System.out.println("Blad 11: Brak jednego z argumentow potrzebnych do wyszukania korzystnej wymiany waluty.");
                    } else if (splittedConsolRead.length > 2) {
                        boolean firstArgErr = false;
                        boolean secondArgErr = false;
                        double amount;
                        if (splittedConsolRead.length > 3) {
                            System.out.println("Ostrzezenie 05: Podano wiecej argumentow niz wymaga tego algorytm wyliczania najkorzystniejszej sciezki wymiany walut. Zostaly wczytane tylko pierwsze 3 argumenty.");
                        }
                        if (!isAlpha(splittedConsolRead[0])) {
                            firstArgErr = true;
                            System.out.println("Blad 12: Pierwszy z argumentow zawiera znaki inne niz litery.");
                        }
                        if (splittedConsolRead[0].length() != 3) {
                            firstArgErr = true;
                            System.out.println("Blad 13: Argument waluty wejsciowej ma dlugosc inna niz 3 znaki.");
                        }
                        if (!isAlpha(splittedConsolRead[2])) {
                            secondArgErr = true;
                            System.out.println("Blad 14: Trzeci z argumentow zawiera znaki inne niz litery.");
                        }
                        if (splittedConsolRead[2].length() != 3) {
                            secondArgErr = true;
                            System.out.println("Blad 15: Argument waluty wyjsciowej ma dlugosc inna niz 3 znaki.");
                        }
                        try {
                            amount = Double.parseDouble(splittedConsolRead[1]);
                            if (amount < 0) {
                                System.out.println("Blad 16: Kwota wymiany mniejsza od 0.");
                            } else {
                                if (!firstArgErr && !secondArgErr) {
                                    boolean firstArgNotExist = false;
                                    boolean secondArgNotExist = false;
                                    int src = graph.getCurrencyID(splittedConsolRead[0]);
                                    int dst = graph.getCurrencyID(splittedConsolRead[2]);
                                    if (src == -1) {
                                        firstArgNotExist = true;
                                        System.out.println("Blad 17: Waluta wejsciowa nie istnieje w pliku.");
                                    }
                                    if (dst == -1) {
                                        secondArgNotExist = true;
                                        System.out.println("Blad 18: Waluta wyjsciowa nie istnieje w pliku.");
                                    }
                                    if (!firstArgNotExist && !secondArgNotExist) {
                                        double result = exchange.exchange(src, amount, dst);
                                        if (result == 0) {
                                            System.out.println("Nie mozna wymienic walut gdyz nie istnieje miedzy nimi bezposrednie albo posrednie polaczenie.");
                                        } else if (result == -1) {
                                            System.out.println("Na sciezce wymiany istnieje powyzszy arbitraz wymiany walut co prowadzi do osiagniecia nieskonczonego wyniku.");
                                        } else {
                                            DecimalFormat df = new DecimalFormat("###.##");
                                            System.out.println("Wynik koncowy wymiany: " + df.format(result));
                                        }
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Blad 19: Drugi z argumentow zawiera znaki inne niz cyfry.");
                        }
                    }
                }
            }
        }
    }
}