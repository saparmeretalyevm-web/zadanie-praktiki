import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// 1. Кастомная непроверяемая ошибка
class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(String message) {
        super(message);
    }
}

// 2. Перечисление цен
enum Prices {
    ECONOMY(100),
    STANDARD(200),
    PRO(300),
    LUX(500),
    ULTRA_LUX(1000);

    private final int price;

    Prices(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

// 3. Абстрактный класс Room
abstract class Room {
    protected int roomNumber;
    protected int maxCapacity;
    protected int pricePerNight;
    protected boolean isBooked;

    public Room(int roomNumber, int maxCapacity, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.maxCapacity = maxCapacity;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    public Room(int roomNumber, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.maxCapacity = (int)(Math.random() * 4) + 1;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    public Room(int roomNumber, Prices price) {
        this.roomNumber = roomNumber;
        this.maxCapacity = (int)(Math.random() * 4) + 1;
        this.pricePerNight = price.getPrice();
        this.isBooked = false;
    }

    public int getRoomNumber() { return roomNumber; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getPricePerNight() { return pricePerNight; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }

    @Override
    public String toString() {
        return "Комната " + roomNumber + " | " + maxCapacity + " мест | $" + pricePerNight +
                " | " + (isBooked ? "ЗАНЯТА" : "Свободна");
    }

    public abstract String getType();
}

// 4. Класс EconomyRoom
class EconomyRoom extends Room {
    public EconomyRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public EconomyRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public EconomyRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "Economy";
    }
}

// 5. Абстрактный класс ProRoom
abstract class ProRoom extends Room {
    public ProRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public ProRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public ProRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }
}

// 6. Класс StandardRoom
class StandardRoom extends ProRoom {
    public StandardRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public StandardRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public StandardRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "Standard";
    }
}

// 7. Класс LuxRoom
class LuxRoom extends ProRoom {
    public LuxRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public LuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public LuxRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "Lux";
    }
}

// 8. Класс UltraLuxRoom
class UltraLuxRoom extends LuxRoom {
    public UltraLuxRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public UltraLuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public UltraLuxRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "UltraLux";
    }
}

// 9. Интерфейс RoomService
interface RoomService<T extends Room> {
    void clean(T room);
    void reserve(T room);
    void free(T room);
}

// 10. Интерфейс LuxRoomService
interface LuxRoomService<T extends LuxRoom> extends RoomService<T> {
    void foodDelivery(T room);
}

// 11. Реализация RoomService
class RoomServiceImpl<T extends Room> implements RoomService<T> {

    @Override
    public void clean(T room) {
        room.setBooked(false);
    }

    @Override
    public void reserve(T room) {
        if (room.isBooked()) {
            throw new RoomAlreadyBookedException("Комната №" + room.getRoomNumber() + " уже забронирована!");
        }
        room.setBooked(true);
    }

    @Override
    public void free(T room) {
        room.setBooked(false);
    }
}

// 12. Реализация LuxRoomService
class LuxRoomServiceImpl<T extends LuxRoom> implements LuxRoomService<T> {

    private final RoomService<T> baseService = new RoomServiceImpl<>();

    @Override
    public void clean(T room) {
        baseService.clean(room);
    }

    @Override
    public void reserve(T room) {
        baseService.reserve(room);
    }

    @Override
    public void free(T room) {
        baseService.free(room);
    }

    @Override
    public void foodDelivery(T room) {
        // Доставка еды доступна
    }
}

// 13. Главное окно приложения
public class HotelManagementGUI extends JFrame {
    private List<Room> rooms;
    private RoomServiceImpl<Room> roomService;
    private LuxRoomServiceImpl<LuxRoom> luxRoomService;

    private JComboBox<String> roomTypeCombo;
    private JTextField roomNumberField;
    private JTextField capacityField;
    private JComboBox<Prices> priceCombo;

    private JTextArea roomsTextArea;
    private JLabel statusLabel;

    private JComboBox<String> actionRoomCombo;

    public HotelManagementGUI() {
        rooms = new ArrayList<>();
        roomService = new RoomServiceImpl<>();
        luxRoomService = new LuxRoomServiceImpl<>();

        setTitle("Система управления отелем");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        // Панель создания комнаты
        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Создание новой комнаты",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));
        createPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Тип комнаты
        gbc.gridx = 0;
        gbc.gridy = 0;
        createPanel.add(new JLabel("Тип комнаты:"), gbc);

        gbc.gridx = 1;
        String[] roomTypes = {"Economy", "Standard", "Lux", "UltraLux"};
        roomTypeCombo = new JComboBox<>(roomTypes);
        roomTypeCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        createPanel.add(roomTypeCombo, gbc);

        // Номер комнаты
        gbc.gridx = 0;
        gbc.gridy = 1;
        createPanel.add(new JLabel("Номер комнаты:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        roomNumberField = new JTextField(10);
        roomNumberField.setFont(new Font("Arial", Font.PLAIN, 12));
        createPanel.add(roomNumberField, gbc);

        // Вместимость
        gbc.gridx = 0;
        gbc.gridy = 2;
        createPanel.add(new JLabel("Вместимость:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        capacityField = new JTextField(10);
        capacityField.setFont(new Font("Arial", Font.PLAIN, 12));
        createPanel.add(capacityField, gbc);

        // Цена
        gbc.gridx = 0;
        gbc.gridy = 3;
        createPanel.add(new JLabel("Цена:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        priceCombo = new JComboBox<>(Prices.values());
        priceCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        createPanel.add(priceCombo, gbc);

        // Кнопка создания
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton createButton = new JButton("Создать комнату");
        createButton.setFont(new Font("Arial", Font.BOLD, 12));
        createButton.setBackground(new Color(70, 130, 180));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.addActionListener(e -> createRoom());
        createPanel.add(createButton, gbc);

        // Панель действий
        JPanel actionPanel = new JPanel(new GridBagLayout());
        actionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Действия с комнатами",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(100, 149, 237)
        ));
        actionPanel.setBackground(Color.WHITE);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Выбор комнаты
        gbc.gridx = 0;
        gbc.gridy = 0;
        actionPanel.add(new JLabel("Выберите комнату:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        actionRoomCombo = new JComboBox<>();
        actionRoomCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        actionPanel.add(actionRoomCombo, gbc);

        // Кнопки действий
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonsPanel.setBackground(Color.WHITE);

        JButton reserveButton = createStyledButton("Забронировать", new Color(220, 20, 60));
        reserveButton.addActionListener(e -> reserveRoom());
        buttonsPanel.add(reserveButton);

        JButton freeButton = createStyledButton("Освободить", new Color(34, 139, 34));
        freeButton.addActionListener(e -> freeRoom());
        buttonsPanel.add(freeButton);

        JButton cleanButton = createStyledButton("Уборка", new Color(255, 140, 0));
        cleanButton.addActionListener(e -> cleanRoom());
        buttonsPanel.add(cleanButton);

        JButton foodButton = createStyledButton("Доставка еды", new Color(138, 43, 226));
        foodButton.addActionListener(e -> foodDelivery());
        buttonsPanel.add(foodButton);

        actionPanel.add(buttonsPanel, gbc);

        // Панель списка комнат
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 179, 113), 2),
                "Список комнат",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(60, 179, 113)
        ));
        listPanel.setBackground(Color.WHITE);

        roomsTextArea = new JTextArea();
        roomsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        roomsTextArea.setEditable(false);
        roomsTextArea.setBackground(new Color(248, 248, 255));
        JScrollPane scrollPane = new JScrollPane(roomsTextArea);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Статус бар
        statusLabel = new JLabel("Готов к работе", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(70, 130, 180));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Основная панель
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weighty = 0.5;
        mainPanel.add(createPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(actionPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        mainPanel.add(listPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        updateRoomCombo();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    private void createRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText().trim());
            int capacity = capacityField.getText().trim().isEmpty() ?
                    (int)(Math.random() * 4) + 1 : Integer.parseInt(capacityField.getText().trim());
            Prices price = (Prices) priceCombo.getSelectedItem();
            String type = (String) roomTypeCombo.getSelectedItem();

            Room room = null;
            switch (type) {
                case "Economy":
                    room = new EconomyRoom(roomNumber, capacity, price.getPrice());
                    break;
                case "Standard":
                    room = new StandardRoom(roomNumber, capacity, price.getPrice());
                    break;
                case "Lux":
                    room = new LuxRoom(roomNumber, capacity, price.getPrice());
                    break;
                case "UltraLux":
                    room = new UltraLuxRoom(roomNumber, capacity, price.getPrice());
                    break;
            }

            if (room != null) {
                rooms.add(room);
                updateRoomCombo();
                updateRoomsList();
                statusLabel.setText("Комната " + roomNumber + " (" + type + ") успешно создана");
                statusLabel.setForeground(new Color(34, 139, 34));

                roomNumberField.setText("");
                capacityField.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Пожалуйста, введите корректные числовые значения",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Ошибка при создании комнаты");
            statusLabel.setForeground(Color.RED);
        }
    }

    private void updateRoomCombo() {
        actionRoomCombo.removeAllItems();
        for (Room room : rooms) {
            actionRoomCombo.addItem("Комната " + room.getRoomNumber() +
                    " (" + room.getType() + ") - " + (room.isBooked() ? "ЗАНЯТА" : "Свободна"));
        }
    }

    private void updateRoomsList() {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(70)).append("\n");
        sb.append(String.format("%-10s %-12s %-8s %-10s %-10s\n",
                "Номер", "Тип", "Места", "Цена", "Статус"));
        sb.append("=".repeat(70)).append("\n");

        for (Room room : rooms) {
            String status = room.isBooked() ? "ЗАНЯТА" : "Свободна";
            sb.append(String.format("%-10d %-12s %-8d $%-9d %-10s\n",
                    room.getRoomNumber(),
                    room.getType(),
                    room.getMaxCapacity(),
                    room.getPricePerNight(),
                    status));
        }

        sb.append("=".repeat(70));
        roomsTextArea.setText(sb.toString());
    }

    private void reserveRoom() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this,
                    "Выберите комнату",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Room room = rooms.get(actionRoomCombo.getSelectedIndex());
            roomService.reserve(room);
            updateRoomCombo();
            updateRoomsList();
            statusLabel.setText("Комната " + room.getRoomNumber() + " забронирована");
            statusLabel.setForeground(new Color(220, 20, 60));
            JOptionPane.showMessageDialog(this,
                    "Комната " + room.getRoomNumber() + " успешно забронирована!",
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (RoomAlreadyBookedException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Ошибка: " + e.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }

    private void freeRoom() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this,
                    "Выберите комнату",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room room = rooms.get(actionRoomCombo.getSelectedIndex());
        roomService.free(room);
        updateRoomCombo();
        updateRoomsList();
        statusLabel.setText("Комната " + room.getRoomNumber() + " освобождена");
        statusLabel.setForeground(new Color(34, 139, 34));
        JOptionPane.showMessageDialog(this,
                "Комната " + room.getRoomNumber() + " освобождена",
                "Успех",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void cleanRoom() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this,
                    "Выберите комнату",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room room = rooms.get(actionRoomCombo.getSelectedIndex());
        roomService.clean(room);
        updateRoomCombo();
        updateRoomsList();
        statusLabel.setText("В комнате " + room.getRoomNumber() + " проведена уборка");
        statusLabel.setForeground(new Color(255, 140, 0));
        JOptionPane.showMessageDialog(this,
                "В комнате " + room.getRoomNumber() + " проведена уборка",
                "Успех",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void foodDelivery() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this,
                    "Выберите комнату",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room room = rooms.get(actionRoomCombo.getSelectedIndex());

        if (!(room instanceof LuxRoom)) {
            JOptionPane.showMessageDialog(this,
                    "Доставка еды доступна только для номеров категорий Lux и UltraLux!",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Ошибка: доставка еды недоступна для " + room.getType());
            statusLabel.setForeground(Color.RED);
            return;
        }

        luxRoomService.foodDelivery((LuxRoom) room);
        statusLabel.setText("Доставка еды в комнату " + room.getRoomNumber());
        statusLabel.setForeground(new Color(138, 43, 226));
        JOptionPane.showMessageDialog(this,
                "Заказана доставка еды в комнату " + room.getRoomNumber() + "\n" +
                        "Тип номера: " + room.getType(),
                "Доставка еды",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelManagementGUI gui = new HotelManagementGUI();
            gui.setVisible(true);
        });
    }
}