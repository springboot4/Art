```java

public class VariablePoolExample {

    public static void main(String[] args) {
        var manager = new VariablePoolManager();

        Map<SystemVariableKey, Object> systemVars = Map.of(
                SystemVariableKey.USER_ID, "user123",
                SystemVariableKey.CONVERSATION_ID, "conv456"
        );

        Map<String, Object> envVars = Map.of(
                "api_key", "sk-xxx",
                "model_name", "gpt-4",
                "temperature", 0.7
        );

        Map<String, Object> conversationVars = Map.of(
                "chat_history", "[]",
                "user_preference", "简洁回答"
        );

        Map<String, Object> userInputs = Map.of(
                "input_text", "请分析销售数据",
                "file_path", "/data/sales.csv"
        );

        // 创建变量池
        var pool = manager.createPool("conv456", systemVars, envVars, conversationVars, userInputs);

        // 读取系统变量
        String userId = pool.getValue(VariableSelector.system(SystemVariableKey.USER_ID), String.class)
                .orElse("unknown");
        System.out.println("用户ID: " + userId);

        // 读取环境变量
        String apiKey = pool.getValue(VariableSelector.environment("api_key"), String.class)
                .orElse("");
        System.out.println("API Key: " + apiKey);

        // 更新会话变量
        pool.update(VariableSelector.conversation("chat_history"),
                "[{\"role\": \"user\", \"content\": \"你好\"}]");

        // 添加节点输出变量
        pool.add(VariableSelector.of("llm_node_1", "response"), "根据数据分析，销售趋势良好");

        // 读取节点输出
        String response = pool.getAsText(VariableSelector.of("llm_node_1", "response"));
        System.out.println("LLM响应: " + response);

        // 读取用户输入
        String userInput = pool.getValue(VariableSelector.of("user_input_node", "input_text"), String.class)
                .orElse("无用户输入");
        System.out.println("用户输入: " + userInput);

        // 读取会话变量
        String chatHistory = pool.getValue(VariableSelector.conversation("chat_history"), String.class)
                .orElse("无聊天记录");
        System.out.println("聊天记录: " + chatHistory);

        // 读取系统变量
        String conversationId = pool.getValue(VariableSelector.system(SystemVariableKey.CONVERSATION_ID), String.class)
                .orElse("无会话ID");
        System.out.println("会话ID: " + conversationId);

        // 读取环境变量
        String modelName = pool.getValue(VariableSelector.environment("model_name"), String.class)
                .orElse("无模型名称");
        System.out.println("模型名称: " + modelName);

        System.out.println(pool);
    }
}
```