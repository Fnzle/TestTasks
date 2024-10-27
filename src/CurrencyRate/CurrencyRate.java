package CurrencyRate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CurrencyRate {
    //Словарь с используемыми валютами и их множителем для конвертации в базовую валюту
    //В случае данной программы в качестве базовой валюты выбран USD
    private static final HashMap<String, Double> currencyRates = new HashMap<>() {{
        put("USD", 1.0);
        put("EUR", 0.9211);
        put("RUR", 88.9707);
        put("CNY", 7.1305);
        put("DKK", 49.0045);
    }};

    public static void main(String[] args) throws IOException, InterruptedException {
        String currency;
        double sum;
        Scanner scanner = new Scanner(System.in);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ProcessBuilder clearConsole = getClearConsoleProcess();

        while (true) {
            //Очистка консоли
            clearConsole.start().waitFor();

            //Вывод меню и получение данных о конвертируемой валюте или запроса на выход из программы
            System.out.println("\nДоступные валюты:\n");
            for (Map.Entry<String, Double> entry : currencyRates.entrySet())
                System.out.format("\t%s\n", entry.getKey());
            System.out.print("\nВыберите конвертируемую валюту из списка (или \\e для выхода): ");
            currency = scanner.nextLine();

            if (currency.equals("\\e")) break;

            currency = currency.toUpperCase();
            if (currencyRates.containsKey(currency)) {
                System.out.print("Введите сумму для конвертации: ");
                //Отлавливаются исключения возникающие в случае ввода неправильных данных пользователем
                try {
                    sum = Double.parseDouble(scanner.nextLine());
                    //Приведение к базовой валюте
                    sum /= currencyRates.get(currency);
                    //Вывод результатов приведения к другим валютам
                    System.out.println();
                    for (Map.Entry<String, Double> entry : currencyRates.entrySet()) {
                        if (entry.getKey().equals(currency)) continue;
                        System.out.format("\t%s: %s\n", entry.getKey(), entry.getValue() * sum);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Необходимо ввести число!");
                }
            }
            else System.out.println("Данной валюты нет в списке!");

            //Приостановка выполнения перед очисткой консоли
            System.out.println("\nНажмите Enter для продолжения...");
            in.readLine();
        }
    }

    //Установка атрибутов для ProcessBuilder в зависимости от использующейся ОС
    private static ProcessBuilder getClearConsoleProcess() {
        if (System.getProperty("os.name").contains("Windows"))
            return new ProcessBuilder("cmd", "/c", "cls").inheritIO();
        else return new ProcessBuilder("/bin/bash", "-c", "clear").inheritIO();
    }
}
