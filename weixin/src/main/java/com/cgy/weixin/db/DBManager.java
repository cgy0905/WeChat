package com.cgy.weixin.db;

import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;

import com.cgy.weixin.api.ApiRetrofit;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.db.model.Friend;
import com.cgy.weixin.db.model.GroupMember;
import com.cgy.weixin.db.model.Groups;
import com.cgy.weixin.manager.BroadcastManager;
import com.cgy.weixin.model.cache.UserCache;
import com.cgy.weixin.model.response.GetGroupInfoResponse;
import com.cgy.weixin.model.response.GetGroupMemberResponse;
import com.cgy.weixin.model.response.GetGroupResponse;
import com.cgy.weixin.model.response.UserRelationshipResponse;
import com.cgy.weixin.utils.LogUtils;
import com.cgy.weixin.utils.NetUtils;
import com.cgy.weixin.utils.PinyinUtils;
import com.cgy.weixin.utils.RongGenerate;
import com.cgy.weixin.utils.UIUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import io.rong.imlib.model.UserInfo;
import rx.schedulers.Schedulers;

/**
 * Created by cgy
 * 2018/5/30  14:52
 * 数据库管理器
 */
public class DBManager {
    private static DBManager mInstance;
    private boolean mHasFetchedFriends = false;
    private boolean mHasFetchedGroups = false;
    private boolean mHasFetchedGroupMembers = false;
    private LinkedHashMap<String, UserInfo>  mUserInfoCache;
    private List<Groups> mGroupsList;

    public DBManager () {
        mUserInfoCache = new LinkedHashMap<>();
    }

    public static DBManager getInstance() {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 登录时同步好友、群组、群组成员、黑名单数据
     */
    public void getAllUserInfo() {
       if (!NetUtils.isNetworkAvailable(UIUtils.getContext())) {
           return;
       }
       fetchFriends();
       fetchGroups();
    }


    /**
     * 同步好友信息
     */
    private void fetchFriends() {
        mHasFetchedFriends = false;
        ApiRetrofit.getInstance().getAllUserRelationship()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(userRelationshipResponse -> {
                    if (userRelationshipResponse != null && userRelationshipResponse.getCode() == 200) {
                        List<UserRelationshipResponse.ResultEntity> list = userRelationshipResponse.getResult();
                        if (list != null && list.size() > 0) {
                            deleteFriends();
                            saveFriends(list);
                        }
                        mHasFetchedFriends = true;
                        checkFetchComplete();
                    } else {
                        mHasFetchedFriends = true;
                        checkFetchComplete();
                    }
                }, this::fetchFriendError);
    }

    /**
     * 同步群组消息
     */
    private void fetchGroups() {
        mHasFetchedGroups = false;
        ApiRetrofit.getInstance().getGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(getGroupResponse -> {
                    if (getGroupResponse != null && getGroupResponse.getCode() == 200) {
                        List<GetGroupResponse.ResultEntity> list = getGroupResponse.getResult();
                        if (list != null && list.size() > 0){
                            deleteGroups();
                            saveGroups(list);
                            //同步群组成员信息
                            fetchGroupMembers();
                        } else {
                            mHasFetchedGroupMembers = true;
                        }
                        mHasFetchedGroups = true;
                        checkFetchComplete();
                    } else {
                        mHasFetchedGroups = true;
                        mHasFetchedGroupMembers = true;
                        checkFetchComplete();
                    }

                },this::fetchGroupsError);
    }


    /**
     * 同步群组成员信息
     */
    private void fetchGroupMembers() {
        mHasFetchedGroupMembers = false;
        rx.Observable.from(getGroups())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(groups ->
                        ApiRetrofit.getInstance().getGroupMember(groups.getGroupId())
                            .subscribe(getGroupMemberResponse -> {
                                if (getGroupMemberResponse != null && getGroupMemberResponse.getCode() == 200) {
                                    List<GetGroupMemberResponse.ResultEntity> list = getGroupMemberResponse.getResult();
                                    if (list != null && list.size() > 0) {
                                        deleteGroupMembersByGroupId(groups.getGroupId());
                                        saveGroupMembers(list, groups.getGroupId());
                                    }
                                    mHasFetchedGroupMembers = true;
                                    checkFetchComplete();
                                } else {
                                    mHasFetchedGroupMembers = true;
                                    checkFetchComplete();
                                }
                        }, this::fetchGroupMembersError));


    }

    private void fetchFriendError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
        mHasFetchedFriends = true;
        checkFetchComplete();
    }
    private void fetchGroupsError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
        mHasFetchedGroups = true;
        mHasFetchedGroupMembers = true;
        checkFetchComplete();
    }

    private void fetchGroupMembersError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
        mHasFetchedGroupMembers = true;
        checkFetchComplete();
    }

    private void checkFetchComplete() {
        if (mHasFetchedFriends && mHasFetchedGroups && mHasFetchedGroupMembers) {
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.FETCH_COMPLETE);
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_FRIEND);
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_GROUP);
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
        }

    }
    public void getGroups(String groupId) {
        if (!mHasFetchedGroups) {
            fetchGroups();
        } else {
            ApiRetrofit.getInstance().getGroupInfo(groupId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe( getGroupInfoResponse -> {
                        if (getGroupInfoResponse != null && getGroupInfoResponse.getCode() == 200) {
                            GetGroupInfoResponse.ResultEntity groupInfo = getGroupInfoResponse.getResult();
                            if (groupInfo != null) {
                                String role = groupInfo.getCreatorId().equalsIgnoreCase(UserCache.getId())  ? "0" : "1";
                                saveOrUpdateGroup(new Groups(groupId, groupInfo.getName(), groupInfo.getPortraitUri(), role));
                            }
                        }
                    },this::loadError);
        }
    }
    public void getGroupMember(String groupId) {
        if (!mHasFetchedGroupMembers) {
            deleteGroupMembers();
            mGroupsList = getGroups();
            fetchGroupMembers();

        } else {
            ApiRetrofit.getInstance().getGroupMember(groupId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(getGroupMemberResponse -> {
                        if (getGroupMemberResponse != null && getGroupMemberResponse.getCode() == 200) {
                            List<GetGroupMemberResponse.ResultEntity> list = getGroupMemberResponse.getResult();
                            if (list != null && list.size() > 0) {
                                deleteGroupMembersByGroupId(groupId);
                                saveGroupMembers(list, groupId);
                                BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_GROUP_MEMBER, groupId);
                                BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                            }
                        }
                    },this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
    }

    /**
     * 查询本地用户信息
     * 1、查缓存
     * 2、查Friend表
     * 3、查GroupMember表
     * @param userId
     * @return
     */
    public UserInfo getUserInfo(String userId) {
        if (TextUtils.isEmpty(userId))
            return null;
        if (mUserInfoCache != null) {
            UserInfo userInfo = mUserInfoCache.get(userId);
            if (userInfo != null) {
                return userInfo;
            }
        }

        UserInfo userInfo;
        Friend friend = getFriendById(userId);
        if (friend != null) {
            String name = friend.getName();
            if (friend.isExitsDisplayName()) {
                name = friend.getDisplayName();
            }
            userInfo = new UserInfo(friend.getUserId(), name, Uri.parse(friend.getPortraitUri()));
            return userInfo;
        }

        List<GroupMember> groupMembers = getGroupMembersWithUserId(userId);
        if (groupMembers != null && groupMembers.size() > 0) {
            GroupMember groupMember = groupMembers.get(0);
            userInfo = new UserInfo(groupMember.getUserId(), groupMember.getName(), Uri.parse(groupMember.getPortraitUri()));
            return userInfo;
        }
       return null;
    }

    /**
     * 清除所有用户数据
     */
    public void deleteAllUserInfo() {
//        deleteFriends();
//        deleteGroups();
//        deleteGroupMembers();

        DataSupport.deleteAll(Friend.class);
        DataSupport.deleteAll(GroupMember.class);
        DataSupport.deleteAll(Groups.class);
        //TODO:删除黑名单数据
    }

    public boolean isMyFriend(String userId) {
        Friend friend = getFriendById(userId);
        if (friend != null) {
            return true;
        }
        return false;
    }

    public boolean isMe(String userId) {
        if (UserCache.getId().equalsIgnoreCase(userId)) {
            return true;
        }
        return false;
    }

    public boolean isInThisGroup(String groupId) {
        Groups groups = getGroupsById(groupId);
        return groups != null;
    }

    /*==============Friend begin=================*/

    public synchronized void  saveOrUpdateFriend(Friend friend) {
        if (friend != null) {
            String portrait = friend.getPortraitUri();
            if (TextUtils.isEmpty(portrait)) {
                portrait = RongGenerate.generateDefaultAvatar(friend.getName(), friend.getUserId());
                friend.setPortraitUri(portrait);
            }
            friend.saveOrUpdate("userId = ?", friend.getUserId());
            //更新过本地好友数据后,清空内存中对应用户信息缓存
            if (mUserInfoCache != null && mUserInfoCache.containsKey(friend.getUserId())) {
                mUserInfoCache.remove(friend.getUserId());
            }

        }
    }
    public synchronized void deleteFriend(Friend friend) {
        DataSupport.deleteAll(Friend.class, "userid = ?", friend.getUserId());
    }
    public synchronized Friend getFriendById(String userid) {
        if (!TextUtils.isEmpty(userid)) {
            List<Friend> friends = DataSupport.where("userid = ?", userid).find(Friend.class);
            if (friends != null && friends.size() > 0) {
                return friends.get(0);
            }
        }
        return null;
    }

    public synchronized  List<Friend> getFriends() {
        return DataSupport.where("userid != ?", UserCache.getId()).find(Friend.class);
    }

    public void saveFriends(List<UserRelationshipResponse.ResultEntity> list) {
        List<Friend> friends = new ArrayList<>();
        for (UserRelationshipResponse.ResultEntity entity : list) {
            if (entity.getStatus() == 20) { //已经是好友
                Friend friend = new Friend(
                        entity.getUser().getId(),
                        entity.getUser().getNickname(),
                        entity.getUser().getPortraitUri(),
                        TextUtils.isEmpty(entity.getDisplayName()) ? entity.getUser().getNickname() : entity.getDisplayName(),
                        null, null, null, null,
                        PinyinUtils.getPinyin(entity.getUser().getNickname()),
                        PinyinUtils.getPinyin(TextUtils.isEmpty(entity.getDisplayName()) ? entity.getUser().getNickname() : entity.getDisplayName())

                );
                if (TextUtils.isEmpty(friend.getPortraitUri())) {
                    friend.setPortraitUri(getPortrait(friend));
                }
                friends.add(friend);

            }
        }
        if (friends != null && friends.size() > 0) {
            DataSupport.saveAll(friends);
        }
    }

    public synchronized void deleteFriends() {
        List<Friend> friends = getFriends();
        for (Friend friend : friends) {
            friend.delete();
        }
    }

    public synchronized void deleteFriendById(String friendId) {
        DataSupport.deleteAll(Friend.class, "userid = ?", friendId);
    }

    /*==================Friend end ==================*/

    /*================== Groups start ==================*/

    public void saveOrUpdateGroup(Groups groups) {
        if (groups != null) {
            String portrait = groups.getPortraitUri();
            if (TextUtils.isEmpty(portrait)) {
                portrait = RongGenerate.generateDefaultAvatar(groups.getName(), groups.getGroupId());
                groups.setPortraitUri(portrait);
            }
            groups.saveOrUpdate("groupid = ?", groups.getGroupId());
        }
    }

    public synchronized void deleteGroup(Groups groups) {
        DataSupport.deleteAll(Groups.class, "groupid = ?", groups.getGroupId());
    }

    public synchronized Groups getGroupsById(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            List<Groups> groupsList = DataSupport.where("groupid = ?", groupId).find(Groups.class);
            if (groupsList != null && groupsList.size() > 0) {
                return groupsList.get(0);
            }
        }
        return null;
    }

    public synchronized List<Groups> getGroups() {
        return DataSupport.findAll(Groups.class);
    }

    public synchronized void saveGroups(List<GetGroupResponse.ResultEntity> list) {
        if (list != null && list.size() > 0) {
            mGroupsList = new ArrayList<>();
            for (GetGroupResponse.ResultEntity groups: list) {
                String portrait = groups.getGroup().getPortraitUri();
                if (TextUtils.isEmpty(portrait)) {
                    portrait = RongGenerate.generateDefaultAvatar(groups.getGroup().getName(), groups.getGroup().getId());
                }
                mGroupsList.add(new Groups(groups.getGroup().getId(), groups.getGroup().getName(), portrait, String.valueOf(groups.getRole())));
            }
        }
        if (mGroupsList.size() > 0) {
            DataSupport.saveAll(mGroupsList);
        }
    }
    public synchronized void deleteGroups() {
        DataSupport.deleteAll(Groups.class);
    }

    public synchronized void deleteGroupsById(String groupId) {
        DataSupport.deleteAll(Groups.class, "groupid = ?", groupId);
    }

    /*================== Groups end ==================*/


    /*================== GroupMember start ==================*/
    public synchronized void saveOrUpdateGroupMember(GroupMember groupMember) {
        if (groupMember != null) {
            String portrait = groupMember.getPortraitUri();
            if (TextUtils.isEmpty(portrait)) {
                portrait = RongGenerate.generateDefaultAvatar(groupMember.getName(), groupMember.getUserId());
                groupMember.setPortraitUri(portrait);
            }
            groupMember.saveOrUpdate("groupid = ? and userid = ?", groupMember.getGroupId(), groupMember.getUserId());
        }
    }

    public synchronized void  updateGroupMemberPortraitUri(String userId, String portraitUri) {
        if (TextUtils.isEmpty(portraitUri))
            return;
        ContentValues values = new ContentValues();
        values.put("portraituri", portraitUri);
        DataSupport.updateAll(GroupMember.class, values, "userid = ?", userId);
    }

    public synchronized List<GroupMember> getGroupMembers(String groupId) {
        return DataSupport.where("groupid = ?", groupId).find(GroupMember.class);
    }

    public synchronized List<GroupMember> getGroupMembersWithUserId(String userId) {
        if (TextUtils.isEmpty(userId))
            return null;

        return DataSupport.where("userid = ?", userId).find(GroupMember.class);
    }

    public synchronized void saveGroupMembers(List<GetGroupMemberResponse.ResultEntity> list, String groupId) {
        if (list != null && list.size() > 0) {
            List<GroupMember> groupMembers = setCreatedToTop(list, groupId);
            for (GroupMember groupMember: groupMembers) {

                if (groupMember != null && TextUtils.isEmpty(groupMember.getPortraitUri())) {
                    String portrait = getPortrait(groupMember);
                    groupMember.setPortraitUri(portrait);
                }
            }
            if (groupMembers.size() > 0) {
                for (GroupMember groupMember: groupMembers) {
                    saveOrUpdateGroupMember(groupMember);
                }
            }
        }
    }

    public synchronized void updateGroupsName(String groupId, String groupName) {
        Groups groups = getGroupsById(groupId);
        if (groups != null) {
            groups.setName(groupName);
            saveOrUpdateGroup(groups);
        }
    }

    public synchronized void deleteGroupMembers() {
        DataSupport.deleteAll(GroupMember.class);
    }
    public synchronized void deleteGroupMembers(String groupId, List<String> kickedUserIds) {
        if (kickedUserIds != null && kickedUserIds.size() > 0) {
            for (String userId: kickedUserIds) {
                DataSupport.deleteAll(GroupMember.class, "groupid = ? and userid = ?", groupId, userId);
            }
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_GROUP_MEMBER, groupId);
            BroadcastManager.getInstance(UIUtils.getContext()).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
        }
    }


    public synchronized void deleteGroupMembersByGroupId(String groupId) {
        DataSupport.deleteAll(GroupMember.class, "groupid = ?", groupId);
    }

    private synchronized List<GroupMember> setCreatedToTop(List<GetGroupMemberResponse.ResultEntity> groupMember, String groupId) {
        List<GroupMember> newList = new ArrayList<>();
        GroupMember created = null;
        for (GetGroupMemberResponse.ResultEntity group: groupMember) {
            String groupName = null;
            String groupPortraitUri = null;
            Groups groups = getGroupsById(groupId);
            if (groups != null) {
                groupName = groups.getName();
                groupPortraitUri = groups.getPortraitUri();
            }
            GroupMember newMember = new GroupMember(groupId,
                    group.getUser().getId(),
                    group.getUser().getNickname(),
                    group.getUser().getPortraitUri(),
                    group.getDisplayName(),
                    PinyinUtils.getPinyin(group.getUser().getNickname()),
                    PinyinUtils.getPinyin(group.getDisplayName()),
                    groupName,
                    PinyinUtils.getPinyin(groupName),
                    groupPortraitUri);
            if (group.getRole() == 0) {
                created = newMember;
            } else {
                newList.add(newMember);
            }
        }
        if (created != null) {
            newList.add(created);
        }
        Collections.reverse(newList);
        return newList;
    }

    /*================== GroupMember end ==================*/

    /**
     * app中获取用户头像的接口
     * 这个方法不涉及读数据库,头像空时直接生成默认头像
     * @param userInfo
     * @return
     */
    public String getPortraitUri (UserInfo userInfo) {
        if (userInfo != null) {
            if (userInfo.getPortraitUri() != null) {
                if (TextUtils.isEmpty(userInfo.getPortraitUri().toString())) {
                    if (userInfo.getName() != null) {
                        return RongGenerate.generateDefaultAvatar(userInfo);
                    } else {
                        return null;
                    }
                } else {
                    return userInfo.getPortraitUri().toString();
                }
            } else {
                if (userInfo.getName() != null) {
                    return RongGenerate.generateDefaultAvatar(userInfo);
                } else {
                    return null;
                }
            }
        }
        return null;
    }
    public String getPortraitUri(String name, String userId) {
        return RongGenerate.generateDefaultAvatar(name, userId);
    }

    /**
     * 获取用户头像,头像为空时生成默认的头像,此默认头像可能已经存在数据库中,不重新生成
     * 先从缓存读,再从数据库读
     * @param friend
     * @return
     */
    private String getPortrait(Friend friend) {
        if (friend != null) {
            if (TextUtils.isEmpty(friend.getPortraitUri().toString())) {
                if (TextUtils.isEmpty(friend.getUserId())) {
                    return null;
                } else {
                    UserInfo userInfo = mUserInfoCache.get(friend.getUserId());
                    if (userInfo != null) {
                        if (!TextUtils.isEmpty(userInfo.getPortraitUri().toString())) {
                            return userInfo.getPortraitUri().toString();
                        } else {
                            mUserInfoCache.remove(friend.getUserId());
                        }
                    }
                    String portrait = RongGenerate.generateDefaultAvatar(friend.getName(), friend.getUserId());
                    //缓存信息kit会使用,备注名存在时需要缓存displayName
                    String name = friend.getName();
                    if (friend.isExitsDisplayName()) {
                        name = friend.getDisplayName();
                    }
                    userInfo = new UserInfo(friend.getUserId(), name, Uri.parse(portrait));
                    mUserInfoCache.put(friend.getUserId(), userInfo);
                    return portrait;
                }
            } else {
                return friend.getPortraitUri().toString();
            }
        }
        return null;
    }


    private String getPortrait(GroupMember groupMember) {
        if (groupMember != null) {
            if (!TextUtils.isEmpty(groupMember.getPortraitUri().toString())) {
                if (TextUtils.isEmpty(groupMember.getGroupId())) {
                    return null;
                } else {
                    UserInfo userInfo = mUserInfoCache.get(groupMember.getUserId());
                    if (userInfo != null) {
                        if (!TextUtils.isEmpty(userInfo.getPortraitUri().toString())) {
                            return userInfo.getPortraitUri().toString();
                        } else {
                            mUserInfoCache.remove(groupMember.getUserId());
                        }
                    }
                    Friend friend = getFriendById(groupMember.getUserId());
                    if (friend != null) {
                        if (!TextUtils.isEmpty(friend.getPortraitUri().toString())) {
                            return friend.getPortraitUri().toString();
                        }
                    }
                    List<GroupMember> groupMemberList = getGroupMembersWithUserId(groupMember.getUserId());
                    if (groupMemberList != null && groupMemberList.size() > 0) {
                        GroupMember member = groupMemberList.get(0);
                        if (!TextUtils.isEmpty(member.getPortraitUri().toString())) {
                            return member.getPortraitUri().toString();
                        }
                    }
                    String portrait = RongGenerate.generateDefaultAvatar(groupMember.getName(), groupMember.getUserId());
                    userInfo = new UserInfo(groupMember.getUserId(), groupMember.getName(), Uri.parse(portrait));
                    mUserInfoCache.put(groupMember.getUserId(), userInfo);
                    return portrait;
                }
            } else {
                return groupMember.getPortraitUri().toString();
            }
        }
        return null;
    }





















}
