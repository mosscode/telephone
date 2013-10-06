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
package com.moss.telephone.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import com.moss.telephone.PhoneNumber;
import com.moss.telephone.TelephoneNumberFormatException;

public class PhoneNumberUserType implements UserType{
	public static final String CLASS_NAME = PhoneNumberUserType.class.getName();
	
	private static final int SQL_TYPE = Types.VARCHAR;
	
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		try {
			return new PhoneNumber(cached.toString());
		} catch (TelephoneNumberFormatException e) {
			throw new HibernateException(e);
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return value.toString();
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if(x==null && y==null) return true;
		else if (x==null || y==null) return false;
		else return x.equals(y);
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public boolean isMutable() {
		return false;
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException {
		try {
			String text = rs.getString(names[0]);
			if(text==null) return null;
			else return new PhoneNumber(text);
		} catch (TelephoneNumberFormatException e) {
			throw new HibernateException(e);
		}
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		if(value==null)
			st.setNull(index, SQL_TYPE);
		else
			st.setString(index, value.toString());
	}

	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	public Class returnedClass() {
		return PhoneNumber.class;
	}

	public int[] sqlTypes() {
		return new int[]{SQL_TYPE};
	}
	
}
