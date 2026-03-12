import java.util.*;

class Reservation {

    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.add(r);
    }

    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }

    public synchronized boolean hasRequests() {
        return !queue.isEmpty();
    }
}

class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public synchronized boolean isAvailable(String type) {
        return rooms.getOrDefault(type, 0) > 0;
    }

    public synchronized void allocateRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }

    public void printInventory() {
        System.out.println("\nRemaining Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + ": " + rooms.get(type));
        }
    }
}

class RoomAllocationService {

    public void allocateRoom(Reservation r, RoomInventory inventory) {

        if (inventory.isAvailable(r.roomType)) {
            inventory.allocateRoom(r.roomType);

            String roomId = r.roomType + "-" + (new Random().nextInt(100));

            System.out.println(
                    "Booking confirmed for Guest: "
                            + r.guestName
                            + ", Room ID: "
                            + roomId);
        } else {
            System.out.println("No rooms available for " + r.roomType);
        }
    }
}

class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue queue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue queue,
            RoomInventory inventory,
            RoomAllocationService allocationService) {

        this.queue = queue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {

        while (queue.hasRequests()) {

            Reservation r;

            synchronized (queue) {
                r = queue.getNextRequest();
            }

            if (r != null) {

                synchronized (inventory) {
                    allocationService.allocateRoom(r, inventory);
                }
            }
        }
    }
}

public class HotelManagementSystem {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation\n");

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Double"));
        queue.addRequest(new Reservation("Kural", "Suite"));
        queue.addRequest(new Reservation("Subha", "Single"));

        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(queue, inventory, allocationService));

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(queue, inventory, allocationService));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        inventory.printInventory();
    }
}