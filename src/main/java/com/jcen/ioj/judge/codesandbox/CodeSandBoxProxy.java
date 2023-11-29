package com.jcen.ioj.judge.codesandbox;

import com.jcen.ioj.judge.codesandbox.model.ExecuteCodeRequest;
import com.jcen.ioj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {

    private final CodeSandBox codeSandBox;

    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("CodeSandBox request info: " + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = null;
        try {
            executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("CodeSandBox response info: " + executeCodeResponse.toString());

        return executeCodeResponse;
    }
}
