import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DataRead {
    private File fileToRead;
    private Graph graph = null;

    public DataRead(String fileNameToRead) {
        this.fileToRead = new File(fileNameToRead);
    }

    private static boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isDouble(String str) {
        str = str.replace(",", ".");
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //TODO Automatyczne przerabianie pliku?
    //TODO Wyszukiwanie danych przy mniejszej liczbie argumentow?
    Graph readFile() {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileToRead));
            String text;
            int hashCount = 0;
            int lineNumber = 1;
            ArrayList<String> listOfCurrency = new ArrayList<>();
            while ((text = br.readLine()) != null) {
                text = text.trim();
                if (text.length() > 0 && text.charAt(0) == '#') {
                    hashCount++;
                    if (hashCount == 2) {
                        graph = new Graph(listOfCurrency);
                    }
                } else {
                    //wczytywanie walut
                    if (hashCount == 1) {
                        int duplicateCount = 0;
                        text = checkData(text, true, lineNumber);
                        if (text != null && !text.equals("-1")) {
                            String[] splited = text.split(" ");
                            if (Integer.parseInt(splited[0]) != lineNumber - 2 - duplicateCount) {
                                System.out.println("Nieprawidłowe ID waluty w linii " + lineNumber + ", ID zostało ustawione na wartość: " + (lineNumber - 2 - duplicateCount));
                                splited[0] = String.valueOf(lineNumber - 2 - duplicateCount);
                            }
                            text = Arrays.toString(splited);
                            listOfCurrency.add(text);
                            //TODO poprawic duplikaty
                            /*if (listOfCurrency.size() == 0) {
                                listOfCurrency.add(text);
                            } else {
                                boolean duplicateChoosen = false;
                                for (int i = 0; i < listOfCurrency.size(); i++) {
                                    if (listOfCurrency.get(i).contains(splited[1])) {
                                        System.out.println("Błąd 11: Wystąpił duplikat podczas wczytywania waluty w linii: " + lineNumber  + ". Wpisz \"E\", aby edytować linię, wpisując \"P\" pominiesz ją, natomiast \"W\" zakończy działanie programu.");
                                        while (true) {
                                            Scanner odczyt = new Scanner(System.in);
                                            String problemSolve = odczyt.nextLine();
                                            if (problemSolve.equals("W")) {
                                                return null;
                                            } else if (problemSolve.equals("E")) {
                                                System.out.println("Wprowadź linię ponownie:");
                                                while (true) {
                                                    odczyt = new Scanner(System.in);
                                                    String lineEdit;
                                                    lineEdit = odczyt.nextLine();
                                                    lineEdit = checkData(lineEdit, true, lineNumber);
                                                    System.out.println("XD");
                                                    String[] lineEditSplit;
                                                    if (lineEdit != null) {
                                                        lineEditSplit = lineEdit.split(" ");
                                                        if (!listOfCurrency.get(i).contains(lineEditSplit[1])) {
                                                            listOfCurrency.add(lineEdit);
                                                            break;
                                                        }
                                                    }
                                                }
                                            } else if (problemSolve.equals("P")) {
                                                duplicateChoosen = true;
                                                duplicateCount++;
                                                break;
                                            }
                                        }
                                    }
                                    if (duplicateChoosen)
                                    {
                                        break;
                                    }
                                }
                                if(!duplicateChoosen)
                                {
                                    listOfCurrency.add(text);
                                }
                            }*/
                        } else {
                            return null;
                        }
                    }
                    //wczytywanie kursow
                    if (hashCount == 2) {
                        text = checkData(text, false, lineNumber);
                        if (text != null && !text.equals("-1")) {
                            String[] splited = text.split(" ");
                            int srcCurrency = graph.getCurrencyID(splited[1]);
                            int dstCurrency = graph.getCurrencyID(splited[2]);
                            double multipler = Double.parseDouble(splited[3]);
                            double cost = Double.parseDouble(splited[5]);
                            boolean isPercent = true;
                            if (!splited[4].equals("PROC")) {
                                isPercent = false;
                            }
                            graph.addEdge(srcCurrency, dstCurrency, multipler, cost, isPercent);
                        }
                        if (text != null && text.equals("-1")) {
                            return null;
                        }
                    }
                }
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Błąd 13: Nie znaleziono pliku o podanej nazwie");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    private String checkData(String lineRead, boolean currencyNameRead, int lineNumber) {
        String[] splited = lineRead.split(" ");
        if (currencyNameRead) {
            if (splited.length == 3) {
                if (splited[0].matches("[0-9]+") && isAlpha(splited[1]) && isAlpha(splited[2])) {
                    return lineRead;
                }
            }
            if (splited.length == 4) {
                if (splited[0].matches("[0-9]+") && isAlpha(splited[1]) && isAlpha(splited[2]) && isAlpha(splited[3])) {
                    return lineRead;
                }
            }
            if (splited.length > 4) {
                if (splited[0].matches("[0-9]+") && isAlpha(splited[1]) && isAlpha(splited[2])) {
                    System.out.println("Zostaną wczytanie pierwsze 3 argumenty. Dodatkowe zostaną pominięte.");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 3; i++) {
                        sb.append(splited[i]);
                    }
                    lineRead = sb.toString();
                    return lineRead;
                }
            }
            System.out.println("Ostrzeżenie 01: Błąd przy odczytywaniu pliku w linii " + lineNumber + ". Wpisz \"E\", aby edytować linię, natomiast wpisanie \"W\" zakończy działanie programu.");
            System.out.println("Prawidłowa linia wczytania walut zawiera 3 argumenty oddzielone białymi znakami: ID waluty, skrót waluty, pełna nazwa waluty.");
            System.out.println("Zawartość linii: " + Arrays.toString(splited));
            while (true) {
                Scanner odczyt = new Scanner(System.in);
                String text = odczyt.nextLine();
                if (text.equals("W")) {
                    return "-1";
                } else if (text.equals("E")) {
                    System.out.println("Wprowadź linię ponownie:");
                    odczyt = new Scanner(System.in);
                    text = odczyt.nextLine();
                    return checkData(text, true, lineNumber);
                }
            }
        } else {
            if (splited.length == 6) {
                lineRead = lineRead.replace(",", ".");
                if (splited[0].matches("[0-9]+") && isAlpha(splited[1]) && isAlpha(splited[2]) && isDouble(splited[3]) && isAlpha(splited[4]) && isDouble(splited[5])) {
                    return lineRead;
                }
            }
            if (splited.length > 6) {
                if (splited[0].matches("[0-9]+") && isAlpha(splited[1]) && isAlpha(splited[2]) && isDouble(splited[3]) && isAlpha(splited[4]) && isDouble(splited[5])) {
                    System.out.println("Zostanie wczytane 6 pierwszych argumentow. Pozostale zostano pominiete");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 5; i++) {
                        sb.append(splited[i]);
                    }
                    lineRead = sb.toString();
                    lineRead = lineRead.replace(",", ".");
                    return lineRead;
                }
            }
            System.out.println("Ostrzeżenie 01: Błąd przy odczytywaniu pliku w linii X. Wpisz \"E\", aby edytować linię, wpisując \"P\" pominiesz ją, natomiast \"W\" zakończy działanie programu.");
            System.out.println("Prawidłowa linia wczytania kursów walut zawiera 6 argumentów oddzielonych białymi znakami: ID kursu, skrót waluty wejściowej, skrót waluty wyjściowej, kurs między walutami, typ opłaty, opłata");
            System.out.println("Zawartość linii: " + Arrays.toString(splited));
            while (true) {
                Scanner odczyt = new Scanner(System.in);
                String text = odczyt.nextLine();

                switch (text) {
                    case "W":
                        return "-1";
                    case "P":
                        return null;
                    case "E":
                        System.out.println("Wprowadź linię ponownie:");
                        odczyt = new Scanner(System.in);
                        text = odczyt.nextLine();
                        return checkData(text, false, lineNumber);
                }
            }
        }
    }
}
