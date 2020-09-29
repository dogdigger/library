import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chengrui
 * <p>create at: 2020/9/24 2:49 下午</p>
 * <p>description: </p>
 */
@Data
public class DepartmentUser {

    @JsonProperty("userid")
    private String userId;

    private String name;

    private List<Integer> department;

    private List<Integer> order;

    private String position;

    private String mobile;

    private String gender;

    private String email;

    @JsonProperty("is_leader_in_dept")
    private List<Integer> leaderInDept;
}
