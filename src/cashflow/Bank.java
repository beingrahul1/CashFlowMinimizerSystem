package cashflow;

import java.util.*;

class Bank {
    String name;
    int netAmount;
    Set<String> paymentModes;

    public Bank(String name) {
        this.name = name;
        this.netAmount = 0;
        this.paymentModes = new HashSet<>();
    }
}

