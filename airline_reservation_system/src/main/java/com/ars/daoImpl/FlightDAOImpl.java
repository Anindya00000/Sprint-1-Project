package com.ars.daoImpl;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.ars.config.HibernateUtil;
import com.ars.dao.FlightDAO;
import com.ars.entity.Airline;
import com.ars.entity.Flight;

public class FlightDAOImpl implements FlightDAO{

	@Override
	public void saveFlight(Flight flight)  //for save Flight
    {
		try(Session session=HibernateUtil.getSession())
		{
			session.beginTransaction();
			session.save(flight);
			session.getTransaction().commit();
			session.clear();
		}catch (HibernateException e) {
			System.out.println("hibernate exception: "+e);
		}catch (Exception e) {
			System.out.println("exception: "+e);
		}
		
	}

	@Override
	public Flight updateFlight(int id,Flight flight) // for update flight details
	{
		try(Session session=HibernateUtil.getSession())
		{
			Flight fl=(Flight)session.load(Flight.class, id);
			fl.setAvilableSeats(flight.getAvilableSeats());
			fl.setDate(flight.getDate());
			fl.setDestination(flight.getDestination());
			fl.setSource(flight.getSource());
			fl.setTime(flight.getTime());
			fl.setTotalSeats(flight.getTotalSeats());
			fl.setTravellerClass(flight.getTravellerClass());
			fl.setAirline(flight.getAirline());
			session.beginTransaction();
			session.saveOrUpdate(fl);
			session.getTransaction().commit();
			return fl;
		}catch (HibernateException e) {
			System.out.println("hibernate exception: "+e);
		}catch (Exception e) {
			System.out.println("exception: "+e);
		}
		return null;
	}

	@Override
	public Flight getFlight(int id)  // for get flight by id
	{
		try(Session session=HibernateUtil.getSession())
		{
		Flight flight=(Flight)session.get(Flight.class, id);
		return flight;
		}catch (HibernateException e) {
			System.out.println("hibernate exception: "+e);
		}catch (Exception e) {
			System.out.println("exception: "+e);
		}
		return null;
	}

	@Override
	public void deleteFlight(int id) throws PersistenceException // for delete flight by id
	{
		try(Session session=HibernateUtil.getSession())
		{
			Flight fl=session.load(Flight.class, id);
			session.beginTransaction();
			int input=JOptionPane.showConfirmDialog(null,"do you want to delete?","select what you want to delete or not",JOptionPane.YES_NO_OPTION);
			if(input==0)
			{
				session.delete(fl);
			}
			else {
				JOptionPane.showMessageDialog(null, "wants to retain it!!!");
			}
			session.getTransaction().commit();
		}catch (HibernateException e) {
			System.out.println("hibernate exception: "+e);
		}catch (PersistenceException e) {
			throw new PersistenceException("can not delete airline bacause data is booked");
		}
	}

	@Override
	public List<Flight> checkFlight(String from, String to, LocalDate date)  // for check flight from source to destination with date
	{
		try(Session session=HibernateUtil.getSession())
		{
			String q="from Flight as f where f.source=:s and f.destination=:d and f.date=:da";
		Query query=session.createQuery(q);
		query.setParameter("s", from);
		query.setParameter("d", to);
		query.setParameter("da", date);
		List<Flight> flights=query.list();
		return flights;
		}catch (HibernateException e) {
			System.out.println("hibernate exception: "+e);
		}catch (PersistenceException e) {
			throw new PersistenceException("can not delete airline bacause data is booked");
		}
		return null;
	}
	

}
