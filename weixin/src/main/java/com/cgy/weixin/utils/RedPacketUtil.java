package com.cgy.weixin.utils;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.cgy.weixin.db.DBManager;
import com.cgy.weixin.model.cache.UserCache;
import com.yunzhanghu.redpacketsdk.RPSendPacketCallback;
import com.yunzhanghu.redpacketsdk.RedPacket;
import com.yunzhanghu.redpacketsdk.bean.RPUserBean;
import com.yunzhanghu.redpacketsdk.bean.RedPacketInfo;
import com.yunzhanghu.redpacketsdk.constant.RPConstant;
import com.yunzhanghu.redpacketui.utils.RPRedPacketUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.UserInfo;

/**
 * Created by cgy
 * 2018/6/1  16:42
 * 云账户红包工具
 */
public class RedPicketUtil {

    public static void startRedPacket(FragmentActivity activity, UserInfo toUserInfo, RPSendPacketCallback callback) {
        if (toUserInfo == null)
            return;
        RPRedPacketUtil.getInstance().startRedPacket(activity, RPConstant.RP_ITEM_TYPE_SINGLE, genPersonalRedPacketInfo(toUserInfo.getUserId(), toUserInfo.getName(), toUserInfo.getPortraitUri().toString()), callback);
    }

    public static void startRedPacket(FragmentActivity activity, String toGroupId, int groupMemberCount, RPSendPacketCallback callback) {
        RPRedPacketUtil.getInstance().startRedPacket(activity, RPConstant.RP_ITEM_TYPE_GROUP, genGroupRedPacketInfo(toGroupId, groupMemberCount,false), callback);
    }

    /**
     *
     * @param activity          FragmentActivity(由于使用了DialogFragment，这个参数类型必须为FragmentActivity)
     * @param chatType          聊天类型
     * @param redPacketId       红包id
     * @param redPacketType     红包类型
     * @param receiverId        接收者id
     * @param messageDirect     消息的方向
     */
    public static void openRedPacket(final FragmentActivity activity, final int chatType, String redPacketId, String redPacketType, String receiverId, String messageDirect) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCanceledOnTouchOutside(false);
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        redPacketInfo.redPacketId = redPacketId;
        redPacketInfo.messageDirect = messageDirect;
        redPacketInfo.chatType = chatType;
        RPRedPacketUtil.getInstance().openRedPacket(redPacketInfo, activity, new RPRedPacketUtil.RPOpenPacketCallback() {
            @Override
            public void onSuccess(String senderId, String senderNickname, String myAmount) {
                //领取红包成功,发送回执消息到聊天窗口
                Toast.makeText(activity, "拆红包成功，红包金额" + myAmount + "元", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void showLoading() {
                progressDialog.show();
            }

            @Override
            public void hideLoading() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(String code, String message) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 生成群红包信息
     * @param toGroupId         群id
     * @param groupMemberCount  群人数
     * @param isExclusive       是否是专属红包
     * @return
     */
    private static RedPacketInfo genGroupRedPacketInfo(String toGroupId, int groupMemberCount, boolean isExclusive) {
        RedPacketInfo redPacketInfo = getCurrentUserInfo();
        redPacketInfo.toGroupId = toGroupId;
        redPacketInfo.groupMemberCount = groupMemberCount;
        //使用专属红包功能需要设置如下回调函数,不需要可不设置
        if (isExclusive) {
            RedPacket.getInstance().setRPGroupMemberListener((groupId, rpValueCallback) -> rpValueCallback.onSuccess(generateGroupMemberList(groupMemberCount)));
        } else {
            //Demo演示使用,如果不需要专属红包,不设置该回调即可
            RedPacket.getInstance().setRPGroupMemberListener(null);
        }
        return redPacketInfo;
    }

    /**
     * 生成个人红包信息
     *
     * @param toUserId
     * @param toUserName
     * @param toUserPortraitUrl
     * @return
     */
    private static RedPacketInfo genPersonalRedPacketInfo(String toUserId, String toUserName, String toUserPortraitUrl) {
        RedPacketInfo redPacketInfo = getCurrentUserInfo();
        //单聊红包、小额随机红包和转账都只传入 ：接收者Id、昵称和头像
        redPacketInfo.toUserId = toUserId;
        redPacketInfo.toNickName = toUserName;
        redPacketInfo.toAvatarUrl = toUserPortraitUrl;

        return redPacketInfo;
    }

    /**
     * 模拟获取当前用户信息的方法
     * @return
     */
    public static RedPacketInfo getCurrentUserInfo() {
        RedPacketInfo redPacketInfo = new RedPacketInfo();
        UserInfo userInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
        //红包发送者昵称 不可为空
        redPacketInfo.fromNickName = userInfo != null ? userInfo.getName() : "CGY";
        //红包发送者头像url 不可为空
        redPacketInfo.fromAvatarUrl = userInfo != null ? userInfo.getPortraitUri().toString() : "http://avatar.csdn.net/6/6/F/1_csdn_lqr.jpg";
        return redPacketInfo;
    }

    /**
     * 模拟生成群成员列表的方法
     * @param groupMemberCount 群成员数量
     * @return 群成员列表
     */
    private static List<RPUserBean> generateGroupMemberList(int groupMemberCount) {
        //专属红包不能给自己发,所以这个列表中不能包含当前用户
        List<RPUserBean> userBeanList = new ArrayList<>();
        for (int i = 0; i < groupMemberCount; i++) {
            RPUserBean userBean = new RPUserBean();
            userBean.userId = "1000" + i;
            userBean.userNickname = "yunzhanghu00" + i;
            userBeanList.add(userBean);
        }
        return userBeanList;
    }

    /**
     * 红包类型的转义方法 用于展示红包的类型
     * @param redPacketType 红包类型
     * @return 返回转义后的红包类型
     */
    public static String getRedPacketType (String redPacketType) {
        String typeStr = "";
        if (TextUtils.isEmpty(redPacketType)) {
            typeStr = "单聊红包";
        } else if (redPacketType.equals(RPConstant.GROUP_RED_PACKET_TYPE_RANDOM)) {
            typeStr = "拼手气群红包";
        } else if (redPacketType.equals(RPConstant.GROUP_RED_PACKET_TYPE_AVERAGE)) {
            typeStr = "普通群红包";
        } else if (redPacketType.equals(RPConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
            typeStr = "专属红包";
        } else if (redPacketType.equals(RPConstant.RED_PACKET_TYPE_RANDOM)) {
            typeStr = "小额随机红包";
        }
        return typeStr;
    }
}
