package rs.ac.uns.ftn.siemcentar.dto.response;

public class TokenDTO {

    private String access_token;
    private String refresh_token;
    private Long expires_in;
    private Long refresh_expires_in;

    public TokenDTO() {
    }

    public String getAccesss_token() {
        return access_token;
    }

    public void setAccess_token(String accesss_token) {
        this.access_token = accesss_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public Long getRefresh_expires_in() {
        return refresh_expires_in;
    }

    public void setRefresh_expires_in(Long refresh_expires_in) {
        this.refresh_expires_in = refresh_expires_in;
    }
}
