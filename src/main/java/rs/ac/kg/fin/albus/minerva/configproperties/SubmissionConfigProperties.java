package rs.ac.kg.fin.albus.minerva.configproperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("submissions")
//@ConstructorBinding
public class SubmissionConfigProperties {

    private final int submissionsLimitPerAssignment;
}
