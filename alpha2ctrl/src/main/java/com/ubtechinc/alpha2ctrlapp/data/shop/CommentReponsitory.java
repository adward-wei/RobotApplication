package com.ubtechinc.alpha2ctrlapp.data.shop;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.data.user.MessageRepository;
import com.ubtechinc.alpha2ctrlapp.entity.net.AddCommentModule;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetCommentListModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;


/**
 * @ClassName CommentReponsitory
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 评论数据类
 * @modifier
 * @modify_time
 */
public class CommentReponsitory implements ICommentDataSource {
    private static CommentReponsitory INSTANCE = null;


    public CommentReponsitory() {

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link MessageRepository} instance
     */
    public static CommentReponsitory getInstance(  ) {
        if (INSTANCE == null) {
            INSTANCE = new CommentReponsitory();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getCommentList(int actionId, final @NonNull CommentDataCallback callback) {
        GetCommentListModule.Request request = new GetCommentListModule().new Request();
        request.setActionId(actionId);
        HttpProxy.get().doGet(request, new ResponseListener<GetCommentListModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetCommentListModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onLoadADData(response.getData().getResult().getRecords());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }

    @Override
    public void addComment(int actionId,String commentContext, int commentType, int replyCommentId,final  @NonNull AddCommentCallback callback) {
        AddCommentModule.Request request = new AddCommentModule().new Request();
        request.setActionId(actionId);
        request.setCommentContext(commentContext);
        request.setCommentType(commentType);
        request.setReplyCommentId(replyCommentId);
        HttpProxy.get().doPost(request, new ResponseListener<AddCommentModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(AddCommentModule.Response response) {


                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess();
                }else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }
            }
        });
    }
}
