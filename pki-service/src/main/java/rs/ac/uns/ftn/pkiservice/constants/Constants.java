package rs.ac.uns.ftn.pkiservice.constants;

public class Constants {

    public static final String ADMIN_ROLE = "admin";
    public static final String OPERATOR_ROLE = "operator";


    public static enum CERT_TYPE {ROOT_CERT, INTERMEDIATE_CERT, LEAF_CERT};

    public static final Integer ROOT_CERT_DURATION = 30;  // znaci 30 godina
    public static final Integer INTERMEDIATE_CERT_DURATION = 10;  // znaci 10 godina
    public static final Integer LEAF_CERT_DURATION = 5; // znaci 5 godina
}
