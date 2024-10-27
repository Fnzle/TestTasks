package Hangman;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    private static final String[] POSSIBLE_WORDS = {"fortune", "gain", "snacks", "performer", "people",
            "crate", "hedgehog", "mare"};
    private static int livesNum = 6;
    private static final StringBuilder foundLetters = new StringBuilder();
    private static final StringBuilder wrongLetters = new StringBuilder();
    private static String hiddenWord;

    public static void main(String[] args) throws IOException, InterruptedException {
        Random random = new Random();
        hiddenWord = POSSIBLE_WORDS[random.nextInt(POSSIBLE_WORDS.length)];
        Scanner scanner = new Scanner(System.in);
        char inputLetter;
        ProcessBuilder clearConsole = getClearConsoleProcess();

        foundLetters.append("_".repeat(hiddenWord.length()));

        while (true) {
            //Очистка консоли и вывод меню в соответствии с текущими результатами игры
            clearConsole.start().waitFor();
            printMenu();

            if (isGameOver()) break;

            System.out.print("Введите букву: ");
            inputLetter = scanner.next().charAt(0);
            inputLetterProcessing(inputLetter);
        }
    }

    //Вывод меню для текущего состояния игры
    private static void printMenu() {
        String head = "";
        String body = "";
        String legs = "";
        //Отображение человечка в зависимости от количества оставшихся жизней
        switch (livesNum) {
            case 0:
                head = "  o";
                body = " /|\\";
                legs = " / \\";
                break;
            case 1:
                head = "  o";
                body = " /|\\";
                legs = " /";
                break;
            case 2:
                head = "  o";
                body = " /|\\";
                break;
            case 3:
                head = "  o";
                body = " /|";
                break;
            case 4:
                head = "  o";
                body = "  |";
                break;
            case 5:
                head = "  o";
                break;
            case 6:
                break;
        }
        System.out.format("""
                    Игра "Виселица"
               \s
               (  Все возможные слова используют только буквы английского алфавита  )
               \s
                Открытые буквы: %s
               \s
                Осталось жизней: %d
                 ______
                |      |
                |    %s
                |    %s
                |    %s
                |_________
               \s
                Ошибки: %s
               \s
               \s""", foundLetters, livesNum, head, body, legs, wrongLetters);
    }

    //Установка атрибутов для ProcessBuilder в зависимости от использующейся ОС
    private static ProcessBuilder getClearConsoleProcess() {
        if (System.getProperty("os.name").contains("Windows"))
            return new ProcessBuilder("cmd", "/c", "cls").inheritIO();
        else return new ProcessBuilder("/bin/bash", "-c", "clear").inheritIO();
    }

    //Проверка на выполнение условий конца игры и последующая обработка
    private static boolean isGameOver() {
        if (livesNum == 0) {
            System.out.format("""
                        Проигрыш
                        
                        Загаданное слово - %s""", hiddenWord);
            return true;
        }
        else if (foundLetters.toString().equals(hiddenWord)) {
            System.out.println("Победа!");
            return true;
        }
        return false;
    }

    private static void inputLetterProcessing(char inputLetter) {
        int numLettersFound;
        inputLetter = Character.toLowerCase(inputLetter);

        //Получение количества вхождений введённой пользователем буквы в загаданном слове
        numLettersFound = hiddenWord.length() - hiddenWord.replace(String.valueOf(inputLetter), "").length();

        if (numLettersFound > 0) {
            int[] lettersIndexArray = getFoundLettersIndex(numLettersFound, inputLetter);
            for (int i = 0; i < numLettersFound; i++) foundLetters.setCharAt(lettersIndexArray[i], inputLetter);
        }
        else {
            wrongLetters.append(inputLetter);
            livesNum--;
        }
    }

    private static int[] getFoundLettersIndex(int numLettersFound, char inputLetter) {
        int searchingIndex = 0;
        StringBuilder tempWord = new StringBuilder(hiddenWord);
        int indexFoundLetter;
        int[] lettersIndexArray = new int[numLettersFound];

        //Цикл для нахождения индекса каждого вхождения буквы
        for (int i = 0; i < numLettersFound; i++) {
            indexFoundLetter = tempWord.indexOf(String.valueOf(inputLetter), searchingIndex);
            searchingIndex = indexFoundLetter + 1;
            lettersIndexArray[i] = indexFoundLetter;
        }
        return lettersIndexArray;
    }
}
