package com.jcen.ioj.judge.codesandbox;

import com.jcen.ioj.judge.codesandbox.model.ExecuteCodeRequest;
import com.jcen.ioj.judge.codesandbox.model.ExecuteCodeResponse;

import java.io.IOException;

public interface CodeSandBox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) throws IOException;
}
