package com.barabanov.listner;

import com.barabanov.entity.Revision;
import org.hibernate.envers.RevisionListener;


public class BarRevisionListener implements RevisionListener
{
    // Мы знаем, что revisionEntity это будет наш класс Revision т.к. мы над ним указали этот Listener.
    @Override
    public void newRevision(Object revisionEntity)
    {
//        В реальных приложениях достанем user из SecurityContext или что-то такое
        ((Revision) revisionEntity).setUsername("bar");
    }
}
