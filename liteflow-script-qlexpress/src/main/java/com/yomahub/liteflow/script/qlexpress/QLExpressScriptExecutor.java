package com.yomahub.liteflow.script.qlexpress;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressLoader;
import com.ql.util.express.ExpressRunner;
import com.yomahub.liteflow.entity.data.DataBus;
import com.yomahub.liteflow.entity.data.Slot;
import com.yomahub.liteflow.script.ScriptExecutor;
import com.yomahub.liteflow.script.exception.ScriptExecuteException;
import com.yomahub.liteflow.script.exception.ScriptLoadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 阿里QLExpress脚本语言的执行器实现
 * @author Bryan.Zhang
 * @since 2.6.0
 */
public class QLExpressScriptExecutor implements ScriptExecutor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ExpressRunner expressRunner;

    @Override
    public ScriptExecutor init() {
        expressRunner = new ExpressRunner();
        return this;
    }

    @Override
    public void load(String nodeId, String script) {
        try{
            expressRunner.loadMutilExpress(nodeId, script);
        }catch (Exception e){
            String errorMsg = StrUtil.format("script loading error for node[{}],error msg:{}", nodeId, e.getMessage());
            throw new ScriptLoadException(errorMsg);
        }
    }

    @Override
    public Object execute(String nodeId, int slotIndex) {
        List<String> errorList = new ArrayList<>();
        try{
            Slot slot = DataBus.getSlot(slotIndex);
            DefaultContext<String, Object> context = new DefaultContext<>();
            context.put("slot", slot);
            return expressRunner.executeByExpressName(nodeId, context, errorList, true, false, null);
        }catch (Exception e){
            for (String scriptErrorMsg : errorList){
                log.error("\n{}", scriptErrorMsg);
            }
            String errorMsg = StrUtil.format("script execute error for node[{}]", nodeId);
            throw new ScriptExecuteException(errorMsg);
        }
    }

    @Override
    public void cleanCache() {
        expressRunner.clearExpressCache();
        ReflectUtil.setFieldValue(expressRunner,"loader",new ExpressLoader(expressRunner));
    }
}
