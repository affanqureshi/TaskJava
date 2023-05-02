package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TransactionDataFetcher transactionDataFetcher=new TransactionDataFetcher();
        System.out.println(transactionDataFetcher.getTotalTransactionAmount());
        System.out.println(transactionDataFetcher.getTransactionsByBeneficiaryName());
    }
}