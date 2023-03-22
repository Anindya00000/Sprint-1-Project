package com.ars.daoImpl;

import java.time.LocalDate;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.ars.config.HibernateUtil;
import com.ars.dao.TicketDAO;
import com.ars.entity.Airline;
import com.ars.entity.Flight;
import com.ars.entity.Passenger;
import com.ars.entity.TicketBooking;

public class TicketDAOImpl implements TicketDAO{

	// for book a flight 
	@Override
	public TicketBooking bookFlight(Airline airline, Passenger p, LocalDate date, Flight f, int no_of_passenger,
			float total_fare, int avilable_seat) {
		try(Session session=HibernateUtil.getSession())
		{
			session.beginTransaction();
			TicketBooking ticketBooking=new TicketBooking();
			ticketBooking.setAid(airline);
			ticketBooking.setDate(date);
			ticketBooking.setFid(f);
			ticketBooking.setNo_of_passenger(no_of_passenger);
			ticketBooking.setPid(p);
			ticketBooking.setTotal_fare(total_fare);
			session.save(ticketBooking);
			f.setAvilableSeats(avilable_seat);
			session.saveOrUpdate(f);
			session.getTransaction().commit();
			
			return ticketBooking;
			
		}catch (HibernateException e) {
			System.out.println("Hibernate Exception: "+e);
		}catch (Exception e) {
			System.out.println("Exception: "+e);
		}
		return null;
	}

	// for cancel a flight booking 
	@Override
	public void cancelBooking(int id){
		try(Session session=HibernateUtil.getSession()) {
			TicketBooking tb=(TicketBooking)session.load(TicketBooking.class, id);
			session.beginTransaction();
			int input=JOptionPane.showConfirmDialog(null, "do you want to cancel?","select what you want to cancel or not",JOptionPane.YES_NO_OPTION);
			if(input==0)
			{
				int nop=tb.getNo_of_passenger();
				tb.getFid().setAvilableSeats(tb.getFid().getAvilableSeats()+nop);
				session.delete(tb);
				
			}
			else 
				JOptionPane.showMessageDialog(null,"user wants to retain it!!!");
				session.getTransaction().commit();
		}catch (HibernateException e) {
			System.out.println("hibernate exception: "+e);
		}
		
	}

	//for get ticket by id
	@Override
	public TicketBooking getTicket(int id) {
		try(Session session=HibernateUtil.getSession()) {
			TicketBooking tick=(TicketBooking)session.get(TicketBooking.class,id );
			return tick;
		}catch (HibernateException e) {
			System.out.println("Hibernate Exception: "+e);
		}catch (Exception e) {
			System.out.println("Exception: "+e);
		}
		return null;
	
	}
}