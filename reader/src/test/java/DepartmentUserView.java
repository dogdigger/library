import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chengrui
 * <p>create at: 2020/9/24 2:55 下午</p>
 * <p>description: </p>
 */
@Data
public class DepartmentUserView extends BaseView {

    @JsonProperty("userlist")
    private List<DepartmentUser> userList;
}
