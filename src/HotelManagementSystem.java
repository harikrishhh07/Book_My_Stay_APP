import java.util.*;

class RoomInventory {

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 1);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public void increaseRoom(String type) {
        availability.put(type, availability.get(type) + 1);
    }

    public int getAvailability(String type) {
        return availability.get(type);
    }
}

class CancellationService {

    private Stack<String> rollbackStack;
    private Map<String, String> reservationRoomTypeMap;

    public CancellationService() {
        rollbackStack = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    public void cancelBooking(String reservationId, RoomInventory inventory) {

        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Cancellation failed: reservation does not exist.");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        rollbackStack.push(reservationId);

        inventory.increaseRoom(roomType);

        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }

    public void showRollbackHistory() {

        System.out.println("\nRollback History (Most Recent First):");

        for (String id : rollbackStack) {
            System.out.println("Released Reservation ID: " + id);
        }
    }
}

public class HotelManagementSystem {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        service.registerBooking("Single-1", "Single");

        service.cancelBooking("Single-1", inventory);

        service.showRollbackHistory();

        System.out.println("\nUpdated Single Room Availability: " +
                inventory.getAvailability("Single"));
    }
}