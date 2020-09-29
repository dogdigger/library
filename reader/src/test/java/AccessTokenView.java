import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chengrui
 * <p>create at: 2020/9/24 2:23 下午</p>
 * <p>description: </p>
 */
@Data
public class AccessTokenView extends BaseView{

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;
}
