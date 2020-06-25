package rs.ac.uns.ftn.pkiservice.constants;

public class Constants {

    public static final String ADMIN_ROLE = "admin";
    public static final String OPERATOR_ROLE = "operator";
    public static final String AGENT_ROLE = "agent";

    public static enum CERT_TYPE {ROOT_CERT, INTERMEDIATE_CERT, LEAF_CERT, SERVER_CERT};

    public static final Integer ROOT_CERT_DURATION = 10;  // znaci 30 godina
    public static final Integer INTERMEDIATE_CERT_DURATION = 5;  // znaci 10 godina
    public static final Integer SERVER_CERT_DURATION = 2;
    public static final Integer LEAF_CERT_DURATION = 1; // znaci 5 godina

    public static String ROOT_ALIAS = "1";
    public static String PKI_ALIAS = "2";
    public static String PKI_COMMUNICATION_ALIAS = "3";
    public static String ANGULAR_ALIAS = "4";
    public static String KEY_CLOAK_ALIAS = "5";

    public static String GENERATED_CERT_DIRECTORY = "src/main/resources/generatedCerts";

}
