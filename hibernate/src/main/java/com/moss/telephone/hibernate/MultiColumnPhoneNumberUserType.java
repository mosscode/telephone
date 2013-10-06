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
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import com.moss.telephone.PhoneNumber;
import com.moss.telephone.TelephoneNumberFormatException;

public class MultiColumnPhoneNumberUserType implements CompositeUserType {

	private enum Property{

		countryCode(new CustomType(CountryCodeUserType.class, null)){
			@Override
			public Object getPropertyValue(PhoneNumber value)
					throws HibernateException {
				return value.getCountryCode();
			}
			@Override
			public void nullSafeSet(PreparedStatement st, int index,
					PhoneNumber value) throws HibernateException , SQLException{
				if(value==null)
					st.setNull(index, Types.VARCHAR);
				else
					st.setString(index, value.getCountryCode().toString());
			}
		},
		areaCode(new CustomType(AreaCodeUserType.class, null)){
			@Override
			public Object getPropertyValue(PhoneNumber value)
					throws HibernateException {
				return value.getAreaCode();
			}
			@Override
			public void nullSafeSet(PreparedStatement st, int index,
					PhoneNumber value) throws HibernateException , SQLException{
				if(value==null)
					st.setNull(index, Types.VARCHAR);
				else
					st.setString(index, value.getAreaCode().toString());
				
			}
		},
		subscriberNumber(new CustomType(SubscriberNumberUserType.class, null)){
			@Override
			public Object getPropertyValue(PhoneNumber value)
					throws HibernateException {
				return value.getSubscriberNumber();
			}
			@Override
			public void nullSafeSet(PreparedStatement st, int index,
					PhoneNumber value) throws HibernateException , SQLException{
				if(value==null)
					st.setNull(index, Types.VARCHAR);
				else
					st.setString(index, value.getSubscriberNumber().toString());
								
			}
		};
		static Type[] types(){
			Type[] types = new Type[Property.values().length];
			for(int x=0;x<values().length;x++){
				types[x] = values()[x].jdbcType;
			}
			return types;
		}
		
		static String[] names(){
			String[] names = new String[Property.values().length];
			for(int x=0;x<values().length;x++){
				names[x] = values()[x].name().toLowerCase();
			}
			return names;
		}
		
		
		
		Type jdbcType;
		private Property(Type jdbcType) {
			this.jdbcType = jdbcType;
		}
		
		public abstract Object getPropertyValue(PhoneNumber value)throws HibernateException;
		public abstract void nullSafeSet(PreparedStatement st, int index, PhoneNumber value) throws HibernateException, SQLException;
		public final void setPropertyValue(Object componentValue, Object value) throws HibernateException{
			throw new HibernateException("Cannot set this because the parent is immutable");
		}
	}

	public Object assemble(Serializable cached, SessionImplementor session,
			Object owner) throws HibernateException {
		return ((PhoneNumber)owner).toString();
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public Serializable disassemble(Object value, SessionImplementor session)
			throws HibernateException {
		return value.toString();
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return x.equals(y);
	}

	public String[] getPropertyNames() {
		return Property.names();
	}

	public Type[] getPropertyTypes() {
		return Property.types();
	}

	public Object getPropertyValue(Object component, int property)
			throws HibernateException {
		return Property.values()[property].getPropertyValue((PhoneNumber)component);
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public boolean isMutable() {
		return false;
	}

	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		String countryCodeString = rs.getString(names[0]);
		String areaCodeString = rs.getString(names[1]);
		String subscriberNumberString = rs.getString(names[2]);
		try {
			return new PhoneNumber(countryCodeString, areaCodeString, subscriberNumberString);
		} catch (TelephoneNumberFormatException e) {
			throw new HibernateException(e);
		}
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
			PhoneNumber number = (PhoneNumber) value;
			for(int x=0;x<Property.values().length;x++){
				Property.values()[x].nullSafeSet(st, index + x, number);
			}
	}

	public Object replace(Object original, Object target,
			SessionImplementor session, Object owner) throws HibernateException {
		return original;
	}

	public Class returnedClass() {
		return PhoneNumber.class;
	}

	public void setPropertyValue(Object component, int property, Object value)
			throws HibernateException {
		Property.values()[property].setPropertyValue(component, value);
	}
	
	
}
