import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chengrui
 * <p>create at: 2020/9/24 3:30 下午</p>
 * <p>description: </p>
 */
@Data
public class MiniProgramMessage {
    @JsonProperty("touser")
    private List<String> toUser;
}
