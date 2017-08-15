// ISpeechGrammarInitListener.aidl
package com.ubtechinc.alpha.serverlibutil.aidl;

// Declare any non-default types here with import statements

interface ISpeechGrammarInitListener {
    /**讯飞初始化回调**/
    void speechGrammarInitCallback(String grammarID, int nErrorCode);

}
