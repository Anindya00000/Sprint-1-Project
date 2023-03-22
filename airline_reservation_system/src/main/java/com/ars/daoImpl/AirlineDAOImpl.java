package com.ars.daoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.ars.config.HibernateUtil;
import com.ars.dao.AirlineDAO;
import com.ars.entity.Airline;
import com.ars.entity.Flight;

public class AirlineDAOImpl implements AirlineDAO{

	@Override
	public void saveAirline(Airline airline)  // for save Airline
	{
		try(Session session=HibernateUtil.getSession())
		{
			session.beginTransaction();
			session.save(airline);
			session.getTransaction().commit();
			session.clear();
		}catch (HibernateException e) {
			System.out.println("hibernate exception: "+e);
		}catch (Exception e) {
			System.out.println("exception: "+e);
		}
		
	}

	@Override
	public void assignAirlineToFlight(int flightId, int airId) //foe assign Airline To Flight
	{
		try(Session session=HibernateUtil.getSession())
		{
			Flight flight=(Flight)session.get(Flight.class, flightId);
			Airline airline=(Airline)session.load(Airline.class, airId);
			List<Flight> flights=new ArrayList<>();
			flights.add(flight);
			airline.setFlights(flights);
			flight.setAirline(airline);
			session.beginTransaction();
			session.saveOrUpdate(airline);
			session.getTransaction().commit();
		}catch (HibernateException e) {
			System.out.println("hibernate exception: "+e);
		}catch (Exception e) {
			System.out.println(" exception: "+e);
		}
		
	}

	@Override
	public Airline getAirlineByName(String name)  //for get Airline By Name
	{
		try(Session session=HibernateUtil.getSession())
		{
			String query="from Airline a where a.airlineName=:an";
			Query q=session.createQuery(query);
			q.setParameter("an", name);
			Airline airline=(Airline)q.uniqueResult();
			return airline;
		}catch (HibernateException e) {
			System.out.println("hibernate exception: "+e);
		}catch (Exception e) {
			System.out.println(" exception: "+e);
		}
		return null;
	}

	@Override
	public Airline updateAirlineById(int id, Airline airline) //for update Airline By Id
	{
		try(Session session=HibernateUtil.getSession())
		{
			Airline air=(Airline)session.load(Airline.class, id);
			air.setAirlineName(airline.getAirlineName());
			air.setFare(airline.getFare());
			session.beginTransaction();
			session.saveOrUpdate(air);
			session.getTransaction().commit();
			return air;
		}catch (HibernateException e) {
			System.out.println("hibernate exception: "+e);
		}catch (Exception e) {
			System.out.println(" exception: "+e);
		}
		return null;
	}

	@Override
	public void deleteAirline(int id) throws PersistenceException  //for delete Airline by id
	{
		try(Session session=HibernateUtil.getSession())
		{
			Airline airline=session.load(Airline.class, id);
			session.beginTransaction();
			int input=JOptionPane.showConfirmDialog(null,"do you want to delete?","select what you want to delete or not",JOptionPane.YES_NO_OPTION);
			if(input==0)
			{
				session.delete(airline);
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
	

}