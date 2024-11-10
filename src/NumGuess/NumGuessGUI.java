package NumGuess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class NumGuessGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("���ھ߱� ����");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            frame.setVisible(true);
            JFrame loginFrame = new JFrame("�α���");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.getContentPane().add(new LoginPanel());
            loginFrame.setSize(300, 150);
            loginFrame.setVisible(true);
        });
    }
}

// �α��� â
class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel() {
        setLayout(new BorderLayout());

        // �Է� �г� ����, ���̾ƿ� ����
        JPanel inputPanel = new JPanel(new GridBagLayout());
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // �׸���� ���̾ƿ��� ���� ���� ����
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // ���̵� ���̺� �߰�
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("���̵�"), gbc);

        // ���̵� �Է� �ʵ� �߰�
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("��    ȣ"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(passwordField, gbc);

        // �α���, ȸ�� ���, ��� ��ư ����, ������ ����
        JButton loginButton = new JButton("�α���");
        loginButton.addActionListener(new LoginButtonListener());

        JButton registerButton = new JButton("ȸ�� ���");
        registerButton.addActionListener(new RegisterButtonListener());

        JButton cancelButton = new JButton("���");
        cancelButton.addActionListener(new CancelButtonListener());

        // ��ư ���� �г� ����
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        // �Է� �гΰ� ��ư �г��� �����ӿ� �߰�
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // �α��� ��ư ������
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();

            LoginResult loginResult = isValidLogin(username, password);

            switch (loginResult) {
                case SUCCESS:
                    // �α��� �Ϸ� �޽���
                    JOptionPane.showMessageDialog(null,
                            "�α��� �Ϸ�",
                            "�α���", JOptionPane.INFORMATION_MESSAGE);

                    // ���� ���� â ����
                    JFrame gameFrame = new JFrame("���ھ߱� ����");
                    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    gameFrame.getContentPane().add(new BaseballGamePanel(username));
                    gameFrame.setSize(500, 300);
                    gameFrame.setVisible(true);

                    // �α��� â �ݱ�
                    SwingUtilities.getWindowAncestor(LoginPanel.this).dispose();
                    break;
                case INVALID_PASSWORD:
                    JOptionPane.showMessageDialog(null,
                            "����Ȯ�� ��ȣ�Դϴ�",
                            "�α���", JOptionPane.ERROR_MESSAGE);
                    break;
                case UNREGISTERED_USERNAME:
                    JOptionPane.showMessageDialog(null,
                            "�̵�� ���̵��Դϴ�",
                            "�α���", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }

        private LoginResult isValidLogin(String username, char[] password) {
            File file = new File("user_info.txt");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String storedUsername = parts[0];
                    String storedPassword = parts[1];

                    if (username.equals(storedUsername)) {
                        if (Arrays.equals(password, storedPassword.toCharArray())) {
                            return LoginResult.SUCCESS; // �α��� ����
                        } else {
                            return LoginResult.INVALID_PASSWORD; // ����Ȯ�� ��ȣ
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return LoginResult.UNREGISTERED_USERNAME; // �̵�� ���̵�
        }

        private enum LoginResult {
            SUCCESS, INVALID_PASSWORD, UNREGISTERED_USERNAME
        }
    }
    
    // ȸ�� ��� ��ư ������
    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame registerFrame = new JFrame("ȸ�� ���");
            registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            registerFrame.getContentPane().add(new RegisterPanel());
            registerFrame.setSize(300, 150);
            registerFrame.setVisible(true);
        }
    }
    
    // ��� ��ư ������
    private class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // �α��� â �ݱ�
            SwingUtilities.getWindowAncestor(LoginPanel.this).dispose();
        }
    }
}

// ȸ����� â
class RegisterPanel extends JPanel {
	private JTextField newUsernameField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public RegisterPanel() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        newUsernameField = new JTextField();
        newPasswordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;  // �� ���̺��� �������� ����

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel(" �� �� �� "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;  // �Է� �ʵ忡 ���� ����ġ ����
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(newUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;  // ����ġ �ʱ�ȭ
        inputPanel.add(new JLabel(" ��      ȣ"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        inputPanel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        inputPanel.add(new JLabel(" ��ȣȮ�� "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        inputPanel.add(confirmPasswordField, gbc);

        JButton registerButton = new JButton("���");
        registerButton.addActionListener(new RegisterButtonListener());

        JButton cancelButton = new JButton("���");
        cancelButton.addActionListener(new CancelRegistrationButtonListener());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean isValidRegistration(String newUsername, char[] newPassword, char[] confirmPassword) {
        File file = new File("user_info.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String storedUsername = parts[0];

                if (newUsername.equals(storedUsername)) {
                    // �̹� �����ϴ� ����ڸ��� ��� ���� �޽��� ǥ��
                    JOptionPane.showMessageDialog(null,
                            "�̹� ��ϵ� ���̵��Դϴ�",
                            "ȸ�� ��� ����", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (!Arrays.equals(newPassword, confirmPassword)) {
            // ��ȣ�� ��ġ���� �ʴ� ��� ���� �޽��� ǥ��
            JOptionPane.showMessageDialog(null,
                    "��ȣ�� ��ġ���� �ʽ��ϴ�",
                    "ȸ�� ��� ����", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // ȸ�� ��� ��ư ������
    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String newUsername = newUsernameField.getText();
            char[] newPassword = newPasswordField.getPassword();
            char[] confirmPassword = confirmPasswordField.getPassword();

            if (isValidRegistration(newUsername, newPassword, confirmPassword)) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_info.txt", true))) {
                    writer.write(newUsername + "," + new String(newPassword));
                    writer.newLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(null,
                        "ȸ�� ��� �Ϸ�",
                        "ȸ�����", JOptionPane.INFORMATION_MESSAGE);

                SwingUtilities.getWindowAncestor(RegisterPanel.this).dispose();
            }
        }
    }
    
    // ��� ��ư ������
    private class CancelRegistrationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // ��� â �ݱ�
            SwingUtilities.getWindowAncestor(RegisterPanel.this).dispose();
        }
    }
}

// ���ھ߱� ���� â
class BaseballGamePanel extends JPanel {
    private BaseballGame game;
    private JTextField userInputField;
    private JTextArea resultTextArea;
    private StringBuilder userInput;
    private String userName;
    private JLabel countLabel;

    public BaseballGamePanel(String userName) {
        game = new BaseballGame();
        userInput = new StringBuilder();
        this.userName = userName;

        setLayout(new BorderLayout());

        int padding = 20;
        int spacing = 20;

        JPanel inputPanel = createNumberPanel();

        // OK ��ư �г�
        JPanel okButtonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new OkButtonListener());
        okButtonPanel.add(okButton);

        // ���� ��ư �г�
        JPanel exitButtonPanel = new JPanel();
        JButton exitButton = new JButton("����");
        exitButton.addActionListener(new ExitButtonListener());
        exitButtonPanel.add(exitButton);

        // ��ư �г��� SOUTH�� �߰�
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(okButtonPanel);
        buttonPanel.add(exitButtonPanel);
        add(buttonPanel, BorderLayout.SOUTH);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setPreferredSize(new Dimension(150, 100));
        resultTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel userInfoLabel = new JLabel("�����: " + userName);
        userInfoLabel.setHorizontalAlignment(JLabel.LEFT);

        countLabel = new JLabel("�õ�Ƚ��: " + game.getCount());
        countLabel.setHorizontalAlignment(JLabel.LEFT);

        setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, spacing));

        add(inputPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(userInfoLabel, BorderLayout.NORTH);
        rightPanel.add(resultTextArea, BorderLayout.CENTER);
        rightPanel.add(countLabel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);
    }


    private JPanel createNumberPanel() {
        JPanel numberPanel = new JPanel(new GridBagLayout());
        userInputField = new JTextField(10);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 1; i <= 9; i++) {
            JButton numberButton = new JButton(Integer.toString(i));
            numberButton.addActionListener(new NumberButtonListener());

            gbc.gridx = (i - 1) % 3;
            gbc.gridy = (i - 1) / 3;
            numberPanel.add(numberButton, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        numberPanel.add(userInputField, gbc);

        return numberPanel;
    }

    private class NumberButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            if (!clickedButton.getText().equals("0")) {
                userInput.append(clickedButton.getText());
                userInputField.setText(userInput.toString());
            }
        }
    }

    private class OkButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = userInput.toString();

            if (input.length() != 4 || !input.matches("\\d{4}")) {
                // ���� �޽����� �� â���� ǥ��
                JOptionPane.showMessageDialog(null,
                        "�Է� ���� ����",
                        "���� ����", JOptionPane.ERROR_MESSAGE);

                // ���� �ʱ�ȭ
                userInput.setLength(0);
                userInputField.setText("");

                return;
            }

            int[] userNumbers = new int[4];
            for (int i = 0; i < 4; i++) {
                userNumbers[i] = Character.getNumericValue(input.charAt(i));
            }

            game.incCount();
            int strikes = game.getStrike(userNumbers);
            int balls = game.getBall(userNumbers);

            String result = input + "  " + strikes + "S" + balls + "B";

            // ����� �����丮�� �߰�
            appendResultToHistory(result);

            if (strikes == 4) {
                // ���� �Ϸ� �޽����� �� â���� ǥ��
                JOptionPane.showMessageDialog(null,
                        "�õ�Ƚ��: " + game.getCount() + "\n�����մϴ�! ���ӿϷ�",
                        "���� �Ϸ�", JOptionPane.INFORMATION_MESSAGE);

                // ���� ������� ���� �ʱ�ȭ
                game = new BaseballGame();
                clearResultHistory();
            }

            userInput.setLength(0);
            userInputField.setText("");
            countLabel.setText("�õ�Ƚ��: " + game.getCount());
        }

        private void appendResultToHistory(String result) {
            resultTextArea.append(result + "\n");
        }

        private void clearResultHistory() {
            resultTextArea.setText("");
        }
    }
    
    private class ExitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // ���� ��ư Ŭ�� ��, ���� â�� ����
            JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(BaseballGamePanel.this);
            gameFrame.dispose();
        }
    }
    
}

class BaseballGame {
    private int[] targetNumbers; // �߱� ������ ���� ���ڸ� �����ϴ� �迭
    private int count; // �õ� Ƚ���� �����ϴ� ����
    private String userName; // ����� �̸��� �����ϴ� ����

    // BaseballGame Ŭ������ ������
    public BaseballGame() {
        targetNumbers = generateRandomNumbers(); // ������ ���ڷ� �迭 �ʱ�ȭ
        count = 0; // �õ� Ƚ�� �ʱ�ȭ
    }

    // ������ 4�ڸ� ���ڸ� �����Ͽ� ��ȯ�ϴ� �޼���
    private int[] generateRandomNumbers() {
        int[] numbers = new int[4]; // 4�ڸ� ���ڸ� �����ϴ� �迭
        boolean[] used = new boolean[10]; // 0���� 9������ ���� ��� ���θ� ��Ÿ���� �迭

        for (int i = 0; i < numbers.length; i++) {
            int randomNumber;
            do {
                // �ߺ��� ���ڰ� ������ �ʵ��� ������ ���� ����
                randomNumber = (int) (Math.random() * 9) + 1;
            } while (used[randomNumber]); // �ߺ��� ������ ��� �ٽ� ���� ����

            numbers[i] = randomNumber; // ������ ���ڸ� �迭�� ����
            used[randomNumber] = true; // ���� ���ڷ� ǥ��
        }

        return numbers; // ������ ���� �迭 ��ȯ
    }

    // �õ� Ƚ���� ������Ű�� �޼���
    public void incCount() {
        count++;
    }

    // ����ڰ� �Է��� ���ڿ� ���� ���ڸ� ���Ͽ� ��Ʈ����ũ ������ ��ȯ�ϴ� �޼���
    public int getStrike(int[] userNumbers) {
        int strikes = 0; // ��Ʈ����ũ ������ �����ϴ� ����
        for (int i = 0; i < 4; i++) {
            if (userNumbers[i] == targetNumbers[i]) {
                strikes++; // ���� ��ġ�� ���ڰ� ��ġ�ϸ� ��Ʈ����ũ ����
            }
        }
        return strikes; // ��Ʈ����ũ ���� ��ȯ
    }

    // ����ڰ� �Է��� ���ڿ� ���� ���ڸ� ���Ͽ� �� ������ ��ȯ�ϴ� �޼���
    public int getBall(int[] userNumbers) {
        int balls = 0; // �� ������ �����ϴ� ����
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != j && userNumbers[i] == targetNumbers[j]) {
                    balls++; // �ٸ� ��ġ�� ���ڰ� ��ġ�ϸ� �� ����
                }
            }
        }
        return balls; // �� ���� ��ȯ
    }

    // ��������� �õ� Ƚ���� ��ȯ�ϴ� �޼���
    public int getCount() {
        return count; // �õ� Ƚ�� ��ȯ
    }
}
