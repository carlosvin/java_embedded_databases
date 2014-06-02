package domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Price {

	private static int n = 0;

	@Id
	private final String instrument;
	@Id
	private final String market;
	@Id
	private final Date date;

	private double price;

	public Price(String market, String instr) {
		this.market = market;
		this.instrument = instr;
		this.date = new Date();
		this.price = 0.0;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(long t) {
		this.date.setTime(t);
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getMarket() {
		return market;
	}

	public String getInstrument() {
		return instrument;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Price [date=");
		builder.append(date);
		builder.append(", price=");
		builder.append(price);
		builder.append(", market=");
		builder.append(market);
		builder.append(", instrument=");
		builder.append(instrument);
		builder.append("]");
		return builder.toString();
	}

	private static Price create(int instr, int numberOfUpdates) {
		Price p = new Price("Market " + (instr % 100), "Instrument " + instr);
		p.date.setTime(numberOfUpdates);
		p.price = instr + 1.1;
		return p;
	}

	public static Price create(int i) {
		return Price.create(i, n++);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((instrument == null) ? 0 : instrument.hashCode());
		result = prime * result + ((market == null) ? 0 : market.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Price)) {
			return false;
		}
		Price other = (Price) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (instrument == null) {
			if (other.instrument != null) {
				return false;
			}
		} else if (!instrument.equals(other.instrument)) {
			return false;
		}
		if (market == null) {
			if (other.market != null) {
				return false;
			}
		} else if (!market.equals(other.market)) {
			return false;
		}
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price)) {
			return false;
		}
		return true;
	}

}
