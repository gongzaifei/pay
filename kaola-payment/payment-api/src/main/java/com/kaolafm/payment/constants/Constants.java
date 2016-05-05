package com.kaolafm.payment.constants;

public class Constants {
	
	public static Double goldleafToCashRatio = 0.6; // 金叶子兑换现金比例
	public static Integer MAX_WITHDRAWAL_DAY_CASH = 1000; // 单日最大体现金额
	public static Integer MIN_WITHDRAWAL_TIMES_CASH = 10; // 单次体现最小金额
	public static Integer MAX_TIMES_WITHDRAWAL_DAY = 3; // 单日最大体现次数
	public static Integer MIN_WITHDRAWAL_GOLDLEAF_EVERY_TIMES = 100;	//每次提现最小金叶子数
	public static Integer MAX_WITHDRAWAL_GOLDLEAF_EVERY_TIMES = 1500;	//每次提现最大金叶子数

	public static enum PAY_TYPE {
		ALIPAY(0, "支付宝"), WEIXIN(1, "微信"), IOSPAY(2, "苹果支付");

		private final Integer code;
		private final String content;

		private PAY_TYPE(Integer code, String content) {
			this.code = code;
			this.content = content;
		}

		public Integer code() {
			return code;
		}

		public String content() {
			return content;
		}
	}

	public static enum RECHARGE_STATUS {
		WAIT(0, "充值中"), SUCCESS(1, "充值成功"), FAIL(2, "支付失败");

		private final Integer code;
		private final String content;

		private RECHARGE_STATUS(Integer code, String content) {
			this.code = code;
			this.content = content;
		}

		public Integer code() {
			return code;
		}

		public String content() {
			return content;
		}
	}

	public static enum WITHDRAWAL_STATUS {
		WAIT_CHECK(0, "待审核"), CHECK_SUCCESS(1, "审核成功"), CHECK_FAIL(2, "审核失败"), PAYMENT_SUCCESS(3, "已转账"), PAYMENT_FAIL(
				4, "转账失败"), DELETE(5, "删除");

		private final Integer code;
		private final String content;

		private WITHDRAWAL_STATUS(Integer code, String content) {
			this.code = code;
			this.content = content;
		}

		public Integer code() {
			return code;
		}

		public String content() {
			return content;
		}
	}

	public static enum USER_GIFT_TYPE {
		RECEIVE(1, "收到"), SEND(2, "发出");

		private final Integer code;
		private final String content;

		private USER_GIFT_TYPE(Integer code, String content) {
			this.code = code;
			this.content = content;
		}

		public Integer code() {
			return code;
		}

		public String content() {
			return content;
		}
	}

	public static enum USER_OPERATE_TYPE {
		RECHARGE(1, "充值"), WITHDRAWAL(2, "提现");

		private final Integer code;
		private final String content;

		private USER_OPERATE_TYPE(Integer code, String content) {
			this.code = code;
			this.content = content;
		}

		public Integer code() {
			return code;
		}

		public String content() {
			return content;
		}
	}

	public static enum USERACCOUNT_INFO_TYPE {
		GREENLEAF_COUNT(1, "绿叶子"), GOLDLEAF_COUNT(2, "金叶子"), RECEIVE_GIFT_COUNT(3, "收到礼物数"), SEND_GIFT_COUNT(4, "送出礼物数"), LIVE_PROGRAM_LEAF_COUNT(
				5, "计划获取叶子数");

		private final Integer code;
		private final String content;

		private USERACCOUNT_INFO_TYPE(Integer code, String content) {
			this.code = code;
			this.content = content;
		}

		public Integer code() {
			return code;
		}

		public String content() {
			return content;
		}
	}

}
