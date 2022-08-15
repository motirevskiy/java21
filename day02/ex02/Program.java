/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: cgretche <cgretche@student.21-school.ru    +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2022/07/20 20:04:16 by cgretche          #+#    #+#             */
/*   Updated: 2022/07/21 19:29:15 by cgretche         ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Program {

    public static final Integer DIVIDER = 1000;

    private static void executeMv(File current, String string) {
        String[] strings = string.split(" ");

        if (strings.length != 3) {
            System.err.println("Error: bad ARGS");
            return;
        }

        Path path1 = Paths.get(current + File.separator + strings[1]);
        Path path2;

        if (strings[2].startsWith("..")) {
            String pathTo = strings[2].replaceFirst("\\.\\.", "");

            if (pathTo.isEmpty()) {
                path2 = Paths.get(current.getParent() + File.separator + strings[1]);
            } else {
                path2 = Paths.get(current.getParent() + File.separator + pathTo + File.separator + strings[1]);
            }
        } else {
            File file = new File(strings[2]);

            if (file.isDirectory()) {
                path2 = Paths.get(strings[2] + File.separator + strings[1]);
            } else {
                path2 = Paths.get(current + File.separator + strings[2]);
            }
        }

        try {
            Files.move(path1, path1.resolveSibling(path2));
        } catch (IOException e) {
            System.err.println("Error: can't move file");
        }
    }

    private static File executeCd(File current, String string) {
        String[] strings = string.split(" ");

        if (strings.length != 2) {
            System.err.println("Error: bad ARGS");
            return current;
        }

        if (strings[1].startsWith("..")) {
            String path = strings[1].replaceFirst("\\.\\.", "");

            if (path.isEmpty()) {
                if (current.getParent() != null) {
                    return new File(current.getParent());
                } else {
                    return new File(File.separator);
                }
            } else {
                if (current.getParent() != null) {
                    File file = new File(current.getParent() + path);

                    return returnFile(file, current);
                } else {
                    File file = new File(path);

                    return returnFile(file, current);
                }
            }
        } else {
            File file = new File(current + File.separator + strings[1]);

            return returnFile(file, current);
        }
    }

    private static File returnFile(File file, File current) {
        if (file.isDirectory()) {
            return file;
        } else {
            System.err.println("Error: bad path");
            return current;
        }
    }

    private static void showContent(File current) {
        File[] files = current.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isHidden()) {
                    if (file.isDirectory()) {
                        System.out.println(file.getName() + " " + folderSize(file) / DIVIDER + " KB");
                    } else {
                        System.out.println(file.getName() + " " + file.length() / DIVIDER + " KB");
                    }
                }
            }
        }
    }

    public static Long folderSize(File directory) {
        Long length = 0L;

        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile())
                    length += file.length();
                else
                    length += folderSize(file);
            }
        }

        return length;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Error: bad ARGS");
            return;
        }

        if (!args[0].startsWith("--current-folder=")) {
            System.err.println("Error: bad ARGS");
            return;
        }

        String path = args[0].replaceFirst("--current-folder=", "");
        if (path.isEmpty()) {
            System.err.println("Error: bad path");
            return;
        }

        File current = new File(path);
        if (!Files.isDirectory(Paths.get(String.valueOf(current)))) {
            System.err.println("Error: bad path");
            return;
        }

        System.out.println(current);

        try (Scanner scanner = new Scanner(System.in)) {
            String string;

            while (true) {
                string = scanner.nextLine().trim();

                if (string.isEmpty()) {
                    continue;
                }

                if (string.equals("exit")) {
                    break;
                } else if (string.equals("ls")) {
                    showContent(current);
                } else if (string.startsWith("cd")) {
                    current = executeCd(current, string);
                    System.out.println(current);
                } else if (string.startsWith("mv")) {
                    executeMv(current, string);
                } else {
                    System.out.println("Available commands: ls, cd, mv");
                }
            }
        }
    }
}
