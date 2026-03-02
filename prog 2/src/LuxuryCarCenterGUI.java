import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class LuxuryCarCenterGUI extends JFrame {
    private AutoShowroom showroom;
    private JTabbedPane tabbedPane;
    private JPanel pnlTotalVehicles, pnlTotalValue, pnlAvgPrice;
    private DefaultTableModel tableModel;
    private JTable vehiclesTableInTab;

    // Цветовая схема - Luxury Dark Theme
    private final Color DARK_BG = new Color(15, 23, 42);
    private final Color CARD_BG = new Color(30, 41, 59);
    private final Color TAB_BG = new Color(220, 38, 38); // Красный для вкладок
    private final Color ACCENT_BLUE = new Color(56, 189, 248);
    private final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private final Color ACCENT_GREEN = new Color(34, 197, 94);
    private final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private final Color TEXT_PRIMARY = new Color(241, 245, 249); // Белый текст
    private final Color TEXT_SECONDARY = new Color(148, 163, 184);
    private final Color GRADIENT_START = new Color(99, 102, 241);
    private final Color GRADIENT_END = new Color(168, 85, 247);

    public LuxuryCarCenterGUI() {
        showroom = new AutoShowroom("Elite Motors");
        addTestVehicles();

        setTitle("Elite Motors - Люксовый Автоцентр");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void addTestVehicles() {
        Random random = new Random();
        String[] manufacturers = {"Mercedes", "BMW", "Audi", "Porsche", "Lexus", "Tesla"};
        String[] models = {"S-Class", "X7", "A8", "Cayenne", "LS", "Model S"};
        VehicleCategory[] categories = {VehicleCategory.SEDAN, VehicleCategory.SUV, VehicleCategory.ELECTRIC, VehicleCategory.COUPE};

        for (int i = 0; i < 20; i++) {
            String vin = "ELT" + String.format("%05d", i);
            String manufacturer = manufacturers[random.nextInt(manufacturers.length)];
            String model = models[random.nextInt(models.length)];
            int year = random.nextInt(10) + 2016;
            int mileage = random.nextInt(150000);
            double price = random.nextInt(8000000) + 2000000;
            VehicleCategory category = categories[random.nextInt(categories.length)];

            showroom.addVehicle(new Automobile(vin, model, manufacturer, year, mileage, price, category));
        }
    }

    private void initComponents() {
        // Настройки для вкладок - КРАСНЫЙ текст на светлом фоне
        UIManager.put("TabbedPane.background", new Color(240, 240, 240)); // Светло-серый фон кнопок
        UIManager.put("TabbedPane.foreground", Color.RED); // КРАСНЫЙ текст
        UIManager.put("TabbedPane.selected", new Color(220, 38, 38)); // Красная выбранная вкладка
        UIManager.put("TabbedPane.tabAreaBackground", new Color(240, 240, 240)); // Светлая область вкладок
        UIManager.put("TabbedPane.contentAreaColor", DARK_BG); // Тёмный фон контента
        UIManager.put("TabbedPane.selectedTabTitleForeground", Color.WHITE); // Белый текст выбранной
        UIManager.put("TabbedPane.unselectedTabTitleForeground", Color.RED); // КРАСНЫЙ текст невыбранной
        UIManager.put("TabbedPane.tabSelectionEnabled", true);

        getContentPane().setBackground(DARK_BG);

        JPanel headerPanel = createLuxuryHeader();
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        tabbedPane.setPreferredSize(new Dimension(220, getHeight()));
        tabbedPane.setBackground(new Color(240, 240, 240));
        tabbedPane.setForeground(Color.RED);
        tabbedPane.setOpaque(true);

        tabbedPane.addTab("Главная", createDashboardPanel());
        tabbedPane.addTab("Автопарк", createInventoryPanel());
        tabbedPane.addTab("Поиск по году", createYearSearchPanel());
        tabbedPane.addTab("Фильтр и сортировка", createFilterPanel());
        tabbedPane.addTab("Добавить авто", createAddVehiclePanel());

        // Применяем красный текст к каждой вкладке
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setBackgroundAt(i, new Color(240, 240, 240)); // Светлый фон
            tabbedPane.setForegroundAt(i, Color.RED); // КРАСНЫЙ текст
        }

        SwingUtilities.invokeLater(() -> {
            tabbedPane.revalidate();
            tabbedPane.repaint();
        });

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    private JPanel createLuxuryHeader() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, GRADIENT_START,
                        getWidth(), 0, GRADIENT_END
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), 100);
            }
        };

        panel.setPreferredSize(new Dimension(getWidth(), 100));
        panel.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("ELITE MOTORS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        panel.add(lblTitle, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DARK_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        statsPanel.setBackground(DARK_BG);
        statsPanel.setMaximumSize(new Dimension(1100, 140));

        pnlTotalVehicles = createLuxuryStatCard("Всего автомобилей", "0", ACCENT_BLUE);
        pnlTotalValue = createLuxuryStatCard("Общая стоимость", "0 RUB", ACCENT_GREEN);
        pnlAvgPrice = createLuxuryStatCard("Средняя цена", "0 RUB", ACCENT_ORANGE);

        statsPanel.add(pnlTotalVehicles);
        statsPanel.add(pnlTotalValue);
        statsPanel.add(pnlAvgPrice);

        panel.add(statsPanel);
        panel.add(Box.createVerticalStrut(25));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Текущий автопарк");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(ACCENT_BLUE);
        tablePanel.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"VIN", "Производитель", "Модель", "Год", "Пробег", "Цена", "Категория"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        styleLuxuryTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(CARD_BG);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel);

        SwingUtilities.invokeLater(this::updateDashboard);
        return panel;
    }

    private JPanel createLuxuryStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setMaximumSize(new Dimension(350, 140));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private void styleLuxuryTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(40);
        table.setSelectionBackground(new Color(56, 189, 248, 30));
        table.setGridColor(new Color(51, 65, 85));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_PRIMARY);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(GRADIENT_START);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(GRADIENT_START);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                return this;
            }
        };

        header.setDefaultRenderer(headerRenderer);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(CARD_BG);
                setForeground(TEXT_PRIMARY);
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(CARD_BG);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_PURPLE, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblManuf = new JLabel("Производитель:");
        lblManuf.setForeground(TEXT_PRIMARY); // Белый текст
        filterPanel.add(lblManuf);

        JComboBox<String> comboManufacturer = new JComboBox<>(new String[]{"Все", "Mercedes", "BMW", "Audi", "Porsche", "Lexus", "Tesla"});
        comboManufacturer.setPreferredSize(new Dimension(160, 32));
        comboManufacturer.setBackground(DARK_BG); // ТЁМНЫЙ фон
        comboManufacturer.setForeground(Color.BLACK); // БЕЛЫЙ текст
        comboManufacturer.setOpaque(true); // Делаем фон видимым
        comboManufacturer.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected ? ACCENT_BLUE : DARK_BG);
                label.setForeground(TEXT_PRIMARY);
                label.setOpaque(true);
                return label;
            }
        });
        filterPanel.add(comboManufacturer);

        filterPanel.add(Box.createHorizontalStrut(20));

        JLabel lblCat = new JLabel("Категория:");
        lblCat.setForeground(TEXT_PRIMARY); // Белый текст
        filterPanel.add(lblCat);

        JComboBox<VehicleCategory> comboCategory = new JComboBox<>(VehicleCategory.values());
        comboCategory.insertItemAt(VehicleCategory.SEDAN, 0);
        comboCategory.setSelectedIndex(0);
        comboCategory.setPreferredSize(new Dimension(160, 32));
        comboCategory.setBackground(DARK_BG); // ТЁМНЫЙ фон
        comboCategory.setForeground(Color.BLACK); // БЕЛЫЙ текст
        comboCategory.setOpaque(true); // Делаем фон видимым
        comboCategory.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected ? ACCENT_BLUE : DARK_BG);
                label.setForeground(TEXT_PRIMARY);
                label.setOpaque(true);
                return label;
            }
        });
        filterPanel.add(comboCategory);

        JButton btnFilter = createLuxuryButton("Фильтр", ACCENT_BLUE);
        btnFilter.addActionListener(e -> filterInventory(comboManufacturer.getSelectedItem().toString(),
                comboCategory.getSelectedItem()));
        filterPanel.add(btnFilter);

        JButton btnReset = createLuxuryButton("Сбросить", ACCENT_ORANGE);
        btnReset.addActionListener(e -> {
            comboManufacturer.setSelectedIndex(0);
            comboCategory.setSelectedIndex(0);
            updateInventoryTable(new ArrayList<>(showroom.getVehiclesSortedByYear()));
        });
        filterPanel.add(btnReset);

        panel.add(filterPanel, BorderLayout.NORTH);

        String[] columns = {"VIN", "Производитель", "Модель", "Год", "Пробег", "Цена", "Категория"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        styleLuxuryTable(table);
        vehiclesTableInTab = table;

        List<Automobile> vehicles = new ArrayList<>(showroom.getVehiclesSortedByYear());
        for (Automobile vehicle : vehicles) {
            model.addRow(new Object[]{
                    vehicle.getVin(),
                    vehicle.getManufacturer(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    String.format("%,d", vehicle.getMileage()),
                    String.format("%,d", (int)vehicle.getPrice()),
                    vehicle.getCategory()
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(CARD_BG);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    private JButton createLuxuryButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void filterInventory(String manufacturer, Object category) {
        List<Automobile> filtered = showroom.getVehiclesSortedByYear().stream()
                .filter(v -> manufacturer.equals("Все") || v.getManufacturer().equals(manufacturer))
                .filter(v -> category == null || v.getCategory() == category)
                .collect(Collectors.toList());

        updateInventoryTable(filtered);
    }

    private void updateInventoryTable(List<Automobile> vehicles) {
        if (vehiclesTableInTab != null) {
            DefaultTableModel model = (DefaultTableModel) vehiclesTableInTab.getModel();
            model.setRowCount(0);
            for (Automobile vehicle : vehicles) {
                model.addRow(new Object[]{
                        vehicle.getVin(),
                        vehicle.getManufacturer(),
                        vehicle.getModel(),
                        vehicle.getYear(),
                        String.format("%,d", vehicle.getMileage()),
                        String.format("%,d", (int)vehicle.getPrice()),
                        vehicle.getCategory()
                });
            }
        }
    }

    private JPanel createYearSearchPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DARK_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        inputPanel.setBackground(CARD_BG);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblFromYear = new JLabel("С года:");
        lblFromYear.setForeground(TEXT_PRIMARY);
        lblFromYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(lblFromYear);

        JTextField txtFromYear = new JTextField(8);
        txtFromYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtFromYear.setPreferredSize(new Dimension(120, 35));
        txtFromYear.setBackground(DARK_BG);
        txtFromYear.setForeground(TEXT_PRIMARY);
        inputPanel.add(txtFromYear);

        JLabel lblToYear = new JLabel("По году:");
        lblToYear.setForeground(TEXT_PRIMARY);
        lblToYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(lblToYear);

        JTextField txtToYear = new JTextField(8);
        txtToYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtToYear.setPreferredSize(new Dimension(120, 35));
        txtToYear.setBackground(DARK_BG);
        txtToYear.setForeground(TEXT_PRIMARY);
        inputPanel.add(txtToYear);

        JButton btnSearch = createLuxuryButton("Поиск", ACCENT_GREEN);
        btnSearch.setPreferredSize(new Dimension(140, 40));
        btnSearch.addActionListener(e -> searchByYear(txtFromYear, txtToYear));
        inputPanel.add(btnSearch);

        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(20));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(CARD_BG);
        textArea.setForeground(TEXT_PRIMARY);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        textArea.setText("Введите диапазон лет для поиска\n\nПример:\nС года: 2018\nПо год: 2022\n\nНажмите 'Поиск' для поиска");

        panel.add(new JScrollPane(textArea));

        return panel;
    }

    private void searchByYear(JTextField txtFrom, JTextField txtTo) {
        try {
            int fromYear = Integer.parseInt(txtFrom.getText().trim());
            int toYear = Integer.parseInt(txtTo.getText().trim());

            if (fromYear > toYear) {
                JOptionPane.showMessageDialog(this, "Год 'С года' не может быть больше 'По год'!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Automobile> filtered = showroom.getVehiclesSortedByYear().stream()
                    .filter(v -> v.getYear() >= fromYear && v.getYear() <= toYear)
                    .collect(Collectors.toList());

            JTextArea textArea = null;
            Component[] components = ((JPanel) tabbedPane.getComponentAt(2)).getComponents();
            for (Component comp : components) {
                if (comp instanceof JScrollPane) {
                    textArea = (JTextArea) ((JScrollPane) comp).getViewport().getView();
                    break;
                }
            }

            if (textArea != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("=============================================================\n");
                sb.append("  РЕЗУЛЬТАТЫ ПОИСКА: ").append(fromYear).append(" - ").append(toYear).append("\n");
                sb.append("=============================================================\n\n");
                sb.append("Найдено: ").append(filtered.size()).append(" автомобилей\n\n");

                int currentYear = 2025;
                int totalAge = 0;
                for (Automobile v : filtered) {
                    totalAge += (currentYear - v.getYear());
                }
                double avgAge = filtered.isEmpty() ? 0 : (double) totalAge / filtered.size();

                sb.append("Средний возраст: ").append(String.format("%.1f", avgAge)).append(" лет\n\n");
                sb.append("-------------------------------------------------------------\n");

                int num = 1;
                for (Automobile v : filtered) {
                    sb.append(String.format("%2d. %s %s (%d)\n", num++, v.getManufacturer(), v.getModel(), v.getYear()));
                    sb.append(String.format("    VIN: %s | Пробег: %,d км | Цена: %,d RUB | %s\n",
                            v.getVin(), v.getMileage(), (int)v.getPrice(), v.getCategory()));
                    sb.append("-------------------------------------------------------------\n");
                }

                textArea.setText(sb.toString());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Введите корректные года!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DARK_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(CARD_BG);
        textArea.setForeground(TEXT_PRIMARY);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnExecute = createLuxuryButton("Выполнить задание", ACCENT_PURPLE);
        btnExecute.setPreferredSize(new Dimension(220, 45));
        btnExecute.addActionListener(e -> executeFilterTask(textArea));

        panel.add(btnExecute);
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JScrollPane(textArea));

        return panel;
    }

    private void executeFilterTask(JTextArea textArea) {
        StringBuilder sb = new StringBuilder();
        sb.append("=============================================================\n");
        sb.append("  ФИЛЬТР И СОРТИРОВКА\n");
        sb.append("=============================================================\n\n");

        List<Automobile> vehicles = new ArrayList<>(showroom.getVehiclesSortedByYear());

        sb.append("1. АВТОМОБИЛИ С ПРОБЕГОМ МЕНЕЕ 50,000 КМ:\n");
        sb.append("-------------------------------------------------------------\n");
        List<Automobile> lowMileage = vehicles.stream()
                .filter(v -> v.getMileage() < 50000)
                .collect(Collectors.toList());
        sb.append("Найдено: ").append(lowMileage.size()).append(" автомобилей\n\n");
        for (Automobile v : lowMileage) {
            sb.append(String.format("   - %s %s (%d) - %,d км\n",
                    v.getManufacturer(), v.getModel(), v.getYear(), v.getMileage()));
        }
        sb.append("\n");

        sb.append("2. ТОП-3 САМЫЕ ДОРОГИЕ:\n");
        sb.append("-------------------------------------------------------------\n");
        List<Automobile> top3 = vehicles.stream()
                .sorted(Comparator.comparingDouble(Automobile::getPrice).reversed())
                .limit(3)
                .collect(Collectors.toList());
        for (int i = 0; i < top3.size(); i++) {
            sb.append(String.format("   %d. %s %s - %,d RUB\n",
                    i+1, top3.get(i).getManufacturer(), top3.get(i).getModel(), (int)top3.get(i).getPrice()));
        }
        sb.append("\n");

        double avgMileage = vehicles.stream()
                .mapToInt(Automobile::getMileage)
                .average()
                .orElse(0);
        sb.append("3. СРЕДНИЙ ПРОБЕГ: ").append(String.format("%,d", (int)avgMileage)).append(" км\n\n");

        sb.append("4. ГРУППИРОВКА ПО ПРОИЗВОДИТЕЛЮ:\n");
        sb.append("-------------------------------------------------------------\n");
        Map<String, List<Automobile>> byManufacturer = vehicles.stream()
                .collect(Collectors.groupingBy(Automobile::getManufacturer));
        byManufacturer.forEach((manufacturer, list) ->
                sb.append(String.format("   %s: %d автомобилей\n", manufacturer, list.size())));

        textArea.setText(sb.toString());
    }

    private JPanel createAddVehiclePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DARK_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_GREEN, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        formPanel.setMaximumSize(new Dimension(650, 380));

        JTextField txtVin = createStyledTextField();
        JTextField txtModel = createStyledTextField();
        JTextField txtManufacturer = createStyledTextField();
        JTextField txtYear = createStyledTextField();
        JTextField txtMileage = createStyledTextField();
        JTextField txtPrice = createStyledTextField();

        // JComboBox с ТЁМНОЙ темой - ИСПРАВЛЕНО
        JComboBox<VehicleCategory> comboCategory = new JComboBox<>(VehicleCategory.values());
        comboCategory.setBackground(DARK_BG); // ТЁМНЫЙ фон
        comboCategory.setForeground(Color.BLACK); // БЕЛЫЙ текст
        comboCategory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboCategory.setPreferredSize(new Dimension(200, 35));
        comboCategory.setOpaque(true); // Делаем фон видимым
        comboCategory.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Рендерер для выпадающего списка
        comboCategory.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected ? ACCENT_BLUE : DARK_BG);
                label.setForeground(TEXT_PRIMARY);
                label.setOpaque(true);
                return label;
            }
        });

        formPanel.add(createStyledLabel("VIN:"));
        formPanel.add(txtVin);
        formPanel.add(createStyledLabel("Модель:"));
        formPanel.add(txtModel);
        formPanel.add(createStyledLabel("Производитель:"));
        formPanel.add(txtManufacturer);
        formPanel.add(createStyledLabel("Год выпуска:"));
        formPanel.add(txtYear);
        formPanel.add(createStyledLabel("Пробег (км):"));
        formPanel.add(txtMileage);
        formPanel.add(createStyledLabel("Цена (RUB):"));
        formPanel.add(txtPrice);
        formPanel.add(createStyledLabel("Категория:"));
        formPanel.add(comboCategory);

        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(20));

        JButton btnAdd = createLuxuryButton("Добавить автомобиль", ACCENT_GREEN);
        btnAdd.setPreferredSize(new Dimension(270, 45));
        btnAdd.addActionListener(e -> addVehicle(txtVin, txtModel, txtManufacturer, txtYear, txtMileage, txtPrice, comboCategory));
        panel.add(btnAdd);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(DARK_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY); // ИСПРАВЛЕНО: белый текст вместо синего
        return label;
    }

    private void addVehicle(JTextField txtVin, JTextField txtModel, JTextField txtManufacturer,
                            JTextField txtYear, JTextField txtMileage, JTextField txtPrice,
                            JComboBox<VehicleCategory> comboCategory) {
        try {
            Automobile vehicle = new Automobile(
                    txtVin.getText().trim(),
                    txtModel.getText().trim(),
                    txtManufacturer.getText().trim(),
                    Integer.parseInt(txtYear.getText().trim()),
                    Integer.parseInt(txtMileage.getText().trim()),
                    Double.parseDouble(txtPrice.getText().trim()),
                    (VehicleCategory) comboCategory.getSelectedItem()
            );

            if (showroom.addVehicle(vehicle)) {
                JOptionPane.showMessageDialog(this, "Автомобиль успешно добавлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);

                txtVin.setText("");
                txtModel.setText("");
                txtManufacturer.setText("");
                txtYear.setText("");
                txtMileage.setText("");
                txtPrice.setText("");

                updateDashboard();
                updateInventoryTable(new ArrayList<>(showroom.getVehiclesSortedByYear()));
            } else {
                JOptionPane.showMessageDialog(this, "Автомобиль с таким VIN уже существует!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Проверьте формат чисел!", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDashboard() {
        List<Automobile> vehicles = new ArrayList<>(showroom.getVehiclesSortedByYear());

        if (pnlTotalVehicles != null) {
            Component[] components = pnlTotalVehicles.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getFont().getSize() == 32) {
                        label.setText(String.valueOf(vehicles.size()));
                        label.setForeground(ACCENT_BLUE);
                    }
                }
            }
        }

        double totalValue = vehicles.stream().mapToDouble(Automobile::getPrice).sum();
        if (pnlTotalValue != null) {
            Component[] components = pnlTotalValue.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getFont().getSize() == 32) {
                        label.setText(String.format("%,d", (int)totalValue) + " RUB");
                        label.setForeground(ACCENT_GREEN);
                    }
                }
            }
        }

        double avgPrice = vehicles.size() > 0 ? vehicles.stream().mapToDouble(Automobile::getPrice).average().orElse(0) : 0;
        if (pnlAvgPrice != null) {
            Component[] components = pnlAvgPrice.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getFont().getSize() == 32) {
                        label.setText(String.format("%,d", (int)avgPrice) + " RUB");
                        label.setForeground(ACCENT_ORANGE);
                    }
                }
            }
        }

        tableModel.setRowCount(0);
        for (Automobile vehicle : vehicles) {
            tableModel.addRow(new Object[]{
                    vehicle.getVin(),
                    vehicle.getManufacturer(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    String.format("%,d", vehicle.getMileage()),
                    String.format("%,d", (int)vehicle.getPrice()),
                    vehicle.getCategory()
            });
        }
    }
}