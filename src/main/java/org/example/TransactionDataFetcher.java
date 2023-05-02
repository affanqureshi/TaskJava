package org.example;





import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TransactionDataFetcher {

    private ObjectMapper objectMapper;
    private Transaction[] transactions;

    public TransactionDataFetcher() throws IOException {
        objectMapper= new ObjectMapper();
        transactions=objectMapper.readValue(new File("transactions.json"), Transaction[].class);
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        try{
            double totalAmount = 0.0;
            for (Transaction transaction : transactions) {
                totalAmount += transaction.getAmount();
            }
            return totalAmount;
        }catch (Exception e){
            throw new UnsupportedOperationException();

        }

    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        try{
            int sum=0;
            for (Transaction transaction : transactions) {
                if (transaction.getSenderFullName().equals(senderFullName)) {
                    sum += transaction.getAmount();
                }
            }
            return sum;
        }catch (Exception e){
            throw new UnsupportedOperationException();

        }


    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        try {
            double maxAmount = Double.NEGATIVE_INFINITY;
            for (Transaction transaction : transactions) {
                double amount = transaction.getAmount();
                if (amount > maxAmount) {
                    maxAmount = amount;
                }
            }
            return maxAmount;

        } catch (Exception e) {
            throw new UnsupportedOperationException("Failed to parse JSON", e);
        }
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        try {
            Set<String> clientSet = new HashSet<>();
            for (Transaction transaction : transactions) {
                clientSet.add(transaction.getSenderFullName());
                clientSet.add(transaction.getBeneficiaryFullName());
            }
            return clientSet.size();

        } catch (Exception e) {
            throw new UnsupportedOperationException("Failed to parse JSON", e);
        }
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        try {
            for (Transaction transaction : transactions) {
                if (transaction.isIssueSolved() == false  && transaction.getBeneficiaryFullName().equals(clientFullName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Failed to parse JSON", e);
        }
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        try {
            Map<String, List<Transaction>> transactionsByBeneficiary = new HashMap<>();
            for (Transaction transaction : transactions) {
                String name = transaction.getBeneficiaryFullName();
                List<Transaction> transactionsForName = transactionsByBeneficiary.getOrDefault(name, new ArrayList<>());
                transactionsForName.add(transaction);
                transactionsByBeneficiary.put(name, transactionsForName);
            }
            return transactionsByBeneficiary;

        } catch (Exception e) {
            throw new UnsupportedOperationException("Failed to parse JSON", e);
        }
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        try {
            Set<Integer> unsolvedIssues = new HashSet<>();
            for (Transaction transaction : transactions) {
                if (!transaction.isIssueSolved()) {
                    unsolvedIssues.add(transaction.getIssueId());
                }
            }
            return unsolvedIssues;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Failed to parse JSON", e);
        }
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        try {
            List<String> solvedIssueMessages = new ArrayList<>();
            for (Transaction transaction : transactions) {
                if (transaction.isIssueSolved()) {
                    solvedIssueMessages.add(transaction.getIssueMessage());
                }
            }
            return solvedIssueMessages;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Failed to parse JSON", e);
        }
    }

    /**
     * Returns the 3 transactions with highest amount sorted by amount descending
     */
    public List<Object> getTop3TransactionsByAmount() {
        try {
            List<Transaction> sortedTransactions = Arrays.asList(transactions);
            Collections.sort(sortedTransactions, (t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()));
            return Collections.singletonList(sortedTransactions.subList(0, Math.min(3, sortedTransactions.size())));
        } catch (Exception e) {
            throw new UnsupportedOperationException("Failed to parse JSON", e);
        }
    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Double> senderAmountMap = new HashMap<>();
            for (Transaction transaction : transactions) {
                String senderFullName = transaction.getSenderFullName();
                double amount = transaction.getAmount();
                double totalAmount = senderAmountMap.getOrDefault(senderFullName, 0.0);
                senderAmountMap.put(senderFullName, totalAmount + amount);
            }
            String topSender = null;
            double maxAmount = 0.0;
            for (Map.Entry<String, Double> entry : senderAmountMap.entrySet()) {
                if (entry.getValue() > maxAmount) {
                    maxAmount = entry.getValue();
                    topSender = entry.getKey();
                }
            }
            return Optional.ofNullable(topSender);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Failed to parse JSON", e);
        }
    }

}
