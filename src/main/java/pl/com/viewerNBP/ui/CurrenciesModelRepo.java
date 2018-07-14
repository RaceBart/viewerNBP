package pl.com.viewerNBP.ui;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrenciesModelRepo extends JpaRepository<CurrenciesModel, Integer>{
	

	List<CurrenciesModel> findByCurrencyname(String currencyname);


}
