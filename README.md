# CashFlowMinimizerSystem


This project focuses on minimizing the number of transactions among multiple banks using different payment modes. The goal is to optimize the cash flow among these banks by reducing the number of payments while ensuring that the transactions are still valid based on shared payment types.

Here are the key components of the solution:

Bank: Each bank has a name, a net balance (calculated from the transactions), and a set of payment modes (e.g., Google Pay, PayTM).

Transactions: Represented in a graph, where each transaction is directed from one bank (debtor) to another (creditor) with a specific amount of money.

Net Balance Calculation: For each bank, we calculate the total money that needs to be paid or received based on the transactions.

Cash Flow Minimization: The core algorithm uses greedy methods to reduce the number of transactions. It identifies the bank with the smallest debt and pairs it with the bank that can receive the most money.
<img width="1440" alt="Screenshot 2024-10-01 at 2 58 03 PM" src="https://github.com/user-attachments/assets/cc5a8168-73cb-44d6-83ef-f31c568743c3">
