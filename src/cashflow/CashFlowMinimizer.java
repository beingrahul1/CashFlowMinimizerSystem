package cashflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import cashflow.CashFlowMinimizer.Pair;

public class CashFlowMinimizer {

    // Function to get the bank with the minimum net amount
    static int getMinIndex(Bank[] banks, int numBanks) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < numBanks; i++) {
            if (banks[i].netAmount == 0) continue;

            if (banks[i].netAmount < min) {
                minIndex = i;
                min = banks[i].netAmount;
            }
        }
        return minIndex;
    }

    // Function to get the bank with the maximum net amount
    static int getMaxIndex(Bank[] banks, int numBanks, int minIndex, Bank[] input, int maxNumTypes) {
        int max = Integer.MIN_VALUE;
        int maxIndex = -1;

        for (int i = 0; i < numBanks; i++) {
            if (banks[i].netAmount == 0 || banks[i].netAmount < 0) continue;

            Set<String> commonModes = new HashSet<>(banks[minIndex].paymentModes);
            commonModes.retainAll(banks[i].paymentModes);

            if (!commonModes.isEmpty() && banks[i].netAmount > max) {
                max = banks[i].netAmount;
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    // Function to calculate the minimum cash flow
    static void minimizeCashFlow(int numBanks, Bank[] banks, int[][] graph, int maxNumTypes) {
        Bank[] netBanks = new Bank[numBanks];
        for (int i = 0; i < numBanks; i++) {
            netBanks[i] = new Bank(banks[i].name);
            netBanks[i].paymentModes = banks[i].paymentModes;

            int amount = 0;
            for (int j = 0; j < numBanks; j++) {
                amount += graph[j][i] - graph[i][j];
            }
            netBanks[i].netAmount = amount;
        }

        List<List<Pair>> ansGraph = new ArrayList<>(numBanks);
        for (int i = 0; i < numBanks; i++) {
            ansGraph.add(new ArrayList<>());
            for (int j = 0; j < numBanks; j++) {
                ansGraph.get(i).add(new Pair(0, ""));
            }
        }

        int numZeroNetAmounts = 0;
        while (numZeroNetAmounts != numBanks) {
            int minIndex = getMinIndex(netBanks, numBanks);
            int maxIndex = getMaxIndex(netBanks, numBanks, minIndex, banks, maxNumTypes);

            if (maxIndex == -1) {
                // World Bank as intermediary
                int simpleMaxIndex = getMinIndex(netBanks, numBanks);
                ansGraph.get(minIndex).get(0).first += Math.abs(netBanks[minIndex].netAmount);
                ansGraph.get(0).get(simpleMaxIndex).first += Math.abs(netBanks[minIndex].netAmount);

                netBanks[simpleMaxIndex].netAmount += netBanks[minIndex].netAmount;
                netBanks[minIndex].netAmount = 0;

                if (netBanks[minIndex].netAmount == 0) numZeroNetAmounts++;
                if (netBanks[simpleMaxIndex].netAmount == 0) numZeroNetAmounts++;
            } else {
                int transactionAmount = Math.min(Math.abs(netBanks[minIndex].netAmount), netBanks[maxIndex].netAmount);
                ansGraph.get(minIndex).get(maxIndex).first += transactionAmount;

                netBanks[minIndex].netAmount += transactionAmount;
                netBanks[maxIndex].netAmount -= transactionAmount;

                if (netBanks[minIndex].netAmount == 0) numZeroNetAmounts++;
                if (netBanks[maxIndex].netAmount == 0) numZeroNetAmounts++;
            }
        }

        printAns(ansGraph, numBanks, banks);
    }

    // Function to print the final transactions
    static void printAns(List<List<Pair>> ansGraph, int numBanks, Bank[] banks) {
        System.out.println("The transactions for minimum cash flow are as follows:");
        for (int i = 0; i < numBanks; i++) {
            for (int j = 0; j < numBanks; j++) {
                if (i == j) continue;

                if (ansGraph.get(i).get(j).first != 0) {
                    System.out.println(banks[i].name + " pays Rs " + ansGraph.get(i).get(j).first + " to " + banks[j].name);
                }
            }
        }
    }

    // Main method to drive the program
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of banks:");
        int numBanks = sc.nextInt();

        Bank[] banks = new Bank[numBanks];
        for (int i = 0; i < numBanks; i++) {
            System.out.println("Enter the name of bank " + (i + 1) + ":");
            String name = sc.next();
            banks[i] = new Bank(name);

            System.out.println("Enter number of payment modes:");
            int numModes = sc.nextInt();
            for (int j = 0; j < numModes; j++) {
                String mode = sc.next();
                banks[i].paymentModes.add(mode);
            }
        }

        System.out.println("Enter the number of transactions:");
        int numTransactions = sc.nextInt();
        int[][] graph = new int[numBanks][numBanks];
        for (int i = 0; i < numTransactions; i++) {
            System.out.println("Enter transaction (debtor, creditor, amount):");
            String debtor = sc.next();
            String creditor = sc.next();
            int amount = sc.nextInt();

            int debtorIndex = -1, creditorIndex = -1;
            for (int j = 0; j < numBanks; j++) {
                if (banks[j].name.equals(debtor)) debtorIndex = j;
                if (banks[j].name.equals(creditor)) creditorIndex = j;
            }

            if (debtorIndex == -1 || creditorIndex == -1) {
                System.out.println("Error: Invalid debtor or creditor name.");
                continue;
            }

            graph[debtorIndex][creditorIndex] = amount;
        }

        minimizeCashFlow(numBanks, banks, graph, banks[0].paymentModes.size());
    }

    // Utility class for storing pairs
    static class Pair {
        int first;
        String second;

        public Pair(int first, String second) {
            this.first = first;
            this.second = second;
        }
    }
}
