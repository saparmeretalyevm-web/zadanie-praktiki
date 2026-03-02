import java.util.*;
import java.util.stream.Collectors;

public class AutoShowroom {
    private String showroomName;
    private Set<Automobile> vehicles;

    public AutoShowroom(String showroomName) {
        this.showroomName = showroomName;
        this.vehicles = new HashSet<>();
    }

    public boolean addVehicle(Automobile vehicle) {
        return vehicles.add(vehicle);
    }

    public List<Automobile> getVehiclesSortedByYear() {
        return vehicles.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Set<Automobile> getVehicles() {
        return vehicles;
    }

    public int getVehiclesCount() {
        return vehicles.size();
    }

    public List<Automobile> findByManufacturer(String manufacturer) {
        return vehicles.stream()
                .filter(v -> v.getManufacturer().equalsIgnoreCase(manufacturer))
                .collect(Collectors.toList());
    }

    public double getAveragePriceByCategory(VehicleCategory category) {
        return vehicles.stream()
                .filter(v -> v.getCategory() == category)
                .mapToDouble(Automobile::getPrice)
                .average()
                .orElse(0);
    }
}