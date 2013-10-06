/**
 * Copyright (C) 2013, Moss Computing Inc.
 *
 * This file is part of telephone.
 *
 * telephone is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * telephone is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with telephone; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */
package com.moss.telephone;

import java.io.Serializable;

/**
 * <p>
 * Represents a phone number on the international phone network
 * without any country-specific concepts.
 * </p>
 * <p>
 * To dial a number:
 * <pre>
 * NationalTelephoneNetworkDialingRules localRules = ...
 * NationalTelephoneNetworkDialingRules remoteRules = ...
 * 
 * localRules.internationalPrefix countryCode [remoteRules.trunkPrefix] areaCode subscriberNumber
 *  </pre>
 * </p>
 * <p>
 * <b><i>FOR MORE INFO:</i></b> 
 * <ul>
 * 	<li>http://www.wtng.info/wtng-glo.html
 * 	<li>http://www.geocities.com/dtmcbride/reference/tel-fmt.html
 * </ul>
 * </p>
 * @see NorthAmericanPhoneNumber
 */
public class PhoneNumber implements Serializable {
	/**
	 * <p>
	 * <b><i>YANKED FROM http://www.wtng.info/wtng-glo.html :</i></b>
	 * </p>
	 * <p>
	 * A country code is used to reach the particular telephone system for each nation 
	 * or special service.
	 * </p>
	 */
	private CountryCode countryCode;
	
	/**
	 * <p>
	 * <b><i>YANKED FROM http://www.wtng.info/wtng-glo.html :</i></b>
	 * </p>
	 * <p>
	 * An area code is used within many nations to route calls to a particular city, 
	 * region or special service. Depending on the nation or region, it may also be 
	 * referred to as a numbering plan area, subscriber trunk dialling code, national 
	 * destination code or routing code.
	 * </p>
	 */
	private AreaCode areaCode;
	

	/**
	 * <p>
	 * <b><i>YANKED FROM http://www.wtng.info/wtng-glo.html :</i></b>
	 * </p>
	 * <p>
	 * The local number or subscriber number represents the specific telephone number 
	 * to be dialed, but does not include the country code, area code (if applicable),
	 * international prefix or trunk prefix.
	 * </p>
	 */
	private SubscriberNumber subscriberNumber;
	
	public PhoneNumber(String text) throws TelephoneNumberFormatException {
		String[] segments = text.trim().split(" ");
		if(segments.length!=3)
			throw new TelephoneNumberFormatException("I was expecting a phone number like this: +CC AREACODE SUBSCRIBERNUMBER, but got this instead: " + text);
		// +1
		this.countryCode = CountryCode.forCode(segments[0].substring(1));
		this.areaCode = new AreaCode(segments[1]);
		this.subscriberNumber = new SubscriberNumber(segments[2]);
	}
	
	
	
	public PhoneNumber(String areaCode, String subscriberNumber) throws TelephoneNumberFormatException {
		this(CountryCode.NORTH_AMERICA, areaCode, subscriberNumber);
	}
	
	public PhoneNumber(AreaCode areaCode, SubscriberNumber subscriberNumber) {
		this(CountryCode.NORTH_AMERICA, areaCode, subscriberNumber);
	}
	public PhoneNumber(String countryCode, String areaCode, String subscriberNumber) throws TelephoneNumberFormatException {
		this.countryCode = CountryCode.forCode(countryCode);
		this.areaCode = new AreaCode(areaCode);
		this.subscriberNumber = new SubscriberNumber(subscriberNumber);
	}
	
	public PhoneNumber(CountryCode countryCode, String areaCode, String subscriberNumber) throws TelephoneNumberFormatException {
		this.countryCode = countryCode;
		this.areaCode = new AreaCode(areaCode);
		this.subscriberNumber = new SubscriberNumber(subscriberNumber);
	}
	
	public PhoneNumber(CountryCode countryCode, AreaCode areaCode, SubscriberNumber subscriberNumber) {
		this.countryCode = countryCode;
		this.areaCode = areaCode;
		this.subscriberNumber = subscriberNumber;
	}

	public AreaCode getAreaCode() {
		return areaCode;
	}

	public CountryCode getCountryCode() {
		return countryCode;
	}

	public SubscriberNumber getSubscriberNumber() {
		return subscriberNumber;
	}
	
	/**
	 * Returns a textual representation of this number in accordance with International Number
	 * Format as defined by http://www.geocities.com/dtmcbride/reference/tel-fmt.html
	 */
	public String toString(){
		return "+" + countryCode.getCode() + " " + areaCode + " " + subscriberNumber;
	}
		
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((areaCode == null) ? 0 : areaCode.hashCode());
		result = PRIME * result + ((countryCode == null) ? 0 : countryCode.hashCode());
		result = PRIME * result + ((subscriberNumber == null) ? 0 : subscriberNumber.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PhoneNumber other = (PhoneNumber) obj;
		if (areaCode == null) {
			if (other.areaCode != null)
				return false;
		} else if (!areaCode.equals(other.areaCode))
			return false;
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equals(other.countryCode))
			return false;
		if (subscriberNumber == null) {
			if (other.subscriberNumber != null)
				return false;
		} else if (!subscriberNumber.equals(other.subscriberNumber))
			return false;
		return true;
	}
}
