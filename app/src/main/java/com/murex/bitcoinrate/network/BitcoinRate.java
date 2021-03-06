package com.murex.bitcoinrate.network;

public class BitcoinRate {
    private Double _15m;
    private Double last;
    private Double buy;
    private Double sell;
    private String symbol;

    public Double get15m() {
        return _15m;
    }

    public void set15m(Double _15m) {
        this._15m = _15m;
    }

    public Double getLast() {
        return last;
    }

    public void setLast(Double last) {
        this.last = last;
    }

    public Double getBuy() {
        return buy;
    }

    public void setBuy(Double buy) {
        this.buy = buy;
    }

    public Double getSell() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
