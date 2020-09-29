import lombok.Data;

import java.util.List;

/**
 * @author chengrui
 * <p>create at: 2020/9/24 2:36 下午</p>
 * <p>description: </p>
 */
@Data
public class DepartmentView extends BaseView {
    private List<Department> department;
}
