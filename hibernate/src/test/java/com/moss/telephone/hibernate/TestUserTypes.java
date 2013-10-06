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

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.moss.blankslate.BlankslateCatalogHandle;
import com.moss.blankslate.CatalogFactory;
import com.moss.jdbcdrivers.DatabaseType;
import com.moss.telephone.AreaCode;
import com.moss.telephone.PhoneNumber;
import com.moss.telephone.SubscriberNumber;

public class TestUserTypes extends TestCase {
	private SessionFactory hFactory;
	private CatalogFactory catFactory;
	private BlankslateCatalogHandle handle;

	protected void setUp() throws Exception {
		catFactory = CatalogFactory.getFactory(DatabaseType.DB_TYPE_HSQLDB);
		handle = catFactory.createCatalog();
		Configuration cfg = new Configuration()
		.setProperty("hibernate.connection.driver_class","org.hsqldb.jdbcDriver")
		.setProperty("hibernate.dialect","org.hibernate.dialect.HSQLDialect")
		.setProperty("hibernate.connection.url","jdbc:hsqldb:mem:test")
		.setProperty("hibernate.connection.username","sa")
		.setProperty("hibernate.connection.password","")
		.setProperty("hibernate.connection.pool_size", "1")
		.setProperty("hibernate.show_sql","true")
		.setProperty("hibernate.hbm2ddl.auto","update")
		.addClass(TestEntity.class);
		
		hFactory = cfg.buildSessionFactory();
	}
	
	protected void tearDown() throws Exception {
		hFactory.close();
		catFactory.deleteCatalog(handle);
	}
	
	public void testValuePersistence() throws Exception {
		Session session;
		
		// SAVE A NEW TestEntity WITH NON-NULL VALUES
		session= hFactory.openSession();
		session.beginTransaction();
		
		TestEntity testEntity = new TestEntity();
		testEntity.setAreaCode(new AreaCode("123"));
		testEntity.setSubscriberNumber(new SubscriberNumber("2342344"));
		testEntity.setPhoneNumber(new PhoneNumber("+1 661 3323332"));
		
		session.save(testEntity);
		session.getTransaction().commit();
		session.flush();
		session.close();
		
		// READ THE SAVED ENTITY WITH A NEW SESSION AND MAKE SURE THE VALUES HAVEN'T CHANGED
		session = hFactory.openSession();
		session.beginTransaction();
		TestEntity testEntity2 = (TestEntity) session.load(TestEntity.class, testEntity.getId());
		assertNotNull(testEntity2);
		assertNotNull(testEntity2.getAreaCode());
		assertNotNull(testEntity2.getSubscriberNumber());
		assertEquals(testEntity.getAreaCode(), testEntity2.getAreaCode());
		assertEquals(testEntity.getSubscriberNumber(), testEntity2.getSubscriberNumber());
		System.out.println(testEntity2.getPhoneNumber());
		assertEquals(testEntity.getPhoneNumber(), testEntity2.getPhoneNumber());
		session.close();
		
	}
	
	public void testNullBehavior(){
		Session session;
		
		// SAVE A TestEntity WITH NULL VALUES
		session = hFactory.openSession();
		session.beginTransaction();
		TestEntity testEntity = new TestEntity();
		session.save(testEntity);
		session.getTransaction().commit();
		session.flush();
		
		session.close();
		
		// READ THE TestEntity FROM A DIFFERENT SESSION AND MAKE SURE EVERYTHING IS STILL NULL
		session = hFactory.openSession();
		session.beginTransaction();
		TestEntity testEntity2 = (TestEntity) session.load(TestEntity.class, testEntity.getId());
		assertNotNull(testEntity2);
		assertNull(testEntity2.getAreaCode());
		assertNull(testEntity2.getSubscriberNumber());
		session.close();
	}
}
