package pl.com.viewerNBP.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="\"currencies\"")
public class CurrenciesModel implements Serializable{

	@Id
	@Column(name="\"id_serial\"")
	private Integer id_serial;
	@Column(name="currency_date")
	private String currency_date;
	@Column(name="currency_name")
	private String currencyname;
	@Column(name="currency_value")
	private Integer currency_value;
	
	public CurrenciesModel(Integer id_serial, String currency_date, String currency_name, Integer currency_value) {
		super();
		this.id_serial = id_serial;
		this.currency_date = currency_date;
		this.currencyname = currency_name;
		this.currency_value = currency_value;
	}
	
	
	public Integer getId_serial() {
		return id_serial;
	}
	public void setId_serial(Integer id_serial) {
		this.id_serial = id_serial;
	}
	public String getCurrency_date() {
		return currency_date;
	}
	public void setCurrency_date(String currency_date) {
		this.currency_date = currency_date;
	}
	public String getCurrency_name() {
		return currencyname;
	}
	public void setCurrency_name(String currency_name) {
		this.currencyname = currency_name;
	}
	public Integer getCurrency_value() {
		return currency_value;
	}
	public void setCurrency_value(Integer currency_value) {
		this.currency_value = currency_value;
	}
	
}
