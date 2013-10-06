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
 * Represents the rules specific to dialing to/from a particular NationalTelephoneNetwork
 */
public class NationalTelephoneNetworkDialingRules implements Serializable  {
	/**
	 * <p>
	 * <b><i>YANKED FROM http://www.wtng.info/wtng-glo.html :</i></b>
	 * </p>
	 * <p>
	 * An international prefix is the code dialled prior to an international number 
	 * (country code, area code if any, then subscriber number). In most nations, 
	 * this will be 00. In some nations in Asia, this is 001 (in some cases, alternate 
	 * codes are available to select the particular international carrier). In North 
	 * America, this is 011 (or 01 for special call processing - collect, person-to-person, 
	 * calling card, etc.).
	 * </p>
	 */
	private Integer internationalPrefix;
	
	/**
	 * <p>
	 * <b><i>YANKED FROM http://www.wtng.info/wtng-glo.html :</i></b>
	 * </p>
	 * <p>
	 * A trunk prefix refers to the initial digit(s) to be dialled in a domestic 
	 * call, prior to the area code (if necessary) and the subscriber number. 0 is 
	 * the trunk prefix in most nations. In the North American Numbering Plan +1 it 
	 * is 1; it is merely co-incidental that the country code and trunk prefix are 
	 * both 1. For calls to another country code, the trunk prefix is generally omitted. 
	 * For example, a call to London, UK within the UK would be dialled as 020 #### #### 
	 * but from outside the UK, the initial 0 (trunk prefix) is omitted: +44 20 #### ####. 
	 * Some nations do not use a trunk prefix, which means only the subscriber number is 
	 * dialled in those cases.
	 * </p>
	 */
	private Integer trunkPrefix;
	
	private Boolean isTrunkPrefixRequiredInCountry;
	
	private Boolean isTrunkPrefixRequiredOutsideCountry;
}