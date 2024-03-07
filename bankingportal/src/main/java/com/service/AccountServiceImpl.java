package com.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dao.AccountRepository;
import com.dao.TransactionRepository;
import com.exception.InsufficientBalanceException;
import com.exception.NotFoundException;
import com.exception.UnauthorizedException;
import com.model.Account;
import com.model.Transaction;
import com.model.User;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
    private  AccountRepository accountRepository;
	
	@Autowired
    private TransactionRepository transactionRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
     EmailService emailService;

	@Override
	public Account createAccount(User user) {
	    String accountNumber = generateUniqueAccountNumber();
	    Account account = new Account();
	    account.setAccountNumber(accountNumber);
	    account.setBalance(0.0);
	    Account savedAccount = accountRepository.save(account);
	    String message = emailService.sendAccountInfo(user.getName(), user.getEmail(), accountNumber, account.getBranch());
	    emailService.sendEmail(user.getEmail(), "Account Open Successfully", message);
	    return savedAccount;
	}

	
	@Override
    public boolean isPinCreated(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        return account.getPin()!=null;
    }
    
	private String generateUniqueAccountNumber() {
	    String accountNumber;
	    do {
	        // Generate a UUID as the account number
	        accountNumber = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
	    } while (accountRepository.findByAccountNumber(accountNumber) != null);

	    return accountNumber;
	}

    
    
    @Override
    public void createPIN(String accountNumber, String password, String pin) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        if (!passwordEncoder.matches(password, account.getUser().getPassword())) {
            throw new UnauthorizedException("Invalid password");
        }

        account.setPin(passwordEncoder.encode(pin));
        accountRepository.save(account);
    }
    
    @Override
    public void updatePIN(String accountNumber, String oldPIN, String password, String newPIN) {
    	System.out.println(accountNumber+"  "+oldPIN+" "+newPIN+"  "+password);
    	
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        if (!passwordEncoder.matches(oldPIN, account.getPin())) {
            throw new UnauthorizedException("Invalid PIN");
        }

        if (!passwordEncoder.matches(password, account.getUser().getPassword())) {
            throw new UnauthorizedException("Invalid password");
        }

        account.setPin(passwordEncoder.encode(newPIN));
        accountRepository.save(account);
    }
    
    @Override
    public void cashDeposit(String accountNumber, String pin, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        if (!passwordEncoder.matches(pin, account.getPin())) {
            throw new UnauthorizedException("Invalid PIN");
        }

        double currentBalance = account.getBalance();
        double newBalance = currentBalance + amount;
        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransaction_type("Deposit");
        transaction.setTransaction_date(new Date());
        transaction.setSourceAccount(account);
        transactionRepository.save(transaction);
        String message=emailService.sendTransactionsByEmail(account.getUser().getName(),"Deposit",accountNumber,amount);
        emailService.sendEmail(account.getUser().getEmail(), "Deposit", message);
    }
    
    @Override
    public void cashWithdrawal(String accountNumber, String pin, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        if (!passwordEncoder.matches(pin, account.getPin())) {
            throw new UnauthorizedException("Invalid PIN");
        }

        double currentBalance = account.getBalance();
        if (currentBalance < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        double newBalance = currentBalance - amount;
        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransaction_type("Withdrawal");
        transaction.setTransaction_date(new Date());
        transaction.setSourceAccount(account);
        transactionRepository.save(transaction);
        String message=emailService.sendTransactionsByEmail(account.getUser().getName(),"Withdraw",accountNumber,amount);
        emailService.sendEmail(account.getUser().getEmail(), "Withdraw", message);
    }
    
    @Override
    public void fundTransfer(String sourceAccountNumber, String targetAccountNumber, String pin, double amount) {
        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
        if (sourceAccount == null) {
            throw new NotFoundException("Source account not found");
        }

        Account targetAccount = accountRepository.findByAccountNumber(targetAccountNumber);
        if (targetAccount == null) {
            throw new NotFoundException("Target account not found");
        }

        if (!passwordEncoder.matches(pin, sourceAccount.getPin())) {
            throw new UnauthorizedException("Invalid PIN");
        }

        double sourceBalance = sourceAccount.getBalance();
        if (sourceBalance < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        double newSourceBalance = sourceBalance - amount;
        sourceAccount.setBalance(newSourceBalance);
        accountRepository.save(sourceAccount);
        
        String message=emailService.sendTransactionsByEmail(sourceAccount.getUser().getName(),"Deposit",sourceAccount.getAccountNumber(),amount);
        emailService.sendEmail(sourceAccount.getUser().getEmail(), "Deposit", message);

        double targetBalance = targetAccount.getBalance();
        double newTargetBalance = targetBalance + amount;
        targetAccount.setBalance(newTargetBalance);
        accountRepository.save(targetAccount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransaction_type("Fund Transfer");
        transaction.setTransaction_date(new Date());
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transactionRepository.save(transaction);
        
        String message1=emailService.sendFundTransferEmail(sourceAccount.getUser().getName(),targetAccount.getAccountNumber(),sourceAccount.getAccountNumber(),amount);
        emailService.sendEmail(targetAccount.getUser().getEmail(), "Fund Transfer", message1);
      
    }
}
