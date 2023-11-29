package com.jcen.ioj.judge.codesandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.jcen.ioj.common.ErrorCode;
import com.jcen.ioj.exception.BusinessException;
import com.jcen.ioj.judge.codesandbox.CodeSandBox;
import com.jcen.ioj.judge.codesandbox.model.ExecuteCodeRequest;
import com.jcen.ioj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 远程代码沙箱（实际调用接口的沙箱）
 */
public class RemoteCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://172.16.56.131:8090/exec";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        //Error request, response status: 502 开了代理
        String responseStr = HttpUtil.createPost(url)
                .header("Content-Type", "application/json")
                .body(json)
                .execute()
                .body();

        if (StrUtil.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "exec remote sandbox error, message: " + responseStr);
        }

        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
