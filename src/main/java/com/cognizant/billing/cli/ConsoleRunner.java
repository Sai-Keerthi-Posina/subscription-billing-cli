package com.cognizant.billing.cli;

import com.cognizant.billing.model.BillingCycle;
import com.cognizant.billing.model.Plan;
import com.cognizant.billing.model.User;
import com.cognizant.billing.service.PlanService;
import com.cognizant.billing.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
        System.out.println("=== Subscription Billing CLI ===");
        System.out.println("Type 'help' for commands. 'exit' to quit.");
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String line = sc.nextLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) break;
                if (line.equalsIgnoreCase("help")) { printHelp(); continue; }

                var t = CommandParser.tokenize(line);
                if (t.isEmpty()) continue;

                String cmd = t.get(0).toLowerCase(Locale.ROOT);
                try {
                    switch (cmd) {
                        case "plan" -> handlePlan(t);
                        case "user" -> handleUser(t);
                        default -> System.out.println("Unknown command. Type 'help'.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
        System.out.println("Bye!");
    }

    private void handlePlan(List<String> t) {
        if (t.size() < 2) { System.out.println("Usage: plan [add|list]"); return; }
        String op = t.get(1).toLowerCase(Locale.ROOT);
        switch (op) {
            case "add" -> {
                String name = CommandParser.getFlag(t, "--name", null);
                String priceStr = CommandParser.getFlag(t, "--price", null);
                String cycleStr = CommandParser.getFlag(t, "--cycle", "MONTHLY");
                if (name == null || priceStr == null) {
                    System.out.println("Usage: plan add --name \"Basic\" --price 499 [--cycle MONTHLY|YEARLY]");
                    return;
                }
                BigDecimal price = new BigDecimal(priceStr);
                BillingCycle cycle = BillingCycle.valueOf(cycleStr.toUpperCase(Locale.ROOT));
                Plan p = planService.addPlan(name, price, cycle);
                System.out.println("Created " + p);
            }
            case "list" -> {
                List<Plan> plans = planService.list();
                if (plans.isEmpty()) System.out.println("(no plans)");
                else plans.forEach(System.out::println);
            }
            default -> System.out.println("Usage: plan [add|list]");
        }
    }

    private void handleUser(List<String> t) {
        if (t.size() < 2) { System.out.println("Usage: user [add|list|update|delete]"); return; }
        String op = t.get(1).toLowerCase(Locale.ROOT);

        switch (op) {
            case "add" -> {
                String name = CommandParser.getFlag(t, "--name", null);
                String email = CommandParser.getFlag(t, "--email", null);
                if (name == null || email == null) {
                    System.out.println("Usage: user add --name \"Alice\" --email alice@example.com");
                    return;
                }
                User u = userService.add(name, email);
                System.out.println("Created " + u);
            }
            case "list" -> {
                List<User> users = userService.list();
                if (users.isEmpty()) System.out.println("(no users)");
                else users.forEach(System.out::println);
            }
            case "update" -> {
                String idStr = CommandParser.getFlag(t, "--id", null);
                if (idStr == null) {
                    System.out.println("Usage: user update --id 1 [--name N] [--email E]");
                    return;
                }
                Long id = Long.parseLong(idStr);
                String name = CommandParser.getFlag(t, "--name", null);   // optional
                String email = CommandParser.getFlag(t, "--email", null); // optional
                User updated = userService.update(id, name, email);
                System.out.println("Updated " + updated);
            }
            case "delete" -> {
                String idStr = CommandParser.getFlag(t, "--id", null);
                if (idStr == null) {
                    System.out.println("Usage: user delete --id 1");
                    return;
                }
                Long id = Long.parseLong(idStr);
                boolean ok = userService.delete(id);
                System.out.println(ok ? "Deleted user " + id : "User not found");
            }
            default -> System.out.println("Usage: user [add|list|update|delete]");
        }
    }

    private void printHelp() {
        System.out.println("""
          Commands:
            help
            exit

            plan add --name "Basic" --price 499 --cycle MONTHLY|YEARLY
            plan list

            user add --name "Alice" --email alice@example.com
            user list
            user update --id 1 --name "Alicia" --email alicia@example.com
            user delete --id 1
        """);
    }
}