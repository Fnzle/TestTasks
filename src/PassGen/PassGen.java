package PassGen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class PassGen {
    private static final char[][] ALLOWED_SYMBOLS = {
            "abcdefghijklmnopqrstuvwxyz".toCharArray(),
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(),
            "0123456789".toCharArray(),
            "!@#$%^&*_,.?\\/|`~".toCharArray()
    };
    private static final int PAS_MIN_LENGTH = 8;
    private static final int PAS_MAX_LENGTH = 12;

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int pasLength;
        ProcessBuilder clearConsole = getClearConsoleProcess();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            //Очистка консоли
            clearConsole.start().waitFor();

            System.out.format("Введите длину пароля (от %s до %s): ", PAS_MIN_LENGTH, PAS_MAX_LENGTH);
            //Отлавливаются исключения возникающие в случае ввода неправильных данных пользователем
            try {
                pasLength = Integer.parseInt(scanner.nextLine());
                if (pasLength >= PAS_MIN_LENGTH) {
                    if (pasLength <= PAS_MAX_LENGTH) {

                        System.out.format("\n\nВаш сгенерированный пароль: %s", generatePassword(pasLength));

                        System.out.println("\n\nНажмите Enter для продолжения...");
                        in.readLine();
                        break;

                    }
                    //Обработка случаев когда длина выходит за допустимые границы
                    else System.out.println("Введенное число недопустимо большое!");
                } else System.out.println("Введенное число недопустимо маленькое!");
            }
            catch (NumberFormatException e) {
                System.out.println("Необходимо ввести число!");
            }
        }
    }

    //Генерация пароля
    private static String generatePassword(int length) {
        int type;
        int[] prevSymbolTypes = new int[2];
        StringBuilder pas = new StringBuilder();

        for (int i = 0; i < length; i++) {
            /*Выбор способа получения случайного типа - если два предыдущих символа были из одного набора, то текущий
            не может быть взят из него снова*/
            if (i > 1) {
                if (prevSymbolTypes[0] == prevSymbolTypes[1])
                    type = getRandSymTypeIndexWExclude(prevSymbolTypes[1]);
                else type = getRandSymTypeIndex();
            } else type = getRandSymTypeIndex();

            pas.append(getRandSym(type));

            //Обновление значений предыдущих типов
            prevSymbolTypes[0] = prevSymbolTypes[1];
            prevSymbolTypes[1] = type;
        }

        return pas.toString();
    }

    //Получить случайный символ из указанного набора
    private static char getRandSym(int symType) {
        Random random = new Random();
        return ALLOWED_SYMBOLS[symType][random.nextInt(ALLOWED_SYMBOLS[symType].length)];
    }

    //Получить индекс случайного набора символов
    private static int getRandSymTypeIndex() {
        Random random = new Random();
        return random.nextInt(ALLOWED_SYMBOLS.length);
    }

    //Получить индекс любого случайного набора символов, помимо указанного
    private static int getRandSymTypeIndexWExclude(int excluded) {
        Random random = new Random();
        int type;
        while (true) {
            type = random.nextInt(ALLOWED_SYMBOLS.length);
            if (type == excluded) continue;
            return type;
        }
    }

    //Установка атрибутов для ProcessBuilder в зависимости от использующейся ОС
    private static ProcessBuilder getClearConsoleProcess() {
        if (System.getProperty("os.name").contains("Windows"))
            return new ProcessBuilder("cmd", "/c", "cls").inheritIO();
        else return new ProcessBuilder("/bin/bash", "-c", "clear").inheritIO();
    }
}
