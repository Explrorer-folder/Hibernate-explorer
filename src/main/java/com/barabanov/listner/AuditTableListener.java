package com.barabanov.listner;


import com.barabanov.entity.Audit;
import org.hibernate.event.spi.*;

import java.io.Serial;


// Слушает события по изменению сущности. (перед удалением, сохранением и т.д.)
// Создаёт свою сущность и отслеживает каждое изменение и записывает их в таблицу.
// Такие таблицы нужны для анализа.
public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener
{
    @Serial
    private static final long serialVersionUID = 2316991099310743060L;

    @Override
    public boolean onPreDelete(PreDeleteEvent event)
    {
        auditEntity(event, Audit.Operation.DELETE);
        return false;
    }


    @Override
    public boolean onPreInsert(PreInsertEvent event)
    {
        auditEntity(event, Audit.Operation.INSERT);
        return false;
    }


    private void auditEntity(AbstractPreDatabaseOperationEvent event, Audit.Operation operation)
    {
        if (event.getEntity().getClass() != Audit.class)
        {
            var audit = Audit.builder()
                    .entityId(event.getId())
                    .entityName(event.getEntityName())
                    .entityContent(event.getEntity().toString())
                    .operation(operation)
                    .build();
            event.getSession().save(audit);
        }
    }

}
