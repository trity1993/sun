package cc.trity.sun.mockdata;

import cc.trity.library.net.Response;
import cc.trity.library.utils.GsonUtils;
import cc.trity.sun.model.UserInfo;

/**
 * Created by TryIT on 2016/2/23.
 */
public class MockLoginSuccessInfo extends MockService {
    @Override
    public String getJsonData() {
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginName("trity");
        userInfo.setUserName("小强");
        userInfo.setScore(100);

        Response response = getSuccessResponse();
        response.setResult(GsonUtils.createGsonString(userInfo));
        return GsonUtils.createGsonString(response);
    }
}