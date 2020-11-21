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
         * DepartmentUser(userId=ChengRui, name=程锐, department=[1, 5], order=[0, 0], position=, mobile=15926643146, gender=1, email=, leaderInDept=[0, 0], avatar=http://wework.qpic.cn/bizmail/XQ8xC60tr9cibkmEoEG3KDJasyKsJmJEiarvB2Dq8eYCGOxHv6yQnaQA/0, thumbAvatar=http://wework.qpic.cn/bizmail/XQ8xC60tr9cibkmEoEG3KDJasyKsJmJEiarvB2Dq8eYCGOxHv6yQnaQA/100, telephone=, alias=, status=1, address=null)
         * DepartmentUser(userId=XiaoCong, name=小葱, department=[1, 3], order=[0, 0], position=, mobile=, gender=0, email=, leaderInDept=[0, 0], avatar=http://wework.qpic.cn/bizmail/6qricOZcNDDB2JExkzEh3n9MB8ylr6xdDI7NREnxA1Foj8cbatIVzjg/0, thumbAvatar=http://wework.qpic.cn/bizmail/6qricOZcNDDB2JExkzEh3n9MB8ylr6xdDI7NREnxA1Foj8cbatIVzjg/100, telephone=, alias=, status=4, address=null)
         * DepartmentUser(userId=hq, name=黄卓权, department=[1, 2], order=[0, 0], position=, mobile=13430316560, gender=1, email=, leaderInDept=[0, 0], avatar=https://wework.qpic.cn/bizmail/TPwTkMVs6tzZow4kUh0KMLt2uD6eokDb0brlicSv54LAawhAibur7ic7g/0, thumbAvatar=https://wework.qpic.cn/bizmail/TPwTkMVs6tzZow4kUh0KMLt2uD6eokDb0brlicSv54LAawhAibur7ic7g/100, telephone=, alias=, status=1, address=null)
         * DepartmentUser(userId=zhangsanfeng, name=张三丰, department=[1], order=[0], position=, mobile=12345678912, gender=1, email=, leaderInDept=[0], avatar=, thumbAvatar=, telephone=, alias=, status=4, address=)
         */
        List<Department> departmentList = getDepartList();
        for (Department depart : departmentList) {
//            for(DepartmentUser user : getDepartEmployee(depart.getId())) {
//                System.out.println(user);
//            }
            System.out.println(depart);
        }
    }
}
