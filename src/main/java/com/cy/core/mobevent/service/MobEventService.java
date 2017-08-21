package com.cy.core.mobevent.service;

import java.util.List;
import java.util.Map;

import com.cy.base.entity.Message;
import com.cy.core.dict.entity.Dict;
import com.cy.core.mobevent.entity.CyEvent;
import com.cy.core.mobevent.entity.CyEventBoard;
import com.cy.core.mobevent.entity.CyEventBoardComment;
import com.cy.core.mobevent.entity.CyEventBoardComplaint;
import com.cy.core.mobevent.entity.CyEventBoardPic;
import com.cy.core.mobevent.entity.CyEventBoardPraise;
import com.cy.core.mobevent.entity.CyEventComment;
import com.cy.core.mobevent.entity.CyEventSign;
import org.apache.commons.lang3.StringUtils;

public interface MobEventService {

	
	
	
	
	/**
	 * 通过指定活动花絮对象中的参数得到活动花絮对象的列表
	 * 
	 * @param CyEventBoard eventBoard
	 * @return List<CyEventBoard>
	 * 
	 */
	public List<CyEventBoard> getEventBoardList(CyEventBoard eventBoard);
	

	/**
	 * 通过指定活动对象中的参数得到活动对象的列表
	 * 
	 * @param CyEventComment eventComment
	 * @return List<CyEventBoard>
	 * 
	 */
	public List<CyEventComment> getEventCommentList(CyEvent event);
	
	
	/**
	 * 通过指定活动花絮对象中的参数得到活动花絮总数
	 * 
	 * @param CyEventBoard eventBoard
	 * @return long
	 * 
	 */
	public long countEventBoard(CyEventBoard eventBoard);	
	
	
	/**
	 * 通过指定活动花絮对象中的参数得到活动花絮的对象
	 * 
	 * @param CyEventBoard eventBoard
	 * @return CyEventBoard
	 * 
	 */
	public CyEventBoard getEventBoard(CyEventBoard eventBoard);
	
	
	/**
	 * 通过指定活动花絮图片对象中的参数得到活动花絮图片对象的列表
	 * 
	 * @param CyEventBoardPic eventBoardPic
	 * @return List<CyEventBoardPic>
	 * 
	 */
	public List<CyEventBoardPic> getEventBoardPicList(CyEventBoardPic eventBoardPic);
	
	
	/**
	 * 得到每个用户发布的活动花絮中的最新的一张的活动花絮图片对象的列表
	 * 
	 * @param CyEventBoard eventBoard
	 * @return List<CyEventBoardPic>
	 * 
	 */
	public List<CyEventBoardPic> getEventBoardPicListForEventDetail(CyEventBoard eventBoard);
	
	
	/**
	 * 通过指定活动花絮评论对象中的参数得到活动花絮评论对象的列表
	 * 
	 * @param CyEventBoardComment eventBoardComment
	 * @return List<CyEventBoardComment>
	 * 
	 */
	public List<CyEventBoardComment> getEventBoardCommentList(CyEventBoardComment eventBoardComment);
	
	
		
	/**
	 * 通过指定活动花絮评论对象中的参数得到活动花絮评论对象的列表(带楼主)
	 * 
	 * @param CyEventBoard eventBoard, CyEventBoardComment eventBoardComment
	 * @return Map<String, Object>
	 * 
	 */
	public Map<String, Object> getEventBoardCommentListWithEventBoard(CyEventBoard eventBoard, CyEventBoardComment eventBoardComment);
	
	
	/**
	 * 通过指定活动花絮举报对象中的参数得到活动花絮举报数
	 * 
	 * @param CyEventBoardComplaint eventBoardComplaint
	 * @return long
	 * 
	 */
	public long countEventBoardComplaint(CyEventBoardComplaint eventBoardComplaint);
	
	
	/**
	 * 通过指定活动花絮点赞对象中的参数得到活动花絮点赞数
	 * 
	 * @param CyEventBoardPraise eventBoardPraise
	 * @return long
	 * 
	 */
	public long countEventBoardPraise(CyEventBoardPraise eventBoardPraise);
	
	
	
	/**
	 * 插入活动花絮点赞对象中的参数到活动花絮点赞表
	 * 
	 * @param CyEventBoardPraise eventBoardPraise
	 * 
	 */
	public void insertEventBoardPraise(CyEventBoardPraise eventBoardPraise);
	
	
	/**
	 * 通过活动花絮点赞对象中的参数删除活动花絮点赞
	 * 
	 * @param CyEventBoardPraise eventBoardPraise
	 * 
	 */
	public void deleteEventBoardPraise(CyEventBoardPraise eventBoardPraise);
	
	
	/**
	 * 通过活动花絮评论对象中的参数删除活动花絮评论
	 * 
	 * @param CyEventBoardComment eventBoardComment
	 * 
	 */
	public void deleteEventBoardComment(CyEventBoardComment eventBoardComment);
	
	
	/**
	 * 通过活动花絮对象中的参数删除活动花絮
	 * 
	 * @param CyEventBoard eventBoard
	 * 
	 */
	public void deleteEventBoard(CyEventBoard eventBoard);
	
	
	/**
	 * 通过活动对象中的参数删除活动
	 * 
	 * @param CyEvent event
	 * 
	 */
	public void deleteEvent(CyEvent event);
	
	
	/**
	 * 通过活动花絮点赞对象中的参数对活动花絮点赞
	 * 
	 * @param CyEventBoardPraise eventBoardPraise
	 * 
	 */
	public void eventBoardPraise(CyEventBoardPraise eventBoardPraise);
	
	
	
	/**
	 * 通过指定活动花絮评论对象中的参数得到活动花絮评论数
	 * 
	 * @param CyEventBoardComment eventBoardComment
	 * @return long
	 * 
	 */
	public long countEventBoardComment(CyEventBoardComment eventBoardComment);
	
	

	/**
	 * 通过指定活动花论对象中的参数得到活动评论数
	 * 
	 * @param CyEventomment eventComment
	 * @return long
	 * 
	 */	
	public long countEventComment(CyEventComment eventComment);
	
	
	/**
	 * 插入活动花絮评论对象中的参数到活动花絮评论表
	 * 
	 * @param CyEventBoardComment eventBoardComment
	 * 
	 */
	public void insertEventBoardComment(CyEventBoardComment eventBoardComment);
	
	
	/**
	 * 插入活动花絮举报对象中的参数到活动花絮举报表
	 * 
	 * @param CyEventBoardComplaint eventBoardComplaint
	 * 
	 */
	public void insertEventBoardComplaint(CyEventBoardComplaint eventBoardComplaint);
	
	
	
	
	/**
	 * 通过活动ID和用户ID得到活动详情
	 * 
	 * @param long id
	 * @param String userId
	 * @return Map<String, Object>
	 * 
	 */
	public Map<String, Object> getEventDetail(String id, String userId);
	
	
	/**
	 * 通过活动ID得到活动对象
	 * 
	 * @param long id
	 * @return CyEvent
	 * 
	 */
	public CyEvent getEventById(String id);
	
	
	/**
	 * 通过指定活动签到报名对象中的参数得到活动签到报名对象
	 * 
	 * @param CyEventSign eventSign
	 * @return CyEventSign
	 * 
	 */
	public CyEventSign getEventSign(CyEventSign eventSign);
	
	
	/**
	 * 通过指定活动签到报名对象中的参数得到活动签到报名对象的列表
	 * 
	 * @param Map<String, Object> map
	 * @return CyEventSign
	 * 
	 */
	public List<CyEventSign> getEventSignList(Map<String, Object> map);
	
	
	/**
	 * 通过指定活动签到报名对象中的参数得到活动签到报名对象的列表（手机列表专用）
	 * 
	 * @param CyEventSign cyEventSign
	 * @return CyEventSign
	 * 
	 */
	public List<CyEventSign> pullEventSignList(CyEventSign cyEventSign);
	
	
	/**
	 * 通过指定活动签到报名对象中的参数得到相应的统计数量
	 * 
	 * @param CyEventSign eventSign
	 * @return long
	 * 
	 */
	public long countEventSign(CyEventSign eventSign);
	
	
	/**
	 * 验证签到码
	 * 
	 * @param CyEvent event
	 * @return CyEvent
	 * 
	 */
	public long verifyEventSignInCode(CyEvent event);
	
	
	/**
	 * 更新报名签到表
	 * 
	 * @param CyEventSign eventSign
	 * 
	 */
	public void updateEventSign(CyEventSign eventSign);
	
	
	/**
	 * 插入报名签到表
	 * 
	 * @param CyEventSign eventSign
	 * 
	 */
	public void insertEventSign(CyEventSign eventSign);
	
	
	/**
	 * 插入活动表
	 * 
	 * @param CyEvent cyEevent
	 * 
	 */
	public void saveMobEvent(CyEvent cyEevent);
	
	
	
	/**
	 * 查询活动列表
	 * 
	 * @param CyEvent cyEevent
	 * 
	 */
	public List<CyEvent> listMobEvens(CyEvent cyEevent);
	
	/**
	 * 查询活动类型列表
	 * 
	 * @param String dictTypeName
	 * 
	 */
	public List<Dict> getDicts(String dictTypeName);
	
	/**
	 * 查询我的所有活动通知数量
	 * 
	 * @param String userInfoId
	 * 
	 */
	public CyEvent getNumOfNotifyForMyEvents(String userInfoId);
	
	/**
	 * 插入花絮表
	 * 
	 * @param CyEventBoard eventBoard
	 * 
	 */
	public void saveMobEventBoard(CyEventBoard eventBoard);
	
	/**
	 * 插入花絮图片表
	 * 
	 * @param CyEventBoardPic eventBoardPic
	 * 
	 */
	public void saveMobEventBoardPic(CyEventBoardPic eventBoardPic);
	
	/**
	 * 获取相关活动列表总数量
	 * 
	 * @param CyEvent cyEevent
	 * @return long
	 * 
	 */
	public long listMobEvensNum(CyEvent cyEevent);
	

	/**
	 * 查询对应用户所在的地域
	 * 
	 * @param CyEvent cyEvent
	 * @return String
	 * 
	 */
	public String getRegionFromAlumniByAccountNum(CyEvent cyEvent);


	/**
	 * 活动花絮创建接口
	 */
	void saveEventBoard(Message message, String content);

	/**
	 * 活动花絮评价／点赞接口
	 */
	void saveCommentOrPraise(Message message, String content);

	/**
	 * 活动花絮反馈接口
	 */
	void saveComplaint(Message message, String content);

	/**
	 * 活动花絮详情／列表接口
	 */
	void findEventBoardList(Message message, String content);

	/**
	 * 活动花絮详情接口
	 */
	void findEventBoard(Message message, String content);

	/**
	 * 刪除活動花絮接口
	 */
	void removeEventBoard(Message message, String content);

	
    /**
     * 活动评论接口
     * @param message
     * @param content
     */
    void findEventComment(Message message, String content);

	
    /**
     * 活动评论接口
     * @param message
     * @param content
     */
    void saveEventCommentOrPraise(Message message, String content);
    
    /**
     * 活动评论总数统计接口
     * @param message
     * @param content
     */
    public void CountCommentForEventid(Message message, String content);
    	
    /**
     * 活动花絮总数统计接口
     * @param message
     * @param content
     */
    public void CountBoardForBoardid(Message message, String content);
    
}
