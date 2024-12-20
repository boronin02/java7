import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class User {
    private int id;
    private String name;
    private String username;
    private String password;
    private String accountCreationDate;
    private double balance;

    public User(int id, String name, String username, String password, String accountCreationDate, double balance) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.accountCreationDate = accountCreationDate;
        this.balance = balance;
    }

    public String getUsername() {
        return this.username;
    }

    public double getBalance() {
        return this.balance;
    }

    public void updateBalance(double amount) throws IllegalArgumentException {
        if (amount + this.balance < 0) {
            throw new IllegalArgumentException("Недостаточно средств на счете.");
        }
        this.balance += amount;
    }

    public void displayInfo() {
        System.out.printf("ID пользователя: %d\n", this.id);
        System.out.printf("Имя пользователя: %s\n", this.name);
        System.out.printf("Логин: %s\n", this.username);
        System.out.printf("Дата создания аккаунта: %s\n", this.accountCreationDate);
        System.out.printf("Баланс: %.2f рублей\n\n", this.balance);
    }
}

class VIPUser extends User {
    private double cashbackRate;

    public VIPUser(int id, String name, String username, String password, String accountCreationDate, double balance,
            double cashbackRate) {
        super(id, name, username, password, accountCreationDate, balance);
        this.cashbackRate = cashbackRate;
    }

    public double getCashbackRate() {
        return this.cashbackRate;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf("Кэшбэк: %.2f%%\n\n", this.cashbackRate * 100);
    }
}

class Game {
    private String gameName;
    private double minBet;
    private double maxBet;
    private double payoutMultiplier;

    public Game(String gameName, double minBet, double maxBet, double payoutMultiplier) {
        this.gameName = gameName;
        this.minBet = minBet;
        this.maxBet = maxBet;
        this.payoutMultiplier = payoutMultiplier;
    }

    public String getGameName() {
        return this.gameName;
    }

    public double getPayoutMultiplier() {
        return this.payoutMultiplier;
    }

    public boolean isBetValid(double bet) {
        return bet >= this.minBet && bet <= this.maxBet;
    }
}

class Jackpot {
    private double currentAmount;
    private static double totalWon = 0;

    public Jackpot(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public void winJackpot() {
        System.out.printf("Поздравляем! Джекпот в размере %.2f рублей сорван!\n", this.currentAmount);
        totalWon += this.currentAmount;
        this.currentAmount = 0;
    }

    public static void displayTotalWon() {
        System.out.printf("Общая сумма сорванных джекпотов: %.2f рублей\n", totalWon);
    }
}

class Bonus {
    private String bonusType;
    private double bonusAmount;
    private boolean isActive;

    public Bonus(String bonusType, double bonusAmount, boolean isActive) {
        this.bonusType = bonusType;
        this.bonusAmount = bonusAmount;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void activateBonus(User user) {
        if (this.isActive) {
            System.out.printf("Бонус '%s' уже активирован.\n", this.bonusType);
            return;
        }
        this.isActive = true;
        user.updateBalance(this.bonusAmount);
        System.out.printf("Бонус '%s' на сумму %.2f рублей активирован для пользователя %s.\n",
                this.bonusType, this.bonusAmount, user.getUsername());
    }
}

class GameRound {
    private User player;
    private Game game;
    private double betAmount;
    private int result;

    public GameRound(User player, Game game, double betAmount, int result) {
        this.player = player;
        this.game = game;
        this.betAmount = betAmount;
        this.result = result;
    }

    public void play() {
        if (!game.isBetValid(betAmount)) {
            System.out.printf("Ставка %.2f рублей недействительна для игры '%s'.\n", betAmount, game.getGameName());
            return;
        }

        try {
            player.updateBalance(-betAmount);
            if (result == 1) {
                double winnings = betAmount * game.getPayoutMultiplier();
                player.updateBalance(winnings);
                System.out.printf("Вы выиграли! Ваш выигрыш: %.2f рублей.\n", winnings);
            } else {
                System.out.println("Вы проиграли. Попробуйте снова!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

class Transaction {
    private int id;
    private int userId;
    private double amount;
    private String timestamp;

    public Transaction(int id, int userId, double amount, String timestamp) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public void displayInfo() {
        System.out.printf("ID транзакции: %d\n", this.id);
        System.out.printf("ID пользователя: %d\n", this.userId);
        System.out.printf("Сумма транзакции: %.2f рублей\n", this.amount);
        System.out.printf("Время транзакции: %s\n\n", this.timestamp);
    }
}

public class CasinoApp {
    public static void main(String[] args) {
        // Инициализация контейнера
        List<Object> objects = new ArrayList<>();

        // Добавление объектов всех классов
        objects.add(new User(1, "Иван Иванов", "ivanov", "password123", "2024-10-01", 100.0));
        objects.add(new VIPUser(2, "Анна Смирнова", "asmirnova", "qwerty", "2023-12-15", 500.0, 0.05));
        objects.add(new Game("Однорукий бандит", 10.0, 100.0, 2.0));
        objects.add(new Jackpot(5000.0));
        objects.add(new Bonus("Фриспины", 50.0, false));
        objects.add(new Transaction(101, 1, -20.0, "2024-11-30 15:00"));

        // Демонстрация работы объектов
        for (Object obj : objects) {
            if (obj instanceof User) {
                ((User) obj).displayInfo();
            } else if (obj instanceof Game) {
                System.out.printf("Игра: %s\n\n", ((Game) obj).getGameName());
            } else if (obj instanceof Jackpot) {
                ((Jackpot) obj).winJackpot();
                Jackpot.displayTotalWon();
            } else if (obj instanceof Bonus) {
                ((Bonus) obj).activateBonus((User) objects.get(0));
            } else if (obj instanceof Transaction) {
                ((Transaction) obj).displayInfo();
            }
        }

        // Сортировка пользователей по балансу
        List<User> users = new ArrayList<>();
        for (Object obj : objects) {
            if (obj instanceof User) {
                users.add((User) obj);
            }
        }
        Collections.sort(users, Comparator.comparingDouble(User::getBalance).reversed());

        System.out.println("\nПользователи после сортировки по балансу:");
        for (User user : users) {
            user.displayInfo();
        }
    }
}
