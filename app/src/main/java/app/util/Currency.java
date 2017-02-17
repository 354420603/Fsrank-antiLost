package app.util;

/**
 * Created by jinbing on 2015/4/16 0016.
 */
public enum Currency {

    RMB("RMB", "￥"),
    USD("USD", "$");

    public final String code;
    public final String symbol;

    Currency(String code, String symbol) {
        this.code = code;
        this.symbol = symbol;
    }

    public static Currency rawFromCode(String code) {
        for (Currency currency : Currency.values()) {
            if (currency.code.equals(code)) {
                return currency;
            }
        }
        return null;
    }

}
