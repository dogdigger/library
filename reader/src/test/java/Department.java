import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chengrui
 * <p>create at: 2020/9/24 2:34 下午</p>
 * <p>description: </p>
 */
@Data
public class Department {
    private Integer id;

    private String name;

    @JsonProperty("name_en")
    private String  englishName;

    @JsonProperty("parentid")
    private Integer parentId;

    private Integer order;
}
