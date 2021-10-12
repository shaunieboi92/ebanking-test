package com.ebanking.common.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

//import com.ebanking.common.model.QTransaction;
import com.ebanking.common.model.Transaction;
//import com.querydsl.core.types.dsl.StringExpression;
//import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

	Page<Transaction> findByTransactionDateBetween(LocalDateTime startDateConv, LocalDateTime endDateConv,
			Pageable paging);

	Page<Transaction> findByTransactionDateBetweenAndAccountIBANIn(
			LocalDateTime startDateConv, LocalDateTime endDateConv,
			List<String> ibanList, Pageable paging);
	
//	Page<Transaction> findByTransactionDateBetweenAndAccountIBAN(LocalDateTime startDateConv, LocalDateTime endDateConv, String accountIBAN,
//			Pageable paging);

}

//@Repository
//public interface TransactionRepository extends JpaRepository<Transaction, Long>, QuerydslPredicateExecutor<Transaction>,
//		QuerydslBinderCustomizer<QTransaction> {
//
//	private Logger logger = LoggerFactory.getLogger(TransactionRepository.class);
//
//	@SuppressWarnings("NullableProblems")
//	@Override
//	default void customize(QuerydslBindings bindings, QTransaction transaction) {
//
//		bindings.excluding(transaction.transactionId);
//		bindings.bind(String.class)
//				.first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
//
//		bindings.bind(transaction.transactionDate).all((path, value) -> {
//			logger.info("datetime is:" + path);
//			Iterator<? extends LocalDateTime> it = value.iterator();
//			LocalDateTime from = it.next();
//			if (value.size() >= 2) {
//				LocalDateTime to = it.next();
//				return Optional.of(path.between(from, to));
//			} else {
//				return Optional.of(path.goe(from));
//			}
//		});
//	}
//
//}
