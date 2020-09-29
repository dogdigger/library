import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chengrui
 * <p>create at: 2020/9/24 2:36 下午</p>
 * <p>description: </p>
 */
@Data
public class BaseView {
    @JsonProperty("errcode")
    private Integer errCode;

    @JsonProperty("errmsg")
    private String errMsg;
}
