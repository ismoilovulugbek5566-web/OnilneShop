package org.example;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<Product> products = new ArrayList<>();
    private static final List<User> users = new ArrayList<>();
    private static final List<Order> orders = new ArrayList<>();

    private static long productIdCounter = 1;
    private static long userIdCounter = 1;
    private static long orderIdCounter = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== ONLINE SHOP SYSTEM ==========");
            System.out.println("1. Product qo'shish");
            System.out.println("2. Productlarni ko'rish");
            System.out.println("3. Product qidirish");
            System.out.println("4. Productni yangilash");
            System.out.println("5. Productni o'chirish");
            System.out.println("6. User qo'shish");
            System.out.println("7. Order yaratish");
            System.out.println("8. Orderlarni ko'rish");
            System.out.println("9. Order statusini o'zgartirish");
            System.out.println("10. Umumiy savdo summasini ko'rish");
            System.out.println("0. Chiqish");
            System.out.print("Tanlang: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addProduct(scanner);
                case 2 -> showProducts();
                case 3 -> searchProduct(scanner);
                case 4 -> updateProduct(scanner);
                case 5 -> deleteProduct(scanner);
                case 6 -> addUser(scanner);
                case 7 -> createOrder(scanner);
                case 8 -> showOrders();
                case 9 -> changeOrderStatus(scanner);
                case 10 -> showTotalSales();
                case 0 -> {
                    System.out.println("Tizimdan chiqildi.");
                    return;
                }
                default -> System.out.println("Noto'g'ri tanlov!");
            }
        }
    }

    // 1. Product qo'shish
    private static void addProduct(Scanner scanner) {
        System.out.print("Nomi: ");
        String name = scanner.nextLine();
        System.out.print("Narxi: ");
        double price = scanner.nextDouble();
        System.out.print("Soni: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Kategoriya: ");
        String category = scanner.nextLine();

        Product product = new Product(productIdCounter++, name, price, quantity, category);
        products.add(product);
        System.out.println("Product muvaffaqiyatli qo'shildi!");
    }

    // 2. Productlarni ko'rish
    private static void showProducts() {
        if (products.isEmpty()) {
            System.out.println("Mahsulotlar mavjud emas.");
            return;
        }
        products.forEach(System.out::println);
    }

    // 3. Product qidirish
    private static void searchProduct(Scanner scanner) {
        System.out.print("Qidirilayotgan mahsulot nomi: ");
        String query = scanner.nextLine().toLowerCase();

        boolean found = false;
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(query)) {
                System.out.println(p);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Mahsulot topilmadi.");
        }
    }

    // 4. Productni yangilash
    private static void updateProduct(Scanner scanner) {
        System.out.print("Yangilanadigan mahsulot ID si: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        Product product = findProductById(id);
        if (product == null) {
            System.out.println("Mahsulot topilmadi!");
            return;
        }

        System.out.print("Yangi nomi (" + product.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) product.setName(name);

        System.out.print("Yangi narxi (" + product.getPrice() + "): ");
        double price = scanner.nextDouble();
        product.setPrice(price);

        System.out.print("Yangi soni (" + product.getQuantity() + "): ");
        int quantity = scanner.nextInt();
        product.setQuantity(quantity);

        System.out.println("Mahsulot yangilandi!");
    }

    // 5. Productni o'chirish
    private static void deleteProduct(Scanner scanner) {
        System.out.print("O'chiriladigan mahsulot ID si: ");
        long id = scanner.nextLong();

        Product product = findProductById(id);
        if (product != null) {
            products.remove(product);
            System.out.println("Mahsulot o'chirildi!");
        } else {
            System.out.println("Mahsulot topilmadi.");
        }
    }

    // 6. User qo'shish
    private static void addUser(Scanner scanner) {
        System.out.print("Ism va familiya: ");
        String name = scanner.nextLine();
        System.out.print("Telefon raqam: ");
        String phone = scanner.nextLine();

        User user = new User(userIdCounter++, name, phone);
        users.add(user);
        System.out.println("Foydalanuvchi qo'shildi: ID " + user.getId());
    }

    // 7. Order yaratish (Qoidalar hisobga olingan)
    private static void createOrder(Scanner scanner) {
        System.out.print("User ID sini kiriting: ");
        long userId = scanner.nextLong();

        User user = findUserById(userId);
        if (user == null) {
            System.out.println("Bunday foydalanuvchi mavjud emas!");
            return;
        }

        List<Product> orderProducts = new ArrayList<>();
        double totalPrice = 0;

        while (true) {
            showProducts();
            System.out.print("Sotib olinadigan mahsulot ID sini kiriting (0 - tugatish): ");
            long prodId = scanner.nextLong();
            if (prodId == 0) break;

            Product product = findProductById(prodId);
            if (product == null) {
                System.out.println("Mahsulot topilmadi!");
                continue;
            }

            // Qoida: Omborda mavjud bo'lmagan productni sotib bo'lmaydi
            if (product.getQuantity() <= 0) {
                System.out.println("Xato: Omborda bu mahsulotdan qolmagan!");
                continue;
            }

            System.out.print("Soni: ");
            int qty = scanner.nextInt();

            if (qty > product.getQuantity()) {
                System.out.println("Xato: Omborda yetarli mahsulot yo'q! Mavjud: " + product.getQuantity());
                continue;
            }

            // Qoida: Product quantity kamayishi kerak
            product.setQuantity(product.getQuantity() - qty);

            // Buyurtma ro'yxatiga qo'shish
            Product purchasedItem = new Product(product.getId(), product.getName(), product.getPrice(), qty, product.getCategory());
            orderProducts.add(purchasedItem);

            // Qoida: Order yaratilganda umumiy summa hisoblanadi
            totalPrice += product.getPrice() * qty;
        }

        if (orderProducts.isEmpty()) {
            System.out.println("Buyurtma rasmiylashtirilmadi.");
            return;
        }

        Order order = new Order(orderIdCounter++, userId, orderProducts, totalPrice, LocalDateTime.now(), OrderStatus.NEW);
        orders.add(order);
        System.out.println("Order yaratildi! Jami summa: " + totalPrice);
    }

    // 8. Orderlarni ko'rish
    private static void showOrders() {
        if (orders.isEmpty()) {
            System.out.println("Buyurtmalar mavjud emas.");
            return;
        }
        orders.forEach(System.out::println);
    }

    // 9. Order statusini o'zgartirish (Qoida: CANCELLED holatida quantity qaytariladi)
    private static void changeOrderStatus(Scanner scanner) {
        System.out.print("Order ID: ");
        long orderId = scanner.nextLong();

        Order order = findOrderById(orderId);
        if (order == null) {
            System.out.println("Order topilmadi!");
            return;
        }

        System.out.println("Mavjud statuslar:");
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println("- " + status);
        }
        System.out.print("Yangi statusni kiriting: ");
        String statusStr = scanner.next().toUpperCase();

        try {
            OrderStatus newStatus = OrderStatus.valueOf(statusStr);

            // Qoida: CANCELLED order uchun product quantity qaytariladi
            if (newStatus == OrderStatus.CANCELLED && order.getStatus() != OrderStatus.CANCELLED) {
                for (Product item : order.getProducts()) {
                    Product mainProduct = findProductById(item.getId());
                    if (mainProduct != null) {
                        mainProduct.setQuantity(mainProduct.getQuantity() + item.getQuantity());
                    }
                }
                System.out.println("Order bekor qilindi. Mahsulotlar miqdori omborga qaytarildi.");
            }

            order.setStatus(newStatus);
            System.out.println("Order statusi o'zgartirildi: " + newStatus);

        } catch (IllegalArgumentException e) {
            System.out.println("Noto'g'ri status kiritildi!");
        }
    }

    // 10. Umumiy savdo summasini ko'rish (CANCELLED bo'lmagan buyurtmalar summasi)
    private static void showTotalSales() {
        double totalSales = 0;
        for (Order order : orders) {
            if (order.getStatus() != OrderStatus.CANCELLED) {
                totalSales += order.getTotalPrice();
            }
        }
        System.out.println("Umumiy savdo summasi (bekor qilinganlarsiz): " + totalSales);
    }

    // Helper metodlar
    private static Product findProductById(long id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    private static User findUserById(long id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    private static Order findOrderById(long id) {
        return orders.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
    }
}