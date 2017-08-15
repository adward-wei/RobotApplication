package com.ubtechinc.alpha2ctrlapp.entity.net;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.UserInfo;
import com.ubtechinc.nets.http.Url;

/**
 * @ClassName EditRobotNameModule
 * @date 2/8/2017
 * @author shiyi.wu
 * @Description 编辑机器人名称、头像等信息
 * @modifier
 * @modify_time
 */
public class UpdateRobotInfoModule {
    @Url("alpha2-web/user/editRobot")
    public class Request {
        private String userName;
        private String userOtherName;
        private String userImage;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserOtherName() {
            return userOtherName;
        }

        public void setUserOtherName(String userOtherName) {
            this.userOtherName = userOtherName;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }
    }

    public class Response extends BaseResponse {

        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public class Data {
        private RobotInfo result;

        public RobotInfo getResult() {
            return result;
        }

        public void setResult(RobotInfo result) {
            this.result = result;
        }
    }
}
