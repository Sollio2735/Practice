package semester1.practice10.homework;

import java.util.Locale;

interface CashbackCapable {
    double MAX_CASHBACK_UAH = 500.0;

    double getCashbackRate();

    default double calculateCashback(double baseAmount) {
        return Math.min(MAX_CASHBACK_UAH, baseAmount * getCashbackRate());
    }
}

interface Refundable {
    boolean requestRefund(String reason);

    boolean isRefunded();
}

interface BlockchainVerifiable {
    String getTxHash();

    int getConfirmations();

    default boolean isFinalized() {
        return getConfirmations() >= 12;
    }
}

abstract class AbstractPaymentTransaction {
    private final String txId;
    private final double baseAmountUah;
    private boolean processed;

    public AbstractPaymentTransaction(String txId, double baseAmountUah) {
        this.txId = txId;
        this.baseAmountUah = baseAmountUah;
        this.processed = false;
    }

    public String getTxId() {
        return txId;
    }

    public double getBaseAmountUah() {
        return baseAmountUah;
    }

    public boolean isProcessed() {
        return processed;
    }

    public abstract double calculateFee();

    public abstract String getPaymentChannel();

    public final boolean process() {
        if (baseAmountUah <= 0 || processed) {
            return false;
        }
        double fee = calculateFee();
        if (fee < 0) {
            return false;
        }
        this.processed = true;
        return true;
    }

    public final double getTotalCharged() {
        return baseAmountUah + calculateFee();
    }
}

class PremiumCardPayment extends AbstractPaymentTransaction implements CashbackCapable, Refundable {
    private final String maskedPan;
    private final double cashbackRate;
    private boolean refunded;

    public PremiumCardPayment(String txId, double baseAmountUah, String maskedPan, double cashbackRate) {
        super(txId, baseAmountUah);
        this.maskedPan = maskedPan;
        this.cashbackRate = cashbackRate;
        this.refunded = false;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    @Override
    public double calculateFee() {
        return getBaseAmountUah() * 0.01; // 1.0% комісії
    }

    @Override
    public String getPaymentChannel() {
        return "Visa Infinite";
    }

    @Override
    public double getCashbackRate() {
        return cashbackRate;
    }

    @Override
    public boolean requestRefund(String reason) {
        if (isProcessed() && !refunded) {
            this.refunded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRefunded() {
        return refunded;
    }
}

class CryptoUsdtPayment extends AbstractPaymentTransaction implements CashbackCapable, BlockchainVerifiable {
    private final String txHash;
    private final int confirmations;

    public CryptoUsdtPayment(String txId, double baseAmountUah, String txHash, int confirmations) {
        super(txId, baseAmountUah);
        this.txHash = txHash;
        this.confirmations = confirmations;
    }

    @Override
    public double calculateFee() {
        return 40.0;
    }

    @Override
    public String getPaymentChannel() {
        return "USDT TRC-20";
    }

    @Override
    public double getCashbackRate() {
        return 0.01;
    }

    @Override
    public String getTxHash() {
        return txHash;
    }

    @Override
    public int getConfirmations() {
        return confirmations;
    }
}

final class TaxTreasuryPayment extends AbstractPaymentTransaction implements Refundable {
    private final String taxCode;
    private boolean refunded;

    public TaxTreasuryPayment(String txId, double baseAmountUah, String taxCode) {
        super(txId, baseAmountUah);
        this.taxCode = taxCode;
        this.refunded = false;
    }

    public String getTaxCode() {
        return taxCode;
    }

    @Override
    public double calculateFee() {
        return 0.0;
    }

    @Override
    public String getPaymentChannel() {
        return "ДержКазначейство";
    }

    @Override
    public boolean requestRefund(String reason) {
        if (isProcessed() && !refunded) {
            this.refunded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRefunded() {
        return refunded;
    }
}

public class Task2SmartPaymentAndAuditGateway {

    public static void runBatchProcessingAndAudit(AbstractPaymentTransaction[] ledger) {
        double totalBaseTurnover = 0.0;
        double totalGatewayFees = 0.0;
        double totalPaidCashback = 0.0;

        System.out.println("================ ПРОЦЕСИНГОВИЙ ЦЕНТР FINTECH (POLYMORPHISM & INTERFACES) ================");
        System.out.println("[1. ПРОВЕДЕННЯ ПАКЕТУ ТРАНЗАКЦІЙ (final process())]:");

        for (AbstractPaymentTransaction tx : ledger) {
            if (tx == null) continue;

            if (tx.process()) {
                double base = tx.getBaseAmountUah();
                double fee = tx.calculateFee();
                double total = tx.getTotalCharged();

                totalBaseTurnover += base;
                totalGatewayFees += fee;

                String header = String.format("[%s | %s]", tx.getTxId(), tx.getPaymentChannel());
                System.out.printf(Locale.US, "  ✅ %-27s Сума: %8.2f грн | Комісія: %6.2f грн | Всього: %8.2f грн%n",
                        header, base, fee, total);
            }
        }

        System.out.println("\n[2. АВТОМАТИЧНИЙ АУДИТ МОЖЛИВОСТЕЙ (Pattern Matching instanceof)]:");

        for (AbstractPaymentTransaction tx : ledger) {
            if (tx instanceof CashbackCapable cb && tx.isProcessed()) {
                double base = tx.getBaseAmountUah();
                double ratePercent = cb.getCashbackRate() * 100.0;
                double rawCashback = base * cb.getCashbackRate();
                double actualCashback = cb.calculateCashback(base);

                totalPaidCashback += actualCashback;

                if (rawCashback > CashbackCapable.MAX_CASHBACK_UAH) {
                    System.out.printf(Locale.US,
                            "  💰 [CashbackCapable] %s: розрахунковий %.0f%% = %.0f грн -> нараховано %.2f грн (Ліміт MAX_CASHBACK_UAH!)%n",
                            tx.getTxId(), ratePercent, rawCashback, actualCashback);
                } else {
                    System.out.printf(Locale.US,
                            "  💰 [CashbackCapable] %s: нараховано %.0f%% крипто-бонусу = %.2f грн%n",
                            tx.getTxId(), ratePercent, actualCashback);
                }
            }
        }

        for (AbstractPaymentTransaction tx : ledger) {
            if (tx instanceof BlockchainVerifiable bv && tx.isProcessed()) {
                System.out.printf(
                        "  ⛓️ [BlockchainVerifiable] %s: Hash=%s, Підтверджень=%d (Finalized: %b)%n",
                        tx.getTxId(), bv.getTxHash(), bv.getConfirmations(), bv.isFinalized());
            }
        }

        for (AbstractPaymentTransaction tx : ledger) {
            if (tx instanceof Refundable ref && "TX-101".equals(tx.getTxId())) {
                String reason = "Подвійне списання";
                boolean refunded = ref.requestRefund(reason);
                System.out.printf("  🔄 [Refundable] Спроба повернення %s ('%s'): %s%n",
                        tx.getTxId(), reason, refunded ? "УСПІШНО ПОВЕРНЕНО!" : "ВІДХИЛЕНО!");
            }
        }

        System.out.println("\n[ПІДСУМКОВИЙ ЗВІТ ШЛЮЗУ]:");
        System.out.printf(Locale.US, "  Сумарний базовий оборот:  %8.2f грн%n", totalBaseTurnover);
        System.out.printf(Locale.US, "  Сумарна комісія шлюзу:     %8.2f грн%n", totalGatewayFees);
        System.out.printf(Locale.US, "  Сумарний виплачений кешбек: %6.2f грн%n", totalPaidCashback);
        System.out.println("=========================================================================================");
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        AbstractPaymentTransaction[] ledger = {
                new PremiumCardPayment("TX-101", 20000.00, "4441 **** **** 8899", 0.03),
                new CryptoUsdtPayment("TX-102", 8500.00, "0x9f8a...c4", 18),
                new TaxTreasuryPayment("TX-103", 5000.00, "101-ЄСВ")
        };

        runBatchProcessingAndAudit(ledger);
    }
}
