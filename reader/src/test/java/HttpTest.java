import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author chengrui
 * <p>create at: 2020/9/24 11:57 上午</p>
 * <p>description: </p>
 */
public class HttpTest {
    private static final String corpId = "ww7811c0de099e73b7";
    private static final String secret = "QwkHB6c9iyQX1SYoOW4alY8MTGbIFxMAurIENxL3ygU";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static String getAccessToken() {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
        ResponseEntity<AccessTokenView> responseEntity = restTemplate.exchange(String.format(url, corpId, secret), HttpMethod.GET, null, AccessTokenView.class);
        if (responseEntity.getBody() == null) {
            throw new RuntimeException("response body is null");
        }
        if (responseEntity.getBody().getErrCode() != 0) {
            throw new RuntimeException("request failed: " + responseEntity.getBody().getErrMsg());
        }
        return responseEntity.getBody().getAccessToken();
    }

    public static List<Department> getDepartList() {
        String url = String.format("https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=%s", getAccessToken());
        ResponseEntity<DepartmentView> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, DepartmentView.class);
        if (responseEntity.getBody() == null) {
            throw new RuntimeException("response body is null");
        }
        if (responseEntity.getBody().getErrCode() != 0) {
            throw new RuntimeException("request failed: " + responseEntity.getBody().getErrMsg());
        }
        return responseEntity.getBody().getDepartment();
    }

    public static List<DepartmentUser> getDepartEmployee(Integer departId) {
        String url = String.format("https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=%s&department_id=%s&fetch_child=1", getAccessToken(), departId);
        ResponseEntity<DepartmentUserView> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, DepartmentUserView.class);
        if (responseEntity.getBody() == null) {
            throw new RuntimeException("response body is null");
        }
        if (responseEntity.getBody().getErrCode() != 0) {
            throw new RuntimeException("request failed: " + responseEntity.getBody().getErrMsg());
        }
        return responseEntity.getBody().getUserList();
    }

    public static void main(String[] args) {
        /**
         * DepartmentUser(userId=ChengRui, name=程锐, department=[5, 1], order=[0, 0], position=, mobile=15926643146, gender=1, email=, leaderInDept=[0, 0])
         * DepartmentUser(userId=XiaoCong, name=小葱, department=[3, 1], order=[0, 0], position=, mobile=, gender=0, email=, leaderInDept=[0, 0])
         * DepartmentUser(userId=hq, name=黄卓权, department=[2, 1], order=[0, 0], position=, mobile=13430316560, gender=1, email=, leaderInDept=[0, 0])
         */
        List<Department> departmentList = getDepartList();
        for (Department depart : departmentList) {
            for(DepartmentUser user : getDepartEmployee(depart.getId())) {
                System.out.println(user);
            }
        }
    }
}
