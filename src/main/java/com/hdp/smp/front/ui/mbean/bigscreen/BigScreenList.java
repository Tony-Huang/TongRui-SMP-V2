package com.hdp.smp.front.ui.mbean.bigscreen;

import com.hdp.smp.model.BigScreen;
import com.hdp.smp.persistence.BigScreenDAO;
import com.hdp.smp.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.Session;

/**
 * ��Ӧ�ڿɱ༭���(ClickToEdit).��Ҫ������ʾ����(List<BigScreen>)���Լ��û�������Ϊ�༭״̬��
 * ����/�޸�/ɾ���´����Ķ���������BigScreenMgt.һ�����������԰�Ҳ��BigScreenMgt.
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