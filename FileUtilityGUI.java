package FileUtilityApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FileUtilityGUI extends JFrame {

    private JTextArea textArea;
    private JTextField fileNameField, searchField, replaceField;
    private JButton openButton, saveButton, appendButton, clearButton, newFileButton, searchReplaceButton;
    private File currentFile;

    public FileUtilityGUI() {
        setTitle("File Utility - Task 1");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());

        fileNameField = new JTextField(25);
        fileNameField.setEditable(false);
        topPanel.add(new JLabel("Current File:"));
        topPanel.add(fileNameField);

        newFileButton = new JButton("New File");
        openButton = new JButton("Open");
        saveButton = new JButton("Save (Overwrite)");
        appendButton = new JButton("Append");
        clearButton = new JButton("Clear");

        topPanel.add(newFileButton);
        topPanel.add(openButton);
        topPanel.add(saveButton);
        topPanel.add(appendButton);
        topPanel.add(clearButton);

        add(topPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(10);
        replaceField = new JTextField(10);
        searchReplaceButton = new JButton("Find & Replace");

        bottomPanel.add(new JLabel("Find:"));
        bottomPanel.add(searchField);
        bottomPanel.add(new JLabel("Replace:"));
        bottomPanel.add(replaceField);
        bottomPanel.add(searchReplaceButton);

        add(bottomPanel, BorderLayout.SOUTH);

        newFileButton.addActionListener(e -> createNewFile());
        openButton.addActionListener(e -> chooseFile());
        saveButton.addActionListener(e -> saveToFile(false));
        appendButton.addActionListener(e -> saveToFile(true));
        clearButton.addActionListener(e -> textArea.setText(""));
        searchReplaceButton.addActionListener(e -> findAndReplace());
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            fileNameField.setText(currentFile.getAbsolutePath());
            readFromFile(currentFile);
        }
    }

    private void createNewFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Create New File");

        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            try {
                if (currentFile.createNewFile()) {
                    fileNameField.setText(currentFile.getAbsolutePath());
                    textArea.setText("");
                    showMessage("New file created.");
                } else {
                    showMessage("File already exists.");
                }
            } catch (IOException e) {
                showMessage("Error creating file: " + e.getMessage());
            }
        }
    }

    private void readFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            textArea.setText("");
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException ex) {
            showMessage("Error reading file: " + ex.getMessage());
        }
    }

    private void saveToFile(boolean append) {
        if (currentFile == null) {
            showMessage("No file selected. Use 'New File' or 'Open' first.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile, append))) {
            writer.write(textArea.getText());
            showMessage(append ? "Content appended successfully." : "File saved successfully.");
        } catch (IOException ex) {
            showMessage("Error writing to file: " + ex.getMessage());
        }
    }

    private void findAndReplace() {
        String search = searchField.getText();
        String replace = replaceField.getText();

        if (search.isEmpty()) {
            showMessage("Enter text to search.");
            return;
        }

        String content = textArea.getText();
        if (!content.contains(search)) {
            showMessage("Text not found.");
            return;
        }

        String newContent = content.replaceAll(search, replace);
        textArea.setText(newContent);
        showMessage("Replace complete.");
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileUtilityGUI().setVisible(true));
    }
}
