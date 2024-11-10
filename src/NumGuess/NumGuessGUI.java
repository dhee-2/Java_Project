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
            JFrame frame = new JFrame("숫자야구 게임");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            frame.setVisible(true);
            JFrame loginFrame = new JFrame("로그인");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.getContentPane().add(new LoginPanel());
            loginFrame.setSize(300, 150);
            loginFrame.setVisible(true);
        });
    }
}

// 로그인 창
class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel() {
        setLayout(new BorderLayout());

        // 입력 패널 생성, 레이아웃 설정
        JPanel inputPanel = new JPanel(new GridBagLayout());
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // 그리드백 레이아웃의 제약 조건 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 아이디 레이블 추가
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("아이디"), gbc);

        // 아이디 입력 필드 추가
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("암    호"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(passwordField, gbc);

        // 로그인, 회원 등록, 취소 버튼 생성, 리스너 연결
        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(new LoginButtonListener());

        JButton registerButton = new JButton("회원 등록");
        registerButton.addActionListener(new RegisterButtonListener());

        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(new CancelButtonListener());

        // 버튼 담을 패널 생성
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        // 입력 패널과 버튼 패널을 프레임에 추가
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // 로그인 버튼 리스너
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();

            LoginResult loginResult = isValidLogin(username, password);

            switch (loginResult) {
                case SUCCESS:
                    // 로그인 완료 메시지
                    JOptionPane.showMessageDialog(null,
                            "로그인 완료",
                            "로그인", JOptionPane.INFORMATION_MESSAGE);

                    // 메인 게임 창 열기
                    JFrame gameFrame = new JFrame("숫자야구 게임");
                    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    gameFrame.getContentPane().add(new BaseballGamePanel(username));
                    gameFrame.setSize(500, 300);
                    gameFrame.setVisible(true);

                    // 로그인 창 닫기
                    SwingUtilities.getWindowAncestor(LoginPanel.this).dispose();
                    break;
                case INVALID_PASSWORD:
                    JOptionPane.showMessageDialog(null,
                            "부정확한 암호입니다",
                            "로그인", JOptionPane.ERROR_MESSAGE);
                    break;
                case UNREGISTERED_USERNAME:
                    JOptionPane.showMessageDialog(null,
                            "미등록 아이디입니다",
                            "로그인", JOptionPane.ERROR_MESSAGE);
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
                            return LoginResult.SUCCESS; // 로그인 성공
                        } else {
                            return LoginResult.INVALID_PASSWORD; // 부정확한 암호
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return LoginResult.UNREGISTERED_USERNAME; // 미등록 아이디
        }

        private enum LoginResult {
            SUCCESS, INVALID_PASSWORD, UNREGISTERED_USERNAME
        }
    }
    
    // 회원 등록 버튼 리스너
    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame registerFrame = new JFrame("회원 등록");
            registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            registerFrame.getContentPane().add(new RegisterPanel());
            registerFrame.setSize(300, 150);
            registerFrame.setVisible(true);
        }
    }
    
    // 취소 버튼 리스너
    private class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 로그인 창 닫기
            SwingUtilities.getWindowAncestor(LoginPanel.this).dispose();
        }
    }
}

// 회원등록 창
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
        gbc.anchor = GridBagConstraints.WEST;  // 각 레이블을 왼쪽으로 정렬

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel(" 아 이 디 "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;  // 입력 필드에 대한 가중치 설정
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(newUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;  // 가중치 초기화
        inputPanel.add(new JLabel(" 암      호"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        inputPanel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        inputPanel.add(new JLabel(" 암호확인 "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        inputPanel.add(confirmPasswordField, gbc);

        JButton registerButton = new JButton("등록");
        registerButton.addActionListener(new RegisterButtonListener());

        JButton cancelButton = new JButton("취소");
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
                    // 이미 존재하는 사용자명인 경우 오류 메시지 표시
                    JOptionPane.showMessageDialog(null,
                            "이미 등록된 아이디입니다",
                            "회원 등록 오류", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (!Arrays.equals(newPassword, confirmPassword)) {
            // 암호가 일치하지 않는 경우 오류 메시지 표시
            JOptionPane.showMessageDialog(null,
                    "암호가 일치하지 않습니다",
                    "회원 등록 오류", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // 회원 등록 버튼 리스너
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
                        "회원 등록 완료",
                        "회원등록", JOptionPane.INFORMATION_MESSAGE);

                SwingUtilities.getWindowAncestor(RegisterPanel.this).dispose();
            }
        }
    }
    
    // 취소 버튼 리스너
    private class CancelRegistrationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 등록 창 닫기
            SwingUtilities.getWindowAncestor(RegisterPanel.this).dispose();
        }
    }
}

// 숫자야구 게임 창
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

        // OK 버튼 패널
        JPanel okButtonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new OkButtonListener());
        okButtonPanel.add(okButton);

        // 종료 버튼 패널
        JPanel exitButtonPanel = new JPanel();
        JButton exitButton = new JButton("종료");
        exitButton.addActionListener(new ExitButtonListener());
        exitButtonPanel.add(exitButton);

        // 버튼 패널을 SOUTH에 추가
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(okButtonPanel);
        buttonPanel.add(exitButtonPanel);
        add(buttonPanel, BorderLayout.SOUTH);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setPreferredSize(new Dimension(150, 100));
        resultTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel userInfoLabel = new JLabel("사용자: " + userName);
        userInfoLabel.setHorizontalAlignment(JLabel.LEFT);

        countLabel = new JLabel("시도횟수: " + game.getCount());
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
                // 오류 메시지를 새 창으로 표시
                JOptionPane.showMessageDialog(null,
                        "입력 숫자 오류",
                        "게임 정보", JOptionPane.ERROR_MESSAGE);

                // 숫자 초기화
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

            // 결과를 히스토리에 추가
            appendResultToHistory(result);

            if (strikes == 4) {
                // 게임 완료 메시지를 새 창으로 표시
                JOptionPane.showMessageDialog(null,
                        "시도횟수: " + game.getCount() + "\n축하합니다! 게임완료",
                        "게임 완료", JOptionPane.INFORMATION_MESSAGE);

                // 게임 재시작을 위해 초기화
                game = new BaseballGame();
                clearResultHistory();
            }

            userInput.setLength(0);
            userInputField.setText("");
            countLabel.setText("시도횟수: " + game.getCount());
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
            // 종료 버튼 클릭 시, 현재 창을 닫음
            JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(BaseballGamePanel.this);
            gameFrame.dispose();
        }
    }
    
}

class BaseballGame {
    private int[] targetNumbers; // 야구 게임의 정답 숫자를 저장하는 배열
    private int count; // 시도 횟수를 저장하는 변수
    private String userName; // 사용자 이름을 저장하는 변수

    // BaseballGame 클래스의 생성자
    public BaseballGame() {
        targetNumbers = generateRandomNumbers(); // 랜덤한 숫자로 배열 초기화
        count = 0; // 시도 횟수 초기화
    }

    // 랜덤한 4자리 숫자를 생성하여 반환하는 메서드
    private int[] generateRandomNumbers() {
        int[] numbers = new int[4]; // 4자리 숫자를 저장하는 배열
        boolean[] used = new boolean[10]; // 0부터 9까지의 숫자 사용 여부를 나타내는 배열

        for (int i = 0; i < numbers.length; i++) {
            int randomNumber;
            do {
                // 중복된 숫자가 나오지 않도록 랜덤한 숫자 생성
                randomNumber = (int) (Math.random() * 9) + 1;
            } while (used[randomNumber]); // 중복된 숫자인 경우 다시 난수 생성

            numbers[i] = randomNumber; // 생성된 숫자를 배열에 저장
            used[randomNumber] = true; // 사용된 숫자로 표시
        }

        return numbers; // 생성된 숫자 배열 반환
    }

    // 시도 횟수를 증가시키는 메서드
    public void incCount() {
        count++;
    }

    // 사용자가 입력한 숫자와 정답 숫자를 비교하여 스트라이크 개수를 반환하는 메서드
    public int getStrike(int[] userNumbers) {
        int strikes = 0; // 스트라이크 개수를 저장하는 변수
        for (int i = 0; i < 4; i++) {
            if (userNumbers[i] == targetNumbers[i]) {
                strikes++; // 같은 위치에 숫자가 일치하면 스트라이크 증가
            }
        }
        return strikes; // 스트라이크 개수 반환
    }

    // 사용자가 입력한 숫자와 정답 숫자를 비교하여 볼 개수를 반환하는 메서드
    public int getBall(int[] userNumbers) {
        int balls = 0; // 볼 개수를 저장하는 변수
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != j && userNumbers[i] == targetNumbers[j]) {
                    balls++; // 다른 위치에 숫자가 일치하면 볼 증가
                }
            }
        }
        return balls; // 볼 개수 반환
    }

    // 현재까지의 시도 횟수를 반환하는 메서드
    public int getCount() {
        return count; // 시도 횟수 반환
    }
}
