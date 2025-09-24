package ui;

import models.PasswordEntry;
import store.FileStorage;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private FileStorage storage;
    private Scanner scanner;
    private String masterPassword;

    public ConsoleUI() {
        this.storage = new FileStorage();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== GESTOR DE CONTRASEÑAS SEGURO ===");

        if (!authenticate()) {
            System.out.println("Autenticación fallida. Saliendo...");
            return;
        }

        showMainMenu();
    }

    private boolean authenticate() {
        System.out.print("Ingrese su contraseña maestra: ");
        masterPassword = scanner.nextLine();

        return storage.verifyMasterPassword(masterPassword);
    }

    private void showMainMenu() {
        int option;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Ver todas las contraseñas");
            System.out.println("2. Agregar nueva contraseña");
            System.out.println("3. Ver detalle de contraseña");
            System.out.println("4. Eliminar contraseña");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    listPasswords();
                    break;
                case 2:
                    addPassword();
                    break;
                case 3:
                    viewPasswordDetail();
                    break;
                case 4:
                    deletePassword();
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        } while (option != 5);
    }

    private void listPasswords() {
        List<PasswordEntry> passwords = storage.getAllPasswords(masterPassword);

        System.out.println("\n+-----------------------------+");
        System.out.println("|    CONTRASEÑAS ALMACENADAS    |");
        System.out.println("+-----------------------------+");
        System.out.println("# Servicio\tUsuario\t\tFecha");
        System.out.println("---------------------------------");

        for (int i = 0; i < passwords.size(); i++) {
            PasswordEntry entry = passwords.get(i);
            System.out.println((i+1) + " " + entry.getService() + "\t" +
                    entry.getUsername() + "\t" + entry.getCreationDate());
        }

        System.out.print("\nSeleccione número para ver detalles o '0' para volver: ");
        int selection = Integer.parseInt(scanner.nextLine());

        if (selection > 0 && selection <= passwords.size()) {
            showPasswordDetail(passwords.get(selection - 1));
        }
    }

    private void showPasswordDetail(PasswordEntry entry) {
        System.out.println("\n--- DETALLES ---");
        System.out.println("Servicio: " + entry.getService());
        System.out.println("Usuario: " + entry.getUsername());
        System.out.println("Contraseña: " + entry.getPassword());
        System.out.println("Fecha creación: " + entry.getCreationDate());
        System.out.println("Notas: " + entry.getNotes());

        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private void addPassword() {
        System.out.println("\n--- AGREGAR NUEVA CONTRASEÑA ---");

        System.out.print("Servicio: ");
        String service = scanner.nextLine();

        System.out.print("Usuario: ");
        String username = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        System.out.print("Notas (opcional): ");
        String notes = scanner.nextLine();

        PasswordEntry newEntry = new PasswordEntry(service, username, password, notes);
        storage.savePassword(newEntry, masterPassword);

        System.out.println("Contraseña guardada exitosamente!");
    }

    private void viewPasswordDetail() {
        List<PasswordEntry> passwords = storage.getAllPasswords(masterPassword);

        if (passwords.isEmpty()) {
            System.out.println("No hay contraseñas almacenadas.");
            return;
        }

        System.out.print("Ingrese el número de la contraseña a ver: ");
        int selection = Integer.parseInt(scanner.nextLine());

        if (selection > 0 && selection <= passwords.size()) {
            showPasswordDetail(passwords.get(selection - 1));
        } else {
            System.out.println("Selección no válida.");
        }
    }

    private void deletePassword() {
        List<PasswordEntry> passwords = storage.getAllPasswords(masterPassword);

        if (passwords.isEmpty()) {
            System.out.println("No hay contraseñas almacenadas.");
            return;
        }

        listPasswords();
        System.out.print("Ingrese el número de la contraseña a eliminar: ");
        int selection = Integer.parseInt(scanner.nextLine());

        if (selection > 0 && selection <= passwords.size()) {
            storage.deletePassword(passwords.get(selection - 1), masterPassword);
            System.out.println("Contraseña eliminada exitosamente!");
        } else {
            System.out.println("Selección no válida.");
        }
    }
}
