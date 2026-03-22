package com.cognizant.billing.cli;

import com.cognizant.billing.model.BillingCycle;
import com.cognizant.billing.model.Plan;
import com.cognizant.billing.model.User;
import com.cognizant.billing.service.PlanService;
import com.cognizant.billing.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Component
public class ConsoleRunner {

    private final PlanService planService;
    private final UserService userService;

    public ConsoleRunner(PlanService planService, UserService userService) {
        this.planService = planService;
        this.userService = userService;
    }

    public void start() {
        System.out.println("=== Subscription Billing CLI (Menu-Driven) ===");
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                switch (mainMenu(sc)) {
                    case 1 -> usersMenu(sc);
                    case 2 -> plansMenu(sc);
                    case 0 -> {
                        System.out.println("Bye!");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        }
    }

    private int mainMenu(Scanner sc) {
        System.out.println("\nMain Menu");
        System.out.println("  1) Users");
        System.out.println("  2) Plans");
        System.out.println("  0) Exit");
        System.out.print("Choose: ");
        return readInt(sc);
    }

    // ===================== USERS =====================
    private void usersMenu(Scanner sc) {
        while (true) {
            System.out.println("\nUsers Menu");
            System.out.println("  1) Add User");
            System.out.println("  2) List Users");
            System.out.println("  3) Update User");
            System.out.println("  4) Delete User");
            System.out.println("  0) Back");
            System.out.print("Choose: ");
            int choice = readInt(sc);
            try {
                switch (choice) {
                    case 1 -> addUser(sc);
                    case 2 -> listUsers();
                    case 3 -> updateUser(sc);
                    case 4 -> deleteUser(sc);
                    case 0 -> { return; }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addUser(Scanner sc) {
        System.out.println("\nAdd User");
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

        User u = userService.add(name, email);
        System.out.println("Created: " + u);
    }

    private void listUsers() {
        System.out.println("\nUsers List");
        List<User> users = userService.list();
        if (users.isEmpty()) {
            System.out.println("(no users)");
        } else {
            users.forEach(System.out::println);
        }
    }

    private void updateUser(Scanner sc) {
        System.out.println("\nUpdate User");
        System.out.print("Enter user id: ");
        Long id = readLong(sc);
        System.out.print("Enter new name (or leave blank to keep current): ");
        String name = sc.nextLine();
        name = name != null && !name.isBlank() ? name.trim() : null;
        System.out.print("Enter new email (or leave blank to keep current): ");
        String email = sc.nextLine();
        email = email != null && !email.isBlank() ? email.trim() : null;

        User updated = userService.update(id, name, email);
        System.out.println("Updated: " + updated);
    }

    private void deleteUser(Scanner sc) {
        System.out.println("\nDelete User");
        System.out.print("Enter user id: ");
        Long id = readLong(sc);
        boolean ok = userService.delete(id);
        System.out.println(ok ? "Deleted user " + id : "User not found");
    }

    // ===================== PLANS =====================
    private void plansMenu(Scanner sc) {
        while (true) {
            System.out.println("\nPlans Menu");
            System.out.println("  1) Add Plan");
            System.out.println("  2) List Plans");
            System.out.println("  0) Back");
            System.out.print("Choose: ");
            int choice = readInt(sc);
            try {
                switch (choice) {
                    case 1 -> addPlan(sc);
                    case 2 -> listPlans();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addPlan(Scanner sc) {
        System.out.println("\nAdd Plan");
        System.out.print("Enter plan name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter price (e.g. 499 or 499.00): ");
        BigDecimal price = readBigDecimal(sc);
        System.out.print("Enter cycle (MONTHLY or YEARLY): ");
        String cycleStr = sc.nextLine().trim().toUpperCase(Locale.ROOT);
        BillingCycle cycle = BillingCycle.valueOf(cycleStr);

        Plan p = planService.addPlan(name, price, cycle);
        System.out.println("Created: " + p);
    }

    private void listPlans() {
        System.out.println("\nPlans List");
        List<Plan> plans = planService.list();
        if (plans.isEmpty()) {
            System.out.println("(no plans)");
        } else {
            plans.forEach(System.out::println);
        }
    }

    // ===================== INPUT HELPERS =====================
    private int readInt(Scanner sc) {
        while (true) {
            try {
                String s = sc.nextLine();
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a number: ");
            }
        }
    }

    private Long readLong(Scanner sc) {
        while (true) {
            try {
                String s = sc.nextLine();
                return Long.parseLong(s.trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer id: ");
            }
        }
    }

    private BigDecimal readBigDecimal(Scanner sc) {
        while (true) {
            try {
                String s = sc.nextLine();
                return new BigDecimal(s.trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid amount (e.g., 499 or 499.00): ");
            }
        }
    }
}