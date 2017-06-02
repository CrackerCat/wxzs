package com.ff.wxzs.activitys;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.ff.wxzs.R;
import com.ff.wxzs.domain.QAInfo;
import com.ff.wxzs.domain.QAListInfo;
import com.ff.wxzs.engin.QAEngin;
import com.ff.wxzs.views.adpaters.QAAdapter;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class QAActivity extends BaseActivity {
    @BindView(R.id.listview)
    ExpandableListView qalistView;

    private QAAdapter qaAdapter;
    private QAEngin qaEngin;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    public void initVars() {
        super.initVars();
        qaAdapter = new QAAdapter(this);
        qaEngin = new QAEngin(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        qalistView.setGroupIndicator(null);
        qalistView.setAdapter(qaAdapter);
        qaAdapter.setOnItemClickListener(new QAAdapter.OnItemClickListener() {
            @Override
            public void onGroupClick(View view) {
                QAInfo info = (QAInfo) view.getTag();
                if (info.isSelected()) {
                    info.setSelected(false);
                    qalistView.collapseGroup(info.getQid());
                    return;
                }
                info.setSelected(true);
                collapseOthers(info.getQid());
                qalistView.expandGroup(info.getQid());
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();
        qaEngin.getQAList().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<QAListInfo>>() {
            @Override
            public void call(ResultInfo<QAListInfo> resultInfo) {
                avLoadingIndicatorView.hide();
                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK) {
                    if (resultInfo.data != null) {
                        qaAdapter.dataInfos = resultInfo.data.getQaInfoList();
                        qaAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                qalist();
            }
        });
    }

    private void qalist() {
        ArrayList<QAInfo> qaInfos = new ArrayList<QAInfo>();
        QAInfo qaInfo = new QAInfo();
        qaInfo.setQuestion("一个手机能开多少个分身");
        qaInfo.setAnswer("应用可以最多制99个分身。主要取决于手机本置");
        qaInfos.add(qaInfo);

        QAInfo qaInfo2 = new QAInfo();
        qaInfo2.setQuestion("分身助手提示制失败？");
        qaInfo2.setAnswer("当用户网络不稳定时，可能会造成制作分身不成功，用户可以把网络切换成4G或是wifi进行尝试。");
        qaInfos.add(qaInfo2);

        QAInfo qaInfo3 = new QAInfo();
        qaInfo3.setQuestion("分身出现接收信息不及时的情况是怎么回事？");
        qaInfo3.setAnswer("分身和分身多开助手需要设置好白名单和自启动才能够及时收取信息。制作过程有任何问题和反馈，具体点击客服中心联系客服");
        qaInfos.add(qaInfo3);

        QAInfo qaInfo4 = new QAInfo();
        qaInfo4.setQuestion("分身是否会导致封号？");
        qaInfo4.setAnswer("1、新注册帐号需要养号，新帐号30天内不要登陆多开制作的分身客户端，分身官方对新注册帐号监管非常严。\n" +
                "\n" +
                "2、使用官方分身客户端注册新帐号，不要使用多开制作的分身客户端注册分身帐号。\n" +
                "\n" +
                "3、分身好友/粉丝过万，过万不一定被封，但是有可能被限制使用或者被封的可能，如果你的好友/粉丝过万最好不要一直发广告（解决方案：当好友或粉丝过万时，不要一直发广告）\n" +
                "\n" +
                "4、“集赞”活动和”转发“活动，这两类相关的活动分身是不太支持的，如果你做太过了也是会被封的，之前分身已经出台了关于分身拒绝诱导性传播的通知，已经说的很明确了。（解决方案：尽可能的不要参与朋友圈内的集赞及转发活动）\n" +
                "\n" +
                "5、不要随便转发或打开朋友圈分享的链接信息（解决方案：不转发或分享朋友圈的链接信息）\n" +
                "\n" +
                "6、不得发布色情图片、色情词语违规、违法信息。这类100%封杀（解决方案：不发布色情图片和色情词语，不发布一些违法信息）\n" +
                "\n" +
                "7、在群内不要随便发布广告等敏感词汇，被踢而损失好友数量(解决方案：在群内我们尽可能的不发布广告或敏感词汇）\n" +
                "\n" +
                "8、在分身群里，添加好友要分时段进行，一次添加限制不得多于20人，隔一小时进行（解决方案：每次添加好友，不要超过15人，下次再添加好友，间隔时间段保持一小时以上）\n" +
                "\n" +
                "9、分身信息发布中出现累计超过10次的敏感词语：  “支付宝、银行、银行卡、汇款、打款、打钱、帐号、帐户、转帐、网银、多少钱、怎么卖、价格”会被封号（解决方案：信息发布中，不要包含有和钱有关的字语）\n" +
                "\n" +
                "10、当有大量好友加你时，依次几天分批通过，并注意对方敏感词。（解决方案：当有大量好友添加你时，我们每天添加不超过15人）\n" +
                "\n" +
                "11、保护自己分身号，打字询价说敏感词的，一律不要直接回复！（解决方案：遇到打字询价或者说一些敏感词的，转化成非敏感词）\n" +
                "\n" +
                "12、避免违规举报，在朋友圈经常更新生活动态。并多与朋友圈好友互动。增加好友之间信作与凝聚力。\n" +
                "\n" +
                "13、选择群，不要添加同质化严重的分身群，群内成员多数相同。\n" +
                "\n" +
                "14、在分身上卖东西，和好友聊天多次使用打钱、支付宝、汇款、建行、工行、付款方式等和分身背道而驰的词语。\n" +
                "\n" +
                "15、使用分身第三方工具进行站街、刷粉丝等，如果你使用分身一些营销工具，做得太够也一样被封号。\n" +
                "\n" +
                "16、分身发的内容被大量举报，如果你发的让粉丝或者朋友圈人举报，并且人数比较多的话也是可能被封的。（解决方案：在色情、敏感词、加人等方面避免）");
        qaInfos.add(qaInfo4);

        QAInfo qaInfo5 = new QAInfo();
        qaInfo5.setQuestion("分身收信息不及时或是收不到信息，请把分身和分身多开助手设置白名单和自启动？");
        qaInfo5.setAnswer("分身多开助手需要设置成白名单和自启动才能够及时收取信息。有任何问题和反馈，具体点击客服中心联系客服");
        qaInfos.add(qaInfo5);

        QAInfo qaInfo6 = new QAInfo();
        qaInfo6.setQuestion("苹果手机或电脑是否可以使用？");
        qaInfo6.setAnswer("应用暂时仅限安卓手机使用。");
        qaInfos.add(qaInfo6);

        QAInfo qaInfo7 = new QAInfo();
        qaInfo7.setQuestion("多开分身是怎样收费的？");
        qaInfo7.setAnswer("因开发需要技术成本，后期运营需要服务器、域名等成本，所以会收取相应的技术成本费用。");
        qaInfos.add(qaInfo7);

        QAInfo qaInfo8 = new QAInfo();
        qaInfo8.setQuestion("手机系统升级或是出厂设置，分身还能用吗？");
        qaInfo8.setAnswer("手机系统升级后， 分身正常使用。恢复出厂设置之后分身将不能正常使用【切记不要升级主分身、不要刷机、还原手机等】");
        qaInfos.add(qaInfo8);

        QAInfo qaInfo9 = new QAInfo();
        qaInfo9.setQuestion("带一键转发或其他特殊功能的分身是否能分身？");
        qaInfo9.setAnswer("本应用分身只支持官方应用，任何经过第三方修改或是带一键转发、带有特殊功能的应用不能进行分身");
        qaInfos.add(qaInfo9);

        QAInfo qaInfo10 = new QAInfo();
        qaInfo10.setQuestion("使用分身助手是否安全？");
        qaInfo10.setAnswer("公司为正规注册， 请放心使用");
        qaInfos.add(qaInfo10);

        QAInfo qaInfo11 = new QAInfo();
        qaInfo11.setQuestion("分身摄像头打不开？");
        qaInfo11.setAnswer("请去手机管理中查看摄像头权限是否全部允许！");
        qaInfos.add(qaInfo11);

        QAInfo qaInfo12 = new QAInfo();
        qaInfo12.setQuestion("为什么添加到桌面没有成功？");
        qaInfo12.setAnswer("某些机型不支持创建桌面快捷方式！");
        qaInfos.add(qaInfo12);

        qaAdapter.dataInfos = qaInfos;
        qaAdapter.notifyDataSetChanged();
    }

    private void collapseOthers(int i) {
        for (QAInfo qaInfo : qaAdapter.dataInfos) {
            if (qaInfo.getQid() != i && qaInfo.isSelected()) {
                qaInfo.setSelected(false);
                qalistView.collapseGroup(qaInfo.getQid());
            }
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_qa;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        String title = menuItem.getTitle().toString();
        if (title.equals(getResources().getString(R.string.back))) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_actions, menu);
        return true;
    }
}
