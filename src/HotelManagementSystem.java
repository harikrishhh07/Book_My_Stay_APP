import java.util.HashMap;
import java.util.Map;

class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }
}

class RoomSearchService {

    public void searchAvailableRooms(RoomInventory inventory) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        if (availability.get("Single") > 0) {
            System.out.println("Single Room Available: " + availability.get("Single"));
        }

        if (availability.get("Double") > 0) {
            System.out.println("Double Room Available: " + availability.get("Double"));
        }

        if (availability.get("Suite") > 0) {
            System.out.println("Suite Room Available: " + availability.get("Suite"));
        }
    }
}

public class HotelManagementSystem {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        RoomSearchService searchService = new RoomSearchService();

        System.out.println("Available Rooms:");

        searchService.searchAvailableRooms(inventory);
    }
}