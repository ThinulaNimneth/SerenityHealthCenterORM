package lk.ijse.serenityhealthcenter.dao.impl;

import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.TherapySessionDAO;
import lk.ijse.serenityhealthcenter.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TherapySessionDAOImpl implements TherapySessionDAO {

    @Override
    public Long save(TherapySession session){
        Session hibernateSession = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = hibernateSession.beginTransaction();
        try {
            Long id = (Long) hibernateSession.save(session);
            transaction.commit();
            return id;
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            hibernateSession.close();
        }
    }


    @Override
    public void update(TherapySession session){

    }

}
