package com.ebanking.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebanking.common.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

	Currency findByCurrencyCode(String currencyCode);

}
