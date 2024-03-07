package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.AmountRequest;
import com.dto.FundTransferRequest;
import com.dto.PinRequest;
import com.dto.PinUpdateRequest;
import com.dto.TransactionDTO;
import com.service.AccountService;
import com.service.TransactionService;
import com.util.LoggedinUser;

@RestController
@RequestMapping("/api/account")
@CrossOrigin
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/pin/check")
	@CrossOrigin
	public ResponseEntity<?> checkAccountPIN() {
		boolean isPINValid = accountService.isPinCreated(LoggedinUser.getAccountNumber());

		Map<String, Object> result = new HashMap<>();
		result.put("hasPIN", isPINValid);

		if (isPINValid) {
			result.put("msg", "PIN Created");

		} else {
			result.put("msg", "Pin Not Created");
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/pin/create")
	@CrossOrigin
	public ResponseEntity<?> createPIN(@RequestBody PinRequest pinRequest) {
		accountService.createPIN(LoggedinUser.getAccountNumber(), pinRequest.getPassword(), pinRequest.getPin());

		Map<String, String> response = new HashMap<>();
		response.put("msg", "PIN created successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/pin/update")
	@CrossOrigin
	public ResponseEntity<?> updatePIN(@RequestBody PinUpdateRequest pinUpdateRequest) {
		accountService.updatePIN(LoggedinUser.getAccountNumber(), pinUpdateRequest.getOldPin(),
				pinUpdateRequest.getPassword(), pinUpdateRequest.getNewPin());

		Map<String, String> response = new HashMap<>();
		response.put("msg", "PIN updated successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/deposit")
	@CrossOrigin
	public ResponseEntity<?> cashDeposit(@RequestBody AmountRequest amountRequest) {
		accountService.cashDeposit(LoggedinUser.getAccountNumber(), amountRequest.getPin(), amountRequest.getAmount());

		Map<String, String> response = new HashMap<>();
		response.put("msg", "Cash deposited successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/withdraw")
	@CrossOrigin
	public ResponseEntity<?> cashWithdrawal(@RequestBody AmountRequest amountRequest) {
		accountService.cashWithdrawal(LoggedinUser.getAccountNumber(), amountRequest.getPin(),
				amountRequest.getAmount());

		Map<String, String> response = new HashMap<>();
		response.put("msg", "Cash withdrawn successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/fund-transfer")
	@CrossOrigin
	public ResponseEntity<?> fundTransfer(@RequestBody FundTransferRequest fundTransferRequest) {
		accountService.fundTransfer(LoggedinUser.getAccountNumber(), fundTransferRequest.getTargetAccountNumber(),
				fundTransferRequest.getPin(), fundTransferRequest.getAmount());
		Map<String, String> response = new HashMap<>();
		response.put("msg", "Fund transferred successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/transactions")
	@CrossOrigin
	public ResponseEntity<List<TransactionDTO>> getAllTransactionsByAccountNumber() {
		List<TransactionDTO> transactions = transactionService
				.getAllTransactionsByAccountNumber(LoggedinUser.getAccountNumber());
		return ResponseEntity.ok(transactions);
	}
}
