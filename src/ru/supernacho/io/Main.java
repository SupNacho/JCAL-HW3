package ru.supernacho.io;


import java.io.*;
import java.util.*;

public class Main {
    private static final int CHARS_IN_PAGE = 1800;

    public static void main(String[] args) {
        FileInputStream fis = null;
//  1. Чтение в Байтовый массив из файла
        try {
            fis = new FileInputStream("byte.txt");
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
//                fis.read(bytes,0,50); //Считываем строго 50 байт
            System.out.println(Arrays.toString(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

//  2. Собираем текст из 5 файлов в один
        ArrayList<FileInputStream> alfi = new ArrayList<>();
        SequenceInputStream sis = null;
        FileOutputStream fos = null;
        try {
            alfi.add(new FileInputStream("part1.txt"));
            alfi.add(new FileInputStream("part2.txt"));
            alfi.add(new FileInputStream("part3.txt"));
            alfi.add(new FileInputStream("part4.txt"));
            alfi.add(new FileInputStream("part5.txt"));
            Enumeration<FileInputStream> enumeration = Collections.enumeration(alfi);
            sis = new SequenceInputStream(enumeration);
            fos = new FileOutputStream("partitions.txt");
            int x;

            while ((x = sis.read()) != -1) {
                fos.write(x);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                sis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

//  3. Читаем Книгу постранично.
        long t = System.currentTimeMillis();
        Scanner scanner = new Scanner(System.in);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile("book.txt", "r");
            long length = raf.length();
            int pages = (int) ((length/ CHARS_IN_PAGE) + 1);
            boolean exit = false;
            System.out.println("Время запуска: " + (System.currentTimeMillis() - t));

            while ( !exit ) {
                System.out.println("В документе " + pages +" стрианиц. Введите номер страницы для просмотра или 0 для выхода.");
                int page = scanner.nextInt(); // Проверку на ввод НЕ чисел не делал, чтоб не раздувать код.

                if (page == 0) {
                    exit = true;
                    System.out.println("Завершение работы...");
                    continue;
                } else if ( page > pages) {
                    System.out.println("В этой книге " + pages + " страниц, введите цифры от 1 до " + pages + " включительно.");
                    continue;
                }

                t = System.currentTimeMillis();
                System.out.println("Страница: " + page);
                int pageStartPoint = (page * CHARS_IN_PAGE) - CHARS_IN_PAGE;// Рассчитываем позицию первого символа страницы
                int pageEnd = pageStartPoint + CHARS_IN_PAGE; // Рассчитываем где страница заканчивается
                raf.seek(pageStartPoint);
                int x;
                StringBuilder sb = new StringBuilder("");

                while ((x = raf.read()) != -1 && raf.getFilePointer() <= pageEnd) {
                    sb.append((char) x);
                }

                System.out.println(sb.toString());
                System.out.println("Время чтения страницы: " + (System.currentTimeMillis() - t));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}