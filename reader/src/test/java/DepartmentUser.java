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

    /**
     * 帐号长度为 1-64 个字符
     */
    @JsonProperty("userid")
    private String userId;

    /**
     * 请输入不多于64个字符
     */
    private String name;

    private List<Integer> department;

    private List<Integer> order;

    private String position;

    private String mobile;

    private String gender;

    private String email;

    @JsonProperty("is_leader_in_dept")
    private List<Integer> leaderInDept;

    private String avatar;

    @JsonProperty("thumb_avatar")
    private String thumbAvatar;

    private String telephone;

    private String alias;

    private Integer status;

    private String address;
}
