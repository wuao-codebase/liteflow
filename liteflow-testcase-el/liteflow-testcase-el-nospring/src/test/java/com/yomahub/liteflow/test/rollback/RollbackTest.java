package com.yomahub.liteflow.test.rollback;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.core.FlowExecutorHolder;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.property.LiteflowConfig;
import com.yomahub.liteflow.test.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;



public class RollbackTest extends BaseTest {

	private static FlowExecutor flowExecutor;

	@BeforeAll
	public static void init() {
		LiteflowConfig config = new LiteflowConfig();
		config.setRuleSource("rollback/flow.el.xml");
		flowExecutor = FlowExecutorHolder.loadInstance(config);
	}

	// 在流程正常执行结束情况下的测试
	@Test
	public void testRollback() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
		Assertions.assertTrue(response.isSuccess());
		Assertions.assertNull(response.getCause());
		Assertions.assertEquals("", response.getRollbackStepStr());
	}

	// 对串行编排与并行编排语法的测试
	@Test
	public void testWhenAndThen() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain2", "arg");
		Assertions.assertFalse(response.isSuccess());
		Assertions.assertEquals("d==>b==>a", response.getRollbackStepStr());
	}

	// 对条件编排语法的测试
	@Test
	public void testIf() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain3", "arg");
		Assertions.assertFalse(response.isSuccess());
		Assertions.assertEquals("d==>x", response.getRollbackStepStr());
	}

	// 对选择编排语法的测试
	@Test
	public void testSwitch() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain4", "arg");
		Assertions.assertFalse(response.isSuccess());
		Assertions.assertEquals("d==>f", response.getRollbackStepStr());
	}

	// 对FOR循环编排语法的测试
	@Test
	public void testFor() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain5", "arg");
		Assertions.assertFalse(response.isSuccess());
		Assertions.assertEquals("h==>b==>g", response.getRollbackStepStr());
	}

	// 对WHILE循环编排语法的测试
	@Test
	public void testWhile() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain6", "arg");
		Assertions.assertFalse(response.isSuccess());
		Assertions.assertEquals("d==>b==>a==>w", response.getRollbackStepStr());
	}

	// 对ITERATOR迭代循环编排语法的测试
	@Test
	public void testIterator() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain7", "arg");
		Assertions.assertFalse(response.isSuccess());
		Assertions.assertEquals("d==>b==>a==>i", response.getRollbackStepStr());
	}

	@Test
	// 对捕获异常表达式的测试
	public void testCatch() throws Exception {
		LiteflowResponse response = flowExecutor.execute2Resp("chain8", "arg");
		Assertions.assertTrue(response.isSuccess());
		Assertions.assertNull(response.getCause());
		Assertions.assertEquals("", response.getRollbackStepStr());
	}


}
