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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import com.moss.telephone.CountryCode;
import com.moss.telephone.PhoneNumber;
import com.moss.telephone.TelephoneNumberFormatException;

public class TestPhoneNumber extends TestCase {
	
	public void testParsing(){
		try {
			String numberString = "+1 805 0340095";
			
			PhoneNumber number = new PhoneNumber(CountryCode.NORTH_AMERICA, "805", "0340095");
			assertEquals(numberString, number.toString());
			assertEquals(CountryCode.NORTH_AMERICA, number.getCountryCode());
			assertEquals("805", number.getAreaCode().toString());
			assertEquals("0340095", number.getSubscriberNumber().toString());
			
			number = new PhoneNumber(numberString);
			assertEquals(numberString, number.toString());
			assertEquals(CountryCode.NORTH_AMERICA, number.getCountryCode());
			assertEquals("805", number.getAreaCode().toString());
			assertEquals("0340095", number.getSubscriberNumber().toString());
		} catch (TelephoneNumberFormatException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	public void testSerialization(){
		try {
			PhoneNumber numberOut = new PhoneNumber(CountryCode.NORTH_AMERICA, "805", "0340095");
			ByteArrayOutputStream rawOut = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(rawOut);
			objectOut.writeObject(numberOut);
			objectOut.close();
			byte[] bytes = rawOut.toByteArray();
			PhoneNumber numberIn = (PhoneNumber) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
			assertNotNull(numberIn);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}