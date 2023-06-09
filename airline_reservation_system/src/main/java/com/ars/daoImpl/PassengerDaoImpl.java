package com.ars.daoImpl;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.ars.config.HibernateUtil;
import com.ars.dao.PassengerDao;
import com.ars.entity.Passenger;

public class PassengerDaoImpl implements PassengerDao{

	@Override
	public void savePassenger(Passenger passenger) //for save Passenger
	{
		try(Session session=HibernateUtil.getSession())
		{
			session.beginTransaction();
			session.save(passenger);
			session.getTransaction().commit();
			session.clear();
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	@Override
	public boolean login(String userName, String password) // for passenger login
	{
		Session session=HibernateUtil.getSession();
		Passenger p=(Passenger)session.get(Passenger.class,Integer.parseInt(JOptionPane.showInputDialog("Enter id: ","type here")));
		if(p.getUserName().equals(userName)&& p.getPassword().equals(password))
		return true;
		else
		return false;
	}

	@Override
	public Passenger getPassenger(int id) // for get passenger by id
	{
		try(Session session=HibernateUtil.getSession())
		{
			Passenger passenger=(Passenger)session.get(Passenger
					.class, id);
			return passenger;
		}catch (HibernateException e) {
			System.out.println(e);
		}
		
		return null;
	}

	@Override
	public Passenger updatePassenger(int id, Passenger passenger) // for update Passenger
	{
		try(Session session=HibernateUtil.getSession())
		{
			Passenger pass=(Passenger)session.load(Passenger.class, id);
			pass.setName(passenger.getName());
			pass.setEmail(passenger.getEmail());
			pass.setPhno(passenger.getPhno());
			pass.setUserName(passenger.getUserName());
			pass.setPassword(passenger.getPassword());
			
			session.beginTransaction();
			session.saveOrUpdate(pass);
			session.getTransaction().commit();
			return pass;
			
		}catch (HibernateException e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public void deletePassenger(int id) throws PersistenceException   //for delete Passenger by id
	{
		try(Session session=HibernateUtil.getSession())
		{
			Passenger passn=session.load(Passenger.class,id);
			session.beginTransaction();
			session.delete(passn);
			session.getTransaction().commit();
		}catch (HibernateException e) {
			System.out.println(e);
		}
		
	}

	@Override
	public Passenger getPassengerByEmail(String email)  //for get Passenger By Email
	{
		try(Session session=HibernateUtil.getSession())
		{
			String query="from Passenger p where p.email=:e";
			Query q=session.createQuery(query);
			q.setParameter("e", email);
			Passenger p=(Passenger)q.uniqueResult();
			return p;
		}catch (HibernateException e) {
			System.out.println(e);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	

}
