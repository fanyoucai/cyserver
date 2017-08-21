package com.cy.util.easemob.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cy.util.easemob.api.EasemobRestAPI;
import com.cy.util.easemob.api.IMUserAPI;
import com.cy.util.easemob.comm.wrapper.BodyWrapper;
import com.cy.util.easemob.comm.body.IMUserBody;
import com.cy.util.easemob.comm.constant.HTTPMethod;
import com.cy.util.easemob.comm.helper.HeaderHelper;
import com.cy.util.easemob.comm.wrapper.HeaderWrapper;
import com.cy.util.easemob.comm.wrapper.QueryWrapper;

public class EasemobIMUsers extends EasemobRestAPI implements IMUserAPI {

	private static final Logger log = LoggerFactory.getLogger(EasemobIMUsers.class);
	
	private static final String ROOT_URI = "/users";

	public Object createNewIMUserSingle(Object payload) {
		String url = getContext().getSeriveURL() + getResourceRootURI();
		BodyWrapper body = (BodyWrapper) payload;
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		
		return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, body, null);
	}

	public Object createNewIMUserBatch(Object payload) {
		String url = getContext().getSeriveURL() + getResourceRootURI();
		BodyWrapper body = (BodyWrapper) payload;
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		
		return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, body, null);
	}

	public Object getIMUsersByUserName(String userName) {
		String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName;
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		//BodyWrapper userBody = new IMUserBody(userName, "111", "");
		return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null);
	}

	public Object getIMUsersBatch(Long limit, String cursor) {
		String url = getContext().getSeriveURL() + getResourceRootURI();
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		QueryWrapper query = QueryWrapper.newInstance().addLimit(limit).addCursor(cursor);
		
		return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, query);
	}

	public Object deleteIMUserByUserName(String userName) {
		String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName;
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

		return getInvoker().sendRequest(HTTPMethod.METHOD_DELETE, url, header, null);
	}

	public Object deleteIMUserBatch(Long limit) {
		String url = getContext().getSeriveURL() + getResourceRootURI();
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
        QueryWrapper query = QueryWrapper.newInstance().addLimit(limit);

        return getInvoker().sendRequest(HTTPMethod.METHOD_DELETE, url, header, query);
	}

	public Object modifyIMUserPasswordWithAdminToken(String userName, Object payload) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/password";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
        BodyWrapper body = (BodyWrapper) payload;

        return getInvoker().sendRequest(HTTPMethod.METHOD_PUT, url, header, body, null);
	}

	public Object modifyIMUserNickNameWithAdminToken(String userName, Object payload) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName;
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
        BodyWrapper body = (BodyWrapper) payload;

        return getInvoker().sendRequest(HTTPMethod.METHOD_PUT, url, header, body, null);
	}

	public Object addFriendSingle(String userName, String friendName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/contacts/users/" + friendName;
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, null);
	}

	public Object deleteFriendSingle(String userName, String friendName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/contacts/users/" + friendName;
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_DELETE, url, header, null);
	}

	public Object getFriends(String userName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/contacts/users";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null);
	}

	public Object getBlackList(String userName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/blocks/users";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null);
	}

	public Object addToBlackList(String userName, Object payload) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/blocks/users";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
        BodyWrapper body = (BodyWrapper) payload;

        return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, body, null);
	}

	public Object removeFromBlackList(String userName, String blackListName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/blocks/users/" + blackListName;
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_DELETE, url, header, null);
	}

	public Object getIMUserStatus(String userName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/status";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null);
	}

	public Object getOfflineMsgCount(String userName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/offline_msg_count";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null);
	}

	public Object getSpecifiedOfflineMsgStatus(String userName, String msgId) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/offline_msg_status/" + msgId;
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null);
	}

	public Object deactivateIMUser(String userName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/deactivate";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, null);
	}

	public Object activateIMUser(String userName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/activate";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, null);
	}

	public Object disconnectIMUser(String userName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/disconnect";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null);
	}

	public Object getIMUserAllChatGroups(String userName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/joined_chatgroups";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null);
	}

	public Object getIMUserAllChatRooms(String userName) {
        String url = getContext().getSeriveURL() + getResourceRootURI() + "/" + userName + "/joined_chatrooms";
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();

        return getInvoker().sendRequest(HTTPMethod.METHOD_GET, url, header, null);
	}

	@Override
	public String getResourceRootURI() {
		return ROOT_URI;
	}
}