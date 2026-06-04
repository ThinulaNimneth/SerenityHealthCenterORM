package lk.ijse.serenityhealthcenter.bo;

import lk.ijse.serenityhealthcenter.bo.custom.impl.*;


public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory() {}

    public static BOFactory getInstance() {
        return boFactory == null ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOType {
        USER, PATIENT, PROGRAM, THERAPIST, SESSION, PAYMENT
    }

    public SuperBO getBO(BOType type) {
        switch (type) {
            case USER:      return new UserBOImpl();
            case PATIENT:   return new PatientBOImpl();
            case PROGRAM:   return new ProgramBOImpl();
            case THERAPIST: return new TherapistBOImpl();
            case SESSION:   return new SessionBOImpl();
            case PAYMENT:   return new PaymentBOImpl();
            default:        return null;
        }
    }
}
