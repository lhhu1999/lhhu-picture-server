package com.lhhu.lhhupictureserver.api.aliyunai;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.lhhu.lhhupictureserver.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.lhhu.lhhupictureserver.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.lhhu.lhhupictureserver.api.aliyunai.model.GetOutPaintingTaskResponse;
import com.lhhu.lhhupictureserver.exception.BusinessException;
import com.lhhu.lhhupictureserver.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AliYunAiApi {

    // 读取配置文件
    @Value("${aliYunAi.apiKey}")
    private String apiKey;

    // 创建任务的地址
    public static final String CREATE_OUT_PAINTING_TASK_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/image2image/out-painting";

    // 查询任务处理结果的地址
    public static final String GET_OUT_PAINTING_TASK_URL = "https://dashscope.aliyuncs.com/api/v1/tasks/%s";

    /**
     * 创建任务
     * @param createOutPaintingTaskRequest 创建任务所需要的参数
     * @return
     */
    public CreateOutPaintingTaskResponse createOutPaintingTask(CreateOutPaintingTaskRequest createOutPaintingTaskRequest) {
        if (createOutPaintingTaskRequest == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI扩图参数为空");
        }
        // 发送请求
        HttpRequest httpRequest = HttpRequest.post(CREATE_OUT_PAINTING_TASK_URL)
                .header(Header.AUTHORIZATION, "Bearer " + apiKey)
                // 模型只支持异步处理，必须开启异步处理
                .header("X-DashScope-Async", "enable")
                .header(Header.CONTENT_TYPE, "application/json")
                .body(JSONUtil.toJsonStr(createOutPaintingTaskRequest));

        try (HttpResponse httpResponse = httpRequest.execute()) {
            if (!httpResponse.isOk()){
                log.error("请求异常：{}", httpResponse.body());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI扩图失败");
            }
            CreateOutPaintingTaskResponse response = JSONUtil.toBean(httpResponse.body(), CreateOutPaintingTaskResponse.class);
            String errorCode = response.getCode();
            if (StrUtil.isNotBlank(errorCode)) {
                log.error("AI扩图失败：errorCode:{}, errorMessage:{}", errorCode, response.getMessage());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI扩图接口响应异常");
            }
            return response;
        }
    }

    /**
     * 查询任务处理结果
     * @param taskId 任务的id
     * @return
     */
    public GetOutPaintingTaskResponse getOutPaintingTask(String taskId) {
        if (StrUtil.isBlank(taskId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "任务id不能为空");
        }
        try (HttpResponse httpResponse = HttpRequest.get(String.format(GET_OUT_PAINTING_TASK_URL, taskId))
                .header(Header.AUTHORIZATION, "Bearer " + apiKey).execute()) {
            if (!httpResponse.isOk()){
                log.error("获取任务结果请求异常：{}", httpResponse.body());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI扩图获取结果失败");
            }
            return JSONUtil.toBean(httpResponse.body(), GetOutPaintingTaskResponse.class);
//            String taskStatus = response.getOutput().getTaskStatus();
//            if (taskStatus.equals("FAILED")) {
//                log.error("AI扩图获取结果失败：errorCode:{}, errorMessage:{}", response.getOutput().getCode(), response.getOutput().getMessage());
//                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI扩图获取结果异常");
//            }
        }

    }
}

