package com.hdp.smp.front.ui.mbean.bigscreen;

import com.hdp.smp.model.BigScreen;
import com.hdp.smp.persistence.BigScreenDAO;
import com.hdp.smp.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.Session;

/**
 * 对应于可编辑表格(ClickToEdit).主要用于显示数据(List<BigScreen>)，以及用户点击后变为编辑状态。
 * 增加/修改/删除新大屏的动作处理在BigScreenMgt.一个新屏的属性绑定也在BigScreenMgt.
 */
public class BigScreenList  extends ArrayList<BigScreen>{
    private static final Logger log =Logger.getLogger(BigScreenList.class);
    private List<BigScreen> data =null;
    
    public BigScreenList() {
        super();
        this.loadData();
    }
    
    private void loadData() {
        if (data ==null){
                BigScreenDAO dao = new BigScreenDAO();
                Session session = HibernateUtil.getSession();
                data = dao.getAll(session, BigScreen.class);
                session.close();
                this.addAll(data);
            }
        }    
    
}