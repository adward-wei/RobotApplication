package com.ubtechinc.alpha2ctrlapp.entity.net;

import android.support.annotation.Keep;

import com.ubtechinc.alpha2ctrlapp.entity.business.shop.CommentInfo;
import com.ubtechinc.nets.http.Url;

import java.util.List;

/**
 * @ClassName AddCommentModule
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 添加评论
 * @modifier
 * @modify_time
 */
@Keep
public class AddCommentModule {
    @Url("alpha2-web/comment/add")
    @Keep
    public class Request {
        //动作id（必填）
        private int actionId;
        //评论内容（必填）
        private String commentContext;
        //1:评论 2：回复（必填）
        private int commentType;
        //	评论id（当commentType为2时必填）
        private int replyCommentId;

        public int getActionId() {
            return actionId;
        }

        public void setActionId(int actionId) {
            this.actionId = actionId;
        }

        public String getCommentContext() {
            return commentContext;
        }

        public void setCommentContext(String commentContext) {
            this.commentContext = commentContext;
        }

        public int getCommentType() {
            return commentType;
        }

        public void setCommentType(int commentType) {
            this.commentType = commentType;
        }

        public int getReplyCommentId() {
            return replyCommentId;
        }

        public void setReplyCommentId(int replyCommentId) {
            this.replyCommentId = replyCommentId;
        }
    }

    @Keep
    public class Response extends BaseResponse {

        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    @Keep
    public class Result {

        List<CommentInfo> records;

        public List<CommentInfo> getRecords() {
            return records;
        }

        public void setRecords(List<CommentInfo> records) {
            this.records = records;
        }
    }

    @Keep
    public class Data {
        private Result result;

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }

}
