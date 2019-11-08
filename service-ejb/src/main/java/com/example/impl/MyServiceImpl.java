package com.example.impl;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;

import com.example.Service;
import com.example.model.Teacher;

@Singleton
public class MyServiceImpl implements Service {

	@PersistenceContext(unitName = "persistence")
	EntityManager em;

	@Override
	public void saveTeacher() {
		Teacher t = new Teacher();
		t.setName("nicolas");

		em.persist(t);

	}

}
