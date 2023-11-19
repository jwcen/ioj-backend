package com.jcen.ioj.judge.codesandbox;

import com.jcen.ioj.judge.codesandbox.impl.ExampleCodeSandBox;
import com.jcen.ioj.judge.codesandbox.impl.RemoteCodeSandBox;
import com.jcen.ioj.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的沙箱实例）
 */
public class CodeSandBoxFactory {

    public static CodeSandBox newInstance(String type) {
        switch (type) {
            case "remote":
                return new RemoteCodeSandBox();
            case "thirdParty":
                return new ThirdPartyCodeSandBox();
            default:
                return new ExampleCodeSandBox();
        }
    }
}
