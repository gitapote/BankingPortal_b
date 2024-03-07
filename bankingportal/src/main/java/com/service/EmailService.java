package com.service;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
	public CompletableFuture<Void> sendEmail(String to, String subject, String text);

	public String getOtpLoginEmailTemplate(String name, String accountNumber, String otp);

	public String sendTransactionsByEmail(String name, String trnsType, String accountNumber, double amount);

	public String sendFundTransferEmail(String senderName, String targetAccount, String senderAccountNumber,
			double amount);
	
	public String sendAccountInfo(String name,String email,String accountNumber,String branch);

}
