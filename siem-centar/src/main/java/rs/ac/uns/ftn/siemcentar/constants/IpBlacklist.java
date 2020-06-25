package rs.ac.uns.ftn.siemcentar.constants;

import java.util.Arrays;
import java.util.List;

public class IpBlacklist {

    public static List<String> BLACKLIST = Arrays.asList(
            "127.0.0.1",
            "123.123.123.123",
            "11.11.11.11",
            "22.22.22.22"
    );

    private IpBlacklist() {
    }
}
