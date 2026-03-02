import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class BankGUI extends JFrame {
    private BankAccount account;
    private BankAccount otherAccount;

    private JLabel lblNumber, lblOwner, lblBalance, lblOpenDate, lblStatus;
    private JTextField txtDepositAmount, txtWithdrawAmount, txtTransferAmount;
    private JButton btnDeposit, btnWithdraw, btnTransfer, btnBlock, btnRefresh;
    private JLabel lblStatusMessage;

    private final Color PRIMARY_COLOR = new Color(76, 175, 80);
    private final Color PRIMARY_DARK = new Color(56, 142, 60);
    private final Color ACCENT_COLOR = new Color(33, 150, 243);
    private final Color CARD_BACKGROUND = new Color(255, 255, 255);
    private final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private final Color TEXT_SECONDARY = new Color(117, 117, 117);

    public BankGUI() {
        account = new BankAccount("Пользователь");
        otherAccount = new BankAccount("Иванов И.И.");
        otherAccount.deposit(50000);

        setTitle(" Банк Онлайн");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshData();

        JOptionPane.showMessageDialog(this,
                " Добро пожаловать в Банк Онлайн!\n\n" +
                        "Счёт для перевода: " + otherAccount.getNumber(),
                "Банк Онлайн",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(25, 118, 210),
                        0, getHeight(), new Color(13, 71, 161)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(255, 255, 255, 10));
                g2d.fillOval(50, 100, 200, 200);
                g2d.fillOval(300, 400, 150, 150);
                g2d.fillOval(-50, 300, 100, 100);
            }
        };

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(" Банк Онлайн", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(5));

        JLabel lblSubtitle = new JLabel("Управление счётом", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblSubtitle);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel balanceCard = createBalanceCard();
        balanceCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        balanceCard.setMaximumSize(new Dimension(420, 180));
        mainPanel.add(balanceCard);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel infoCard = createInfoCard();
        infoCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoCard.setMaximumSize(new Dimension(420, 140));
        mainPanel.add(infoCard);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel operationsPanel = createOperationsPanel();
        operationsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        operationsPanel.setMaximumSize(new Dimension(420, 320));
        mainPanel.add(operationsPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        lblStatusMessage = new JLabel(" Готов к работе", SwingConstants.CENTER);
        lblStatusMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatusMessage.setForeground(new Color(255, 255, 255, 220));
        lblStatusMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblStatusMessage);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);
    }

    private JPanel createBalanceCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, PRIMARY_COLOR,
                        0, getHeight(), PRIMARY_DARK
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight()/2, 20, 20);
            }
        };

        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        card.setPreferredSize(new Dimension(420, 180));

        JPanel leftPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        leftPanel.setOpaque(false);

        JLabel lblBalanceTitle = new JLabel(" Ваш баланс");
        lblBalanceTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBalanceTitle.setForeground(new Color(255, 255, 255, 200));

        lblBalance = new JLabel("0 руб.");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBalance.setForeground(Color.WHITE);

        JLabel lblAvailable = new JLabel("Доступно для снятия");
        lblAvailable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAvailable.setForeground(new Color(255, 255, 255, 180));

        leftPanel.add(lblBalanceTitle);
        leftPanel.add(lblBalance);
        leftPanel.add(lblAvailable);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        JLabel lblIcon = new JLabel("");
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        rightPanel.add(lblIcon);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    private JPanel createInfoCard() {
        JPanel card = new JPanel(new GridLayout(4, 2, 10, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        card.add(createInfoLabel("Номер счёта:"));
        lblNumber = createInfoValue("");
        card.add(lblNumber);

        card.add(createInfoLabel("Владелец:"));
        lblOwner = createInfoValue("");
        card.add(lblOwner);

        card.add(createInfoLabel("Дата открытия:"));
        lblOpenDate = createInfoValue("");
        card.add(lblOpenDate);

        card.add(createInfoLabel("Статус:"));
        lblStatus = createInfoValue("");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        card.add(lblStatus);

        return card;
    }

    private JPanel createOperationsPanel() {
        // Создаём панель СРАЗУ с BoxLayout
        JPanel styledCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };

        // Устанавливаем BoxLayout для этой панели
        styledCard.setLayout(new BoxLayout(styledCard, BoxLayout.Y_AXIS));
        styledCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        styledCard.add(Box.createVerticalStrut(5));

        JLabel lblOpsTitle = new JLabel(" Операции");
        lblOpsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblOpsTitle.setForeground(TEXT_PRIMARY);
        lblOpsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        styledCard.add(lblOpsTitle);
        styledCard.add(Box.createVerticalStrut(15));

        styledCard.add(createOperationRow("➕ Пополнить", txtDepositAmount = new JTextField(8), btnDeposit = createButton("Пополнить", PRIMARY_COLOR)));
        btnDeposit.addActionListener(e -> deposit());
        styledCard.add(Box.createVerticalStrut(10));

        styledCard.add(createOperationRow("➖ Снять", txtWithdrawAmount = new JTextField(8), btnWithdraw = createButton("Снять", ACCENT_COLOR)));
        btnWithdraw.addActionListener(e -> withdraw());
        styledCard.add(Box.createVerticalStrut(10));

        JPanel transferPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        transferPanel.setOpaque(false);
        transferPanel.setMaximumSize(new Dimension(380, 35));

        JLabel lblTransfer = new JLabel(" Перевод:");
        lblTransfer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTransfer.setForeground(TEXT_SECONDARY);
        transferPanel.add(lblTransfer);

        txtTransferAmount = new JTextField(6);
        txtTransferAmount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTransferAmount.setPreferredSize(new Dimension(80, 30));
        transferPanel.add(txtTransferAmount);

        JLabel lblToAccount = new JLabel("на счёт " + otherAccount.getNumber().substring(0, 4) + "...");
        lblToAccount.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblToAccount.setForeground(TEXT_SECONDARY);
        transferPanel.add(lblToAccount);

        btnTransfer = createButton("→", ACCENT_COLOR);
        btnTransfer.setPreferredSize(new Dimension(50, 30));
        btnTransfer.addActionListener(e -> transfer());
        transferPanel.add(btnTransfer);

        styledCard.add(transferPanel);
        styledCard.add(Box.createVerticalStrut(15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setOpaque(false);

        btnBlock = createButton(" Блок/Разблок", new Color(255, 152, 0));
        btnBlock.addActionListener(e -> toggleBlock());
        btnPanel.add(btnBlock);

        btnRefresh = createButton(" Обновить", new Color(158, 158, 158));
        btnRefresh.addActionListener(e -> refreshData());
        btnPanel.add(btnRefresh);

        styledCard.add(btnPanel);

        return styledCard;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    private JLabel createInfoValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    private JPanel createOperationRow(String labelText, JTextField textField, JButton button) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(380, 35));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        panel.add(label);

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setPreferredSize(new Dimension(100, 30));
        panel.add(textField);

        button.setPreferredSize(new Dimension(100, 30));
        panel.add(button);

        return panel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void refreshData() {
        lblNumber.setText(account.getNumber());
        lblOwner.setText(account.getOwnerName());
        lblBalance.setText(String.format("%,d руб.", account.getBalance()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        lblOpenDate.setText(account.getOpenDate().format(formatter));

        if (account.isBlocked()) {
            lblStatus.setText(" Заблокирован");
            lblStatus.setForeground(Color.RED);
        } else {
            lblStatus.setText(" Активен");
            lblStatus.setForeground(PRIMARY_COLOR);
        }

        boolean blocked = account.isBlocked();
        btnDeposit.setEnabled(!blocked);
        btnWithdraw.setEnabled(!blocked);
        btnTransfer.setEnabled(!blocked);
    }

    private void deposit() {
        try {
            String text = txtDepositAmount.getText().trim();
            if (text.isEmpty()) {
                showMessage(" Введите сумму", Color.ORANGE);
                return;
            }
            int amount = Integer.parseInt(text);
            if (amount <= 0) {
                showMessage(" Сумма > 0", Color.ORANGE);
                return;
            }
            if (account.deposit(amount)) {
                showMessage(" +" + amount + " руб.", PRIMARY_COLOR);
                refreshData();
                txtDepositAmount.setText("");
            }
        } catch (NumberFormatException e) {
            showMessage(" Ошибка ввода", Color.RED);
        }
    }

    private void withdraw() {
        try {
            String text = txtWithdrawAmount.getText().trim();
            if (text.isEmpty()) {
                showMessage(" Введите сумму", Color.ORANGE);
                return;
            }
            int amount = Integer.parseInt(text);
            if (amount <= 0) {
                showMessage(" Сумма > 0", Color.ORANGE);
                return;
            }
            if (account.withdraw(amount)) {
                showMessage(" -" + amount + " руб.", PRIMARY_COLOR);
                refreshData();
                txtWithdrawAmount.setText("");
            } else {
                showMessage(" Недостаточно средств", Color.RED);
            }
        } catch (NumberFormatException e) {
            showMessage(" Ошибка ввода", Color.RED);
        }
    }

    private void transfer() {
        try {
            String text = txtTransferAmount.getText().trim();
            if (text.isEmpty()) {
                showMessage(" Введите сумму", Color.ORANGE);
                return;
            }
            int amount = Integer.parseInt(text);
            if (amount <= 0) {
                showMessage(" Сумма > 0", Color.ORANGE);
                return;
            }
            if (account.transfer(otherAccount, amount)) {
                showMessage(" Перевод " + amount + " руб.", PRIMARY_COLOR);
                refreshData();
                txtTransferAmount.setText("");
            } else {
                showMessage(" Ошибка перевода", Color.RED);
            }
        } catch (NumberFormatException e) {
            showMessage(" Ошибка ввода", Color.RED);
        }
    }

    private void toggleBlock() {
        account.setBlocked(!account.isBlocked());
        if (account.isBlocked()) {
            showMessage(" Счёт заблокирован", Color.RED);
        } else {
            showMessage(" Счёт разблокирован", PRIMARY_COLOR);
        }
        refreshData();
    }

    private void showMessage(String text, Color color) {
        lblStatusMessage.setText(text);
        lblStatusMessage.setForeground(color);
    }
}