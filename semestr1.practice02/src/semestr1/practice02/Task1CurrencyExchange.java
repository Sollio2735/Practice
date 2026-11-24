package semestr1.practice02;

public class Task1CurrencyExchange {
    public static void main(){
        double usdAmount = 250.0;
        final double EXCHANGE_RATE = 41.25;
        final double COMMISSION_PERCENT = 1.5;
        final double withoutComission = EXCHANGE_RATE * usdAmount;
        double summComission = withoutComission * (COMMISSION_PERCENT / 100.0);
        final double clearSumm = withoutComission - summComission;
        System.out.println("================ КВИТАНЦІЯ ОБМІНУ ================\nСума до обміну:\t\t\t\t" + usdAmount + " USD" + "\nКурс валют:\t\t\t\t\t" + EXCHANGE_RATE + " UAH/USD" + "\nНараховано до комісії:\t\t" + withoutComission + " UAH" + "\nКомісія банку(" + COMMISSION_PERCENT + "%)\t\t\t" + summComission + "\n--------------------------------------------------\nЧиста сума до видачі:\t\t" + clearSumm + " UAH" + "\nДо видачі готівкою:\t\t\t" + Math.round(clearSumm) + " UAH" + "\n==================================================");

    }
}
