package com.kaolafm.payment.service;

import java.util.List;

import com.kaolafm.payment.dto.PageDto;
import com.kaolafm.payment.entity.PayGift;
import com.kaolafm.payment.entity.PayLog;

public interface IGiftService {
	/**
	 * 礼物总数
	 * @return
     */
	public Long getGiftCount();

	/**
	 * 礼物列表
	 * @param pagenum
	 * @param pagesize
     * @return
     */
	public List<PayGift> getGiftList(Integer pagenum, Integer pagesize);

	/**
	 * 礼物详情
	 * @param giftId
	 * @return
     */
	public PayGift getGiftUse(Long giftId);


	public PayGift getGift(Long giftId);
	
	/**
	 * 保存礼物
	 * @param payGift
	 * @return
	 */
	public boolean savePayGift(PayGift payGift,Long uid,String userName);
	
	/**
	 * 更新礼物
	 * @param uid 更新人id
	 * @param userName 更新人名称
	 * @return
	 */
	public boolean updatePayGift(PayGift payGift,Long uid,String userName);

	/**
	 * 批量修改礼物
	 * @param giftIds
	 * * @param status 0:启用 1:暂停 2:删除
	 * @return
	 */
	public boolean batchUpdateGift(List<Long> giftIds,Integer status,Long uid,String userName);

	/**
	 * 礼物列表
	 * @param name 名称
	 * @param status 状态 0:启用 1:暂停
	 * @param pageNum
	 * @param pageSize
     * @return
     */
	public PageDto<PayGift> getPageGift(String name,Integer status,Integer pageNum,Integer pageSize);

	/**
	 *
	 * @param orderJson 排序json串
	 * @param uid 操作人id
	 * @param userName 操作人Name
	 * @return
     */
	public Boolean giftOrder(String orderJson,Long uid,String userName);

	/**
	 * 礼物操作日志
	 * @param dataId
	 * @return
     */
	public List<PayLog> getLogList(String dataId);


	/**
	 * 礼物名称校验
	 * @param giftName
	 * @return  true 存在  false 不存在
     */
	public Boolean verifyName(String giftName,Long id);


	public List<PayGift> getGiftList(Integer status, String name, Integer pagenum, Integer pagesize);

	public Long getGiftCount(Integer status, String name);

}
