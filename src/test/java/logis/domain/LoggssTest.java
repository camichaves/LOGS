package logis.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import logis.web.rest.TestUtil;

public class LoggssTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Loggss.class);
        Loggss loggss1 = new Loggss();
        loggss1.setId(1L);
        Loggss loggss2 = new Loggss();
        loggss2.setId(loggss1.getId());
        assertThat(loggss1).isEqualTo(loggss2);
        loggss2.setId(2L);
        assertThat(loggss1).isNotEqualTo(loggss2);
        loggss1.setId(null);
        assertThat(loggss1).isNotEqualTo(loggss2);
    }
}
