package com.corelibs.subscriber;

import android.content.Context;
import android.util.Log;

import com.corelibs.R;
import com.corelibs.base.BasePaginationView;
import com.corelibs.base.BaseView;
import com.corelibs.common.AppManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 网络结果处理类, 此类会判断网络错误与业务错误.
 *
 * <P>
 *     {@link ResponseSubscriber}与{@link ResponseAction}均是调用此类来实现网络结果判断, 错误处理,
 *     以及重置加载状态.
 */
public class ResponseHandler<T> {

    private BaseView view;
    private CustomHandler<T> handler;

    public ResponseHandler(CustomHandler<T> handler) {
        this.handler = handler;
    }

    public ResponseHandler(CustomHandler<T> handler, BaseView view) {
        this.handler = handler;
        this.view = view;
    }

    public boolean checkDataNotNull(IBaseData data) {
        return data != null && data.data() != null;
    }

    public boolean checkListNotNull(List data) {
        return data != null && data.size() > 0;
    }

    public void onCompleted() {
        release();
    }

    public void onError(Throwable e) {
        resetLoadingStatus();
        e.printStackTrace();
        if (!handler.error(e)) {
            handleException(e);
        }
        release();
    }

    public void onNext(T t) {
        resetLoadingStatus();
        IBaseData data;
        if (t instanceof IBaseData) {
            data = (IBaseData) t;
            if (data.isSuccess()) {
                handler.success(t);
            } else {
                if (!handler.operationError(t, data.status(), data.msg())) {
                    /*if(data.status()==403){
                        handlerGoLogin();
                    }else if(data.status()==900){
                        handleOperationError(view.getViewContext().getString(R.string.code_900));
                    }else if(data.status()==999){
                        handleOperationError(view.getViewContext().getString(R.string.code_999));
                    }else if(data.status()==403){
                        handleOperationError(view.getViewContext().getString(R.string.code_403));
                    }else if(data.status()==201){
                        handleOperationError(view.getViewContext().getString(R.string.code_201));
                    }else if(data.status()==202){
                        handleOperationError(view.getViewContext().getString(R.string.code_202));
                    } else if(data.status()==203){
                        handleOperationError(view.getViewContext().getString(R.string.code_203));
                    }else if(data.status()==204){
                        handleOperationError(view.getViewContext().getString(R.string.code_204));
                    }else if(data.status()==205){
                        handleOperationError(view.getViewContext().getString(R.string.code_205));
                    }else if(data.status()==206){
                        handleOperationError(view.getViewContext().getString(R.string.code_206));
                    }else if(data.status()==207){
                        handleOperationError(view.getViewContext().getString(R.string.code_207));
                    }else if(data.status()==208){
                        handleOperationError(view.getViewContext().getString(R.string.code_208));
                    }else if(data.status()==210){
                        handleOperationError(view.getViewContext().getString(R.string.code_210));
                    }else if(data.status()==214){
                        handleOperationError(view.getViewContext().getString(R.string.code_214));
                    }else if(data.status()==220){
                        handleOperationError(view.getViewContext().getString(R.string.code_220));
                    }else if(data.status()==221){
                        handleOperationError(view.getViewContext().getString(R.string.code_221));
                    }else if(data.status()==222){
                        handleOperationError(view.getViewContext().getString(R.string.code_222));
                    }else if(data.status()==223){
                        handleOperationError(view.getViewContext().getString(R.string.code_223));
                    }else if(data.status()==250){
                        handleOperationError(view.getViewContext().getString(R.string.code_250));
                    }else if(data.status()==251){
                        handleOperationError(view.getViewContext().getString(R.string.code_251));
                    }else if(data.status()==252){
                        handleOperationError(view.getViewContext().getString(R.string.code_252));
                    }else if(data.status()==296){
                        handleOperationError(view.getViewContext().getString(R.string.code_296));
                    }else if(data.status()==297){
                        handleOperationError(view.getViewContext().getString(R.string.code_297));
                    }else if(data.status()==294){
                        handleOperationError(view.getViewContext().getString(R.string.code_294));
                    }else if(data.status()==870){
                        handleOperationError(view.getViewContext().getString(R.string.code_870));
                    }else if(data.status()==871){
                        handleOperationError(view.getViewContext().getString(R.string.code_871));
                    }else if(data.status()==890){
                        handleOperationError(view.getViewContext().getString(R.string.code_890));
                    }else if(data.status()==891){
                        handleOperationError(view.getViewContext().getString(R.string.code_891));
                    }else if(data.status()==892){
                        handleOperationError(view.getViewContext().getString(R.string.code_892));
                    }else if(data.status()==893){
                        handleOperationError(view.getViewContext().getString(R.string.code_893));
                    }else if(data.status()==894){
                        handleOperationError(view.getViewContext().getString(R.string.code_894));
                    }else if(data.status()==895){
                        handleOperationError(view.getViewContext().getString(R.string.code_895));
                    }else if(data.status()==896){
                        handleOperationError(view.getViewContext().getString(R.string.code_896));
                    }else if(data.status()==897){
                        handleOperationError(view.getViewContext().getString(R.string.code_897));
                    }else if(data.status()==898){
                        handleOperationError(view.getViewContext().getString(R.string.code_898));
                    }else if(data.status()==899){
                        handleOperationError(view.getViewContext().getString(R.string.code_899));
                    }else{
                        handleOperationError(data.msg());
                    }*/
                    Context context = view.getViewContext() == null ? AppManager.getAppManager().getAppContext() : view.getViewContext();
                    if(data.status()==403){
                        handlerGoLogin();
                    }else if(data.status()==900){
                        handleOperationError(context.getString(R.string.code_900));
                    }else if(data.status()==999){
                        handleOperationError(context.getString(R.string.code_999));
                    }else if(data.status()==403){
                        handleOperationError(context.getString(R.string.code_403));
                    }else if(data.status()==201){
                        handleOperationError(context.getString(R.string.code_201));
                    }else if(data.status()==202){
                        handleOperationError(context.getString(R.string.code_202));
                    } else if(data.status()==203){
                        handleOperationError(context.getString(R.string.code_203));
                    }else if(data.status()==204){
                        handleOperationError(context.getString(R.string.code_204));
                    }else if(data.status()==205){
                        handleOperationError(context.getString(R.string.code_205));
                    }else if(data.status()==206){
                        handleOperationError(context.getString(R.string.code_206));
                    }else if(data.status()==207){
                        handleOperationError(context.getString(R.string.code_207));
                    }else if(data.status()==208){
                        handleOperationError(context.getString(R.string.code_208));
                    }else if(data.status()==210){
                        handleOperationError(context.getString(R.string.code_210));
                    }else if(data.status()==214){
                        handleOperationError(context.getString(R.string.code_214));
                    }else if(data.status()==220){
                        handleOperationError(context.getString(R.string.code_220));
                    }else if(data.status()==221){
                        handleOperationError(context.getString(R.string.code_221));
                    }else if(data.status()==222){
                        handleOperationError(context.getString(R.string.code_222));
                    }else if(data.status()==223){
                        handleOperationError(context.getString(R.string.code_223));
                    }else if(data.status()==250){
                        handleOperationError(context.getString(R.string.code_250));
                    }else if(data.status()==251){
                        handleOperationError(context.getString(R.string.code_251));
                    }else if(data.status()==252){
                        handleOperationError(context.getString(R.string.code_252));
                    }else if(data.status()==296){
                        handleOperationError(context.getString(R.string.code_296));
                    }else if(data.status()==297){
                        handleOperationError(context.getString(R.string.code_297));
                    }else if(data.status()==294){
                        handleOperationError(context.getString(R.string.code_294));
                    }else if(data.status()==870){
                        handleOperationError(context.getString(R.string.code_870));
                    }else if(data.status()==871){
                        handleOperationError(context.getString(R.string.code_871));
                    }else if(data.status()==890){
                        handleOperationError(context.getString(R.string.code_890));
                    }else if(data.status()==891){
                        handleOperationError(context.getString(R.string.code_891));
                    }else if(data.status()==892){
                        handleOperationError(context.getString(R.string.code_892));
                    }else if(data.status()==893){
                        handleOperationError(context.getString(R.string.code_893));
                    }else if(data.status()==894){
                        handleOperationError(context.getString(R.string.code_894));
                    }else if(data.status()==895){
                        handleOperationError(context.getString(R.string.code_895));
                    }else if(data.status()==896){
                        handleOperationError(context.getString(R.string.code_896));
                    }else if(data.status()==897){
                        handleOperationError(context.getString(R.string.code_897));
                    }else if(data.status()==898){
                        handleOperationError(context.getString(R.string.code_898));
                    }else if(data.status()==899){
                        handleOperationError(context.getString(R.string.code_899));
                    }else{
                        handleOperationError(data.msg());
                    }

                }
            }
        } else {
            handler.success(t);
        }
        release();
    }

    public void resetLoadingStatus() {
        if (view != null && handler.autoHideLoading()) {
            if (view instanceof BasePaginationView) {
                BasePaginationView paginationView = (BasePaginationView) view;
                paginationView.onLoadingCompleted();
            }
            view.hideLoading();
        }
    }

    public void release() {
        view = null;
        handler = null;
    }

    public void handleException(Throwable e) {
        Context context = AppManager.getAppManager().getAppContext();
        if (view != null) {
            if (e instanceof ConnectException) {
                view.showToastMessage(context.getString(R.string.network_error));
            } else if (e instanceof HttpException) {
                view.showToastMessage(context.getString(R.string.network_server_error));
            } else if (e instanceof SocketTimeoutException) {
                view.showToastMessage(context.getString(R.string.network_timeout));
            } else {
                view.showToastMessage(view.getViewContext().getString(R.string.network_other));
            }
        }
    }

    public void handleOperationError(String message) {
        if (view != null)
            view.showToastMessage(message);
    }

    public void handlerGoLogin(){
        if (view != null){
            view.showToastMessage("登录失效");
            view.goLogin();
        }

    }

    public BaseView getView() {
        return view;
    }

    public interface CustomHandler<T> {
        /**
         * 请求成功同时业务成功的情况下会调用此函数
         */
        void success(T t);

        /**
         * 请求成功但业务失败的情况下会调用此函数.
         * @return 是否需要自行处理业务错误.
         * <P>
         * true - 需要, 父类不会处理错误
         * </P>
         * false - 不需要, 交由父类处理
         */
        boolean operationError(T t, int status, String message);

        /**
         * 请求失败的情况下会调用此函数
         * @return 是否需要自行处理系统错误.
         * <P>
         * true - 需要, 父类不会处理错误
         * </P>
         * false - 不需要, 交由父类处理
         */
        boolean error(Throwable e);

        /**
         * 是否需要自动隐藏加载框
         */
        boolean autoHideLoading();
    }

    public interface IBaseData {
        boolean isSuccess();
        int status();
        Object data();
        String msg();
    }
}
