package store;

import models.PasswordEntry;
import security.CryptoUtils;
import security.HashUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private static final String DATA_FILE = "data/passwords.encrypted";
    private static final String MASTER_HASH_FILE = "data/master.hash";

    public boolean verifyMasterPassword(String password) {
        try {
            // Si es la primera vez, crear el hash
            File file = new File(MASTER_HASH_FILE);
            if (!file.exists()) {
                String hash = HashUtils.hashPassword(password);
                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.println(hash);
                }
                return true;
            }

            // Verificar contra el hash almacenado
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String storedHash = reader.readLine();
                return HashUtils.verifyPassword(password, storedHash);
            }
        } catch (IOException e) {
            System.err.println("Error verificando contraseña maestra: " + e.getMessage());
            return false;
        }
    }

    public void savePassword(PasswordEntry entry, String masterPassword) {
        List<PasswordEntry> entries = getAllPasswords(masterPassword);
        entries.add(entry);
        saveAllPasswords(entries, masterPassword);
    }

    public List<PasswordEntry> getAllPasswords(String masterPassword) {
        List<PasswordEntry> entries = new ArrayList<>();
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            return entries;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String decryptedLine = CryptoUtils.decrypt(line, masterPassword);
                    String[] parts = decryptedLine.split("\\|");
                    if (parts.length >= 4) {
                        PasswordEntry entry = new PasswordEntry(
                                parts[0], parts[1], parts[2],
                                parts.length > 3 ? parts[3] : ""
                        );
                        entries.add(entry);
                    }
                } catch (Exception e) {
                    System.err.println("Error decrypting line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading passwords: " + e.getMessage());
        }

        return entries;
    }

    public void deletePassword(PasswordEntry entryToDelete, String masterPassword) {
        List<PasswordEntry> entries = getAllPasswords(masterPassword);
        entries.removeIf(entry ->
                entry.getService().equals(entryToDelete.getService()) &&
                        entry.getUsername().equals(entryToDelete.getUsername())
        );
        saveAllPasswords(entries, masterPassword);
    }

    private void saveAllPasswords(List<PasswordEntry> entries, String masterPassword) {
        new File("data").mkdirs();

        try (PrintWriter writer = new PrintWriter(DATA_FILE)) {
            for (PasswordEntry entry : entries) {
                String plainText = entry.getService() + "|" +
                        entry.getUsername() + "|" +
                        entry.getPassword() + "|" +
                        entry.getNotes();
                String encrypted = CryptoUtils.encrypt(plainText, masterPassword);
                writer.println(encrypted);
            }
        } catch (Exception e) {
            System.err.println("Error saving passwords: " + e.getMessage());
        }
    }
}