package com.elias.model.view;

import com.elias.entity.ClientUser;
import com.elias.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2021/1/8 5:14 下午</p>
 * <p>description: </p>
 */
@Data
public class UserInfoView {
    private UUID userId;
    private String name;
    private Integer gender;
    private String avatar;
    private String mobile;
    private String email;

    public static UserInfoView create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("参数user为空");
        }
        UserInfoView view = new UserInfoView();
        BeanUtils.copyProperties(user, view);
        view.setUserId(user.getId());
        return view;
    }

    public static UserInfoView create(User user, ClientUser clientUser) {
        UserInfoView view = create(user);
        if (clientUser != null) {
            view.setAvatar(clientUser.getAvatar());
            view.setName(clientUser.getDisplayName());
        }
        return view;
    }
}
