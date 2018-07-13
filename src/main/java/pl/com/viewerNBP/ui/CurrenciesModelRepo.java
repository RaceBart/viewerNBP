package pl.com.viewerNBP.ui;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrenciesModelRepo extends JpaRepository<CurrenciesModel, Integer>{
	


	List<CurrenciesModel> findByCurrencyname(String currencyname);
	
//	@Query("SELECT DISTINCT currency_name FROM currencies")
//	List<String> findDistinctCurrency_name();
	
//	List<String> findDistinctCurrencynameByCurrencyname(String currencyname);
	
//	@Query("select u from currencies u")
//	Stream<CurrenciesModel> findAllByCustomQueryAndStream();
	
//	@Query("select u from currencies u where u.currency_value = 4")
//	public CurrenciesModel getStartDate();
}
