package com.ubtechinc.alpha2ctrlapp.data.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ubtechinc.alpha2ctrlapp.entity.business.user.NoticeMessage;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：tanghongyu
 * @date：4/7/2017 3:34 PM
 * @modifier：tanghongyu
 * @modify_date：4/7/2017 3:34 PM
 * [A brief description]
 * version
 */

public class MessageRepository implements IMessageDataSource {

    private static MessageRepository INSTANCE = null;

    private final MessageRemoteDataSource mNoticeMessageRemoteDataSource;

    private final MessageLocalDataSource mNoticeMessageLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, NoticeMessage> mCachedTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private MessageRepository(@NonNull MessageRemoteDataSource noticeMessageRemoteDataSource,
                              @NonNull MessageLocalDataSource noticeMessageLocalDataSource) {
        mNoticeMessageRemoteDataSource = noticeMessageRemoteDataSource;
        mNoticeMessageLocalDataSource = noticeMessageLocalDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param noticeMessageRemoteDataSource the backend data source
     * @param noticeMessageLocalDataSource  the device storage data source
     * @return the {@link MessageRepository} instance
     */
    public static MessageRepository getInstance(MessageRemoteDataSource noticeMessageRemoteDataSource,
                                                MessageLocalDataSource noticeMessageLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MessageRepository(noticeMessageRemoteDataSource, noticeMessageLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getMessages(final String userId,@NonNull final LoadMessageCallback callback) {
        // Respond immediately with cache if available and not dirty
        if (mCachedTasks != null && !mCacheIsDirty) {
            callback.onMessageLoaded(new ArrayList<>(mCachedTasks.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getMessageListFromRemoteDataSource(userId,callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mNoticeMessageLocalDataSource.getMessages( userId,new LoadMessageCallback() {
                @Override
                public void onMessageLoaded(List<NoticeMessage> tasks) {
                    refreshCache(tasks);
                    callback.onMessageLoaded(new ArrayList<>(mCachedTasks.values()));
                }

                @Override
                public void onDataNotAvailable(ThrowableWrapper e) {
                    getMessageListFromRemoteDataSource(userId,callback);
                }
            });
        }
    }

    @Override
    public void getMessageByMessageId(final String userId ,@NonNull final String messageId, @NonNull final GetMessageCallback callback) {
        NoticeMessage cachedTask = getTaskWithId(messageId);

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onMessageLoaded(cachedTask);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mNoticeMessageLocalDataSource.getMessageByMessageId(userId,messageId, new GetMessageCallback() {
            @Override
            public void onMessageLoaded(NoticeMessage message) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedTasks == null) {
                    mCachedTasks = new LinkedHashMap<>();
                }
//                mCachedTasks.put(message.getNoticeId(), message);
                callback.onMessageLoaded(message);
            }

            @Override
            public void onDataNotAvailable() {
                mNoticeMessageRemoteDataSource.getMessageByMessageId(userId,messageId, new GetMessageCallback() {
                    @Override
                    public void onMessageLoaded(NoticeMessage message) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedTasks == null) {
                            mCachedTasks = new LinkedHashMap<>();
                        }
//                        mCachedTasks.put(message.getNoticeId(), message);
                        callback.onMessageLoaded(message);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveMessage(@NonNull NoticeMessage message) {

    }

    @Override
    public void saveMessages(@NonNull List<NoticeMessage> messages) {

    }

    @Override
    public void getUnreadMessageAuthorize(String userId,final LoadMessageCallback callback) {
         mNoticeMessageLocalDataSource.getUnreadMessageAuthorize(userId, new LoadMessageCallback() {
             @Override
             public void onMessageLoaded(List<NoticeMessage> tasks) {
                 callback.onMessageLoaded(tasks);
             }

             @Override
             public void onDataNotAvailable(ThrowableWrapper e) {
                 callback.onDataNotAvailable(e);
             }


         });
    }


    @Override
    public void refreshMessage() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllMessage(String userId) {
        mNoticeMessageLocalDataSource.deleteAllMessage(userId);
        mNoticeMessageRemoteDataSource.deleteAllMessage(userId);

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    @Override
    public void deleteMessageByMessageId(String userId,@NonNull String messageId) {
        mNoticeMessageLocalDataSource.deleteMessageByMessageId(userId,messageId);
        mNoticeMessageRemoteDataSource.deleteMessageByMessageId(userId,messageId);

        mCachedTasks.remove(messageId);
    }

    @Override
    public void deleteMessageByMessageType(String userId,@NonNull int messageType) {
        mNoticeMessageLocalDataSource.deleteMessageByMessageType(userId,messageType);
        mNoticeMessageRemoteDataSource.deleteMessageByMessageType(userId,messageType);

    }



    private void getMessageListFromRemoteDataSource(final String userId,@NonNull final LoadMessageCallback callback) {
        mNoticeMessageRemoteDataSource.getMessages(userId,new LoadMessageCallback() {
            @Override
            public void onMessageLoaded(List<NoticeMessage> messages) {
                refreshCache(messages);
                refreshLocalDataSource(userId,messages);
                callback.onMessageLoaded(new ArrayList<>(mCachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }
        });
    }

    private void refreshCache(List<NoticeMessage> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (NoticeMessage task : tasks) {
            mCachedTasks.put(task.getNoticeId(), task);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(String userId, List<NoticeMessage> tasks) {
        mNoticeMessageLocalDataSource.deleteAllMessage(userId);
        for (NoticeMessage task : tasks) {
            mNoticeMessageLocalDataSource.saveMessage(task);
        }
    }

    @Nullable
    private NoticeMessage getTaskWithId(@NonNull String id) {
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }
}
