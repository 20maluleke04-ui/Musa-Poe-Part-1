package com.mycompany.quickchatapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class QuickChatApp {

    // Global trackers managed by main application engine (Part 2)
    public static int totalMessagesSent = 0;
    public static ArrayList<String> allSentMessages = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    // =========================================================================
    // PART 3 GLOBAL PARALLEL ARRAYS & TRACKERS (No hard-coding values)
    // =========================================================================
    public static String[] developers = new String[100];    // Stores recipient data
    public static String[] messageTexts = new String[100];  // Stores message body strings
    public static String[] flags = new String[100];         // Stores message process states
    public static String[] messageIDs = new String[100];    // Stores alphanumeric unique IDs
    public static String[] messageHashes = new String[100]; // Stores generated hash strings
    public static int storedMessageCount = 0;               // Parallel array active pointer

    // =========================================================================
    // 1. NESTED MESSAGE CLASS (Part 2 Enhanced with Part 3 Hooks)
    // =========================================================================
    public static class Message {
        private String messageID;
        private int messageNumber;
        private String recipient;
        private String messageText;
        private String messageHash;

        // Constructor
        public Message(int messageNumber, Scanner scanner) {
            this.messageID = generateMessageID();
            this.messageNumber = messageNumber;
        }

        // Helper to generate a unique random 10-digit number
        private String generateMessageID() {
            Random rand = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                sb.append(rand.nextInt(10));
            }
            return sb.toString();
        }

        // Method Name: checkMessageID()
        public boolean checkMessageID() {
            return this.messageID != null && this.messageID.length() <= 10;
        }

        // Method Name: checkRecipientCell()
        public String checkRecipientCell(String cell) {
            if (cell.length() <= 12 && cell.startsWith("+")) {
                return "Cell number accepted.";
            }
            return "Invalid cell format. Must be max 12 chars and start with a code.";
        }

        // Method Name: createMessageHash()
        public String createMessageHash() {
            String firstTwoID = this.messageID.substring(0, 2);
            String trimmed = this.messageText.trim();
            String[] words = trimmed.split("\\s+");

            String firstWord = words[0];
            String lastWord = words[words.length - 1];

            this.messageHash = (firstTwoID + ":" + this.messageNumber + ":" + firstWord + lastWord).toLowerCase();
            return this.messageHash;
        }

        // Method Name: sendMessage()
        public String sendMessage() {
            System.out.println("\nWhat would you like to do?");
            System.out.println("(1) Send Message");
            System.out.println("(2) Discard Message");
            System.out.println("(3) Store Message to send later");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    QuickChatApp.totalMessagesSent++;
                    String textLog = "ID: " + messageID + " | Hash: " + messageHash + 
                                     " | To: " + recipient + " | Msg: " + messageText;
                    QuickChatApp.allSentMessages.add(textLog + "\n");
                    
                    // Track automatically in Part 3 parallel arrays
                    QuickChatApp.captureToArray(recipient, messageText, "Sent", messageID, messageHash);
                    return "Message successfully sent.";
                    
                case "2":
                    // Track explicitly to arrays as 'Disregarded'
                    QuickChatApp.captureToArray(recipient, messageText, "Disregarded", messageID, messageHash);
                    return "Message discarded.";
                    
                case "3":
                    storeMessage();
                    // Track explicitly to arrays as 'Stored'
                    QuickChatApp.captureToArray(recipient, messageText, "Stored", messageID, messageHash);
                    return "Message successfully stored.";
                    
                default:
                    QuickChatApp.captureToArray(recipient, messageText, "Disregarded", messageID, messageHash);
                    return "Invalid choice. Message discarded.";
            }
        }

        // Method Name: storeMessage()
        public void storeMessage() {
            String json = "{\n" +
                          "  \"messageID\": \"" + messageID + "\",\n" +
                          "  \"messageNumber\": " + messageNumber + ",\n" +
                          "  \"recipient\": \"" + recipient + "\",\n" +
                          "  \"messageText\": \"" + messageText + "\",\n" +
                          "  \"hash\": \"" + messageHash + "\"\n" +
                          "},\n";

            try (FileWriter fw = new FileWriter("stored_messages.json", true)) {
                fw.write(json);
                System.out.println("Saved to stored_messages.json");
            } catch (IOException e) {
                System.out.println("Could not save: " + e.getMessage());
            }
        }

        // Setters & Getters
        public void setRecipient(String r) { this.recipient = r; }
        public void setMessageText(String m) { this.messageText = m; }
        public String getMessageID() { return messageID; }
        public String getMessageHash() { return messageHash; }
        public String getRecipient() { return recipient; }
        public String getMessageText() { return messageText; }
    }

    // Loops through every sent message and returns them all as one String
    public static String printMessages() {
        if (allSentMessages.isEmpty()) {
            return "No messages were sent this session.";
        }
        StringBuilder sb = new StringBuilder("\n===== ALL SENT MESSAGES =====\n");
        for (String msg : allSentMessages) {
            sb.append("- ").append(msg);
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // =========================================================================
    // PART 1 METHODS (Validation Controls)
    // =========================================================================
    public static boolean checkUsername(String username) {
        if (username.contains("_") && username.length() <= 5) {
            System.out.println("Username successfully captured.");
            return true;
        } else {
            System.out.println("Username is not correctly formatted.");
            return false;
        }
    }

    public static boolean checkPassword(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        if (password.length() >= 8 && hasCapital && hasNumber && hasSpecial) {
            System.out.println("Password successfully captured.");
            return true;
        } else {
            System.out.println("Password is not correctly formatted.");
            return false;
        }
    }

    public static boolean checkPhoneNumber(String phone) {
        if (phone.startsWith("0") && phone.length() == 10) {
            System.out.println("Cellphone number successfully added.");
            return true;
        } else {
            System.out.println("Cellphone number incorrectly formatted.");
            return false;
        }
    }

    public static boolean loginUser(String storedUsername, String storedPassword, String storedPhone,
                                    String inputUsername, String inputPassword, String inputPhone) {
        if (storedUsername.equals(inputUsername) &&
            storedPassword.equals(inputPassword) &&
            storedPhone.equals(inputPhone)) {
            System.out.println("Login successful! Welcome " + storedUsername);
            return true;
        } else {
            System.out.println("Login failed. Details are incorrect.");
            return false;
        }
    }

    // =========================================================================
    // INTERFACES & PARALLEL LOGIC ROUTINES (Part 3 Specifications)
    // =========================================================================
    public static void captureToArray(String dev, String text, String flag, String id, String hash) {
        if (storedMessageCount < 100) {
            developers[storedMessageCount] = dev;
            messageTexts[storedMessageCount] = text;
            flags[storedMessageCount] = flag;
            messageIDs[storedMessageCount] = id;
            messageHashes[storedMessageCount] = hash;
            storedMessageCount++;
        }
    }

    // Task 3.a: Populate explicit assignment criteria directly into arrays dynamically
    public static void populateTestData() {
        captureToArray("+27834557896", "Did you get the cake?", "Sent", "ID_A1", "HASH_A1");
        captureToArray("+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored", "ID_B2", "HASH_B2");
        captureToArray("+27834484567", "Yohoooo, I am at your gate.", "Disregarded", "ID_C3", "HASH_C3");
        captureToArray("0838884567", "It is dinner time!", "Sent", "ID_D4", "HASH_D4");
        captureToArray("+27838884567", "Ok, I am leaving without you.", "Stored", "ID_E5", "HASH_E5");
        System.out.println(">>> Assignment Test Data successfully integrated into parallel arrays.");
    }

    // Task 2: Create a fourth main menu option for "Stored Messages Report Sub-Menu"
    public static void runStoredMessagesMenu() {
        int selection;
        do {
            System.out.println("\n--- STORED MESSAGES REPORT ENGINE ---");
            System.out.println("1. Display Developer (Recipient) and Status of all data entries");
            System.out.println("2. Display the longest recorded message text string");
            System.out.println("3. Search for data entry via unique Message ID");
            System.out.println("4. Search for all active entries matching a specific Developer cell");
            System.out.println("5. Delete a specific message record using unique Message Hash");
            System.out.println("6. Display complete institutional tracking report");
            System.out.println("7. Return to Primary Dashboard");
            System.out.print("Enter structural choice parameter: ");
            
            try {
                selection = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                selection = 0;
            }

            switch (selection) {
                case 1 -> displaySendersAndStatus();
                case 2 -> displayLongestMessageText();
                case 3 -> searchMessageID();
                case 4 -> searchDeveloperMessages();
                case 5 -> deleteMessageByHash();
                case 6 -> displayFullSystemReport();
            }
        } while (selection != 7);
    }

    private static void displaySendersAndStatus() {
        System.out.println("\n=== DEVELOPER & STATUS MANIFEST ===");
        for (int i = 0; i < storedMessageCount; i++) {
            System.out.println("Developer: " + developers[i] + " | Current Operational Status: " + flags[i]);
        }
    }

    public static String displayLongestMessageText() {
        if (storedMessageCount == 0) {
            System.out.println("Zero historical elements tracked.");
            return "No entries";
        }
        int trackingIdx = 0;
        for (int i = 1; i < storedMessageCount; i++) {
            if (messageTexts[i].length() > messageTexts[trackingIdx].length()) {
                trackingIdx = i;
            }
        }
        System.out.println("\n=== LONGEST STORED TEXT ENTRY ===");
        System.out.println("Developer: " + developers[trackingIdx]);
        System.out.println("Message: \"" + messageTexts[trackingIdx] + "\"");
        System.out.println("Length: " + messageTexts[trackingIdx].length() + " characters.");
        return messageTexts[trackingIdx];
    }

    private static void searchMessageID() {
        System.out.print("Enter unique Message ID query key: ");
        String query = scanner.nextLine();
        for (int i = 0; i < storedMessageCount; i++) {
            if (messageIDs[i].equalsIgnoreCase(query)) {
                System.out.println("\n[Match Located] Recipient/Developer: " + developers[i] + " | Message Content: \"" + messageTexts[i] + "\"");
                return;
            }
        }
        System.out.println("Message ID: " + query + " does not exist within current records.");
    }

    private static void searchDeveloperMessages() {
        System.out.print("Enter target Developer (Recipient) mobile number: ");
        String query = scanner.nextLine();
        int tracksFound = 0;
        System.out.println("\nEntries matching: " + query);
        for (int i = 0; i < storedMessageCount; i++) {
            if (developers[i].equals(query)) {
                System.out.println("-> Status [" + flags[i] + "] | Content: \"" + messageTexts[i] + "\"");
                tracksFound++;
            }
        }
        if (tracksFound == 0) System.out.println("No matching records managed for user context.");
    }

    public static boolean deleteMessageByHash() {
        System.out.print("Enter specific unique Message Hash string to wipe: ");
        String targetHash = scanner.nextLine();
        for (int i = 0; i < storedMessageCount; i++) {
            if (messageHashes[i].equals(targetHash)) {
                System.out.println("Wiping Message trace: \"" + messageTexts[i] + "\" -> [OK]");
                
                // Shift downstream pointer elements leftwards to balance sequential storage structure
                for (int j = i; j < storedMessageCount - 1; j++) {
                    developers[j] = developers[j + 1];
                    messageTexts[j] = messageTexts[j + 1];
                    flags[j] = flags[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                }
                developers[storedMessageCount - 1] = null;
                messageTexts[storedMessageCount - 1] = null;
                flags[storedMessageCount - 1] = null;
                messageIDs[storedMessageCount - 1] = null;
                messageHashes[storedMessageCount - 1] = null;
                
                storedMessageCount--;
                return true;
            }
        }
        System.out.println("Hash identity code requested is not registered inside structure framework.");
        return false;
    }

    private static void displayFullSystemReport() {
        System.out.println("\n=========================================================================");
        System.out.println("                  SYSTEM COMPREHENSIVE MESSAGE REPORT                    ");
        System.out.println("=========================================================================");
        for (int i = 0; i < storedMessageCount; i++) {
            System.out.println("Message Tracking ID : " + messageIDs[i]);
            System.out.println("Unique Data Hash Key: " + messageHashes[i]);
            System.out.println("Target Developer    : " + developers[i]);
            System.out.println("Message Body Content: " + messageTexts[i]);
            System.out.println("State Process Flag  : " + flags[i]);
            System.out.println("-------------------------------------------------------------------------");
        }
    }

    // =========================================================================
    // 2. MAIN APPLICATION CONTROL ENGINE (Unified Lifecycle)
    // =========================================================================
    public static void main(String[] args) {
        
        // Dynamic loading of assignment test boundaries upon boot
        populateTestData();

        // ---------------------------------------------------------------------
        // REGISTRATION PHASE (Part 1 Logic)
        // ---------------------------------------------------------------------
        System.out.println("= Register =");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        while (!checkUsername(username)) {
            System.out.print("Try again: ");
            username = scanner.nextLine();
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        while (!checkPassword(password)) {
            System.out.print("Try again: ");
            password = scanner.nextLine();
        }

        System.out.print("Enter phone number starting with (0): ");
        String phone = scanner.nextLine();
        while (!checkPhoneNumber(phone)) {
            System.out.print("Try again: ");
            phone = scanner.nextLine();
        }

        // ---------------------------------------------------------------------
        // LOGIN PHASE (Part 1 Logic)
        // ---------------------------------------------------------------------
        System.out.println("\n= Login =");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String loginPhone = scanner.nextLine();

        // Perform validation evaluation
        if (!loginUser(username, password, phone, loginUsername, loginPassword, loginPhone)) {
            System.out.println("Login status evaluation failed. Closing application stream.");
            return;
        }

        System.out.println("\nYou have successfully logged in!\n");
        System.out.println("Welcome to QuickChat.");

        // ---------------------------------------------------------------------
        // SESSION INITIALIZATION & APPLICATION MENU LOOP (Part 2 + Part 3 Added)
        // ---------------------------------------------------------------------
        System.out.print("How many messages do you want to send? ");
        int numMessages;
        try {
            numMessages = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Exiting system initialization profile.\n");
            return;
        }

        boolean chatRunning = true;
        int msgCount = 0; // Tracks allocations processed

        while (chatRunning) {
            System.out.println("\n--- QUICKCHAT ---");
            System.out.println("(1) Send Messages");
            System.out.println("(2) Show recently sent messages");
            System.out.println("(3) Show total messages sent");
            System.out.println("(4) Stored Messages Report Menu (Part 3 Feature)");
            System.out.println("(5) Quit");
            System.out.print("Your choice: ");
            String chatChoice = scanner.nextLine();

            switch (chatChoice) {
                case "1": // SEND MESSAGES LIFECYCLE LOOP
                    if (msgCount == numMessages) {
                        System.out.println("Allocation threshold exhausted. You already processed your " + numMessages + " allocation batches.");
                        break;
                    }

                    while (msgCount < numMessages) {
                        msgCount++;
                        System.out.println("\n--- MESSAGE " + msgCount + " of " + numMessages + " ---");

                        // Instantiate individual Message tracking model mapping object structures
                        Message msg = new Message(msgCount, scanner);

                        // --- GET RECIPIENT CELL (loop until acceptable syntax) ---
                        String cell;
                        while (true) {
                            System.out.print("Recipient cell (e.g. +27): ");
                            cell = scanner.nextLine();
                            String cellCheck = msg.checkRecipientCell(cell);
                            System.out.println(cellCheck);
                            if (cellCheck.equals("Cell number accepted.")) {
                                break;
                            }
                        }
                        msg.setRecipient(cell);

                        // --- GET MESSAGE TEXT (loop until legal bounds are matched) ---
                        String text;
                        while (true) {
                            System.out.print("Message (max 250 chars): ");
                            text = scanner.nextLine();
                            if (text.length() > 250) {
                                System.out.println("Please enter a message of less than 250 characters.");
                            } else if (text.trim().isEmpty()) {
                                System.out.println("Message cannot be empty.");
                            } else {
                                System.out.println("Message received.");
                                break;
                            }
                        }
                        msg.setMessageText(text);

                        // --- BUILD VALIDATION DATA ELEMENT CODES ---
                        String hash = msg.createMessageHash();

                        // --- SHOW STRUCTURAL DETAILS BEFORE ROUTING ---
                        System.out.println("\n--- MESSAGE DETAILS ---");
                        System.out.println("Message ID  : " + msg.getMessageID());
                        System.out.println("Message Hash: " + hash);
                        System.out.println("Recipient   : " + msg.getRecipient());
                        System.out.println("Message     : " + msg.getMessageText());

                        // --- PROCESS FINAL ACTION TERMINALS ---
                        String result = msg.sendMessage();
                        System.out.println(result);
                    }
                    break;

                case "2":
                    System.out.println(printMessages());
                    break;

                case "3":
                    System.out.println("\nTotal messages sent this session: " + returnTotalMessages());
                    break;

                case "4":
                    // Invokes the brand new parallel-array search, update, delete engine menu
                    runStoredMessagesMenu();
                    break;

                case "5":
                    System.out.println("Shutting down operational connection lines. Goodbye.");
                    chatRunning = false;
                    break;

                default:
                    System.out.println("Invalid console command option entered.");
            }
        }
    }
}